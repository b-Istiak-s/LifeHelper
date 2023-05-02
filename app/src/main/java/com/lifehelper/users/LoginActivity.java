package com.lifehelper.users;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lifehelper.MainActivity;
import com.lifehelper.R;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.HashConverter;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etxtUsername, etxtPassword;
    Button btnLogin;
    TextView forgotPass, register;
    private ProgressDialog loading;
    SharedPreference sharedPreference = new SharedPreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etxtUsername = findViewById(R.id.username_login_etxt);
        etxtPassword = findViewById(R.id.password_edittext);
        btnLogin = findViewById(R.id.btnLogin);
        forgotPass = findViewById(R.id.forget_password_textview);
        register = findViewById(R.id.register_textview);

        btnLogin.setOnClickListener(v->{
            String username = etxtUsername.getText().toString().trim();
            String password = etxtPassword.getText().toString().trim();



            if(username.isEmpty()){
                etxtUsername.setError("Enter your username");
                etxtUsername.requestFocus();
            }else if (password.isEmpty()){
                etxtPassword.setError("Enter your password");
                etxtPassword.requestFocus();
            }else{
                login(username,password);
            }
        });

        forgotPass.setOnClickListener(v->{
            String username = etxtUsername.getText().toString().trim();
            if(username.isEmpty()){
                etxtUsername.setError("Enter your username then click on forgot password");
                etxtUsername.requestFocus();
            }else{
                forgotPass(username);
            }
        });

        register.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void login(String username, String password){
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

                    String[] array = message.split(" ");
                    sharedPreference.saveToSharedPref(array[0],hashConverter.textToHash(password),LoginActivity.this,array[1]);
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (value.equals("not_activated")){
                    loading.dismiss();
                    AlertDialog.Builder alertDialog;
                    alertDialog = new AlertDialog.Builder(LoginActivity.this);
                    alertDialog.setTitle("Check your email and enter the verification code in the following field");

                    final EditText input = new EditText(LoginActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    alertDialog.setView(input);


                    alertDialog.setPositiveButton("Submit", (dialogInterface, i) -> {
                        String verificationCode = input.getText().toString().trim();
                        if (verificationCode.isEmpty()){
                            input.setError("Please enter verification code");
                            input.requestFocus();
                        }else if (!verificationCode.equals(message.trim())){
                            Toast.makeText(LoginActivity.this, "Please recheck the verification code.", Toast.LENGTH_SHORT).show();
                        }else{
                            resend(username, "email","activate",password);
                        }
                    });
                    alertDialog.setNeutralButton("Resend code", (dialogInterface, i) -> {
                        resend(username, "email","send",password);
                        loading.dismiss();
                    });
                    alertDialog.show();
                }else if (value.equals("failure")){
                    Toast.makeText(LoginActivity.this, "Username or password is wrong.",Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }else{
                    Toast.makeText(LoginActivity.this, message,Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {

                loading.dismiss();
                Toast.makeText(LoginActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resend(String username, String email, String type,String password){
        loading=new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Contacts> call = apiInterface.resendMail(username, email, type);
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {

                String value = response.body().getValue();
                String message = response.body().getMessage();

                if (value.equals("sent"))
                {
                    loading.dismiss();

                    AlertDialog.Builder alertDialog;
                    alertDialog = new AlertDialog.Builder(LoginActivity.this);
                    alertDialog.setTitle("Check your email and enter the verification code in the following field");

                    final EditText input = new EditText(LoginActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    alertDialog.setView(input);

                    alertDialog.setPositiveButton("Submit", (dialogInterface, i) -> {
                        String verificationCode = input.getText().toString().trim();
                        if (verificationCode.isEmpty()){
                            input.setError("Please enter verification code");
                            input.requestFocus();
                        }else if (!verificationCode.equals(message.trim())){
                            Toast.makeText(LoginActivity.this, "Please recheck the verification code.", Toast.LENGTH_SHORT).show();
                        }else{
                            resend(username, email,"activate",password);
                        }
                    });

                    alertDialog.show();
                }
                else if(value.equals("activated")){
                    loading.dismiss();
                    sharedPreference.saveToSharedPref(username,password,LoginActivity.this,message);
                    Toast.makeText(LoginActivity.this, "Successfully verified your account.", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Something went wrong. "+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(LoginActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void forgotPass(String username){
        loading=new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Contacts> call = apiInterface.forgotPass(username);
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {

                String value = response.body().getValue();
                String message = response.body().getMessage();


                if (value.equals("sent"))
                {
                    loading.dismiss();
                    String[] email_verification_code = message.split(",");
                    String email = email_verification_code[0];
                    String separateEmail[] = email.split("@");
                    String emailUsername = separateEmail[0].substring(0,2)+"****"+separateEmail[0].substring(Math.max(separateEmail[0].length()-2,0));
                    String verification_code = email_verification_code[1];

                    AlertDialog.Builder alertDialog;
                    alertDialog = new AlertDialog.Builder(LoginActivity.this);
                    alertDialog.setTitle("Check your email ("+emailUsername+separateEmail[1]+") and enter the verification code in the following field");

                    final EditText input = new EditText(LoginActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    alertDialog.setView(input);

                    alertDialog.setPositiveButton("Submit", (dialogInterface, i) -> {
                        String verificationCode = input.getText().toString().trim();
                        if (verificationCode.isEmpty()){
                            input.setError("Please enter verification code");
                            input.requestFocus();
                        }else if (!verificationCode.equals(verification_code.trim())){
                            Toast.makeText(LoginActivity.this, "Please recheck the verification code.", Toast.LENGTH_SHORT).show();
                        }else if(verificationCode.equals(verification_code)){
                            changePass(username);
                        }else{
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.show();
                }
                else if(value.equals("failure")){
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    loading.dismiss();

                }else{
                    Toast.makeText(LoginActivity.this, "Something went wrong. "+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(LoginActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePass(String username){
        AlertDialog.Builder alertDialog;
        alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("Enter new password");

        final EditText input = new EditText(LoginActivity.this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Submit", (dialogInterface, i) -> {
            String password = input.getText().toString().trim();
            if (password.isEmpty()){
                input.setError("Please enter new password");
                input.requestFocus();
            }else{
                loading=new ProgressDialog(this);
                loading.setMessage("Please wait....");
                loading.show();

                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                HashConverter hashConverter = new HashConverter();
                Call<Contacts> call = apiInterface.changePass(hashConverter.textToHash(password),username);
                call.enqueue(new Callback<Contacts>() {
                    @Override
                    public void onResponse(Call<Contacts> call, Response<Contacts> response) {

                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if (value.equals("succeed"))
                        {
                            Toast.makeText(LoginActivity.this, "Your password has changed successfully.", Toast.LENGTH_SHORT).show();
                        }
                        else if(value.equals("failure")){
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(LoginActivity.this, "Something went wrong. "+message, Toast.LENGTH_SHORT).show();
                        }
                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Contacts> call, Throwable t) {
                        loading.dismiss();
                        Toast.makeText(LoginActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        alertDialog.show();
    }
}