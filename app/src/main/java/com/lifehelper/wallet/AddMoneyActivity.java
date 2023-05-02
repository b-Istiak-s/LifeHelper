package com.lifehelper.wallet;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lifehelper.R;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.HashConverter;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;
import com.lifehelper.users.LoginActivity;
import com.lifehelper.wallet.SQL.Sqlite;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;

public class AddMoneyActivity extends AppCompatActivity {

    EditText etxtUsername, etxtAmount, etxtPassword;
    Button btnAddNow;
    private String paymentIntentClientSecret;
    private PaymentSheet paymentSheet;
    SharedPreference sharedPreference = new SharedPreference();
    private String username, amount, password;
    HashConverter hashConverter = new HashConverter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        Stripe.setAdvancedFraudSignalsEnabled(false);
        
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_TYooMQauvdEDq54NiTphI7jx"
        );
        
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);


        etxtUsername = findViewById(R.id.etxtUsernameAddMoney);
        etxtAmount = findViewById(R.id.etxtAmountAddMoney);
        etxtPassword = findViewById(R.id.etxtPasswordAddMoney);
        btnAddNow = findViewById(R.id.btnMoneyAddNow);

        etxtUsername.setText(sharedPreference.getUsernameSharedPref(AddMoneyActivity.this));
        btnAddNow.setOnClickListener(v->{
            username = etxtUsername.getText().toString().trim();
            amount = etxtAmount.getText().toString().trim();
            password = etxtPassword.getText().toString().trim();
            if (username.isEmpty()){
                sharedPreference.saveToSharedPref("","", AddMoneyActivity.this,"");
                Intent logoutIntent = new Intent(AddMoneyActivity.this, LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(this, "Something went wrong. You are logged out.", Toast.LENGTH_SHORT).show();
                startActivity(logoutIntent);
                finish();
            }else if (amount.isEmpty()){
                etxtAmount.setError("Enter amount of money you want to add");
                etxtAmount.requestFocus();
            }else if (password.isEmpty()){
                etxtPassword.setError("Enter your password");
                etxtPassword.requestFocus();
            }else if (!hashConverter.textToHash(password).equals(sharedPreference.getPasswordSharedPref(AddMoneyActivity.this))){
                Toast.makeText(this, "You have entered incorrect password", Toast.LENGTH_SHORT).show();
            }
            else {
                fetchPaymentIntent(username, amount+"00", hashConverter.textToHash(password), v);
            }
        });
    }
    private void showAlert(String title, @Nullable String message) {
        runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Ok", null)
                    .create();
            dialog.show();
        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    private void fetchPaymentIntent(String username, String amount, String password,View view) {
        final String cartContent = "Add to wallet";

//        final RequestBody requestBody = RequestBody.create(
//                MediaType.parse("application/json; charset=utf-8"),
//                cartContent
//        );
//
//        Request request = new Request.Builder()
//                .url(Constants.BASE_URL + "/life_helper/users/wallet/add.php")
//                .post(requestBody)
//                .build();
//
//        new OkHttpClient()
//                .newCall(request)
//                .enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Request request, IOException e) {
//                        showAlert("Failed to load data", "Error: " + e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(Response response) {
//                        if (!response.isSuccessful()) {
//                            showAlert(
//                                    "Failed to load page",
//                                    "Error: " + response
//                            );
//                        } else {
//                            final JSONObject responseJson = parseResponse(response.body());
//                            paymentIntentClientSecret = responseJson.optString("clientSecret");
//                            runOnUiThread(() -> btnAddNow.setEnabled(true));
//                            Log.i("payment_intent", "Retrieved PaymentIntent");
//                        }
//                    }
//                });


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Contacts> call = apiInterface.addMoneyToWallet(username,cartContent,password,amount);
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, retrofit2.Response<Contacts> response) {

                if (!response.isSuccessful()){
                    showAlert(
                            "Failed to load page",
                            "Error: " + response
                    );
                }else{
                    paymentIntentClientSecret = response.body().getClientSecret();
                    onPayClicked(view);
                    Log.i("payment_intent", "Retrieved PaymentIntent");
                }

            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {

                Toast.makeText(AddMoneyActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onPayClicked(View view) {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration("Life helper, Inc.");
//        configuration.setAllowsDelayedPaymentMethods(true);
//        configuration.setPrimaryButtonColor(ColorStateList.valueOf(Color.rgb(248, 72, 94)));
//        configuration.setGooglePay(new PaymentSheet.GooglePayConfiguration(
//                PaymentSheet.GooglePayConfiguration.Environment.Test,
//                "US"
//        ));
//        configuration.getGooglePay();
//        configuration.getAllowsDelayedPaymentMethods();

        // Present Payment Sheet
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration);
    }

    private void onPaymentSheetResult(
            final PaymentSheetResult paymentSheetResult
    ) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            addToDB(currentDateandTime,String.valueOf(TimeZone.getDefault()));
            showToast("Payment complete!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            showToast("Payment canceled");
            Log.i("payment", "Payment canceled!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Throwable error = ((PaymentSheetResult.Failed) paymentSheetResult).getError();
            showAlert("Payment failed", error.getLocalizedMessage());
        }
    }

    private void addToDB(String datetime, String timezone){
        Sqlite sqlite = new Sqlite(AddMoneyActivity.this);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Contacts> call = apiInterface.addMoneyToDB(username,hashConverter.textToHash(password),amount,datetime,timezone);
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, retrofit2.Response<Contacts> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();

                if (value.equals("completed")) {
                    showToast("Successfully added to database");
                    Log.d("SQLITE_RESULT", String.valueOf(sqlite.insertData(username,hashConverter.textToHash(password),amount,datetime, String.valueOf(TimeZone.getDefault()),"inserted")));
                    Intent intent = new Intent(AddMoneyActivity.this,WalletActivity.class);
                    startActivity(intent);
                    finish();
                }else if (value.equals("inserted_to_wallet_only")){
                    showToast("Wallet has been updated.");
                    Log.d("SQLITE_RESULT", String.valueOf(sqlite.insertData(username,hashConverter.textToHash(password),amount,datetime, String.valueOf(TimeZone.getDefault()),"only_inserted_wallet")));
                }else if (value.equals("not_inserted")){
                    showToast("Data wasn't inserted to DB.");
                    Log.d("SQLITE_RESULT", String.valueOf(sqlite.insertData(username,hashConverter.textToHash(password),amount,datetime, String.valueOf(TimeZone.getDefault()),"not_in_db")));
                }else if (value.equals("password_not_matched")){
                    showToast("Password didn't match.");
                    Log.d("SQLITE_RESULT", String.valueOf(sqlite.insertData(username,hashConverter.textToHash(password),amount,datetime, String.valueOf(TimeZone.getDefault()),"password_wrong")));
                }
                else{
                    Toast.makeText(AddMoneyActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                Log.d("SQLITE_RESULT", String.valueOf(sqlite.insertData(username,hashConverter.textToHash(password),amount,datetime, String.valueOf(TimeZone.getDefault()),"not_in_db")));
                Toast.makeText(AddMoneyActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}