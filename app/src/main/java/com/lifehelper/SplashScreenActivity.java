package com.lifehelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lifehelper.model.Contacts;
import com.lifehelper.model.HashConverter;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;
import com.lifehelper.users.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.R){
            if (!Environment.isExternalStorageManager()){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package",SplashScreenActivity.this.getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
            }
        }
        SharedPreference sharedPreference = new SharedPreference();
        if (sharedPreference.getUsernameSharedPref(SplashScreenActivity.this).isEmpty()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            login(sharedPreference.getUsernameSharedPref(SplashScreenActivity.this),sharedPreference.getPasswordSharedPref(SplashScreenActivity.this));
        }
    }

    public void login(String username, String password){
        loading=new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        HashConverter hashConverter = new HashConverter();

        Call<Contacts> call = apiInterface.loginUser(username, hashConverter.textToHash(password));
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {

                String value = response.body().getValue();
                String message = response.body().getMessage();

                if (value.equals("succeed"))
                {
                    loading.dismiss();
                    Intent intent=new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if (value.equals("failure")){
                    Toast.makeText(SplashScreenActivity.this, "Username or password has been changed.",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SplashScreenActivity.this, LoginActivity.class);
                    SharedPreference sharedPreference = new SharedPreference();
                    sharedPreference.saveToSharedPref("","",SplashScreenActivity.this,"");
                    startActivity(intent);
                    finish();
                    loading.dismiss();
                }else{
                    Toast.makeText(SplashScreenActivity.this, message,Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(SplashScreenActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}