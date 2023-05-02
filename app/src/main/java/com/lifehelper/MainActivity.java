package com.lifehelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lifehelper.adapters.AdapterPeopleData;
import com.lifehelper.adapters.AdapterRestaurantsData;
import com.lifehelper.dating.DatingActivity;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.Scrolled;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;
import com.lifehelper.restaurants.RestaurantsActivity;
import com.lifehelper.users.LoginActivity;
import com.lifehelper.users.ProfileActivity;
import com.lifehelper.wallet.WalletActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerViewRestaurants, recyclerViewFoodOrder,recyclerViewOnlineDoctor,recyclerViewDating;
    RecyclerView.LayoutManager layoutManagerDating;
    SharedPreference sharedPreference = new SharedPreference();
    List<Contacts> contacts = new ArrayList<>();
    List<Contacts> contacts1 = new ArrayList<>();
    TextView txtDatingShowMore, txtRestaurantsShowMore, txtFoodOrderShowMore,txtOnlineDoctorShowMore;
    int i=-10,j=-10;
    Button btnProfile, btnWallet, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);
        recyclerViewFoodOrder = findViewById(R.id.recyclerViewFoodOrder);
        recyclerViewOnlineDoctor = findViewById(R.id.recyclerViewOnlineDoctor);
        recyclerViewDating = findViewById(R.id.recyclerViewDating);
        txtDatingShowMore = findViewById(R.id.txtShowMoreDating);
        txtRestaurantsShowMore = findViewById(R.id.txtShowMoreRestaurants);
        txtFoodOrderShowMore = findViewById(R.id.txtShowMoreFoodOrder);
        txtOnlineDoctorShowMore = findViewById(R.id.txtShowMoreOnlineDoctor);
        btnProfile = findViewById(R.id.btnProfile);
        btnWallet = findViewById(R.id.btnWallet);
        btnLogout = findViewById(R.id.btnLogout);

        getSupportActionBar().hide();

        layoutManagerDating = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);

        recyclerViewRestaurants.setLayoutManager(new GridLayoutManager(this,2));
//        recyclerViewFoodOrder.setLayoutManager(layoutManager);
//        recyclerViewOnlineDoctor.setLayoutManager(layoutManager);
        recyclerViewDating.setLayoutManager(layoutManagerDating);

        getPeopleData();
        recyclerViewDating.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Scrolled scrolled = new Scrolled();
                if(scrolled.isLastItemDisplaying(recyclerView)){
                    Log.i("Reached end: ", "Load more");
                    getPeopleData();
                }
            }
        });

        getRestaurantsData("city");
        recyclerViewRestaurants.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Scrolled scrolled = new Scrolled();
                if(scrolled.isLastItemDisplaying(recyclerView)){
                    Log.i("Reached end: ", "Load more");
                    if (j<15) {
                        getRestaurantsData("");
                    }
                }
            }
        });

        txtRestaurantsShowMore.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
            startActivity(intent);
        });
        txtDatingShowMore.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, DatingActivity.class);
            startActivity(intent);
        });
        btnProfile.setOnClickListener(v->{
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });
        btnWallet.setOnClickListener(v->{
            Intent intent  = new Intent(MainActivity.this, WalletActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(v->{
            SharedPreference sharedPreference = new SharedPreference();
            sharedPreference.saveToSharedPref("","",MainActivity.this,"");
            Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
            finish();
        });
    }


    public void getPeopleData(){
        i=i+10;
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Contacts>> call = apiInterface.getPeopleData(sharedPreference.getUsernameSharedPref(MainActivity.this),10,i,"all");
        call.enqueue(new Callback<List<Contacts>>() {
            @Override
            public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                    if (response.body().size() != 0) {
                        if (i == 0) {
                            contacts = response.body();
                        } else {
                            contacts.addAll(response.body());
                            Log.d("CONTACT_DATA1",contacts.toString());
                        }
                        Log.d("CONTACT_DATA",contacts.toString());
                        AdapterPeopleData adapterPeopleData = new AdapterPeopleData(contacts, MainActivity.this);
                        recyclerViewDating.setAdapter(adapterPeopleData);
                        adapterPeopleData.notifyDataSetChanged();
                    }
            }

            @Override
            public void onFailure(Call<List<Contacts>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d("THROWABLE",t.toString());
            }
        });
    }
    public void getRestaurantsData(String city){
        j=j+10;
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Contacts>> call = apiInterface.getRestaurantsData(sharedPreference.getUsernameSharedPref(this),10,j,city);
        call.enqueue(new Callback<List<Contacts>>() {
            @Override
            public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                if (response.body().size()!=0) {
                    if (j == 0) {
                        contacts1 = response.body();
                    } else {
                        contacts1.addAll(response.body());
                    }
                    AdapterRestaurantsData adapterRestaurantsData = new AdapterRestaurantsData(contacts1, MainActivity.this);
                    recyclerViewRestaurants.setAdapter(adapterRestaurantsData);
                    adapterRestaurantsData.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Contacts>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d("THROWABLE",t.toString());
            }
        });
    }
}