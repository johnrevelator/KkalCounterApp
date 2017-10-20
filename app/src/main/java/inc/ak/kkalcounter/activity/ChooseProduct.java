package inc.ak.kkalcounter.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.malinskiy.superrecyclerview.swipe.SparseItemRemoveAnimator;
import com.malinskiy.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import inc.ak.kkalcounter.adapters.ContactAdapter;
import inc.ak.kkalcounter.KkalCounter;
import inc.ak.kkalcounter.OnLoadMoreListener;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.adapters.ProductAdapter;
import inc.ak.kkalcounter.listener.ClickListener;
import inc.ak.kkalcounter.listener.RecyclerTouchListener;
import inc.ak.kkalcounter.model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseProduct extends SuperActivity implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener, SwipeDismissRecyclerViewTouchListener.DismissCallbacks {
    private List<Product> contacts;

    @Bind(R.id.list)
    SuperRecyclerView mRecycler;
    private ProductAdapter         mAdapter;
    private SparseItemRemoveAnimator mSparseAnimator;
    private RecyclerView.LayoutManager mLayoutManager;
    private Handler mHandler;
    String sugest="";

    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Bind(R.id.nothing)
    LinearLayout nothing;

    @Bind(R.id.search_view)
    MaterialSearchView searchView;


    @Override
    protected int getContentViewId() {
        return R.layout.choose_product;
    }


    protected int getLayoutId() {
        return R.layout.choose_product;
    }

    protected boolean isSwipeToDismissEnabled() {
        return true;
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        contacts=new ArrayList<>();




        mAdapter = new ProductAdapter(contacts);

        setupClickListener();
        mLayoutManager = getLayoutManager();
        mRecycler.setLayoutManager(mLayoutManager);

        boolean dismissEnabled = isSwipeToDismissEnabled();
        if (dismissEnabled) {
            mRecycler.setupSwipeToDismiss(this);
            mSparseAnimator = new SparseItemRemoveAnimator();
            mRecycler.getRecyclerView().setItemAnimator(mSparseAnimator);
        }

        mHandler = new Handler(Looper.getMainLooper());
     mRecycler.setAdapter(mAdapter);

                        loadDataFilter(sugest,1);



        mRecycler.setRefreshListener(this);
        mRecycler.setRefreshingColor(Color.parseColor(getResources().getStringArray(R.array.devlight)[0]),Color.parseColor(getResources().getStringArray(R.array.devlight)[1]),Color.parseColor(getResources().getStringArray(R.array.devlight)[2]),Color.parseColor(getResources().getStringArray(R.array.devlight)[3]));
        mRecycler.setupMoreListener(this, 1);





        //set load more listener for the RecyclerView adapter

        loadDataFilter(sugest,0);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sugest=newText;
                mAdapter.clear();
                loadDataFilter(sugest,1);
                Log.i("MyLog","suggest");
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                sugest="";
                //Do some magic
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    public void loadDataFilter(final String sugest, int flag){
        nothing.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);



        KkalCounter.getApi().getProductListFilter("substringof('"+sugest+"',name)",contacts.size()-1,30).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, final Response<List<Product>> response) {

                Log.i("MyLog","adding");

                        if(response.body().size()==0){

                            nothing.setVisibility(View.VISIBLE);
                            mRecycler.setVisibility(View.GONE);

                        } else {
                    mAdapter.clear();
                    mAdapter.addAll(response.body());
                    mAdapter.notifyDataSetChanged();

                }





            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("MyLog", String.valueOf(t));

                //Произошла ошибка
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){

            case android.R.id.home:
                finish();
                return true;



        }

        return super.onOptionsItemSelected(item);
    }


    int check=0;

    @Override
    public void onRefresh() {
        mAdapter.clear();
        loadDataFilter(sugest,1);

    }

    public void add(View view){
        finish();
        startActivity(new Intent(this,AddProductActivity.class).putExtra("s",sugest));

    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
      /*  mHandler.postDelayed(new Runnable() {
            public void run() {
                loadDataFilter(sugest,1);
            }
        }, 300);*/
    }

    @Override
    public boolean canDismiss(int position) {
        return true;
    }

    @Override
    public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mSparseAnimator.setSkipNext(true);
            mAdapter.remove(position);
        }
    }
    public void setupClickListener(){
        mRecycler.addOnItemTouchListener(new RecyclerTouchListener(this, mRecycler, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
               startActivity(new Intent(getBaseContext(),DescScrollingActivity.class).putExtra("product",contacts.get(position)).putExtra("tp",getIntent().getStringExtra("tp")));

            }

            @Override
            public void onLongClick(View view, final int position) {


            }
        }));
    }

}
