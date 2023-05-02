package com.lifehelper.dating;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.lifehelper.Constants;
import com.lifehelper.R;
import com.lifehelper.adapters.AdapterPeopleData;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.Scrolled;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;
import com.lifehelper.users.ProfileActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatingActivity extends AppCompatActivity {


    ImageView usrImg;
    Button btnFind, sendMessage,btnRemove, btnSelected;
    RecyclerView recyclerViewPeopleAtYourAge, recyclerViewPeopleAvailable;
    RecyclerView.LayoutManager layoutManagerPeopleAtYourAge,layoutManagerPeopleAvailable;
    private ProgressDialog loading;
    TextView txtName;
    SharedPreference sharedPreference = new SharedPreference();
    List<Contacts> contacts,contacts1;
    int i=-10,j=-10;
    Scrolled scrolled = new Scrolled();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating);

        usrImg = findViewById(R.id.usrImg);
        btnFind = findViewById(R.id.btnFindPartner);
        sendMessage = findViewById(R.id.btnMessage);
        btnRemove = findViewById(R.id.btnRemove);
        btnSelected = findViewById(R.id.btnSelected);
        recyclerViewPeopleAtYourAge = findViewById(R.id.recyclerViewPeopleAtYourAge);
        recyclerViewPeopleAvailable = findViewById(R.id.recyclerViewPeopleAvailable);
        txtName = findViewById(R.id.txtPartnerName);

        getSupportActionBar().setTitle("Dating");

        layoutManagerPeopleAtYourAge = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPeopleAtYourAge.setLayoutManager(layoutManagerPeopleAtYourAge);
        layoutManagerPeopleAvailable = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPeopleAvailable.setLayoutManager(layoutManagerPeopleAvailable);

        btnFind.setOnClickListener(v-> findPartner(v));
        btnRemove.setOnClickListener(v-> {
            AlertDialog.Builder alertDialog;
            alertDialog = new AlertDialog.Builder(DatingActivity.this);
            alertDialog.setTitle("Are you sure, you want to remove the partner?");
            alertDialog.setMessage("Please, note that if you remove the partner. You will never find her as your partner in the app.");

            alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> removePartner());
            alertDialog.setNegativeButton("No", (dialogInterface, i) -> finish());
            alertDialog.show();
        });
        btnSelected.setOnClickListener(v-> partnerSelected());
        sendMessage.setOnClickListener(v-> sendMessage());

        getPartnerData();
        getPeoplesData();
        getPeoplesDataSameAge();

        recyclerViewPeopleAvailable.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(scrolled.isLastItemDisplaying(recyclerView)){
                    getPeoplesData();
                }
            }
        });

        recyclerViewPeopleAtYourAge.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(scrolled.isLastItemDisplaying(recyclerView)){
                    getPeoplesDataSameAge();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.message:
                Intent intent = new Intent(DatingActivity.this, MessageListActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //classes for backend work
    private void findPartner(View v){
        loading=new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Contacts> call = apiInterface.findPartner(sharedPreference.getUsernameSharedPref(DatingActivity.this));
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {

                String value = response.body().getValue();
                String message = response.body().getMessage();

                if (value.equals("found"))
                {
                    loading.dismiss();
                    Snackbar.make(v, message,Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(DatingActivity.this, DatingActivity.class);
                    startActivity(intent);
                    finish();
                }else if (value.equals("found_but_not_inserted")){
                    loading.dismiss();
                    Snackbar.make(v, message,Snackbar.LENGTH_LONG).show();
                }else if (value.equals("update_profile")){
                    loading.dismiss();
                    Snackbar.make(v, message,Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(DatingActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }else if (value.equals("uncommon_gender")){
                    loading.dismiss();
                    Snackbar.make(v, message,Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(DatingActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    loading.dismiss();
                    Toast.makeText(DatingActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {

                loading.dismiss();
                Toast.makeText(DatingActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removePartner(){
        loading=new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Contacts> call = apiInterface.removePartner(sharedPreference.getUsernameSharedPref(DatingActivity.this));
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {


                String value = response.body().getValue();
                String message = response.body().getMessage();

                loading.dismiss();
                if (value.equals("success")){
                    finish();
                    Toast.makeText(DatingActivity.this, "Now you are again single.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DatingActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {

                loading.dismiss();
                Toast.makeText(DatingActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPartnerData(){
        SharedPreference sharedPreference = new SharedPreference();


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Contacts> call = apiInterface.getPartnerData(sharedPreference.getUsernameSharedPref(DatingActivity.this));
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                String username = response.body().getUsername();
                String img = response.body().getImg();
                String relationType = response.body().getRelationType();
                String selector = response.body().getSelector();

                txtName.setText(username);
                if (txtName.getText().toString().trim().isEmpty()){
                    btnSelected.setVisibility(View.GONE);
                    btnRemove.setVisibility(View.GONE);
                    sendMessage.setVisibility(View.GONE);
                    txtName.setText("Name");
                }else{
                    btnFind.setEnabled(false);
                    if (relationType.equals("succeed")){
                        btnFind.setVisibility(View.GONE);
                        btnRemove.setVisibility(View.GONE);
                        btnSelected.setText("Selected");
                        btnSelected.setEnabled(true);
                    }else if (relationType.equals("removed") || username.isEmpty()){
                        btnRemove.setVisibility(View.GONE);
                        btnSelected.setVisibility(View.GONE);
                    }else if(relationType.equals("yes")){
                        btnFind.setVisibility(View.GONE);
                    }
                    if (selector.equals("false")){
                        btnSelected.setVisibility(View.GONE);
                    }else if (selector.isEmpty()){
                        btnSelected.setVisibility(View.GONE);
                    }
                    Glide
                            .with(DatingActivity.this)
                            .load(Constants.BASE_URL+"/life_helper/users/"+username+"/"+img)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(usrImg);
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {

            }
        });
    }

    private void getPeoplesDataSameAge(){
        j=j+10;
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Contacts>> call = apiInterface.getPeopleData(sharedPreference.getUsernameSharedPref(DatingActivity.this),10,j,"age");
        call.enqueue(new Callback<List<Contacts>>() {
            @Override
            public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                if (response.body().size()!=0) {
                    if (j == 0) {
                        contacts = response.body();
                    } else {
                        contacts.addAll(response.body());
                    }
                    AdapterPeopleData adapterPeopleData = new AdapterPeopleData(contacts, DatingActivity.this);
                    recyclerViewPeopleAtYourAge.setAdapter(adapterPeopleData);
                    adapterPeopleData.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Contacts>> call, Throwable t) {

            }
        });
    }

    private void getPeoplesData(){
        i=i+10;
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Contacts>> call = apiInterface.getPeopleData(sharedPreference.getUsernameSharedPref(DatingActivity.this),10,i,"all_but_not_age");
        call.enqueue(new Callback<List<Contacts>>() {
            @Override
            public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                if (response.body().size()!=0) {
                    if (i == 0) {
                        contacts1 = response.body();
                    } else {
                        contacts1.addAll(response.body());
                    }
                    AdapterPeopleData adapterPeopleData = new AdapterPeopleData(contacts1, DatingActivity.this);
                    recyclerViewPeopleAvailable.setAdapter(adapterPeopleData);
                    adapterPeopleData.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Contacts>> call, Throwable t) {

            }
        });
    }

    private void partnerSelected(){
        SharedPreference sharedPreference = new SharedPreference();


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Contacts> call = apiInterface.selectPartner(sharedPreference.getUsernameSharedPref(DatingActivity.this));
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();

                if(value.equals("succeed")){
                    Toast.makeText(DatingActivity.this, "Enjoy, your life with your partner. You have chose a wise choice.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DatingActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {

            }
        });
    }

    private void sendMessage(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Contacts> call = apiInterface.createMessageList(sharedPreference.getUsernameSharedPref(DatingActivity.this));
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {

                String value = response.body().getValue();
                String message = response.body().getMessage();

                if (value.equals("succeed"))
                {
                    Intent intent = new Intent(DatingActivity.this, MessageListActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(DatingActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                Toast.makeText(DatingActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}