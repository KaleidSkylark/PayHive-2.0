package com.skylark.payhive;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainLayoutActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isButtonTapped = false;
    private AlertDialog alertDialog;
    private Animation scaleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlayout);
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);

        // Show the welcome dialog box
        showWelcomeDialog();

        TextView fragmentTitle = findViewById(R.id.fragment_title);
        fragmentTitle.setText("Wallet");

        int[] buttonIds = {R.id.ibWallet, R.id.ibStats, R.id.ibTransaction, R.id.ibSettings};
        for (int id : buttonIds) {
            ImageButton button = findViewById(id);
            button.setOnClickListener(this);
        }

        // Load the WalletFragment by default
        loadFragment(new WalletFragment());
    }

    @Override
    public void onClick(View v) {
        if (!isButtonTapped) {
            isButtonTapped = true;
            TextView fragmentTitle = findViewById(R.id.fragment_title);
            int viewId = v.getId();

            // Load and start the animation
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.icon_tap_animation);
            v.startAnimation(animation);

            // Set the ImageButton as selected
            v.setSelected(true);

            if (viewId == R.id.ibWallet) {
                fragmentTitle.setText("Wallet");
                loadFragment(new WalletFragment());
            } else if (viewId == R.id.ibStats) {
                fragmentTitle.setText("Statistics");
                loadFragment(new StatsFragment());
            } else if (viewId == R.id.ibTransaction) {
                fragmentTitle.setText("Exchange");
                loadFragment(new ExchangeFragment());
            } else if (viewId == R.id.ibSettings) {
                fragmentTitle.setText("Settings");
                loadFragment(new SettingsFragment());
            }
            isButtonTapped = false;
            // Reset the selected state of other ImageButtons
            int[] buttonIds = {R.id.ibWallet, R.id.ibStats, R.id.ibTransaction, R.id.ibSettings};
            for (int id : buttonIds) {
                if (id != viewId) {
                    ImageButton button = findViewById(id);
                    button.setSelected(false);
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        // Inflate the custom layout file
        View promptView = getLayoutInflater().inflate(R.layout.dialog_backpressed, null);

        // Set the action when the user clicks the "No" button
        Button noButton = promptView.findViewById(R.id.no_button);
        noButton.setOnClickListener(v -> {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        });

        // Set the action when the user clicks the "Yes" button
        Button yesButton = promptView.findViewById(R.id.yes_button);
        yesButton.setOnClickListener(v -> finish());

        // Create the alert dialog builder and show the dialog box
        alertDialog = new AlertDialog.Builder(this)
                .setView(promptView)
                .create();
        alertDialog.show();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    private void showWelcomeDialog() {
        // Inflate layout and find views
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_welcome, null);
        TextView welcomeTextView = view.findViewById(R.id.welcome_message);
        Button closeButton = view.findViewById(R.id.close_button);

        // Set full name from Realtime Database as welcome message text
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference fullNameRef = FirebaseDatabase.getInstance().getReference("UserData/" + userId + "/fullName");
        fullNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullName = snapshot.getValue(String.class);
                if (fullName != null) {
                    welcomeTextView.setText("Welcome, \n" + fullName + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        // Create and show AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Set close button click listener
        closeButton.setOnClickListener(v -> alertDialog.dismiss());
    }
}
