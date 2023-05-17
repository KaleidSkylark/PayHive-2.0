package com.skylark.payhive;

import static android.content.ContentValues.TAG;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity {

    // Firebase components
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
    private FirebaseAuth mAuth;

    // UI components
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private CheckBox mShowPasswordCheckBox;
    private Button mLoginButton;
    private ProgressBar mLoginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        initializeUIComponents();
    }

    private void initializeUIComponents() {
        mEmailEditText = findViewById(R.id.InputLoginEmail);
        mPasswordEditText = findViewById(R.id.InputLoginPassword);
        mShowPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        mLoginButton = findViewById(R.id.login_button);
        Button createButton = findViewById(R.id.create_button);
        mLoginProgressBar = findViewById(R.id.loginProgressBar);

        mLoginButton.setOnClickListener(onLoginClickListener);
        mShowPasswordCheckBox.setOnCheckedChangeListener(onShowPasswordCheckedChangeListener);
        createButton.setOnClickListener(onCreateClickListener);
    }

    private View.OnClickListener onLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = mEmailEditText.getText().toString().trim();
            String password = mPasswordEditText.getText().toString().trim();

            if (isValidEmail(email) && isValidPassword(password)) {
                checkUsernameAndLogin(email, password);
            } else {
                setErrorMessages(email, password);
            }
        }
    };

    private void checkUsernameAndLogin(String email, String password) {
        databaseReference.orderByChild("username").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userEmail = getEmailFromSnapshot(snapshot, email);
                authenticateAndLogin(userEmail, password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Unable to check for user in database", error.toException());
            }
        });
    }

    private String getEmailFromSnapshot(DataSnapshot snapshot, String input) {
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String email = dataSnapshot.child("email").getValue(String.class);
                if (!TextUtils.isEmpty(email)) {
                    return email;
                }
            }
        }
        return input;
    }

    private void authenticateAndLogin(String email, String password) {
        mLoginProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mLoginProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainLayoutActivity.class));
                } else {
                    StyleableToast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), R.style.mytoast).show();
                }
            }
        });
    }

    private void setErrorMessages(String email, String password) {
        if (!isValidEmail(email)) {
            mEmailEditText.setError("Invalid email or username. Please enter a valid email address or username.");
        }
        if (!isValidPassword(password)) {
            mPasswordEditText.setError("Password must be at least 6 characters long");
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private CompoundButton.OnCheckedChangeListener onShowPasswordCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            mPasswordEditText.setInputType(isChecked ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    };

    private View.OnClickListener onCreateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    };
}