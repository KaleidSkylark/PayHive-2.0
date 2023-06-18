package com.skylark.payhive;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.muddz.styleabletoast.StyleableToast;

public class ExchangeTransfer extends AppCompatActivity {

    private DatabaseReference usersRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_transfer);

        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("UserData");

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());

        EditText recipientEmailField = findViewById(R.id.recipient_emailField);
        EditText transferAmountField = findViewById(R.id.transfer_amountField);
        Button submitTransferButton = findViewById(R.id.submit_transferButton);

        submitTransferButton.setOnClickListener(v -> {
            String recipientEmail = recipientEmailField.getText().toString().trim();
            String transferAmountString = transferAmountField.getText().toString().trim();

            if (recipientEmail.isEmpty() || transferAmountString.isEmpty()) {
                StyleableToast.makeText(ExchangeTransfer.this, "Please enter all fields!", Toast.LENGTH_SHORT, R.style.mytoast).show();
            } else {
                double transferAmount = Double.parseDouble(transferAmountString);
                transferMoney(recipientEmail, transferAmount);
            }
        });
    }

    private void transferMoney(String recipientEmail, double transferAmount) {
        String currentUserUid = firebaseAuth.getCurrentUser().getUid();

        usersRef.child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserData sender = dataSnapshot.getValue(UserData.class);
                    if (sender != null) {
                        // Check if the sender has enough balance
                        if (sender.getWalletTotalBalance() >= transferAmount) {
                            if (!sender.getEmail().equals(recipientEmail)) {
                                usersRef.orderByChild("email").equalTo(recipientEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                UserData recipient = snapshot.getValue(UserData.class);
                                                if (recipient != null) {
                                                    // Update sender's balance
                                                    double newSenderBalance = sender.getWalletTotalBalance() - transferAmount;
                                                    sender.setWalletTotalBalance((long) newSenderBalance);
                                                    usersRef.child(currentUserUid).setValue(sender);

                                                    // Update recipient's balance
                                                    double newRecipientBalance = recipient.getWalletTotalBalance() + transferAmount;
                                                    recipient.setWalletTotalBalance((long) newRecipientBalance);
                                                    snapshot.getRef().setValue(recipient);

                                                    // Show a success message
                                                    StyleableToast.makeText(ExchangeTransfer.this, "Transfer Successful", Toast.LENGTH_SHORT, R.style.mytoast).show();
                                                } else {
                                                    showErrorToast("Recipient data not found!");
                                                }
                                            }
                                        } else {
                                            showErrorToast("Recipient data not found!");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.w("TransferMoney", "Error getting recipient.", databaseError.toException());
                                    }
                                });
                            } else {
                                showErrorToast("Cannot transfer to your own account!");
                            }
                        } else {
                            showErrorToast("Insufficient Balance!");
                        }
                    } else {
                        showErrorToast("Sender data not found!");
                    }
                } else {
                    showErrorToast("Sender data not found!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TransferMoney", "Error getting sender.", databaseError.toException());
            }
        });
    }

    private void showErrorToast(String message) {
        StyleableToast.makeText(ExchangeTransfer.this, message, Toast.LENGTH_SHORT, R.style.mytoast).show();
    }
}