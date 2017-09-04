package inc.ak.kkalcounter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rm.rmswitch.RMTristateSwitch;

import butterknife.Bind;
import fr.ganfra.materialspinner.MaterialSpinner;
import inc.ak.kkalcounter.KkalCounter;
import inc.ak.kkalcounter.Preferences;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.activity.ChooseProduct;
import inc.ak.kkalcounter.activity.MainActivity;
import inc.ak.kkalcounter.activity.SuperActivity;
import inc.ak.kkalcounter.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends SuperActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.age)
    MaterialEditText age;
    @Bind(R.id.weight)
    MaterialEditText weight;
    @Bind(R.id.height)
    MaterialEditText height;
    @Bind(R.id.rm_triswitch2)
    RMTristateSwitch aSwitch;
    @Bind(R.id.spinner)
    MaterialSpinner spinner;
    double koef=1.2;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(!Preferences.getString(Preferences.SEX).equals("")) {
            aSwitch.setState(Integer.parseInt(Preferences.getString(Preferences.SEX)));
        }
        if(!Preferences.getString(Preferences.HEIGHT).equals("")) {
            height.setText(Preferences.getString(Preferences.HEIGHT));
        }
        if(!Preferences.getString(Preferences.WEIGHT).equals("")) {
            weight.setText(Preferences.getString(Preferences.WEIGHT));

        }
        if(!Preferences.getString(Preferences.AGE).equals("")) {
            age.setText(Preferences.getString(Preferences.AGE));

        }
        if(!Preferences.getString(Preferences.ACTIVE).equals("")) {
            spinner.setSelection(Integer.parseInt(Preferences.getString(Preferences.ACTIVE)));

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.active));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        koef=1.2;
                        break;
                    case 1:
                        koef=1.375;

                        break;
                    case 2:
                        koef=1.55;

                        break;
                    case 3:
                        koef=1.725;

                        break;
                    case 4:
                        koef=1.9;

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                koef=1.2;

            }
        });


        }

    public void updateUser(String id, User user){
        KkalCounter.getApi().updateUser(id, user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                startActivity(new Intent(getBaseContext(),MainActivity.class));

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
    public void addUser(User user){
        KkalCounter.getApi().setUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                startActivity(new Intent(getBaseContext(),MainActivity.class));

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
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
               /* StringBuffer myString = new StringBuffer();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                   update(0);

                } catch (IOException e) {
                    Log.d("MyLog", String.valueOf(e));
                }*/

               Preferences.addString(Preferences.SEX, String.valueOf(aSwitch.getState()));
               Preferences.addString(Preferences.ACTIVE, String.valueOf(spinner.getSelectedItemPosition()));
               Preferences.addString(Preferences.AGE, age.getText().toString());
               Preferences.addString(Preferences.HEIGHT, height.getText().toString());
               Preferences.addString(Preferences.WEIGHT, weight.getText().toString());
                double BMR;
                if(aSwitch.getState()==0){
                    BMR= (88.36 + (13.4*Integer.parseInt(weight.getText().toString())) + (4.8 *Integer.parseInt(height.getText().toString()))
                            - (5.7*Integer.parseInt(age.getText().toString())))*koef;
                }
                else {
                    BMR = (447.6 + (9.2 * Integer.parseInt(weight.getText().toString())) + (3.1 * Integer.parseInt(height.getText().toString()))
                            - (4.3 * Integer.parseInt(age.getText().toString())))*koef;
                }
                //по формуле Харриса-Бенедикта

                Preferences.addString(Preferences.BMR, String.valueOf((int)BMR));
                Preferences.addString(Preferences.NEED_PROT, String.valueOf((int)(2*Integer.parseInt(weight.getText().toString())*koef)));
                Preferences.addString(Preferences.PROT_KCAL, String.valueOf((int)(8*Integer.parseInt(weight.getText().toString())*koef)));
                Preferences.addString(Preferences.NEED_FAT, String.valueOf((int)(Integer.parseInt(weight.getText().toString())*koef)));
                Preferences.addString(Preferences.FAT_KCAL, String.valueOf((int)(9*Integer.parseInt(weight.getText().toString())*koef)));
                Preferences.addString(Preferences.NEED_CARB, String.valueOf((int)((BMR-17*Integer.parseInt(weight.getText().toString())*koef)/4)));
                Preferences.addString(Preferences.CARB_KCAL, String.valueOf((int)(BMR-17*Integer.parseInt(weight.getText().toString())*koef)));
                if(getIntent().getIntExtra("update",0)==1){
                    updateUser(getIntent().getStringExtra("id"),new User(Preferences.getString(Preferences.ACCOUNT_NAME),Preferences.getString(Preferences.ACCOUNT),height.getText().toString(),
                            weight.getText().toString(),age.getText().toString(),aSwitch.getState()));
                }
                else{
                    addUser(new User(Preferences.getString(Preferences.ACCOUNT_NAME),Preferences.getString(Preferences.ACCOUNT),height.getText().toString(),
                            weight.getText().toString(),age.getText().toString(),aSwitch.getState()));
                }


               /* try {
                    Log.d("MyLog", Jsoup.connect("http://www.calorizator.ru/product/all").get().select("tbody").toString());

                  *//*  String[] result=Jsoup.connect("http://www.calorizator.ru/product/all").get().select("tbody").text().split(" ");
                    for(int i=0;i<result.length;i++){
                    Log.d("MyLog", result[i]);
                    }*//*
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
               /* KkalCounter.getApi().addProduct(new Product("test","test","test","test")).enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        Log.i("MyLog",response.body().getName());
                    }
                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Log.i("MyLog", String.valueOf(t));

                        //Произошла ошибка
                    }
                });*/
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

}
