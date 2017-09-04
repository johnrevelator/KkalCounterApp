package inc.ak.kkalcounter.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.eyalbira.loadingdots.LoadingDots;

import butterknife.ButterKnife;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.activity.SuperActivity;
import inc.ak.kkalcounter.view.LoadingDialog;


public abstract class SuperFragment extends Fragment {

    private String TAG = getClass().getSimpleName();

    LoadingDots mDilatingDotsProgressBar;

    public abstract int onInflateViewFragment();

    public abstract void onCreateFragment(Bundle instance);

    public abstract View onCreateViewFragment(View view);

    public abstract void onAttachFragment(Activity activity);

    protected View view;

    private SuperActivity mActivity;

    private LinearLayout ll;

    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateFragment(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(onInflateViewFragment(), container, false);

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View pbView = inflater.inflate(R.layout.layout_progress_bar, null);

        mDilatingDotsProgressBar = ButterKnife.findById(pbView, R.id.progress);

        ll = (LinearLayout) pbView.findViewById(R.id.llRoot);

        ll.addView(view);

        ButterKnife.bind(this, pbView);
        view = onCreateViewFragment(pbView);
        return pbView;
    }

    protected void showProgressDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.setCancelable(false);
        }
        loadingDialog.show();
    }

    protected void closeProgressDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (SuperActivity) context;
        onAttachFragment(mActivity);
    }

    public void showLoading() {
        mDilatingDotsProgressBar.setVisibility(View.VISIBLE);
        ll.setVisibility(View.GONE);
    }

    public void hideLoading() {
        mDilatingDotsProgressBar.setVisibility(View.GONE);
        ll.setVisibility(View.VISIBLE);
    }

    protected void showToast(String text) {
        mActivity.showToast(text);
    }

    protected void showToast(int res) {
        mActivity.showToast(res);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onCreate");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onCreate");
    }
}
