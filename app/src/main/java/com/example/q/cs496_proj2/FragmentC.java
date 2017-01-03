package com.example.q.cs496_proj2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.Future;

public class FragmentC extends Fragment {

    private Context context;
    private static ListView listView2;
    RankingListViewAdapter rankingListViewAdapter;
    private int scoreIDCount = 1;
    private static boolean firstView = true;

    private ArrayList<String[]> rankingList = new ArrayList<>();

    public FragmentC() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentc, container, false);
        context = view.getContext();

        listView2 = (ListView) view.findViewById(R.id.listView2);

        Button startBtn = (Button) view.findViewById(R.id.startButton);
        startBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TetrisActivity.class);
                        startActivity(intent);
                    }
        });

        getRanksInServer("http://52.78.101.202:3000/api/ranks");

        return view;
    }

    public void updateListView(ArrayList<String[]> ranking) {
        rankingListViewAdapter = new RankingListViewAdapter(context, ranking);
        listView2.setAdapter(rankingListViewAdapter);
    }

    public void getRanksInServer (String url){
        Future downloading = Ion.with(context)
                .load("GET", url)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<com.koushikdutta.ion.Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, com.koushikdutta.ion.Response<String> result) {
                        Log.d("RESPONSE", "***************************************** " + result.getResult());
                        try {
                            if (firstView) {
                                JSONArray scoreList = new JSONArray(result.getResult());
                                String[] data;
                                for (int i = 0; i < scoreList.length(); i++) {
                                    data = new String[3];
                                    data[0] = Integer.toString(scoreIDCount);
                                    scoreIDCount += 1;
                                    data[1] = scoreList.getJSONObject(i).getString("name");
                                    data[2] = scoreList.getJSONObject(i).getString("score");
                                    rankingList.add(data);
                                }
                                firstView = false;
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        updateListView(rankingList);
                    }
                });
    }
}
