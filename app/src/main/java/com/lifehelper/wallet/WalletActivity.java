package com.lifehelper.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lifehelper.R;
import com.lifehelper.adapters.AdapterTransaction;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.Scrolled;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {

    Button goBackBtn, btnAddMoney;
    List<Contacts> contacts;
    int i=0;
    RecyclerView recyclerView;
    SharedPreference sharedPreference = new SharedPreference();
    Scrolled scrolled = new Scrolled();
    TextView txtAvailableMoneyInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        getSupportActionBar().hide();
        goBackBtn = findViewById(R.id.goBackbtn);
        btnAddMoney = findViewById(R.id.btnAddMoney);
        recyclerView = findViewById(R.id.recyclerViewTransaction);
        txtAvailableMoneyInAccount = findViewById(R.id.txtAvailableMoneyInAccount);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().hide();

        goBackBtn.setOnClickListener(v-> finish());
        btnAddMoney.setOnClickListener(v->{
            Intent intent = new Intent(WalletActivity.this, AddMoneyActivity.class);
            startActivity(intent);
        });

        transactionDetails();
        moneyAvailable();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (scrolled.isLastItemDisplaying(recyclerView)){
                    transactionDetails();
                }
            }
        });
    }

    private void transactionDetails(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Contacts>> call = apiInterface.getTransactionDetails(sharedPreference.getUsernameSharedPref(WalletActivity.this));
        call.enqueue(new Callback<List<Contacts>>() {
            @Override
            public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                if (response.body().size() != 0) {
                    if (i == 0) {
                        contacts = response.body();
                    } else {
                        contacts.addAll(response.body());
                    }
                    AdapterTransaction adapterPeopleData = new AdapterTransaction(contacts, WalletActivity.this);
                    recyclerView.setAdapter(adapterPeopleData);
                    adapterPeopleData.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Contacts>> call, Throwable t) {
                Toast.makeText(WalletActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moneyAvailable(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Contacts> call = apiInterface.getAvailableMoneyData(sharedPreference.getUsernameSharedPref(WalletActivity.this));
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                String money = response.body().getAmount();

                txtAvailableMoneyInAccount.setText("Avaialble : $"+money);
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {

            }
        });
    }
}