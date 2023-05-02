package com.lifehelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lifehelper.Constants;
import com.lifehelper.R;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterPeopleData extends RecyclerView.Adapter<AdapterPeopleData.MyViewHolder>{

    private List<Contacts> contacts;
    private Context context;

    public AdapterPeopleData(List<Contacts> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterPeopleData.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AdapterPeopleData.MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(context.getResources().getConfiguration().isNightModeActive()){
                holder.card.getBackground().setTint(Color.parseColor("#0B091F"));
                holder.card.setOutlineAmbientShadowColor(Color.parseColor("#3D3D3D"));
            }else{
                holder.card.getBackground().setTint(Color.parseColor("#EFEFEF"));
                holder.card.setOutlineAmbientShadowColor(Color.parseColor("#3D3D3D"));
            }
        }
        holder.txtName.setText(contacts.get(position).getUsername());
//        if (!contacts.get(position).getImg().isEmpty()) {
            Glide
                    .with(context)
                    .load(Constants.BASE_URL + "/life_helper/users/" + contacts.get(position).getUsername() + "/" + contacts.get(position).getImg())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.user_graduate)
                    .into(holder.imgUser);
//        }
        holder.itemView.setOnLongClickListener(view -> {
            interaction(contacts.get(position).getUsername(),"item");
            return false;
        });
        holder.imgUser.setOnClickListener(v->{
            interaction(contacts.get(position).getUsername(),"img");
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imgUser;
        CardView card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.itemUserImage);
            txtName = itemView.findViewById(R.id.itemtxtPeopleName);
            card = itemView.findViewById(R.id.itemUserCard);
        }
    }

    private void interaction(String peopleUsername,String interacted){
        SharedPreference sharedPreference = new SharedPreference();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Contacts> call = apiInterface.interacted(sharedPreference.getUsernameSharedPref(context),peopleUsername,interacted);
        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                if (value.equals("found") || value.equals("taken") || value.equals("not_single")){
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }

                Log.d("interaction",value);
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {

            }
        });
    }
}
