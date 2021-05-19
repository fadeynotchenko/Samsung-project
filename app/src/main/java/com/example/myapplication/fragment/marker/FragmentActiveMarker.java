package com.example.myapplication.fragment.marker;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentActiveMarker extends Fragment {

    private String phoneSP;
    private FirebaseAuth firebaseAuth;
    private TextView nameView, phoneView, textView, namep, phonep, problemp, checktext;
    private String info, phone, name, code, imageurl;
    private Button cancel;
    private DatabaseReference databaseReference, df;
    private CircleImageView circleImageView;
    private Context context;


    public FragmentActiveMarker() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_active_marker, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        df = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("markers");

        nameView = v.findViewById(R.id.activeName);
        phoneView = v.findViewById(R.id.activePhone);
        textView = v.findViewById(R.id.activeText);
        namep = v.findViewById(R.id.namep);
        phonep = v.findViewById(R.id.phonep);
        problemp = v.findViewById(R.id.problemp);
        checktext = v.findViewById(R.id.checktext);
        circleImageView = v.findViewById(R.id.v1);

        context = getActivity().getApplicationContext();

        //user data
        String emailUser = firebaseAuth.getCurrentUser().getEmail();
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(emailUser, Context.MODE_PRIVATE);
        phoneSP = sharedPreferences.getString("phone", "");

        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                info = String.valueOf(snapshot.child(phoneSP).child("active").child("info").getValue());
                name = String.valueOf(snapshot.child(phoneSP).child("active").child("name").getValue());
                phone = String.valueOf(snapshot.child(phoneSP).child("active").child("phone").getValue());
                code = String.valueOf(snapshot.child(phoneSP).child("active").child("code").getValue());
                imageurl = String.valueOf(snapshot.child(phoneSP).child("active").child("image").getValue());
                setText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cancel = v.findViewById(R.id.del);
        cancel.setOnClickListener(view -> {
            deleteActiveMarker();
        });


        return v;
    }

    private void checkActiveMarker() {
        namep.setVisibility(View.VISIBLE);
        phonep.setVisibility(View.VISIBLE);
        problemp.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        circleImageView.setVisibility(View.VISIBLE);
        nameView.setVisibility(View.VISIBLE);
        phoneView.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        checktext.setVisibility(View.INVISIBLE);

    }

    private void deleteActiveMarker() {
        if (!name.equals("null")) {

            getActivity().finish();
            Toast.makeText(getActivity().getApplicationContext(), "Вы отказались от подопечного!", Toast.LENGTH_SHORT).show();

            databaseReference.child(phone).child("phone").setValue(phone);
            df.child(phoneSP).child("active").child("phone").removeValue();
            databaseReference.child(phone).child("name").setValue(name);
            df.child(phoneSP).child("active").child("name").removeValue();

            databaseReference.child(phone).child("info").setValue(info);
            databaseReference.child(phone).child("code").setValue(code);
            databaseReference.child(phone).child("image").child("imageUri").setValue(imageurl);

            df.child(phoneSP).child("active").child("code").removeValue();
            df.child(phoneSP).child("active").child("info").removeValue();
            df.child(phoneSP).child("active").child("image").removeValue();


        }

    }

    private void setText() {
        if (info.equals("null")) {
            textView.setText("");
        } else {
            checkActiveMarker();
            textView.setText(info);
            Glide.with(context).load(Uri.parse(imageurl)).into(circleImageView);
        }

        if (name.equals("null")) {
            nameView.setText("");
        } else {
            nameView.setText(name);
        }

        if (phone.equals("null")) {
            phoneView.setText("");
        } else {
            phoneView.setText(phone);
        }
    }
}