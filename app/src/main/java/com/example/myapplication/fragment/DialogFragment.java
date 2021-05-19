package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MapsActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.dialog_fragment, container, false);

        Button yes = v.findViewById(R.id.yesBtn);

        yes.setOnClickListener(v1 -> {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(requireActivity().getApplicationContext(), LoginActivity.class));
            requireActivity().finish();

        });
        Button no = v.findViewById(R.id.noBtn);
        no.setOnClickListener(v1 -> { startActivity(new Intent(requireActivity().getApplicationContext(), MapsActivity.class)); });
        return v;
    }
}
