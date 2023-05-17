package com.skylark.payhive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private String[] LANGUAGES;
    private String selectedLanguage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String selectedLanguage = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("selected_language", null);
        if (selectedLanguage != null) {
            Configuration configuration = requireContext().getResources().getConfiguration();
            if (selectedLanguage.equals(getString(R.string.language_english))) {
                configuration.setLocale(new Locale("en"));
            } else if (selectedLanguage.equals(getString(R.string.language_japanese))) {
                configuration.setLocale(new Locale("ja"));
            }else if (selectedLanguage.equals(getString(R.string.language_chinese))) {
                configuration.setLocale(new Locale("zh", "CN"));
            }
            requireContext().getResources().updateConfiguration(configuration, requireContext().getResources().getDisplayMetrics());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize the LANGUAGES array here
        LANGUAGES = new String[] {
                getString(R.string.language_english),
                getString(R.string.language_japanese),
                getString(R.string.language_chinese)
        };

        Spinner languageSpinner = view.findViewById(R.id.language_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, LANGUAGES);
        languageSpinner.setAdapter(adapter);
        selectedLanguage = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("selected_language", null);
        if (selectedLanguage != null) {
            int position = adapter.getPosition(selectedLanguage);
            languageSpinner.setSelection(position);
        }

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String language = (String) parent.getItemAtPosition(position);
                if (!language.equals(selectedLanguage)) {
                    selectedLanguage = language;
                    SharedPreferences.Editor editor = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
                    editor.putString("selected_language", selectedLanguage);
                    editor.apply();
                    setLocale(selectedLanguage);
                    restartActivity();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Find the privacy_button ImageButton
        ImageButton privacyButton = view.findViewById(R.id.privacy_button);

        // Set an OnClickListener for the privacy_button
        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the PrivacyPolicy activity
                Intent intent = new Intent(getActivity(), SettingsPrivacyPolicy.class);
                startActivity(intent);
            }
        });

        // Find the privacy_button ImageButton
        ImageButton tacButton = view.findViewById(R.id.termsandcondi_button);

        // Set an OnClickListener for the privacy_button
        tacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the PrivacyPolicy activity
                Intent intent = new Intent(getActivity(), SettingsTermsAndCondi.class);
                startActivity(intent);
            }
        });

        // Find the privacy_button ImageButton
        ImageButton abputButton = view.findViewById(R.id.about_button);

        // Set an OnClickListener for the privacy_button
        abputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the PrivacyPolicy activity
                Intent intent = new Intent(getActivity(), SettingsAbout.class);
                startActivity(intent);
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("UserData");

        mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fullName = dataSnapshot.child("fullName").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);

                TextView profileFullName = view.findViewById(R.id.profile_fullName);
                TextView profileEmail = view.findViewById(R.id.profile_email);
                profileFullName.setText(fullName);
                profileEmail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        Button logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View customView = inflater.inflate(R.layout.dialog_logout, null);

                builder.setView(customView);
                Button positiveButton = customView.findViewById(R.id.yes_button);
                Button negativeButton = customView.findViewById(R.id.no_button);
                AlertDialog dialog = builder.create();

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    public void setLocale(String language) {
        Locale locale = null;
        if (language.equals(getString(R.string.language_english))) {
            locale = new Locale("en");
        } else if (language.equals(getString(R.string.language_japanese))) {
            locale = new Locale("ja");
        } else if (language.equals(getString(R.string.language_chinese))) {
            locale = new Locale("zh", "CN");
        }
        if (locale != null) {
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());
        }
    }

    private void restartActivity() {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}


