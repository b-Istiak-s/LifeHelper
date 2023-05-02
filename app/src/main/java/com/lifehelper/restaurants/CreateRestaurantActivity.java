package com.lifehelper.restaurants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.lifehelper.R;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.PathUtil;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRestaurantActivity extends AppCompatActivity {

    EditText etxtRestaurantName,etxtAddress,etxtCity,etxtCountry,etxtOpeningHours,etxtClosingHours,etxtMapLocation,etxtRestaurantPhone;
    RadioButton openingHoursAM, openingHoursPM, closingHoursAM,closingHoursPM;
    Button btnCreate;
    ImageView imgLogo;
    Bitmap selectedImageBitmap = null;
    Uri selectedImageUri=null;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        etxtRestaurantName = findViewById(R.id.etxtRestaurantName);
        etxtAddress = findViewById(R.id.etxtAddress);
        etxtCity = findViewById(R.id.etxtRestaurantCity);
        etxtCountry = findViewById(R.id.etxtRestaurantCountry);
        etxtOpeningHours = findViewById(R.id.etxtOpeningHours);
        etxtClosingHours = findViewById(R.id.etxtClosingHours);
        etxtMapLocation = findViewById(R.id.etxtMapLocation);
        openingHoursAM = findViewById(R.id.rbAMOpening);
        openingHoursPM = findViewById(R.id.rbPMOpening);
        closingHoursAM = findViewById(R.id.rbAMClosing);
        closingHoursPM = findViewById(R.id.rbPMClosing);
        etxtRestaurantPhone = findViewById(R.id.etxtRestaurantPhone);
        btnCreate = findViewById(R.id.btnAddRestaurant);
        imgLogo = findViewById(R.id.imgRestaurantLogo);

        imgLogo.setOnClickListener(v->{
            Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
            imageIntent.setType("image/*");
            imageSelector.launch(imageIntent);
        });

        btnCreate.setOnClickListener(this::onClick);
    }


    ActivityResultLauncher<Intent> imageSelector = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                path = PathUtil.getPath(selectedImageUri,this);
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);
                }catch (IOException e){
                    e.printStackTrace();
                }
                imgLogo.setImageBitmap(selectedImageBitmap);
            }
        }
    });

    private RequestBody textToRequestBody(String text){
        return RequestBody.create(MediaType.parse("text/plain"),text);
    }

    private void createRestaurant(RequestBody restaurantName, RequestBody address, RequestBody city, RequestBody country, RequestBody phone, RequestBody openingHours, RequestBody closingHours, RequestBody mapLinkAddress, View v){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        File file;
        MultipartBody.Part fileToUpload = null;
        if (!path.equals("null")) {
            file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        }
        SharedPreference sharedPreference = new SharedPreference();
        Call<Contacts> call = apiInterface.createRestaurant(fileToUpload,textToRequestBody(sharedPreference.getUsernameSharedPref(CreateRestaurantActivity.this)),restaurantName, address, city, country,phone, openingHours, closingHours, mapLinkAddress);
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {

                String value = response.body().getValue();
                String message = response.body().getMessage();
                Toast.makeText(CreateRestaurantActivity.this, message, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {

                Toast.makeText(CreateRestaurantActivity.this, "Error! " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClick(View v) {
        String restaurantName = etxtRestaurantName.getText().toString().trim();
        String address = etxtAddress.getText().toString().trim();
        String city = etxtCity.getText().toString().trim();
        String country = etxtCountry.getText().toString().trim();
        String openingHours = etxtOpeningHours.getText().toString().trim();
        String closingHours = etxtClosingHours.getText().toString().trim();
        String mapLocation = etxtMapLocation.getText().toString().trim();
        String phone = etxtRestaurantPhone.getText().toString().trim();

        if (restaurantName.isEmpty()) {
            etxtRestaurantName.setError("Enter restaurant name");
            etxtRestaurantName.requestFocus();
        } else if (address.isEmpty()) {
            etxtAddress.setError("Enter address");
            etxtAddress.requestFocus();
        } else if (city.isEmpty()) {
            etxtCity.setError("Enter city");
            etxtCity.requestFocus();
        } else if (country.isEmpty()) {
            etxtCountry.setError("Enter country");
            etxtCountry.requestFocus();
        } else if (phone.isEmpty()) {
            etxtRestaurantPhone.setError("Enter restaurant's phone number");
            etxtRestaurantPhone.requestFocus();
        } else if (openingHours.isEmpty()) {
            etxtOpeningHours.setError("Enter opening hours");
            etxtOpeningHours.requestFocus();
        } else if (!openingHoursPM.isChecked() && !openingHoursAM.isChecked()) {
            Toast.makeText(this, "Please select opening hour's type", Toast.LENGTH_SHORT).show();
        } else if (!openingHours.contains(":")) {
            Toast.makeText(this, "Please write opening hours correct way (e.g. 12:00)", Toast.LENGTH_SHORT).show();
        } else if (closingHours.isEmpty()) {
            etxtClosingHours.setError("Enter closing hours");
            etxtClosingHours.requestFocus();
        } else if (!closingHoursPM.isChecked() && !closingHoursAM.isChecked()) {
            Toast.makeText(this, "Please select closing hour's type", Toast.LENGTH_SHORT).show();
        } else if (!closingHours.contains(":")) {
            Toast.makeText(this, "Please write closing hours correct way (e.g. 12:00)", Toast.LENGTH_SHORT).show();
        } else if (mapLocation.isEmpty()) {
            etxtMapLocation.setError("Enter map location");
            etxtMapLocation.requestFocus();
        } else {
            if (openingHoursPM.isChecked()) {
                String[] openingHoursSplit = openingHours.split(":");
                openingHours = String.valueOf(Integer.parseInt(openingHoursSplit[0]) + 12);
                openingHours = openingHours+":"+openingHoursSplit[1];
            }
            if (closingHoursPM.isChecked()) {
                String[] closingHoursSplit = closingHours.split(":");
                closingHours = String.valueOf(Integer.parseInt(closingHoursSplit[0]) + 12);
                closingHours = closingHours+":"+closingHoursSplit[1];
            }
            createRestaurant(textToRequestBody(restaurantName), textToRequestBody(address), textToRequestBody(city), textToRequestBody(country), textToRequestBody(phone), textToRequestBody(openingHours), textToRequestBody(closingHours), textToRequestBody(mapLocation), v);
        }
    }
}