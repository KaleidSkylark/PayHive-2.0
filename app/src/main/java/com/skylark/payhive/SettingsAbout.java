package com.skylark.payhive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsAbout extends AppCompatActivity {
    private ImageView mEmailIcon;
    private ImageView mFacebookIcon;
    private ImageView mTwitterIcon;
    private ImageView mGithubIcon;
    private CircleImageView mProfileImageAmit;
    private CircleImageView mProfileImageJanabajab;
    private CircleImageView mProfileImageBertos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_about);

        // Initialize UI elements
        mEmailIcon = findViewById(R.id.email_icon);
        mFacebookIcon = findViewById(R.id.facebook_icon);
        mTwitterIcon = findViewById(R.id.twitter_icon);
        mGithubIcon = findViewById(R.id.github_icon);
        mProfileImageAmit = findViewById(R.id.member_1_picture);
        mProfileImageJanabajab = findViewById(R.id.member_2_picture);
        mProfileImageBertos = findViewById(R.id.member_3_picture);

        mEmailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open email link
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "janabajab.234539@bacoor.sti.edu.ph", null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        mFacebookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Facebook link
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/XerxSkylark"));
                startActivity(facebookIntent);
            }
        });

        mTwitterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Twitter link
                Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/SkylarkXerx"));
                startActivity(twitterIntent);
            }
        });

        mGithubIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open GitHub link
                Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/KaleidSkylark/PayHive-2.0"));
                startActivity(githubIntent);
            }
        });

        mProfileImageAmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open GitHub link
                Intent amitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/jakeamit420"));
                startActivity(amitIntent);
            }
        });

        mProfileImageJanabajab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open GitHub link
                Intent janabajabIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/XerxSkylark"));
                startActivity(janabajabIntent);
            }
        });

        mProfileImageBertos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open GitHub link
                Intent bertosIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/jeroy.bertos"));
                startActivity(bertosIntent);
            }
        });

        // Find the back button in the layout
        ImageButton backButton = findViewById(R.id.back_button);

        // Set a click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity and return to the previous activity
                onBackPressed();
            }
        });
    }
}