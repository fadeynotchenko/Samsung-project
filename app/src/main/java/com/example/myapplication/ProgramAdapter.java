package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProgramAdapter extends ArrayAdapter<String> {

    public Context context;
    public ArrayList<String> text, name, phone, view, image;
    public ProgramViewHolder holder = null;

    public ProgramAdapter(Context context, ArrayList<String> name, ArrayList<String> phone, ArrayList<String> text, ArrayList<String> view, ArrayList<String> image) {
        super(context, R.layout.row, R.id.listName, name);
        this.context = context;
        this.name = name;
        this.phone = phone;
        this.text = text;
        this.view = view;
        this.image = image;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row, parent, false);
            holder = new ProgramViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ProgramViewHolder) row.getTag();
        }
        holder.Viewtext.setText(text.get(position));
        holder.Viewname.setText(name.get(position));
        holder.viewList.setText(getImportance(position));
        if (image.get(position).equals("null")) {
            holder.imageView.setImageResource(R.drawable.ic_user2);
        } else {
            Glide.with(context).load(image.get(position)).into(holder.imageView);
        }
        return row;
    }

    private String getImportance(int position) throws IndexOutOfBoundsException {
        String string = view.get(position);
        String text = null;

        if (string.equals("2131231144")) {
            text = "Cтепень срочности: Высокая!";
            holder.viewList.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        if (string.equals("2131231147")) {
            text = "Cтепень срочности: Средняя!";
            holder.viewList.setTextColor(ContextCompat.getColor(context, R.color.quantum_deeporange300));
        }
        if (string.equals("2131231146")) {
            text = "Cтепень срочности: Низкая!";
            holder.viewList.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        return text;
    }
}
