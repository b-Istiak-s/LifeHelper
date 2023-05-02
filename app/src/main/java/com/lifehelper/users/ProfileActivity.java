package com.lifehelper.users;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.lifehelper.Constants;
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

public class ProfileActivity extends AppCompatActivity {

    ImageView usrImg;
    EditText etxtUsername, etxtEmail, etxtFullName, etxtPhone, etxtBirthOfYear,etxtHobby, etxtHeight;
    Spinner gender, charType;
    String[] genderType = new String[]{"Female","Male"};
    String[] charTypeType = new String[]{"Introvert","Extrovert","Ambivert"};
    Button btnEdit, btnUpdate;
    Bitmap selectedImageBitmap = null;
    Uri selectedImageUri=null;
    ProgressDialog progressDialog;
	String genderGB=null,charTypeTypeGB=null;
    ArrayAdapter<String> adapterGenderType,adaptercharTypeType;
    String path="null";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usrImg = findViewById(R.id.imgUserProfile);
        etxtUsername = findViewById(R.id.etxtUsernameAddMoney);
        etxtEmail = findViewById(R.id.etxtEmail);
        etxtFullName = findViewById(R.id.etxtFullName);
        etxtPhone = findViewById(R.id.etxtPhone);
        etxtBirthOfYear = findViewById(R.id.etxtBirthYear);
        gender = findViewById(R.id.spinnerGender);
        charType = findViewById(R.id.spinnerCharType);
        etxtHobby = findViewById(R.id.etxtHobby);
        etxtHeight = findViewById(R.id.etxtHeight);
        btnEdit = findViewById(R.id.btnEdit);
        btnUpdate = findViewById(R.id.btnUpdate);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading.....");


        adapterGenderType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderType);
        gender.setAdapter(adapterGenderType);
        adaptercharTypeType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, charTypeType);
        charType.setAdapter(adaptercharTypeType);

		
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderGB =genderType[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
		
		
        charType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                charTypeTypeGB =charTypeType[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        usrImg.setOnClickListener(v->{
            Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
            imageIntent.setType("image/*");
            imageSelector.launch(imageIntent);
        });

        btnEdit.setOnClickListener(v->{
//            etxtEmail.setEnabled(true);
            etxtFullName.setEnabled(true);
            etxtPhone.setEnabled(true);
            etxtBirthOfYear.setEnabled(true);
            etxtHobby.setEnabled(true);
            etxtHeight.setEnabled(true);
        });

        btnUpdate.setOnClickListener(v->{
            String username = etxtUsername.getText().toString().trim();
            String email = etxtUsername.getText().toString().trim();
            String fullName = etxtFullName.getText().toString().trim();
            String phone = etxtPhone.getText().toString().trim();
            String yearOfBirthGB = etxtBirthOfYear.getText().toString().trim();
            String Hobby = etxtHobby.getText().toString().trim();
            String height = etxtHeight.getText().toString().trim();
            if (etxtFullName.isEnabled()){
                if(username.isEmpty()){
                    etxtUsername.setError("Please enter username");
                    etxtUsername.requestFocus();
                }else if (email.isEmpty()){
                    etxtEmail.setError("Please enter your email address");
                    etxtEmail.requestFocus();
                }else if (fullName.isEmpty()){
                    etxtFullName.setError("Please enter your full name");
                    etxtFullName.requestFocus();
                }else if (phone.isEmpty()){
                    etxtPhone.setError("Please enter your phone number");
                    etxtPhone.requestFocus();
                }else if (phone.isEmpty()){
                    etxtBirthOfYear.setError("Please enter your phone number");
                    etxtBirthOfYear.requestFocus();
                }else if (Hobby.isEmpty()){
                    etxtHobby.setError("Please enter your type of Hobby");
                    etxtHobby.requestFocus();
                }else if (height.isEmpty()){
                    etxtHeight.setError("Please enter your height");
                    etxtHeight.requestFocus();
                }
				else if(genderGB.isEmpty()){
                    Toast.makeText(this, "Please select your sex type", Toast.LENGTH_SHORT).show();
				}else if(charTypeTypeGB.isEmpty()){
                    Toast.makeText(this, "Please select your characteristic type", Toast.LENGTH_SHORT).show();
				}
                else{
					uploadFile(fullName,phone, genderGB, yearOfBirthGB,selectedImageUri,Hobby, height, charTypeTypeGB);
                }
            }else{
                Snackbar.make(v,"Please edit your profile first.",Snackbar.LENGTH_SHORT).show();
            }
        });

        userData();

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
                usrImg.setImageBitmap(selectedImageBitmap);
            }
        }
    });

    private void uploadFile(String fullName, String phoneNumber, String genderToUpdate, String yearOfBirth, Uri contentUri,String Hobby, String height, String charTypeTypeToUpdate) {
        progressDialog.show();

        SharedPreference sharedPreference = new SharedPreference();

        File file = null;
        MultipartBody.Part fileToUpload = null;
        if (!path.equals("null")) {
            file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        }

        RequestBody fullNameRB = RequestBody.create(MediaType.parse("text/plain"), fullName);
        RequestBody phoneNumberRB = RequestBody.create(MediaType.parse("text/plain"), phoneNumber);
        RequestBody genderRB = RequestBody.create(MediaType.parse("text/plain"), genderToUpdate);
        RequestBody yearOfBirthRB = RequestBody.create(MediaType.parse("text/plain"), yearOfBirth);
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), sharedPreference.getUsernameSharedPref(ProfileActivity.this));
        RequestBody HobbyRB = RequestBody.create(MediaType.parse("text/plain"), Hobby);
        RequestBody heightRB = RequestBody.create(MediaType.parse("text/plain"), height);
        RequestBody charTypeTypeRB = RequestBody.create(MediaType.parse("text/plain"), charTypeTypeToUpdate);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Contacts> call = apiInterface.updateProfile(fileToUpload, username, fullNameRB, phoneNumberRB, genderRB, yearOfBirthRB, HobbyRB, heightRB, charTypeTypeRB);
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();

                if (value.equals("success")) {
                    Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void userData(){

        SharedPreference sharedPreference = new SharedPreference();


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Contacts> call = apiInterface.userData(sharedPreference.getUsernameSharedPref(ProfileActivity.this),sharedPreference.getPasswordSharedPref(ProfileActivity.this));
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                String username = response.body().getUsername();
                String email = response.body().getEmail();
                String full_name = response.body().getFullName();
                String phone = response.body().getPhone();
                String latitude = response.body().getLatitude();
                String longitude = response.body().getLongitude();
                String city = response.body().getCity();
                String country = response.body().getCountry();
                String img = response.body().getImg();
                String genderString = response.body().getGender();
                String birth_year = response.body().getBirthYear();
                String hobby = response.body().getHobby();
                String height = response.body().getHeight();
                String char_typeString = response.body().getCharType();
                String partner = response.body().getPartner();
                String last_active = response.body().getLastActive();
                String user_type = response.body().getUserType();

                etxtUsername.setText(username);
                etxtEmail.setText(email);
                etxtFullName.setText(full_name);
                etxtPhone.setText(phone);
                etxtBirthOfYear.setText(birth_year);
                etxtHobby.setText(hobby);
                etxtHeight.setText(height);
                progressDialog.dismiss();

                if (!genderString.isEmpty()){
                    int selectionPositionOfGender = adapterGenderType.getPosition(genderString);
                    gender.setSelection(selectionPositionOfGender);
                }else{
                    gender.setSelection(0);
                }

                if (!char_typeString.isEmpty()){
                    int selectionPositionOfChar = adaptercharTypeType.getPosition(char_typeString);
                    charType.setSelection(selectionPositionOfChar);
                }else{
                    charType.setSelection(0);
                }

                if (!img.isEmpty()){
                    Glide
                            .with(ProfileActivity.this)
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
}