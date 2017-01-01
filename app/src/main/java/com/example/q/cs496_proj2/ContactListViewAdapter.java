package com.example.q.cs496_proj2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String[]> contacts;
    private LayoutInflater inflater;
    private TextView textViewl1;
    private TextView textViewl2;
    private ImageView imageViewl1;

    public ContactListViewAdapter(Context context, ArrayList<String[]> contacts) {
        this.context = context;
        this.contacts = contacts;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.contactlistviewitem, null);
        }

        textViewl1 = (TextView) view.findViewById(R.id.textViewl1);
        textViewl2 = (TextView) view.findViewById(R.id.textViewl2);
        imageViewl1 = (ImageView) view.findViewById(R.id.imageViewl1);

        textViewl1.setText(contacts.get(position)[1]);
        textViewl2.setText(contacts.get(position)[2]);

//        if (MainActivity.starredList.contains(Integer.parseInt(contacts.get(position)[2]))) {
//            imageViewl1.setImageResource(R.drawable.starfull);
//        } else {
//            imageViewl1.setImageResource(R.drawable.star);
//        }

        return view;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(contacts.get(position)[0]);
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }
}