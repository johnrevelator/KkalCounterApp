package inc.ak.kkalcounter.activity;

import android.content.Intent;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import inc.ak.kkalcounter.KkalCounter;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.fragment.DailyActFragment;
import inc.ak.kkalcounter.fragment.HistoryFragment;
import inc.ak.kkalcounter.fragment.ProfileFragment;
import inc.ak.kkalcounter.model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static inc.ak.kkalcounter.Constants.BASE_URL;
import static inc.ak.kkalcounter.Constants.CALORY_FIRST_URL;
import static inc.ak.kkalcounter.Constants.CALORY_SECOND_URL;

public class MainActivity extends SuperActivity  {

    FragmentPagerItemAdapter adapter;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.viewpagertab)
    SmartTabLayout viewPagerTab;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    Elements elements;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        //создаем адаптер
        setSupportActionBar(toolbar);
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Активность", DailyActFragment.class)
                .add("История", HistoryFragment.class)
                .add("Профиль", ProfileFragment.class)
                .create());

        //устанавливаем адаптер
        viewPager.setAdapter(adapter);
        //создаем вьюпейджер
        viewPagerTab.setViewPager(viewPager);





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){

            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add:
    /*            StringBuffer myString = new StringBuffer();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                   update(0);

                } catch (IOException e) {
                    Log.d("MyLog", String.valueOf(e));
                }*/

                new MaterialDialog.Builder(this)
                        .title("Выберите")
                        .items(R.array.items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                startActivity(new Intent(getBaseContext(),ChooseProduct.class).putExtra("tp",text));

                            }
                        })
                        .show();

                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    public void update(int i) throws IOException {
        Log.d("Count","Update+1 "+ "\n");

        if(i!=0){
            Log.d("Count","1< "+ "\n");

            elements=Jsoup.connect(CALORY_SECOND_URL+i).get().select("tr");
            select(0,i);
        }
        else {
            Log.d("Count","0 "+ "\n");

            elements=Jsoup.connect(CALORY_FIRST_URL).get().select("tr");
            select(0,i);
        }


    }

    public void send(Product product, final int g, final int y){
        KkalCounter.getApi().addProduct(product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(g<elements.size()-1)
                    try {
                        Log.d("Count",g+" "+y + "\n");

                        select(g+1,y);
                    } catch (IOException e) {
                        Log.d("Count", String.valueOf(e));
                    }
                else
                    try {
                        Log.d("Count"," "+y + "\n");

                        update(y+1);
                    } catch (IOException e) {
                        Log.d("Count", String.valueOf(e));
                    }

            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.i("MyLog", String.valueOf(t));

                //Произошла ошибка
            }
        });
    }
    public void select(int g,int y) throws IOException {
        if(elements.get(g).attr("class").equals("even")||elements.get(g).attr("class").equals("odd")) {
            Log.d("MyLog",elements.get(g).select("a").text() + "\n");
            Product product = new Product(null, null, null, null, null,null);
            product.setName(elements.get(g).select("a").text());
            for (Element e : elements.get(g).select("td")) {
                if (e.select("td").attr("class").equals("views-field views-field-field-picture-fid")) {
                    Log.d("MyLog", e.select("td").select("a").attr("href") + "\n");
                    product.setImg(e.select("td").select("a").attr("href"));
                }
                if (e.select("td").attr("class").equals("views-field views-field-field-protein-value")) {
                    Log.d("MyLog", e.select("td").text() + "\n");
                    product.setProt(e.select("td").text());
                }
                if (e.select("td").attr("class").equals("views-field views-field-field-fat-value")) {
                    product.setFat(e.select("td").text());

                    Log.d("MyLog", e.select("td").text() + "\n");
                }
                if (e.select("td").attr("class").equals("views-field views-field-field-carbohydrate-value")) {
                    Log.d("MyLog", e.select("td").text() + "\n");
                    product.setCarb(e.select("td").text());

                }
                if (e.select("td").attr("class").equals("views-field views-field-field-kcal-value")) {
                    Log.d("MyLog", e.select("td").text() + "\n");
                    product.setKcal(e.select("td").text());

                }
            }

            send(product,g,y);
        }
        else {
            if (g < elements.size() - 1) {
                Log.d("Count", g + " " + y + "\n");

                select(g + 1, y);
            }else {
                try {
                    Log.d("Count"," "+y + "\n");

                    update(y+1);
                } catch (IOException e) {
                    Log.d("Count", String.valueOf(e));
                }
            }
        }
    }


    public void onPageSelected(int position) {

        //.instantiateItem() from until .destoryItem() is called it will be able to get the Fragment of page.
        Fragment page = adapter.getPage(position);

    }



}
