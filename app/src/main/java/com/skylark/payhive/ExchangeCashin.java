package com.skylark.payhive;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import io.github.muddz.styleabletoast.StyleableToast;

public class ExchangeCashin extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_cashin);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageButton backButton = findViewById(R.id.back_button);
        EditText cashInTextField = findViewById(R.id.cashInTextField);
        Button submitCashInButton = findViewById(R.id.submitCashInButton);

        submitCashInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCashIn(cashInTextField.getText().toString());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void submitCashIn(String amount) {
        if (amount.isEmpty()) {
            StyleableToast.makeText(ExchangeCashin.this, "Please enter an amount!", Toast.LENGTH_SHORT, R.style.mytoast).show();
            return;
        }

        try {
            double amountDouble = Double.parseDouble(amount);
            long amountLong = (long) (amountDouble * 100);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            String currentUserUid = mAuth.getCurrentUser().getUid();
            DatabaseReference currentUserDataRef = mDatabase.getReference("UserData").child(currentUserUid);

            currentUserDataRef.child("walletTotalBalance").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Long currentBalance = mutableData.getValue(Long.class);
                    if (currentBalance == null) {
                        mutableData.setValue(amountLong);
                    } else {
                        mutableData.setValue(currentBalance + amountLong);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot dataSnapshot) {
                    if (committed) {
                        StyleableToast.makeText(ExchangeCashin.this, "Cash-in successful!", Toast.LENGTH_SHORT, R.style.mytoast).show();

                    } else {
                        StyleableToast.makeText(ExchangeCashin.this, "Cash-in failed!", Toast.LENGTH_SHORT, R.style.mytoast).show();
                    }
                }
            });
        } catch (NumberFormatException e) {
            StyleableToast.makeText(ExchangeCashin.this, "Invalid amount format!", Toast.LENGTH_SHORT, R.style.mytoast).show();
            e.printStackTrace();
        }
    }
}