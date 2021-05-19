package com.example.myapplication.fragment.edit.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.MapsActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FragmentEditPass extends Fragment {

    private FirebaseAuth mAuth;

    public FragmentEditPass() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_edit_pass, container, false);

        Button saveBtn = v.findViewById(R.id.savePass);
        saveBtn.setOnClickListener(view -> {

            EditText newPass = v.findViewById(R.id.newPass);

            mAuth = FirebaseAuth.getInstance();
            String email = mAuth.getCurrentUser().getEmail();
            SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(email, Context.MODE_PRIVATE);

            String pass = newPass.getText().toString().trim();

            if (pass.isEmpty()) {
                newPass.setError(getString(R.string.passEmpty));
                return;

            } else {
                //SP editor
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pass", pass);
                editor.apply();

                String oldPhone = sharedPreferences.getString("phone", "");

                //FD edit
                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                DatabaseReference reference = rootNode.getReference("users");
                reference.child(oldPhone).child("pass").setValue(pass);

                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.updatePass), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity().getApplicationContext(), MapsActivity.class));

            }

        });

        return v;
    }
}