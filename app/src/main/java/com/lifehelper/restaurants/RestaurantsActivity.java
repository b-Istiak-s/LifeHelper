package com.lifehelper.restaurants;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lifehelper.Constants;
import com.lifehelper.R;
import com.lifehelper.adapters.AdapterRestaurantsData;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.Scrolled;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.model.SuggestionProvider;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsActivity extends AppCompatActivity {

    SharedPreference sharedPreference = new SharedPreference();
    RecyclerView recyclerView;
    List<Contacts> contacts;
    int i=-10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        recyclerView = findViewById(R.id.recyclerViewRestaurants);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        getRestaurantsData("city");

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            contacts.clear();
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            getRestaurantsData(query);
        }

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
                    getRestaurantsData("");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        if (!sharedPreference.getUserType(RestaurantsActivity.this).equals(Constants.userType[1])) {
            menu.removeItem(0);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menuCreate:
                Intent intent = new Intent(RestaurantsActivity.this, CreateRestaurantActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuStarred:
                Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuBooked:
                Toast.makeText(this, "hell", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getRestaurantsData(String city){
        i=i+10;
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Contacts>> call = apiInterface.getRestaurantsData(sharedPreference.getUsernameSharedPref(RestaurantsActivity.this),10,i,city);
        call.enqueue(new Callback<List<Contacts>>() {
            @Override
            public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                if (response.body().size()!=0) {
                    if (i == 0) {
                        contacts = response.body();
                    } else {
                        contacts.add(response.body().get(response.body().size() - 1));
                    }
                    AdapterRestaurantsData adapterRestaurantsData = new AdapterRestaurantsData(contacts, RestaurantsActivity.this);
                    recyclerView.setAdapter(adapterRestaurantsData);
                    adapterRestaurantsData.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Contacts>> call, Throwable t) {
                Toast.makeText(RestaurantsActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d("THROWABLE",t.toString());
            }
        });
    }
}