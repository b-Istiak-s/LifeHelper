package com.lifehelper.restaurants;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lifehelper.R;
import com.lifehelper.model.Contacts;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;

public class CreateFeedPostActivity extends AppCompatActivity {

    EditText etxtFeedPostDesc;
    ImageButton imgBtnSelectImg;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feed_post);

        etxtFeedPostDesc =findViewById(R.id.etxtFeedPostDesc);
        imgBtnSelectImg = findViewById(R.id.imgBtnSelectImgFeed);
        btnSubmit = findViewById(R.id.btnUploadRestaurantFeedPost);

        btnSubmit.setOnClickListener(v->{
            String feedDesc = etxtFeedPostDesc.getText().toString();
            postFeed("",feedDesc);
        });
    }
    private void postFeed(String id, String desc){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Contacts> call = apiInterface.post_feedWithoutImg(id,desc);
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, retrofit2.Response<Contacts> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();

                if(value.equals("succeed")){

                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                Toast.makeText(CreateFeedPostActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}