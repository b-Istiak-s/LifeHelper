package com.lifehelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.lifehelper.dating.ChatActivity;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.SharedPreference;

import java.util.List;

public class AdapterMessageList extends RecyclerView.Adapter<AdapterMessageList.MyViewHolder>{

    private List<Contacts> contacts;
    private Context context;
    SharedPreference sharedPreference = new SharedPreference();

    public AdapterMessageList(List<Contacts> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterMessageList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_list, parent, false);
        return new AdapterMessageList.MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AdapterMessageList.MyViewHolder holder, int position) {
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
        String senderName = "";
        if (!contacts.get(position).getUser1().equals(sharedPreference.getUsernameSharedPref(context))){
            senderName = contacts.get(position).getUser1();
        }else{
            senderName = contacts.get(position).getUser2();
        }
        holder.txtName.setText(senderName);

        if (!contacts.get(position).getImg().isEmpty()) {
            Glide
                    .with(context)
                    .load(Constants.BASE_URL + "/life_helper/users/" + senderName + "/" + contacts.get(position).getImg())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.imgUsrProfile);
        }

        if (contacts.get(position).getMessageFrom().equals(sharedPreference.getUsernameSharedPref(context))){
            holder.txtMessage.setText("You : "+contacts.get(position).getMessage());
        }else{
            holder.txtMessage.setText(senderName+" : "+contacts.get(position).getMessage());
        }
        holder.txtTimestamp.setText(contacts.get(position).getTimestamp());
        if (contacts.get(position).getRead().equals("sent")){
            holder.iconReadReceipt.setImageResource(R.drawable.sent);
        }else if (contacts.get(position).getRead().equals("delivered")){
            holder.iconReadReceipt.setImageResource(R.drawable.delivered);
        }else if (contacts.get(position).getRead().equals("read")){
            holder.iconReadReceipt.setImageResource(R.drawable.read);
        }

        String finalSenderName = senderName;
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(Constants.SENDER, finalSenderName);
            intent.putExtra(Constants.IMG,contacts.get(position).getImg());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtMessage,txtTimestamp;
        ImageView imgUsrProfile,iconReadReceipt;
        CardView card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUsrProfile = itemView.findViewById(R.id.item_usr_profile);
            txtName = itemView.findViewById(R.id.item_txt_usr_profile);
            txtMessage = itemView.findViewById(R.id.item_txt_message);
            iconReadReceipt = itemView.findViewById(R.id.item_icon_read_receipt);
            txtTimestamp = itemView.findViewById(R.id.item_txt_timestamp);
            card = itemView.findViewById(R.id.message_list_card);
        }
    }

}
