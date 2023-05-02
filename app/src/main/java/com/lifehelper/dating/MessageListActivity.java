package com.lifehelper.dating;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lifehelper.R;
import com.lifehelper.adapters.AdapterMessageList;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.Scrolled;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    int i=-10;
    List<Contacts> contacts;
    SharedPreference sharedPreference = new SharedPreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        recyclerView = findViewById(R.id.recyclerViewMessgaeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        messageLists();
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
                    messageLists();
                }
            }
        });
    }
    private void messageLists(){
        i=i+10;
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Contacts>> call = apiInterface.getMessageList(sharedPreference.getUsernameSharedPref(MessageListActivity.this),10,i);
        call.enqueue(new Callback<List<Contacts>>() {
            @Override
            public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                if (response.body().size()!=0) {
                    if (i == 0) {
                        contacts = response.body();
                    } else {
                        contacts.add(response.body().get(response.body().size() - 1));
                    }
                    AdapterMessageList adapterMessageList = new AdapterMessageList(contacts, MessageListActivity.this);
                    recyclerView.setAdapter(adapterMessageList);
                    adapterMessageList.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Contacts>> call, Throwable t) {
                Toast.makeText(MessageListActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d("THROWABLE",t.toString());
            }
        });
    }
}