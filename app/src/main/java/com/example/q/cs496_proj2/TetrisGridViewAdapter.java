package com.example.q.cs496_proj2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TetrisGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> map;
    private LayoutInflater inflater;
    private TextView itemView;

    public TetrisGridViewAdapter(Context context, ArrayList<Integer> map) {
        this.context = context;
        this.map = map;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int position) {
        return map.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.gridviewitem, null);
        }
        itemView = (TextView) view.findViewById(R.id.itemView);

        if (map.get(position) == 1) {
            itemView.setBackgroundColor(Color.BLACK);
        } else {
            itemView.setBackground(context.getResources().getDrawable(R.drawable.border));
        }

        return view;
    }
}
