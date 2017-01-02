package com.example.q.cs496_proj2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.util.ArrayList;

public class TetrisActivity extends AppCompatActivity {

    private ArrayList<Integer> map = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tetris);

        for (int i = 0; i < 11 * 19; i++) {
            if (i % 10 == 0) {
                map.add(1);
            } else {
                map.add(0);
            }
        }

        GridView gridView = (GridView) findViewById(R.id.gridView2);
        gridView.setAdapter(new TetrisGridViewAdapter(getApplicationContext(), map));
    }

}
