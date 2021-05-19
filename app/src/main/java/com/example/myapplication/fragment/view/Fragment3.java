package com.example.myapplication.fragment.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Fragment3 extends Fragment {

    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_3, container, false);

        imageView = root.findViewById(R.id.fab);
        imageView.setOnClickListener(v -> Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle(R.string.permission)
                                    .setMessage(R.string.permission_full)
                                    .setNegativeButton(R.string.cancel, null)
                                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                                        Intent intent = new Intent();
                                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                                        intent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                                    })
                                    .show();
                        } else {
                            Toast.makeText(getContext(), R.string.permission, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check());


        return root;

    }


}
