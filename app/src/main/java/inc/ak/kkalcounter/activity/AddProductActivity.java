package inc.ak.kkalcounter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;

import butterknife.Bind;
import inc.ak.kkalcounter.KkalCounter;
import inc.ak.kkalcounter.Preferences;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends SuperActivity {

    @Bind(R.id.name)
    MaterialEditText name;
    @Bind(R.id.kcal)
    MaterialEditText kcal;
    @Bind(R.id.fat)
    MaterialEditText fat;
    @Bind(R.id.carb)
    MaterialEditText carb;
    @Bind(R.id.prot)
    MaterialEditText prot;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_product;
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Добавить продукт");
        name.setText(getIntent().getStringExtra("s"));

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){

            case android.R.id.home:
                finish();
                return true;
            case R.id.action_edit:
                addProduct();

                return true;


        }

        return super.onOptionsItemSelected(item);
    }


    public void addProduct(){

        KkalCounter.getApi().addProduct(new Product(name.getText().toString(),fat.getText().toString(),carb.getText().toString(),prot.getText().toString(),kcal.getText().toString(),null)).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                startActivity(new Intent(getBaseContext(),DescScrollingActivity.class).putExtra("product",new Product(name.getText().toString(),fat.getText().toString(),carb.getText().toString(),prot.getText().toString(),kcal.getText().toString(),null)));
                finish();
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.i("MyLog", String.valueOf(t));

                //Произошла ошибка
            }
        });
    }


}
