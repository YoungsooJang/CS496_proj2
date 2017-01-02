package com.example.q.cs496_proj2;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class TetrisActivity extends AppCompatActivity {

    private ArrayList<Integer> map = new ArrayList<>();
    private ArrayList<Integer> stuck = new ArrayList<>();
    private ArrayList<Integer> falling = new ArrayList<>();
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tetris);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        for (int i = 0; i < 11 * 23; i++) {
            map.add(0);
        }
        randomCreate();

        for (int i = 11 * 23; i < 11 * 24; i++) {
            stuck.add(i);
        }

        gridView = (GridView) findViewById(R.id.gridView2);
        gridView.setAdapter(new TetrisGridViewAdapter(getApplicationContext(), map));

        Button rotateBtn = (Button) findViewById(R.id.button1);
        rotateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotatePiece();
            }
        });
        Button leftBtn = (Button) findViewById(R.id.button2);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftPiece();
            }
        });
        Button rightBtn = (Button) findViewById(R.id.button3);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightPiece();
            }
        });
        Button dropBtn = (Button) findViewById(R.id.button4);
        dropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropPiece();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gridView.setAdapter(new TetrisGridViewAdapter(getApplicationContext(), map));
                        }
                    });
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    fallView();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void fallView() {
        //Log.d("TEST", Integer.toString(map.size()));
        Collections.sort(falling, Collections.reverseOrder());
        boolean fall = true;
        for (Integer index : falling) {
            if (stuck.contains(index + 11)) {
                fall = false;
            }
        }

        if (fall) {
            for (int i = 0; i < 4; i ++) {
                int index = falling.get(i);
                int current = map.get(index);
                map.set(index + 11, current);
                map.set(index, 0);
                falling.set(i, index + 11);
            }
        } else {
            stuck.addAll(falling);
            falling.clear();
            randomCreate();
        }
    }

    public void leftPiece() {
        Collections.sort(falling, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return ((o1 % 11) - (o2 % 11));
            }
        });
        boolean left = true;
        for (Integer index : falling) {
            if ((index % 11 == 0) || (stuck.contains(index - 1))) {
                left = false;
            }
        }

        if (left) {
            for (int i = 0; i < 4; i ++) {
                int index = falling.get(i);
                int current = map.get(index);
                map.set(index - 1, current);
                map.set(index, 0);
                falling.set(i, index - 1);
            }
        }
    }

    public void rightPiece() {
        Collections.sort(falling, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return ((o2 % 11) - (o1 % 11));
            }
        });
        boolean right = true;
        for (Integer index : falling) {
            if ((index % 11 == 10) || (stuck.contains(index + 1))) {
                right = false;
            }
        }

        if (right) {
            for (int i = 0; i < 4; i ++) {
                int index = falling.get(i);
                int current = map.get(index);
                map.set(index + 1, current);
                map.set(index, 0);
                falling.set(i, index + 1);
            }
        }
    }

    public void dropPiece() {
        Collections.sort(falling, Collections.reverseOrder());
        boolean drop = true;
        for (Integer index : falling) {
            if (stuck.contains(index + 11)) {
                drop = false;
            }
        }

        while(drop) {
            for (int i = 0; i < 4; i ++) {
                int index = falling.get(i);
                int current = map.get(index);
                map.set(index + 11, current);
                map.set(index, 0);
                falling.set(i, index + 11);
            }

            for (Integer index : falling) {
                if (stuck.contains(index + 11)) {
                    drop = false;
                }
            }
        }
    }

    public void rotatePiece() {

    }

    public void createI() {
        map.set(5, 1);
        map.set(16, 1);
        map.set(27, 1);
        map.set(38, 1);
        falling.add(5);
        falling.add(16);
        falling.add(27);
        falling.add(38);
    }

    public void createL() {
        map.set(5, 2);
        map.set(16, 2);
        map.set(27, 2);
        map.set(28, 2);
        falling.add(5);
        falling.add(16);
        falling.add(27);
        falling.add(28);
    }

    public void createL2() {
        map.set(5, 3);
        map.set(16, 3);
        map.set(27, 3);
        map.set(26, 3);
        falling.add(5);
        falling.add(16);
        falling.add(27);
        falling.add(26);
    }

    public void createS() {
        map.set(5, 4);
        map.set(16, 4);
        map.set(17, 4);
        map.set(28, 4);
        falling.add(5);
        falling.add(16);
        falling.add(17);
        falling.add(28);
    }

    public void createS2() {
        map.set(5, 5);
        map.set(16, 5);
        map.set(15, 5);
        map.set(26, 5);
        falling.add(5);
        falling.add(16);
        falling.add(15);
        falling.add(26);
    }

    public void createF() {
        map.set(5, 6);
        map.set(16, 6);
        map.set(15, 6);
        map.set(17, 6);
        falling.add(5);
        falling.add(16);
        falling.add(15);
        falling.add(17);
    }

    public void createD() {
        map.set(5, 7);
        map.set(6, 7);
        map.set(16, 7);
        map.set(17, 7);
        falling.add(5);
        falling.add(6);
        falling.add(16);
        falling.add(17);
    }

    public void randomCreate() {
        Random random = new Random();
        switch(random.nextInt(7)) {
            case 0: createI(); break;
            case 1: createL(); break;
            case 2: createL2(); break;
            case 3: createS(); break;
            case 4: createS2(); break;
            case 5: createF(); break;
            case 6: createD(); break;
            default: break;
        }
    }

}
