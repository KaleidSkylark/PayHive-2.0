package com.skylark.payhive;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.typeAmount.setText(transaction.getType() + " " + transaction.getAmount());
        holder.transactionAmount.setText(String.valueOf(transaction.getAmount()));
        Log.d("TransactionAdapter", "Binding transaction: " + transaction.toString());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView typeAmount;
        private TextView transactionAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeAmount = itemView.findViewById(R.id.type_amount);
            transactionAmount = itemView.findViewById(R.id.transaction_amount);
        }
    }
}