package com.skylark.payhive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> {
    private List<CryptoDataItem> dataItems;

    public CryptoAdapter(List<CryptoDataItem> dataItems) {
        this.dataItems = dataItems;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CryptoDataItem item = dataItems.get(position);
        holder.nameTextView.setText(item.getName());
        holder.symbolTextView.setText(item.getSymbol());
        holder.balanceTextView.setText(item.getBalance());
        holder.priceTextView.setText(item.getPrice());
        holder.cryptoImageView.setImageResource(item.getImageResId());
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView symbolTextView;
        public TextView balanceTextView;
        public TextView priceTextView;
        public ImageView cryptoImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            symbolTextView = itemView.findViewById(R.id.symbolTextView);
            balanceTextView = itemView.findViewById(R.id.balanceTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            cryptoImageView = itemView.findViewById(R.id.cryptoImageView);

        }
    }
}
