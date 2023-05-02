package com.lifehelper.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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

import java.util.List;

public class AdapterFeed  extends RecyclerView.Adapter<AdapterFeed.MyViewHolder>{

    private List<Contacts> contacts;
    private Context context;

    public AdapterFeed(List<Contacts> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterFeed.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_feed, parent, false);
        return new AdapterFeed.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFeed.MyViewHolder holder, int position) {
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

        holder.txtPosterName.setText(contacts.get(position).getUsername());
        holder.txtPostedTimestamp.setText(contacts.get(position).getTimestamp());
        holder.txtPostDesc.setText(contacts.get(position).getDesc());
        Glide
                .with(context)
                .load(Constants.BASE_URL + "/life_helper/users/" + contacts.get(position).getUsername() + "/" + contacts.get(position).getImg())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.user_graduate)
                .into(holder.imgUsr);

        if (!contacts.get(position).getPostImage().equals("")){
            Glide
                    .with(context)
                    .load(Constants.BASE_URL + "/life_helper/restaurants/feed/" + contacts.get(position).getPostImage())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.imgUsr);
        }else{
            holder.imgPost.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtPosterName,txtPostedTimestamp,txtPostDesc;
        ImageView imgUsr,imgPost;
        CardView card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPosterName = itemView.findViewById(R.id.txtFeedPosterName);
            txtPostedTimestamp = itemView.findViewById(R.id.txtFeedPostedTimestamp);
            txtPostDesc = itemView.findViewById(R.id.txtFeedPostDesc);
            imgUsr = itemView.findViewById(R.id.imgFeedPosterProfile);
            imgPost = itemView.findViewById(R.id.imgFeedPostImage);
            card = itemView.findViewById(R.id.cardRestaurantFeed);
        }
    }

}