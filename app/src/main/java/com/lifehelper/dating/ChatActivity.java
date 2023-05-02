package com.lifehelper.dating;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lifehelper.Constants;
import com.lifehelper.R;
import com.lifehelper.adapters.AdapterChat;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.Scrolled;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {


    RecyclerView recyclerViewChat;
    String messageTo,usrImg;
    int i=0;
    List<Contacts> contacts;
    SharedPreference sharedPreference = new SharedPreference();
    EditText etxtMessage;
    Button btnSend;
    AdapterChat adapterChat;
    int scrollPosition;
    TextView txtNewMessages;
    LinearLayout llNewMessages;
    boolean neverCallGetMessages;
    boolean loadNewMessage=false;
    Scrolled scrolled = new Scrolled();
    String timestamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this,RecyclerView.VERTICAL,true));

        etxtMessage = findViewById(R.id.etxtMessage);
        btnSend = findViewById(R.id.btnSendMessage);
        llNewMessages = findViewById(R.id.llNewMessages);
        txtNewMessages = findViewById(R.id.txtNewMessages);

        messageTo = getIntent().getStringExtra(Constants.SENDER);
        usrImg = getIntent().getStringExtra(Constants.IMG);

        getSupportActionBar().setTitle(messageTo);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Glide.with(this).asDrawable().load(Constants.BASE_URL + "/life_helper/users/" + messageTo + "/" + usrImg).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                getSupportActionBar().setLogo(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

        btnSend.setOnClickListener(v->{
            String message = etxtMessage.getText().toString();
            if (!message.isEmpty()){
                sendMessage(messageTo,message);
                etxtMessage.setText("");
            }

        });

        getMessages();
        convertSentToSeenStatus();
        llNewMessages.setVisibility(View.GONE);
        llNewMessages.setOnClickListener(v-> loadNewestMessages());
        handlerToCheckNewMessages();

        recyclerViewChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollPosition = scrollPosition + dy;
                if (scrolled.isLastItemDisplaying(recyclerView)){
                    if(!neverCallGetMessages) getMessages();
                }
            }
        });

    }

    public void getMessages(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Contacts>> call = apiInterface.getChat(sharedPreference.getUsernameSharedPref(ChatActivity.this),messageTo,20,i);
        call.enqueue(new Callback<List<Contacts>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Contacts>> call, @NonNull Response<List<Contacts>> response) {
                if (response.body().size()!=0) {
                    if (i == 0) {
                        contacts = response.body();
                        timestamp = contacts.get(0).getTimestamp();
                    }
                    else {
                        contacts.addAll(i, response.body());
                    }

                    adapterChat = new AdapterChat(contacts, ChatActivity.this);
                    recyclerViewChat.setAdapter(adapterChat);
                    adapterChat.notifyDataSetChanged();
                    i=i+response.body().size();
                    neverCallGetMessages = false;
                }else{
                    neverCallGetMessages = true;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Contacts>> call, @NonNull Throwable t) {
                Toast.makeText(ChatActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d("THROWABLE",t.toString());
            }
        });
    }
    public void loadNewestMessages(){
        if (loadNewMessage) {
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<List<Contacts>> call = apiInterface.getNewMessages(sharedPreference.getUsernameSharedPref(ChatActivity.this), messageTo, timestamp);
            call.enqueue(new Callback<List<Contacts>>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<List<Contacts>> call, @NonNull Response<List<Contacts>> response) {
                    if (llNewMessages.getVisibility()==View.VISIBLE){
                        llNewMessages.setVisibility(View.GONE);
                    }
                    if (response.body().size() != 0) {
                        contacts.addAll(0, response.body());
                        timestamp = contacts.get(0).getTimestamp();
                        adapterChat = new AdapterChat(contacts, ChatActivity.this);
                        recyclerViewChat.setAdapter(adapterChat);
                        adapterChat.notifyDataSetChanged();
                        i = i + response.body().size();
                        convertSentToSeenStatus();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Contacts>> call, @NonNull Throwable t) {
                    Toast.makeText(ChatActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("THROWABLE", t.toString());
                }
            });
        }
    }


    public void sendMessage(String to, String message){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Contacts> call = apiInterface.sendMessage(sharedPreference.getUsernameSharedPref(ChatActivity.this),to,message);
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                recyclerViewChat.smoothScrollBy(0,0);
                scrollPosition=0;
                if (response.body().getValue().equals("succeed")){
                    checkIfNewMessagesAvailable();
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void convertSentToSeenStatus(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Contacts> call = apiInterface.messageSeen(sharedPreference.getUsernameSharedPref(ChatActivity.this),messageTo);
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {

//                String value = response.body().getValue();
//                String message = response.body().getMessage();

            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlerToCheckNewMessages(){
        new Handler().postDelayed(() -> checkIfNewMessagesAvailable(),6000);
    }
    public void checkIfNewMessagesAvailable(){
        if(llNewMessages.getVisibility()!=View.VISIBLE || scrollPosition==0) {
            if (llNewMessages.getVisibility()==View.VISIBLE && scrollPosition==0){
                loadNewMessage = true;
                loadNewestMessages();
            }else {
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<Contacts> call = apiInterface.checkForNewMessages(sharedPreference.getUsernameSharedPref(ChatActivity.this), messageTo, timestamp);
                call.enqueue(new Callback<Contacts>() {
                    @Override
                    public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                        String value = response.body().getValue();
                        if (Integer.parseInt(value) != 0) {
                            loadNewMessage = true;
                            if (scrollPosition == 0) {
                                loadNewestMessages();
                            } else {
                                llNewMessages.setVisibility(View.VISIBLE);
                                txtNewMessages.setText(value + " New Messages");
                            }
                        } else {
                            loadNewMessage = false;
                            llNewMessages.setVisibility(View.GONE);
                        }
                        handlerToCheckNewMessages();
                    }

                    @Override
                    public void onFailure(Call<Contacts> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
