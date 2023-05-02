package com.lifehelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lifehelper.R;
import com.lifehelper.model.Contacts;

import java.util.List;

public class AdapterTransaction  extends RecyclerView.Adapter<AdapterTransaction.MyViewHolder>{

    private List<Contacts> contacts;
    private Context context;

    public AdapterTransaction(List<Contacts> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterTransaction.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new AdapterTransaction.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTransaction.MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.txtAmount.setText("$"+contacts.get(position).getAmount());
        holder.txtTimestamp.setText(contacts.get(position).getTimestamp());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtAmount, txtTimestamp;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAmount = itemView.findViewById(R.id.txtTransactionAmount);
            txtTimestamp = itemView.findViewById(R.id.txtTransactionTimstamp);
        }
    }

}