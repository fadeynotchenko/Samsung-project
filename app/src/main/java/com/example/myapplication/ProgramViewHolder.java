package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProgramViewHolder {
    TextView Viewname;
    TextView Viewtext;
    CircleImageView imageView;
    TextView viewList;

    ProgramViewHolder(View v){
        Viewname = v.findViewById(R.id.listName);
        Viewtext = v.findViewById(R.id.listText);
        imageView = v.findViewById(R.id.viewavatar);
        viewList = v.findViewById(R.id.listView2);
    }
}
