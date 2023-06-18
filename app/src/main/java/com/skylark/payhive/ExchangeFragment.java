package com.skylark.payhive;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ExchangeFragment extends Fragment {
    private DatabaseReference currentUserDataRef;
    private Button CashinButton;
    private Button TransferButton;
    private List<Transaction> transactions;
    private TransactionAdapter transactionAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);

        // Initialize views
        CashinButton = view.findViewById(R.id.cashInButton);
        TransferButton = view.findViewById(R.id.transferButton);

        transactions = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(transactions);
        recyclerView = view.findViewById(R.id.TransactionrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transactionAdapter);

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

                        // Set the value to the TextView with currency symbol and decimal format
                        TextView totalBalanceTextView = view.findViewById(R.id.textViewBalance);
                        DecimalFormat currencyFormat = new DecimalFormat("₱ #,##0.00");
                        totalBalanceTextView.setText(currencyFormat.format(walletBalance / 1.0));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("WalletFragment", "Error retrieving wallet balance: " + error.getMessage());
            }
        });
        CashinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ExchangeCashin.class));
            }
        });

        TransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ExchangeTransfer.class));
            }
        });
        fetchTransactions();
        return view;
    }
    private void fetchTransactions() {
        currentUserDataRef.child("transactions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                transactions.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction transaction = snapshot.getValue(Transaction.class);
                    transactions.add(transaction);
                    Log.d("ExchangeFragment", "Transaction fetched: " + transaction.toString());
                }
                transactions.add(new Transaction("1", "TRIAL", 11));
                transactions.add(new Transaction("2", "BETA", 11));
                transactionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}