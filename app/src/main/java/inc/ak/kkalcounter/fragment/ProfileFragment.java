package inc.ak.kkalcounter.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.OnClick;
import inc.ak.kkalcounter.activity.EditProfile;
import inc.ak.kkalcounter.Preferences;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.activity.SplashScreen;


public class ProfileFragment extends SuperFragment {

    public static final String EXTRA = "extra_search_item";
    public static final String TAG = "MyLog";
    ProgressDialog progress;
    AlertDialog.Builder ad;
    Context context;

    @Bind(R.id.avatar)
    ImageView avatar;

    @Bind(R.id.edit)
    Button edit;

    @Bind(R.id.param_value)
    TextView params;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.email)
    TextView email;

    @Bind(R.id.exit)
    TextView exit;






    @Override
    public int onInflateViewFragment() {
        return R.layout.profile_fragment;
    }

    @Override
    public void onCreateFragment(Bundle instance) {

    }

    @Override
    public void onResume() {
        super.onResume();
        hideLoading();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    String sex;
    @Override
    public View onCreateViewFragment(View view) {
        Picasso.with(view.getContext()).load(Preferences.getString(Preferences.ACCOUNT_IMG)).placeholder(R.drawable.placeholder)
                .into(avatar, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        avatar.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                        Bitmap imageBitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        avatar.setImageDrawable(imageDrawable);

                    }
                });
        switch (Integer.valueOf(Preferences.getString(Preferences.SEX))){
            case 0:
                sex="Женский";
                break;
            case 1:
                sex="Все сложно";
                break;
            case 2:
                sex="Мужской";
                break;


        }
        params.setText(getActivity().getResources().getString(R.string.params,  Preferences.getString(Preferences.WEIGHT),
                                                                                Preferences.getString(Preferences.HEIGHT),
                                                                                Preferences.getString(Preferences.AGE),
                                                                                sex));
        name.setText(Preferences.getString(Preferences.ACCOUNT_NAME));
        email.setText(Preferences.getString(Preferences.ACCOUNT));
        return view;

    }

    @Override
    public void onAttachFragment(Activity activity) {


    }
    @OnClick({R.id.edit,R.id.exit})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.edit:
                startActivity(new Intent(getContext(),EditProfile.class));

                break;
            case R.id.exit:
                Preferences.addString(Preferences.ACCOUNT, "");
                Preferences.addString(Preferences.ACCOUNT_NAME, "");
                Preferences.addString(Preferences.ACCOUNT_IMG, "");
                startActivity(new Intent(getContext(),SplashScreen.class));

                break;
        }

    }

}

