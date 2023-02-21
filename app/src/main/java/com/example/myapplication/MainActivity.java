package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button registerBtn;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String IS_REGISTERED_FLAG = "isRegistered";
    public static final String REGISTERED_USER = "registereddUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        if(prefs.contains(IS_REGISTERED_FLAG)){
            setContentView(R.layout.activity_welcome);
            TextView textView = (TextView) findViewById(R.id.welcome_text_view);
            textView.setText("Welcome " + prefs.getString(REGISTERED_USER, ""));
        }else{
            setContentView(R.layout.activity_main);
            registerBtn = findViewById(R.id.register_button);
            // Set click listener for register button
            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the RegisterActivity to register a new user
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}