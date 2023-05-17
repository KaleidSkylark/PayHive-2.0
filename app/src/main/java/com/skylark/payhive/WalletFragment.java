package com.skylark.payhive;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class WalletFragment extends Fragment {
    private CryptoAdapter adapter;
    private DatabaseReference currentUserDataRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);

        List<CryptoDataItem> dataItems = populateDataItems();

        adapter = new CryptoAdapter(dataItems);
        recyclerView.setAdapter(adapter);

        // Get a reference to currentUserDataRef
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        String currentUserUid = mAuth.getCurrentUser().getUid();
        currentUserDataRef = mDatabase.getReference("UserData").child(currentUserUid);

        // Attach a listener to update the wallet balance in the TextView
        currentUserDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserData userData = snapshot.getValue(UserData.class);
                    if (userData != null) {
                        long walletBalance = userData.getWalletTotalBalance();

                        // Convert the balance to a string
                        String walletBalanceString = String.valueOf(walletBalance);

                        // Convert the balance to a double
                        double walletBalanceDouble = Double.parseDouble(walletBalanceString);

                        // Set the value to the TextView with currency symbol and decimal format
                        TextView totalBalanceTextView = view.findViewById(R.id.textViewBalance);
                        DecimalFormat currencyFormat = new DecimalFormat("₱ #,##0.00");
                        totalBalanceTextView.setText(currencyFormat.format(walletBalanceDouble));
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("WalletFragment", "Error retrieving wallet balance: " + error.getMessage());
            }
        });

        return view;
    }

    private List<CryptoDataItem> populateDataItems() {
        List<CryptoDataItem> dataItems = new ArrayList<>();

        dataItems.add(new CryptoDataItem("Bitcoin", "(BTC)", "500Php", "500Php", R.drawable.bitcoin));
        dataItems.add(new CryptoDataItem("Ethereum", "(ETH)", "500Php", "500Php", R.drawable.eth));
        dataItems.add(new CryptoDataItem("BNB", "(BNB)", "500Php", "500Php", R.drawable.bnb));
        dataItems.add(new CryptoDataItem("XRP", "(XRP)", "500Php", "500Php", R.drawable.xrp));
        dataItems.add(new CryptoDataItem("Cardano", "(ADA)", "500Php", "500Php", R.drawable.cardano));
        dataItems.add(new CryptoDataItem("Dogecoin", "(DOGE)", "500Php", "500Php", R.drawable.dogecoin));
        dataItems.add(new CryptoDataItem("Tether", "(USDT)", "500Php", "500Php", R.drawable.tether));
        dataItems.add(new CryptoDataItem("Polygon", "(Matic)", "500Php", "500Php", R.drawable.polygon));
        dataItems.add(new CryptoDataItem("Solana", "(SOL)", "500Php", "500Php", R.drawable.solana));
        dataItems.add(new CryptoDataItem("Monero", "(XMR)", "500Php", "500Php", R.drawable.monero));

        return dataItems;
    }
}