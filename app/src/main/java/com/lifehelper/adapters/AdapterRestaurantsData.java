package com.lifehelper.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lifehelper.Constants;
import com.lifehelper.R;
import com.lifehelper.model.Contacts;
import com.lifehelper.restaurants.RestaurantDetailsActivity;

import java.util.List;

public class AdapterRestaurantsData extends RecyclerView.Adapter<AdapterRestaurantsData.MyViewHolder>{

    private List<Contacts> contacts;
    private Context context;

    public AdapterRestaurantsData(List<Contacts> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterRestaurantsData.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new AdapterRestaurantsData.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRestaurantsData.MyViewHolder holder, int position) {
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
        holder.txtRestaurantName.setText(contacts.get(position).getRestaurantName());
        holder.txtOpeningHours.setText(contacts.get(position).getOpeningHours());
        String location = "<b>Location : </b>"+contacts.get(position).getAddress()+", "+contacts.get(position).getCity()+", "+contacts.get(position).getCountry();
        String opening = "<b>Opening : </b>"+contacts.get(position).getOpeningHours()+" - "+contacts.get(position).getClosingHours();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.txtLocation.setText(Html.fromHtml(location,Html.FROM_HTML_MODE_COMPACT));
            holder.txtOpeningHours.setText(Html.fromHtml(opening,Html.FROM_HTML_MODE_COMPACT));
        }else{
            holder.txtLocation.setText(Html.fromHtml(location));
            holder.txtOpeningHours.setText(Html.fromHtml(opening));
        }
        Glide
                .with(context)
                .load(Constants.BASE_URL + "/life_helper/restaurants/" + contacts.get(position).getRestaurantLogo())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imgLogo);

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, RestaurantDetailsActivity.class);
            intent.putExtra(Constants.ID,contacts.get(position).getId());
            intent.putExtra(Constants.RESTAURANT_NAME,contacts.get(position).getRestaurantName());
            intent.putExtra(Constants.RESTAURANT_LOGO,contacts.get(position).getRestaurantLogo());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtRestaurantName,txtLocation,txtOpeningHours;
        ImageView imgLogo;
        CardView card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRestaurantName = itemView.findViewById(R.id.itemTxtRestaurantName);
            txtLocation = itemView.findViewById(R.id.itemTxtRestaurantLocation);
            txtOpeningHours = itemView.findViewById(R.id.itemTxtOpeningHours);
            imgLogo = itemView.findViewById(R.id.itemImgRestaurantLogo);
            card = itemView.findViewById(R.id.itemRestaurantCard);
        }
    }

}