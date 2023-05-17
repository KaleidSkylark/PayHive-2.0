package com.skylark.payhive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.muddz.styleabletoast.StyleableToast;

public class RegisterActivity extends AppCompatActivity {

    private EditText mFullNameEditText, mEmailEditText, mPhoneNumberEditText, mPasswordEditText;
    private Button mCreateAccountButton, mLoginButton;
    private ProgressBar mRegisterProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mFullNameEditText = findViewById(R.id.InputFullName);
        mEmailEditText = findViewById(R.id.InputRegisterEmail);
        mPhoneNumberEditText = findViewById(R.id.InputPhoneNumber);
        mPasswordEditText = findViewById(R.id.InputRegisterPassword);
        mCreateAccountButton = findViewById(R.id.register_button);
        mLoginButton = findViewById(R.id.login_button);
        mRegisterProgressBar = findViewById(R.id.RegisterProgressBar);

        mCreateAccountButton.setOnClickListener(this::onCreateAccountButtonClick);
        mLoginButton.setOnClickListener(this::onLoginButtonClick);
    }

    private void onCreateAccountButtonClick(View view) {
        String fullName = mFullNameEditText.getText().toString().trim();
        String email = mEmailEditText.getText().toString().trim();
        String phoneNumberString = mPhoneNumberEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        if (validateInputFields(fullName, email, phoneNumberString, password)) {
            mRegisterProgressBar.setVisibility(View.VISIBLE);
            long phoneNumber = Long.parseLong(phoneNumberString);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, task -> {
                        mRegisterProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                mDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference userDataRef = mDatabase.getReference("UserData");
                                UserData userData = new UserData(fullName, email, phoneNumber, password);

                                // Set initial wallet balance to PHP 50,000
                                userData.setWalletTotalBalance((long) 50000);
                                userDataRef.child(user.getUid()).setValue(userData);

                                DatabaseReference userBalanceRef = mDatabase.getReference("UserTotalBalance");
                                userBalanceRef.child(user.getUid()).setValue(50000);
                            }
                            showToast("Account created successfully!");
                            startActivity(new Intent(RegisterActivity.this, MainLayoutActivity.class));
                            finish();
                        } else {
                            showToast("Account creation failed: " + task.getException().getMessage());
                        }
                    });
        }
    }


    private void onLoginButtonClick(View view) {
        finish();
    }

    private void showToast(String message) {
        StyleableToast.makeText(RegisterActivity.this, message, R.style.mytoast).show();
    }

    private boolean validateInputFields(String fullName, String email, String phoneNumberString, String password) {
        boolean hasErrors = false;

        if (TextUtils.isEmpty(fullName) || !fullName.matches("^[a-zA-Z ]+$")) {
            mFullNameEditText.setError("Full name is required and should contain only letters and spaces");
            hasErrors = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError("Email is required");
            hasErrors = true;
        }

        if (TextUtils.isEmpty(phoneNumberString) || !phoneNumberString.matches("^\\d{10,11}$")) {
            mPhoneNumberEditText.setError("Valid phone number with 10 or 11 digits is required");
            hasErrors = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError("Password is required");
            hasErrors = true;
        } else if (password.length() < 6) {
            mPasswordEditText.setError("Password must be at least 6 characters");
            hasErrors = true;
        }

        return !hasErrors;
    }
}
