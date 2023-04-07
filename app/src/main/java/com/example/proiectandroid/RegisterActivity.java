package com.example.proiectandroid;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextView alreadyHaveAccount;
    private EditText inputEmail, inputPassword, inputConfirmPassword;
    private Button btnRegister;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAtuh;
    private FirebaseUser mUser;


    private ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createNotificationChannel();
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams
                .FLAG_FULLSCREEN, WindowManager.LayoutParams
                .FLAG_FULLSCREEN);


        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {

                    if (result.get(Manifest.permission.POST_NOTIFICATIONS) != null)
                        isPermissionGranted = result.get(Manifest.permission.POST_NOTIFICATIONS);
                });

        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);


        progressDialog = new ProgressDialog(this);
        mAtuh = FirebaseAuth.getInstance();
        mUser = mAtuh.getCurrentUser();
        requestPermission();

        alreadyHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });


        btnRegister.setOnClickListener(v -> {
            performAuth();
        });

    }

    private void performAuth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Enter valid email");
            inputEmail.requestFocus();
        } else if (TextUtils.isEmpty(password) || password.length() < 6) {
            inputPassword.setError("Enter Proper Password");
            inputPassword.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            inputConfirmPassword.setError("Passwords do not match");
            inputConfirmPassword.requestFocus();
        } else {
            progressDialog.setMessage("Registering...");
            progressDialog.setTitle("Please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            mAtuh.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            Map<String, Object> map = new HashMap<>();
                            map.put("username", null);
                            map.put("image", null);

                            FirebaseFirestore.getInstance().collection("users")
                                    .document(mAtuh.getCurrentUser().getUid()).set(map);
                            sendUserToNextActivity();
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    });


            notifUser();


        }

    }


    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
        startActivity(intent);
    }


    private void notifUser() {

        Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//        Toast.makeText(RegisterActivity.this, "notify", Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(RegisterActivity.this, "channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Profile")
                .setContentText("Don't forget to update your profile!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            requestPermission();

        }
        notificationManager.notify(1, builder.build());
    }


    private void requestPermission() {
        isPermissionGranted = ContextCompat.checkSelfPermission(
                RegisterActivity.this,
                android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED;
        List<String> permissionRequest = new ArrayList<String>();

        if (!isPermissionGranted) {
            permissionRequest.add(Manifest.permission.POST_NOTIFICATIONS);
        }

        if (!permissionRequest.isEmpty()) {
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel name";
            String description = "channel desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}