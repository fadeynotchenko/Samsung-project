package com.example.myapplication.fragment.edit.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.MapsActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FragmentEditEmail extends Fragment {

    private Button save;
    private EditText newEmail;
    private FirebaseAuth mAuth;


    public FragmentEditEmail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_email, container, false);

        newEmail = v.findViewById(R.id.newEmail);
        save = v.findViewById(R.id.saveEmail);

        save.setOnClickListener(view -> {
            save();
        });
        return v;
    }

    private void save() {

        String emailText = newEmail.getText().toString().trim();
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(email, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("newEmail", emailText);
        editor.apply();

        String phone = sharedPreferences.getString("phone", "");

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users");
        reference.child(phone).child("email").setValue(emailText);

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            newEmail.setError(getString(R.string.emailEmpty));
            return;

        } else {
            startActivity(new Intent(getActivity().getApplicationContext(), MapsActivity.class));

        }
    }
}