package inc.ak.kkalcounter.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.gigamole.library.ArcProgressStackView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import inc.ak.kkalcounter.KkalCounter;
import inc.ak.kkalcounter.OnLoadMoreListener;
import inc.ak.kkalcounter.Preferences;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.adapters.FoodAdapter;
import inc.ak.kkalcounter.listener.ClickListener;
import inc.ak.kkalcounter.listener.RecyclerTouchListener;
import inc.ak.kkalcounter.model.Eating;
import inc.ak.kkalcounter.model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DailyActFragment extends SuperFragment {

    public static final String EXTRA = "extra_search_item";
    public static final String TAG = "MyLog";
    ProgressDialog progress;
    AlertDialog.Builder ad;
    Context context;
    ArcProgressStackView mArcProgressStackView;



    TextView moreB;

    List<Eating> eatingList;
    FoodAdapter mAdapter;
    BottomSheetBehavior behavior;

    @Bind(R.id.bottom_sheet)
    View bottomSheet;

    @Bind(R.id.darker)
    FrameLayout darker;

    @Bind(R.id.minKcal)
   TextView min;
    @Bind(R.id.maxKcal)
   TextView max;




    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.kcalBar)
    RoundCornerProgressBar kcalBar;





    @Override
    public int onInflateViewFragment() {
        return R.layout.daily_act_fragment;
    }

    @Override
    public void onCreateFragment(Bundle instance) {

    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading();
        setupItems();
    }

    int[] colors;
    int[] bgColors;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    int mCounter;


    @Override
    public View onCreateViewFragment(View view) {
        setupBottomSheet();



        max.setText(Preferences.getString(Preferences.BMR));

        mArcProgressStackView = (ArcProgressStackView) view.findViewById(R.id.apsv);
        mArcProgressStackView.setShadowColor(Color.argb(200, 0, 0, 0));
        mArcProgressStackView.setAnimationDuration(1000);
        mArcProgressStackView.setSweepAngle(360);

        final String[] stringColors = getResources().getStringArray(R.array.devlight);
        final String[] stringBgColors = getResources().getStringArray(R.array.bg);

        colors = new int[4];
        bgColors = new int[4];
        for (int i = 0; i < 4; i++) {
            colors[i] = Color.parseColor(stringColors[i]);
            bgColors[i] = Color.parseColor(stringBgColors[i]);
        }




        return view;
    }

    @Override
    public void onAttachFragment(Activity activity) {


    }

    public void setupBottomSheet(){
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.d("bottomsheet-", "STATE_COLLAPSED");


                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.d("bottomsheet-", "STATE_DRAGGING");
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        setupAdapter();
                        Log.d("bottomsheet-", "STATE_ANCHOR_POINT");

                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.d("bottomsheet-", "STATE_HIDDEN");



                        break;
                    default:

                        Log.d("bottomsheet-", "STATE_SETTLING");


                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if(slideOffset==0.0)
                            darker.setBackgroundColor(0x00000000);
                         else if(slideOffset<0.05)
                            darker.setBackgroundColor(0x03000000);
                else if(slideOffset<0.1)
                            darker.setBackgroundColor(0x05000000);
                else if(slideOffset<0.15)
                            darker.setBackgroundColor(0x08000000);
                else if(slideOffset<0.2)
                            darker.setBackgroundColor(0x10000000);
                else if(slideOffset<0.25)
                            darker.setBackgroundColor(0x13000000);
                else if(slideOffset<0.3)
                            darker.setBackgroundColor(0x15000000);
                else if(slideOffset<0.35)
                            darker.setBackgroundColor(0x18000000);
                else if(slideOffset<0.40)
                            darker.setBackgroundColor(0x20000000);
                else if(slideOffset<0.45)
                            darker.setBackgroundColor(0x23000000);
                else if(slideOffset<0.50)
                            darker.setBackgroundColor(0x28000000);
                         else if(slideOffset<0.55)
                            darker.setBackgroundColor(0x35000000);
                         else if(slideOffset<0.60)
                            darker.setBackgroundColor(0x43000000);
                        else if(slideOffset<0.65)
                            darker.setBackgroundColor(0x50000000);
                else if(slideOffset<0.70)
                            darker.setBackgroundColor(0x60000000);

                Log.d("MyLog", String.valueOf(slideOffset));
            }
        });
    }

    public void setupItems(){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        eatingList =new ArrayList<>();
        double totfat=0.0;
        double totcarb=0.0;
        double totprot=0.0;
        double totkcal=0.0;
        KkalCounter.getApi().getEatingList("date eq '"+date+"'").enqueue(new Callback<List<Eating>>() {
            @Override
            public void onResponse(final Call<List<Eating>> call, final Response<List<Eating>> response) {

                Log.i("MyLog","adding");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        double totfat=0.0;
                        double totcarb=0.0;
                        double totprot=0.0;
                        double totkcal=0.0;
                        for(int i=0;i<getResources().getStringArray(R.array.items).length;i++){
                            Eating eating=new Eating(getResources().getStringArray(R.array.items)[i],null,null,null,null,null);
                            for(int r=0;r<response.body().size();r++) {
                                if(response.body().get(r).getType().equals(getResources().getStringArray(R.array.items)[i])){
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
                                    eating.setDate(eating.getDate()+response.body().get(r).getName()+"\n");

                                }

                            }
                            if(eating.getProt()!=null){
                                eatingList.add(eating);
                            }

                        }
                        min.setText(String.valueOf((int)totkcal));
                        Log.i("MyLog", String.valueOf((int)(totprot/Integer.valueOf(Preferences.getString(Preferences.NEED_PROT))*100)));
                        Log.i("MyLog", String.valueOf((int)(totfat/Integer.valueOf(Preferences.getString(Preferences.NEED_FAT))*100)));
                        Log.i("MyLog", String.valueOf((int)(totcarb/Integer.valueOf(Preferences.getString(Preferences.NEED_CARB))*100)));
                        Log.i("MyLog", String.valueOf((int)(totcarb)));
                        Log.i("MyLog", String.valueOf((Integer.valueOf(Preferences.getString(Preferences.NEED_CARB)))));
                        Log.i("MyLog", String.valueOf((Integer.valueOf(Preferences.getString(Preferences.NEED_PROT)))));
                        Log.i("MyLog", String.valueOf((Integer.valueOf(Preferences.getString(Preferences.NEED_FAT)))));


                        hideLoading();
                        coordinatorLayout.setVisibility(View.VISIBLE);
                        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
                        models.add(new ArcProgressStackView.Model("БЕЛКИ", (int)(totprot/Integer.valueOf(Preferences.getString(Preferences.NEED_PROT))*100), bgColors[0], colors[0]));
                        models.add(new ArcProgressStackView.Model("ЖИРЫ",(int)(totfat/Integer.valueOf(Preferences.getString(Preferences.NEED_FAT))*100), bgColors[1], colors[1]));
                        models.add(new ArcProgressStackView.Model("УГЛЕВОДЫ", (int)(totcarb/Integer.valueOf(Preferences.getString(Preferences.NEED_CARB))*100), bgColors[2], colors[2]));
                        mArcProgressStackView.setModels(models);


                        kcalBar.setSecondaryProgress((int)(totkcal/Integer.valueOf(Preferences.getString(Preferences.BMR))*100));
                        Log.i("MyLog", String.valueOf((int)(totkcal/Integer.valueOf(Preferences.getString(Preferences.BMR))*100)));

                        if((int)(totkcal/Integer.valueOf(Preferences.getString(Preferences.BMR))*100)>100){
                            kcalBar.setProgress((int)(totkcal/Integer.valueOf(Preferences.getString(Preferences.BMR))*100)-100);
                        }



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
        mAdapter = new FoodAdapter(eatingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

}

