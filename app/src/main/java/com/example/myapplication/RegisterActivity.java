package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText editTextEmail, editTextPass, editTextPhone, editTextName;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.emailSignUp);
        editTextPass = findViewById(R.id.passSignUp);
        editTextPhone = findViewById(R.id.phoneSignUp);
        editTextName = findViewById(R.id.nameSignUp);
        Button reg = findViewById(R.id.reg);
        TextView signIn = findViewById(R.id.text2);
        TextView reg_acc = findViewById(R.id.reg_acc);
        TextView info = findViewById(R.id.info);
        TextView singUp = findViewById(R.id.signUp);

        AnimView animView = new AnimView(editTextEmail, editTextName, editTextPass, editTextPhone, reg, signIn, reg_acc, info, singUp);
        animView.animRegister();

        signIn.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

        reg.setOnClickListener(v -> RegisterUser());
    }

    private void RegisterUser() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPass.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String name = editTextName.getText().toString().trim();

        if(pass.isEmpty()){
            editTextPass.setError(getString(R.string.passEmpty));
            return;
        }
        if(phone.isEmpty()){
            editTextPhone.setError(getString(R.string.phoneEmpty));
            return;
        }
        if(name.isEmpty()){
            editTextName.setError(getString(R.string.nameEmpty));
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(getString(R.string.emailEmpty));
            return;
        }

        String info = "null";
        String geo1 = "null";
        String geo2 = "null";
        User user = new User(email, pass, phone, name, info, geo1, geo2);
        reference.child(phone).setValue(user);
        sharedPreferences = getSharedPreferences(email, Context.MODE_PRIVATE);

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email);
                editor.putString("pass", pass);
                editor.putString("phone", phone);
                editor.putString("name", name);
                editor.putString("info", info);
                editor.putString("newEmail", "null");
                editor.apply();
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error"+ task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}