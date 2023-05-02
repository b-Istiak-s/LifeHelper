package com.lifehelper.users;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.lifehelper.Constants;
import com.lifehelper.R;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.HashConverter;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {


    EditText etxtUsername, etxtMail, etxtPassword, etxtReenterPass, etxtFullName, etxtPhone;
    TextView haveAnAccount;
    Button btnRegistration;

    double longitude = 0;
    double latitude;
    FusedLocationProviderClient client;
    static final String[] userType = new String[]{"Member", "Restaurant management", "Food seller", "Food Delivery", "Online Doctor"};
    Spinner spinnerUserType;
    private ProgressDialog loading;
    int userTypePosition;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        View layout = findViewById(R.id.RegistrationMainLayout);

        etxtUsername = findViewById(R.id.username_edittext);
        etxtMail = findViewById(R.id.etxt_email);
        etxtPassword = findViewById(R.id.enterpass_edittext);
        etxtReenterPass = findViewById(R.id.reenterpass_edittext);
        etxtFullName = findViewById(R.id.fullname_edittext);
        etxtPhone = findViewById(R.id.phone_edittext);
        btnRegistration = findViewById(R.id.register_button);
        spinnerUserType = findViewById(R.id.spinnerUserType);
        haveAnAccount = findViewById(R.id.haveAnAccount);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0xFF616261, 0xFF131313});
        gd.setCornerRadius(0f);

        layout.setBackground(gd);

        ArrayAdapter<String> adapterUserType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userType);
        spinnerUserType.setAdapter(adapterUserType);

        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userTypePosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnRegistration.setOnClickListener(v -> {
            String username = etxtUsername.getText().toString().trim();
            String email = etxtMail.getText().toString().trim();
            String password = etxtPassword.getText().toString().trim();
            String reEnterPass = etxtReenterPass.getText().toString().trim();
            String fullName = etxtFullName.getText().toString().trim();
            String phone = etxtPhone.getText().toString().trim();

            if (username.isEmpty()) {
                etxtUsername.setError("Please enter username");
                etxtUsername.requestFocus();
            } else if (email.isEmpty()) {
                etxtMail.setError("Please enter your email address");
                etxtMail.requestFocus();
            } else if (password.isEmpty()) {
                etxtPassword.setError("Please enter password");
                etxtPassword.requestFocus();
            } else if (reEnterPass.isEmpty()) {
                etxtReenterPass.setError("Please re-enter password");
                etxtReenterPass.requestFocus();
            } else if (fullName.isEmpty()) {
                etxtFullName.setError("Please enter your full name");
                etxtFullName.requestFocus();
            } else if (phone.isEmpty()) {
                etxtPhone.setError("Please enter your phone number");
                etxtPhone.requestFocus();
            } else {
                if (password.equals(reEnterPass)) {
                    Registration(username, email, password, fullName, phone, String.valueOf(latitude), String.valueOf(longitude), Constants.userType[userTypePosition]);
                } else {
                    Snackbar.make(v, "Your password do not match", Snackbar.LENGTH_SHORT).show();
                }
            }

        });

        haveAnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        client = LocationServices.getFusedLocationProviderClient(RegistrationActivity.this);

        client.getLastLocation().addOnSuccessListener(RegistrationActivity.this, location -> {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        });
    }


    private void Registration(String username, String email, String password, String fullName, String phone, String latitude, String longitude, String userType){
        loading=new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();

        HashConverter hashConverter = new HashConverter();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Contacts> call = apiInterface.signUpUser(username, email, hashConverter.textToHash(password), fullName, phone, latitude, longitude, userType);
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {


                String value = response.body().getValue();
                String message = response.body().getMessage();

                if (value.equals("success"))
                {
                    loading.dismiss();

                    AlertDialog.Builder alertDialog;
                    alertDialog = new AlertDialog.Builder(RegistrationActivity.this);
                    alertDialog.setTitle("Check your email and enter the verification code in the following field");

                    final EditText input = new EditText(RegistrationActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    alertDialog.setView(input);

                    alertDialog.setPositiveButton("Submit", (dialogInterface, i) -> {
                        String verificationCode = input.getText().toString().trim();
                        if (verificationCode.isEmpty()){
                            input.setError("Please enter verification code");
                            input.requestFocus();
                        }else if (!verificationCode.equals(message.trim())){
                            Toast.makeText(RegistrationActivity.this, "Please recheck the verification code.", Toast.LENGTH_SHORT).show();
                        }else{
                            resend(username, email,"activate");
                        }
                    });
                    alertDialog.setNeutralButton("Resend code", (dialogInterface, i) -> {
                        resend(username, email,"send");
                    });
                    alertDialog.show();
                }
                else {
                    loading.dismiss();
                    Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {

                loading.dismiss();
                Toast.makeText(RegistrationActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void resend(String username, String email, String type){
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
                    alertDialog = new AlertDialog.Builder(RegistrationActivity.this);
                    alertDialog.setTitle("Check your email and enter the verification code in the following field");

                    final EditText input = new EditText(RegistrationActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    alertDialog.setView(input);

                    alertDialog.setPositiveButton("Submit", (dialogInterface, i) -> {
                        String verificationCode = input.getText().toString().trim();
                        if (verificationCode.isEmpty()){
                            input.setError("Please enter verification code");
                            input.requestFocus();
                        }else if (!verificationCode.equals(message.trim())){
                            Toast.makeText(RegistrationActivity.this, "Please recheck the verification code.", Toast.LENGTH_SHORT).show();
                        }else{
                            resend(username, email,"activate");
                        }
                    });

                    alertDialog.show();
                }
                else if(value.equals("activated")){
                    loading.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Successfully created and verified your account.", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(RegistrationActivity.this, "Something went wrong. "+message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(RegistrationActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}