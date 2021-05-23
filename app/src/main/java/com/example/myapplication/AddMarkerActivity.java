package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddMarkerActivity extends AppCompatActivity {

    private DatabaseReference reference, reference2;
    private String email, name, phone;
    private RadioGroup radioGroup;
    private FirebaseAuth mAuth;
    private ImageView uploadImage;
    private CircleImageView imageView;
    private StorageReference root = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        radioGroup = findViewById(R.id.radioGroup);
        imageView = findViewById(R.id.viewImage);
        imageView.setImageResource(R.drawable.ic_user2);
        uploadImage = findViewById(R.id.addImage);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //data
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("markers");
        reference2 = rootNode.getReference("users");
        mAuth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(email, MODE_PRIVATE);
        name = sharedPreferences.getString("name", "");
        phone = sharedPreferences.getString("phone", "");


        ImageView close = findViewById(R.id.close2);
        close.setOnClickListener(v -> finish());

        Button save = findViewById(R.id.addBtn);
        save.setOnClickListener(v -> addMarker());

        uploadImage.setOnClickListener(v -> {
            Intent gallery = new Intent();
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            gallery.setType("image/*");
            startActivityForResult(gallery, 2);
        });

    }

    private void addMarker() {

        if (imageUri != null) {
            uploadToFirebase(imageUri);
        }

        int radioId = radioGroup.getCheckedRadioButtonId();
        String markerString = Integer.toString(radioId);

        EditText addproblem = findViewById(R.id.addProblem);
        String info = addproblem.getText().toString().trim();

        if (info.isEmpty()) {
            addproblem.setError(getString(R.string.infoEmpty));
            return;
        }
        String img = "null";
        Marker marker = new Marker(info, name, phone, markerString, img);
        reference.child(phone).setValue(marker);
        reference2.child(phone).child("info").setValue(markerString);

        startActivity(new Intent(AddMarkerActivity.this, MapsActivity.class));
        finish();
    }

    private void uploadToFirebase(Uri uri) {

        StorageReference fileRef = root.child(System.currentTimeMillis() + "." + getFile(uri));
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri1 -> {

            Model model = new Model(uri1.toString());
            reference.child(phone).child("image").setValue(model);
            Toast.makeText(this, getString(R.string.addMarker), Toast.LENGTH_SHORT).show();
        })).addOnProgressListener(snapshot -> progressBar.setVisibility(View.VISIBLE)).addOnFailureListener(e -> {

            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Ошибка!" + e, Toast.LENGTH_LONG).show();
        });
    }

    private String getFile(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }


}