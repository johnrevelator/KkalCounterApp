package inc.ak.kkalcounter.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import inc.ak.kkalcounter.KkalCounter;
import inc.ak.kkalcounter.Preferences;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.adapters.DetailsAdapter;
import inc.ak.kkalcounter.adapters.HistoryAdapter;
import inc.ak.kkalcounter.listener.ClickListener;
import inc.ak.kkalcounter.listener.RecyclerTouchListener;
import inc.ak.kkalcounter.listener.RvTouchListener;
import inc.ak.kkalcounter.model.Eating;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EatingDetailsActivity extends SuperActivity{

    List<Eating> eatingList;
    DetailsAdapter mAdapter;




    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_eating_details;
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(getIntent().getStringExtra("type"));
        setupItems();

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


    public void setupItems() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final String date = df.format(Calendar.getInstance().getTime());
        eatingList = new ArrayList<>();
        double totfat = 0.0;
        double totcarb = 0.0;
        double totprot = 0.0;
        double totkcal = 0.0;
        KkalCounter.getApi().getEating("date eq '"+date+"' and userId eq '" + Preferences.getString(Preferences.USER_ID) + "' and type eq '" + getIntent().getStringExtra("type") + "'", "createdAt desc").enqueue(new Callback<List<Eating>>() {
            @Override
            public void onResponse(final Call<List<Eating>> call, final Response<List<Eating>> response) {


                Log.i("MyLog", "adding");
                if(response.body()!=null)

                        Log.i("MyLog", date + " time");


                        eatingList=response.body();
                setupAdapter();







            }

            @Override
            public void onFailure(Call<List<Eating>> call, Throwable t) {
                Log.i("MyLog", String.valueOf(t));

                //Произошла ошибка
            }
        });


    }

    public void showDial(final Eating eating){
        new MaterialDialog.Builder(this)
                .title("Удаление")
                .content("Этот прием пищи будет удален")
                .positiveText("Ок")
                .negativeText("Отмена")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        KkalCounter.getApi().deleteEating(eating.getId()).enqueue(new Callback<Eating>() {
                            @Override
                            public void onResponse(Call<Eating> call, Response<Eating> response) {
                                showToast("Удалено");
                                setupItems();
                            }

                            @Override
                            public void onFailure(Call<Eating> call, Throwable t) {

                            }
                        });

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }





    public void setupAdapter(){
        mAdapter = new DetailsAdapter(eatingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RvTouchListener(this,recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i("MyLog", String.valueOf(position));

            }

            @Override
            public void onLongClick(View view, final int position) {
                showDial(eatingList.get(position));
            }
        }));
    }


}
