package inc.ak.kkalcounter.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gigamole.library.ArcProgressStackView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import inc.ak.kkalcounter.KkalCounter;
import inc.ak.kkalcounter.Preferences;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.adapters.FoodAdapter;
import inc.ak.kkalcounter.adapters.HistoryAdapter;
import inc.ak.kkalcounter.model.Eating;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends SuperFragment {

    public static final String EXTRA = "extra_search_item";
    public static final String TAG = "MyLog";
    ProgressDialog progress;
    AlertDialog.Builder ad;
    Context context;


    List<Eating> eatingList;
    HistoryAdapter mAdapter;




    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;







    @Override
    public int onInflateViewFragment() {
        return R.layout.history_fragment;
    }

    @Override
    public void onCreateFragment(Bundle instance) {

    }

    @Override
    public void onResume() {
        showLoading();
        setupItems();
        super.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public View onCreateViewFragment(View view) {

        return view;
    }

    @Override
    public void onAttachFragment(Activity activity) {


    }

    public void setupItems(){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final String date = df.format(Calendar.getInstance().getTime());
        eatingList =new ArrayList<>();
        setupAdapter();
        double totfat=0.0;
        double totcarb=0.0;
        double totprot=0.0;
        double totkcal=0.0;
        KkalCounter.getApi().getEating("userId eq '"+Preferences.getString(Preferences.USER_ID)+"'","createdAt desc").enqueue(new Callback<List<Eating>>() {
            @Override
            public void onResponse(final Call<List<Eating>> call, final Response<List<Eating>> response) {


                Log.i("MyLog","adding");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String currdate=date;
                        Log.i("MyLog",date+" time");

                        double totfat=0.0;
                        double totcarb=0.0;
                        double totprot=0.0;
                        double totkcal=0.0;
                        Eating eating=new Eating(null,"0.0","0.0","0.0","0.0",date,Preferences.getString(Preferences.USER_ID));

                            for(int r=0;r<response.body().size();r++) {
                                if(!response.body().get(r).getDate().equals(currdate)) {
                                    eatingList.add(eating);
                                    currdate=response.body().get(r).getDate();

                                    eating=new Eating(null,null,null,null,null,null,Preferences.getString(Preferences.USER_ID));
                                }
                                    double fat=0.0;
                                    double carb=0.0;
                                    double prot=0.0;
                                    double kcal=0.0;
                                    try {
                                        fat = Double.parseDouble(eating.getFat());
                                        carb = Double.parseDouble(eating.getCarb());
                                        prot = Double.parseDouble(eating.getProt());
                                        kcal = Double.parseDouble(eating.getKcal());
                                    }catch (NullPointerException e){
                                        Log.i("MyLog", String.valueOf(e));
                                    }

                                    eating.setProt(String.valueOf(prot+Double.parseDouble(response.body().get(r).getProt())));
                                    eating.setFat(String.valueOf(fat+Double.parseDouble(response.body().get(r).getFat())));
                                    eating.setCarb(String.valueOf(carb+Double.parseDouble(response.body().get(r).getCarb())));
                                    eating.setKcal(String.valueOf(kcal+Double.parseDouble(response.body().get(r).getKcal())));
                                    totcarb=totcarb+Double.parseDouble(response.body().get(r).getCarb());
                                    totfat=totfat+Double.parseDouble(response.body().get(r).getFat());
                                    totprot=totprot+Double.parseDouble(response.body().get(r).getProt());
                                    totkcal=totkcal+Double.parseDouble(response.body().get(r).getKcal());
                                    eating.setDate(response.body().get(r).getDate());



                            }



                        Log.i("MyLog", String.valueOf((int)(totprot/Integer.valueOf(Preferences.getString(Preferences.NEED_PROT))*100)));
                        Log.i("MyLog", String.valueOf((int)(totfat/Integer.valueOf(Preferences.getString(Preferences.NEED_FAT))*100)));
                        Log.i("MyLog", String.valueOf((int)(totcarb/Integer.valueOf(Preferences.getString(Preferences.NEED_CARB))*100)));
                        Log.i("MyLog", String.valueOf((int)(totcarb)));
                        Log.i("MyLog", String.valueOf((Integer.valueOf(Preferences.getString(Preferences.NEED_CARB)))));
                        Log.i("MyLog", String.valueOf((Integer.valueOf(Preferences.getString(Preferences.NEED_PROT)))));
                        Log.i("MyLog", String.valueOf((Integer.valueOf(Preferences.getString(Preferences.NEED_FAT)))));


                        hideLoading();
                        mAdapter.notifyDataSetChanged();




                    }
                });



            }
            @Override
            public void onFailure(Call<List<Eating>> call, Throwable t) {
                Log.i("MyLog", String.valueOf(t));

                //Произошла ошибка
            }
        });






    }

    public void setupAdapter(){
        mAdapter = new HistoryAdapter(eatingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }


}

