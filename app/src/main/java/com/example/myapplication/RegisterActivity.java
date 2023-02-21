package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button registerBtn;

    private static final String ONESIGNAL_APP_ID = "<YOUR-ONE-SIGNAL-APP-ID>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.email_edit_text);
        registerBtn = findViewById(R.id.register_button);
        TextView welcomeTextView = findViewById(R.id.welcome_text_view);
        welcomeTextView.setVisibility(View.GONE);

        // Set click listener for register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                try {
                    String result = registerUser(email);
                    showResultPopup(result);

                    if(!result.equals("Error Occurred")){
                        emailEditText.setVisibility(View.GONE);
                        registerBtn.setVisibility(View.GONE);
                        findViewById(R.id.email_label).setVisibility(View.GONE);
                        welcomeTextView.setVisibility(View.VISIBLE);
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void showResultPopup(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registration Result");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String registerUser(String email) throws IOException, JSONException, ExecutionException, InterruptedException {
        // TODO: Implement user registration logic here
        System.out.println("-> registerUser");
        String response = new NetworkTask().execute(email).get();
        if(response.equals("Error Occurred")){
            return response;
        }
        JSONObject jsonResponse = new JSONObject(response);

        JSONObject resultObject = jsonResponse.getJSONObject("result");

        boolean alreadyExist = resultObject.getBoolean("alreadyExist");
        String externalUserId = resultObject.getString("externalUserId");

        if (!alreadyExist) {
            registerWithOneSignal(externalUserId);
            response = "Device registered successfully. ";
            SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(MainActivity.IS_REGISTERED_FLAG, true);
            editor.putString(MainActivity.REGISTERED_USER, email);
            editor.apply();
        }else {
            response = "Device already registered. ";
        }
        return response;
    }

    private void registerWithOneSignal(String externalUserId) {
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();
        OneSignal.setExternalUserId(externalUserId);
    }
}