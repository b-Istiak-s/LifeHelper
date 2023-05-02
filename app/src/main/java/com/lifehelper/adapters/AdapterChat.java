package com.lifehelper.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lifehelper.R;
import com.lifehelper.model.Contacts;
import com.lifehelper.model.SharedPreference;
import com.lifehelper.remote.ApiClient;
import com.lifehelper.remote.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyViewHolder>{

    private List<Contacts> contacts;
    private Context context;
    SharedPreference sharedPreference = new SharedPreference();

    public AdapterChat(List<Contacts> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterChat.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new AdapterChat.MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AdapterChat.MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.txtIncomingMessage.setText(contacts.get(position).getMessage());
        holder.txtOutgoingMessage.setText(contacts.get(position).getMessage());

        if (contacts.get(position).getFrom().equals(sharedPreference.getUsernameSharedPref(context))){
            holder.txtIncomingMessage.setVisibility(View.GONE);
        }else{
            holder.txtOutgoingMessage.setVisibility(View.GONE);
            if (contacts.get(position).getNotified().equals("delivered") || contacts.get(position).getNotified().equals("seen")){
                changeReadReceipt(contacts.get(position).getId(),holder);
            }
        }
        switch (contacts.get(position).getNotified()){
            case "sent":
                holder.txtOutgoingMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.sent,0);
                break;
            case "delivered":
                holder.txtOutgoingMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.delivered,0);
                break;
            case "seen":
                holder.txtOutgoingMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.read,0);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtIncomingMessage,txtOutgoingMessage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIncomingMessage = itemView.findViewById(R.id.itemTxtIncomingMessage);
            txtOutgoingMessage = itemView.findViewById(R.id.itemTxtOutgoingMessage);
        }
    }

    private void changeReadReceipt(String id, AdapterChat.MyViewHolder holder){
        new Handler().postDelayed(() -> {
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<Contacts> call = apiInterface.changeReadReceipt(id);
            call.enqueue(new Callback<Contacts>() {
                @Override
                public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                    String read = response.body().getRead();

                    if (read.equals("seen")){
                        holder.txtOutgoingMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.read,0);
                    }else if (read.equals("delivered")){
                        holder.txtOutgoingMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.delivered,0);
                        changeReadReceipt(id,holder);
                    }else if (read.equals("sent")){
                        holder.txtOutgoingMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.sent,0);
                        changeReadReceipt(id,holder);
                    }
                }

                @Override
                public void onFailure(Call<Contacts> call, Throwable t) {
                    Toast.makeText(context, "Error! " + t, Toast.LENGTH_SHORT).show();
                }
            });
        },7000);
    }
}
