package com.lifehelper.restaurants;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.lifehelper.Constants;
import com.lifehelper.R;
import com.lifehelper.adapters.AdapterFeed;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.Scrolled;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView star1,star2,star3,star4,star5;
    TextView txtFieldOfRestaurant;
    int rateNumber,restaurantId;
    List<Contacts> contacts;
    int i=0;
    AdapterFeed adapterFeed;
    AppCompatImageView imgRestaurantDetailsLogo;
    ImageView imgUsrProfile;
    SharedPreference sharedPreference = new SharedPreference();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbarLayout.setTitle(getIntent().getStringExtra(Constants.RESTAURANT_NAME));
//        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
//        toolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.holo_blue_dark));
        restaurantId = Integer.parseInt(getIntent().getStringExtra(Constants.ID));


        recyclerView = findViewById(R.id.recyclerViewFeed);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        imgRestaurantDetailsLogo = findViewById(R.id.imgRestaurantDetailsLogo);
        txtFieldOfRestaurant = findViewById(R.id.txtTextFieldOfRestaurant);
        imgUsrProfile = findViewById(R.id.imgUsrProfilePicture);

        recyclerView.setLayoutManager(new LinearLayoutManager(RestaurantDetailsActivity.this));

        Glide
                .with(this)
                .load(Constants.BASE_URL + "/life_helper/restaurants/" + getIntent().getStringExtra(Constants.RESTAURANT_LOGO))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imgRestaurantDetailsLogo);
        
        Glide
                .with(this)
                .load(Constants.BASE_URL+"/life_helper/users/"+sharedPreference.getUsernameSharedPref(RestaurantDetailsActivity.this)+"/profile.jpg")
                .into(imgUsrProfile);

        restaurantFeed();

        txtFieldOfRestaurant.setOnClickListener(v->{
            Intent intent = new Intent(RestaurantDetailsActivity.this,CreateFeedPostActivity.class);
            intent.putExtra(Constants.ID,restaurantId);
            startActivity(intent);
        });

        star1.setOnClickListener(v->{
            star1.setImageResource(R.drawable.blue_star);
            star2.setImageResource(R.drawable.star);
            star3.setImageResource(R.drawable.star);
            star4.setImageResource(R.drawable.star);
            star5.setImageResource(R.drawable.star);
            rateNumber = 1;
        });
        star2.setOnClickListener(v->{
            star1.setImageResource(R.drawable.blue_star);
            star2.setImageResource(R.drawable.blue_star);
            star3.setImageResource(R.drawable.star);
            star4.setImageResource(R.drawable.star);
            star5.setImageResource(R.drawable.star);
            rateNumber = 2;
        });
        star3.setOnClickListener(v->{
            star1.setImageResource(R.drawable.blue_star);
            star2.setImageResource(R.drawable.blue_star);
            star3.setImageResource(R.drawable.blue_star);
            star4.setImageResource(R.drawable.star);
            star5.setImageResource(R.drawable.star);
            rateNumber = 3;
        });
        star4.setOnClickListener(v->{
            star1.setImageResource(R.drawable.blue_star);
            star2.setImageResource(R.drawable.blue_star);
            star3.setImageResource(R.drawable.blue_star);
            star4.setImageResource(R.drawable.blue_star);
            star5.setImageResource(R.drawable.star);
            rateNumber = 4;
        });
        star5.setOnClickListener(v->{
            star1.setImageResource(R.drawable.blue_star);
            star2.setImageResource(R.drawable.blue_star);
            star3.setImageResource(R.drawable.blue_star);
            star4.setImageResource(R.drawable.blue_star);
            star5.setImageResource(R.drawable.blue_star);
            rateNumber = 5;
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    restaurantFeed();
                }
            }
        });
    }
    private void restaurantFeed(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Contacts>> call = apiInterface.getRestaurantFeed(String.valueOf(restaurantId),10,i);
        call.enqueue(new Callback<List<Contacts>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Contacts>> call, @NonNull Response<List<Contacts>> response) {
                if (response.body()!=null){
                    if(i==0){
                        contacts = response.body();
                    }else {
                        contacts.addAll(0, response.body());
                    }
                    adapterFeed = new AdapterFeed(contacts, RestaurantDetailsActivity.this);
                    recyclerView.setAdapter(adapterFeed);
                    adapterFeed.notifyDataSetChanged();
                    i = i + response.body().size();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Contacts>> call, @NonNull Throwable t) {
                Toast.makeText(RestaurantDetailsActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d("THROWABLE", t.toString());
            }
        });
    }
}
