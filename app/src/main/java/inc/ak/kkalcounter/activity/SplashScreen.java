package inc.ak.kkalcounter.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import inc.ak.kkalcounter.KkalCounter;
import inc.ak.kkalcounter.Preferences;
import inc.ak.kkalcounter.R;
import inc.ak.kkalcounter.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends SuperActivity implements GoogleApiClient.OnConnectionFailedListener
{
    @Bind(R.id.sign_in_button)
    Button signInButton;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Preferences.getString(Preferences.ACCOUNT).equals(""))
                    signInButton.setVisibility(View.VISIBLE);
                else
                {
                    if(Preferences.getString(Preferences.SEX).equals("")){
                        Intent intent = new Intent(getBaseContext(), EditProfile.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }},2000);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    public void checkUser(String email){
        KkalCounter.getApi().getUser("email eq '"+email+"'").enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                askUpdate(response.body().get(0).getId());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("MyLog", String.valueOf(t));
                startActivity(new Intent(getBaseContext(),EditProfile.class));

            }
        });
    }

    public void askUpdate(final String id){
        new MaterialDialog.Builder(this)
                .title("Обновить")
                .content("Хотите обновить ваши данные?")
                .positiveText("Да")
                .negativeText("Нет")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getBaseContext(),EditProfile.class).putExtra("update",1).putExtra("id",id));

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getBaseContext(),EditProfile.class));

                    }
                })
                .show();
    }



    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1);
    }
    @OnClick(R.id.sign_in_button)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            try {
                Preferences.addString(Preferences.ACCOUNT, acct.getEmail());
                Preferences.addString(Preferences.ACCOUNT_NAME, acct.getDisplayName());
                Preferences.addString(Preferences.ACCOUNT_IMG, String.valueOf(acct.getPhotoUrl()));
            }catch (Exception e){
                Log.i("MyLog", String.valueOf(e));

            }
            Log.i("MyLog",acct.getEmail());
            Log.i("MyLog",acct.getDisplayName());
            Log.i("MyLog", String.valueOf(acct.getPhotoUrl()));

            checkUser(acct.getEmail());

        } else {
            // Signed out, show unauthenticated UI.
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
