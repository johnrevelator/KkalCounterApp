package inc.ak.kkalcounter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import inc.ak.kkalcounter.KkalCounter;
import inc.ak.kkalcounter.Preferences;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.activity.SuperActivity;
import inc.ak.kkalcounter.model.Eating;
import inc.ak.kkalcounter.model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescScrollingActivity extends SuperActivity{
    Product product;
    @Bind(R.id.image)
    ImageView prodImg;
    @Bind(R.id.prot)
    TextView prot;
    @Bind(R.id.carb)
    TextView carb;
    @Bind(R.id.fat)
    TextView fat;
    @Bind(R.id.kcal)
    TextView kcal;
    @Bind(R.id.add)
    Button add;
    @Bind(R.id.weight)
    EditText weight;
    Double koef;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_desc_scrolling;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        product=(Product)getIntent().getSerializableExtra("product");
        try {
            Picasso.with(this).load(product.getImg()).into(prodImg);
        }catch (NullPointerException e){

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(product.getName());
        prot.setText(getString(R.string.prot,product.getProt()));
        carb.setText(getString(R.string.carb,product.getCarb()));
        fat.setText(getString(R.string.fat,product.getFat()));
        kcal.setText(getString(R.string.kcal,product.getKcal()));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkType();


            }
        });

    }

    public void checkType() {
        if (getIntent().getStringExtra("tp") == null) {
            new MaterialDialog.Builder(this)
                    .title("Выберите")
                    .items(R.array.items)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (weight.getText().toString().equals("")) {
                                koef = 1.0;
                            } else {
                                koef = Double.parseDouble(weight.getText().toString()) / 100;
                            }
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            String date = df.format(Calendar.getInstance().getTime());
                            Eating eating;
                            send(new Eating(text.toString(), String.valueOf(Double.parseDouble(product.getFat()) * koef),
                                    String.valueOf(Double.parseDouble(product.getCarb()) * koef),
                                    String.valueOf(Double.parseDouble(product.getProt()) * koef),
                                    String.valueOf(Double.parseDouble(product.getKcal()) * koef), date, Preferences.getString(Preferences.USER_ID),product.getName()));
                        }
                    })
                    .show();
        } else {
            if (weight.getText().toString().equals("")) {
                koef = 1.0;
            } else {
                koef = Double.parseDouble(weight.getText().toString()) / 100;
            }
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String date = df.format(Calendar.getInstance().getTime());
            Eating eating;
            send(new Eating(getIntent().getStringExtra("tp"), String.valueOf(Double.parseDouble(product.getFat()) * koef),
                    String.valueOf(Double.parseDouble(product.getCarb()) * koef),
                    String.valueOf(Double.parseDouble(product.getProt()) * koef),
                    String.valueOf(Double.parseDouble(product.getKcal()) * koef), date, Preferences.getString(Preferences.USER_ID),product.getName()));
        }
    }


    public void send(Eating eating){

        KkalCounter.getApi().addEating(eating).enqueue(new Callback<Eating>() {
            @Override
            public void onResponse(Call<Eating> call, Response<Eating> response) {
                finish();

            }
            @Override
            public void onFailure(Call<Eating> call, Throwable t) {
                Log.i("MyLog", String.valueOf(t));

                //Произошла ошибка
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
