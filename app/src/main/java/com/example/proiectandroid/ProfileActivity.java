package com.example.proiectandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Slide;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 2;
    private ImageView imgProfile;
    private TextView username;
    private EditText edtUsername;
    private Button btnUsername, btnImg;

    private FirebaseUser mUser;
    private FirebaseFirestore db;

    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setEnterTransition(new Slide());
        getWindow().setExitTransition(new Explode());

        imgProfile = findViewById(R.id.imgProfile);
        username = findViewById(R.id.username);
        edtUsername = findViewById(R.id.edtUsername);
        btnUsername = findViewById(R.id.btnUsername);
        btnImg = findViewById(R.id.btnImg);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("users").document(mUser.getUid()).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    if (doc.getData().get("username") != null) {
                        username.setText(doc.getData().get("username").toString());
                        if (doc.getData().get("image") != null) {
                            Picasso.get().load(doc.getData().get("image").toString()).into(imgProfile);

                        }
                    }
                }
            }
        });

        btnUsername.setOnClickListener(v -> {
            changeUsername();
        });

        btnImg.setOnClickListener(v -> {
            openImage();
        });


    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();
            uploadImg();
        }
    }

    private void uploadImg() {

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null) {
            StorageReference fileRef =
                    FirebaseStorage.getInstance().getReference().child(mUser.getUid())
                            .child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(task -> {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();

                    Picasso.get().load(url).into(imgProfile);
                    db.collection("users").document(mUser.getUid())
                            .update("image", url);
                    pd.dismiss();
                });
            });
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void changeUsername() {
        String txtUsername = edtUsername.getText().toString();
        if (TextUtils.isEmpty(txtUsername)) {
            edtUsername.setError("Enter a valid username");
            edtUsername.requestFocus();
        } else {
            db.collection("users").document(mUser.getUid())
                    .update("username", txtUsername);
            username.setText(txtUsername);
            edtUsername.setText("");
        }
    }
}