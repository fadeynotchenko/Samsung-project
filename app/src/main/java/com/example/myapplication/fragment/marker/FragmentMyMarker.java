package com.example.myapplication.fragment.marker;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class FragmentMyMarker extends Fragment {

    private TextView viewName, viewPhone, viewText, viewMain, v1, v2, v3;
    private CircleImageView circleImageView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String phoneSP, name, problem, url;
    private Context context;

    public FragmentMyMarker() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_markerr, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        String email = firebaseAuth.getCurrentUser().getEmail();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(email, Context.MODE_PRIVATE);
        phoneSP = sharedPreferences.getString("phone", "");

        viewName = v.findViewById(R.id.activeName2);
        viewPhone = v.findViewById(R.id.activePhone2);
        viewText = v.findViewById(R.id.activeText2);
        v1 = v.findViewById(R.id.namep2);
        v2 = v.findViewById(R.id.phonep2);
        v3 = v.findViewById(R.id.problemp2);
        viewMain = v.findViewById(R.id.checktext23);

        context = getActivity().getApplicationContext();

        circleImageView = v.findViewById(R.id.v2);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("markers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = String.valueOf(snapshot.child(phoneSP).child("name").getValue());
                problem = String.valueOf(snapshot.child(phoneSP).child("info").getValue());
                url = String.valueOf(snapshot.child(phoneSP).child("image").child("imageUri").getValue());
                setText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }

    private void setText(){
        if (name.equals("null")) {
            viewName.setText("");
        } else {

            viewPhone.setVisibility(View.VISIBLE);
            viewName.setVisibility(View.VISIBLE);
            viewMain.setVisibility(View.INVISIBLE);
            viewText.setVisibility(View.VISIBLE);
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
            v3.setVisibility(View.VISIBLE);
            circleImageView.setVisibility(View.VISIBLE);

            Glide.with(context).load(Uri.parse(url)).into(circleImageView);
            viewName.setText(name);
            viewPhone.setText(phoneSP);
            viewText.setText(problem);
        }


    }
}