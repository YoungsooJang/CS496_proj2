package com.example.q.cs496_proj2;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
        this.map = shrink(map);
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

        switch (map.get(position)) {
            case 0 : itemView.setBackground(context.getResources().getDrawable(R.drawable.border));
                break;
            case 1 : itemView.setBackgroundColor(Color.RED);
                break;
            case 2 : itemView.setBackgroundColor(Color.MAGENTA);
                break;
            case 3 : itemView.setBackgroundColor(Color.YELLOW);
                break;
            case 4 : itemView.setBackgroundColor(Color.GREEN);
                break;
            case 5 : itemView.setBackgroundColor(Color.BLUE);
                break;
            case 6 : itemView.setBackgroundColor(Color.CYAN);
                break;
            case 7 : itemView.setBackgroundColor(Color.LTGRAY);
                break;
            default : break;
        }

        return view;
    }

    public ArrayList<Integer> shrink(ArrayList<Integer> input) {
        ArrayList<Integer> output = new ArrayList<>();
        output.addAll(input);
        for (int i = 0; i < 44; i++) {
            //Log.d("COLOR", Integer.toString(i) + ", " + output.get(i).toString());
            output.remove(0);
        }
        return output;
    }
}
