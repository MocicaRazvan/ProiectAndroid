package com.example.proiectandroid;

import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView createNewAccount;

    private EditText inputEmail, inputPassword;
    private Button btnLogin;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAtuh;
    private FirebaseUser mUser;

    ImageView btnGoogle, btnFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams
                .FLAG_FULLSCREEN, WindowManager.LayoutParams
                .FLAG_FULLSCREEN);

        createNewAccount = findViewById(R.id.createNewAccount);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);


        progressDialog = new ProgressDialog(this);
        mAtuh = FirebaseAuth.getInstance();
        mUser = mAtuh.getCurrentUser();

        if (mUser != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
            );
            startActivity(intent);
        }

        setAutoLogAppEventsEnabled(true);


        createNewAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        btnLogin.setOnClickListener(v -> {
            performLogin();
        });

        btnGoogle.setOnClickListener(v -> {
            Intent intent = new Intent(this, GoogleSingInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });

        btnFacebook.setOnClickListener(v -> {
            Intent intent = new Intent(this, FacebookAuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });

    }

    private void performLogin() {

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Enter valid email");
            inputEmail.requestFocus();
        } else if (TextUtils.isEmpty(password) || password.length() < 6) {
            inputPassword.setError("Enter Proper Password");
            inputPassword.requestFocus();
        } else {
            progressDialog.setMessage("Login...");
            progressDialog.setTitle("Please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAtuh.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            sendUserToNextActivity();
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }


                    });

        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
        startActivity(intent);
    }
}