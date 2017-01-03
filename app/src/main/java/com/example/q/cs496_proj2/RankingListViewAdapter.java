package com.example.q.cs496_proj2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RankingListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String[]> rankingList;
    private LayoutInflater inflater;
    private TextView textViewR1;
    private TextView textViewR2;
    private TextView textViewR3;

    public RankingListViewAdapter(Context context, ArrayList<String[]> rankingList) {
        this.context = context;
        this.rankingList = rankingList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return rankingList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.rankinglistviewitem, null);
        }

        textViewR1 = (TextView) view.findViewById(R.id.textViewR1);
        textViewR2 = (TextView) view.findViewById(R.id.textViewR2);
        textViewR3 = (TextView) view.findViewById(R.id.textViewR3);

        Collections.sort(rankingList, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return (Integer.parseInt(o2[2]) - Integer.parseInt(o1[2]));
            }
        });

        textViewR1.setText(Integer.toString(position + 1));
        textViewR2.setText(rankingList.get(position)[1]);
        textViewR3.setText(rankingList.get(position)[2]);

        return view;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(rankingList.get(position)[0]);
    }

    @Override
    public Object getItem(int position) {
        return rankingList.get(position);
    }
}