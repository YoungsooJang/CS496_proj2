package com.example.q.cs496_proj2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.data;
import static com.example.q.cs496_proj2.R.id.container;

public class TetrisActivity extends AppCompatActivity {

    private String[] ranking;
    private ArrayList<Integer> map = new ArrayList<>();
    private ArrayList<Integer> stuck = new ArrayList<>();
    private ArrayList<Integer> falling = new ArrayList<>();
    private GridView gridView;
    private TextView scoreView;
    private TextView nameView;
    private int score;
    private boolean gameOver = false;
    String[] data;

    final Context context = this;
    private AlertDialog alertDialog;

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

        score = 0;

        scoreView = (TextView) findViewById(R.id.textViewScore);
        nameView = (TextView) findViewById(R.id.textViewName);
        nameView.setText("Name : " + MainActivity.userName);

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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder
                .setMessage("Final Score : " + Integer.toString(score))
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(context, TetrisActivity.class);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("Go to First Screen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!gameOver) {
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
                return;
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!gameOver) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fallView();
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return;
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
            removeCompleteLine();
            falling.clear();
            GameOver();
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
        while(true) {
            for (Integer index : falling) {
                if (stuck.contains(index + 11)) {
                    return;
                }
            }

            for (int i = 0; i < 4; i ++) {
                int index1 = falling.get(i);
                int current = map.get(index1);
                map.set(index1 + 11, current);
                map.set(index1, 0);
                falling.set(i, index1 + 11);
            }
        }
    }

    public void rotatePiece() {
        ArrayList<Integer> After = new ArrayList<>();
        Collections.sort(falling);
        After.addAll(falling);
        int shape = map.get(falling.get(0));
        int min, center;
        switch(shape) {
            case 1:
                //base
                center = falling.get(1);
                if (falling.contains(center + 1)) {
                    After.set(0, center - 11);
                    After.set(2, center + 11);
                    After.set(3, center + 22);
                } else {
                    if ((center % 11) == 0) {
                        After.set(0, center + 1);
                        After.set(2, center + 2);
                        After.set(3, center + 3);
                    } else if ((center % 11) == 10) {
                        After.set(0, center - 1);
                        After.set(2, center - 2);
                        After.set(3, center - 3);
                    } else if ((center % 11) == 9) {
                        After.set(0, center + 1);
                        After.set(2, center - 1);
                        After.set(3, center - 2);
                    } else {
                        After.set(0, center - 1);
                        After.set(2, center + 1);
                        After.set(3, center + 2);
                    }
                }
                break;
            case 2:
                min = falling.get(0);
                if (falling.contains(min + 11) && falling.contains(min + 22)) {
                    center = min + 11;
                    if ((center % 11) == 0) {
                        After.set(0, center + 1);
                        After.set(3, center + 2);
                    } else {
                        After.set(0, center - 1);
                        After.set(2, center + 1);
                        After.set(3, center + 10);
                    }
                } else if (falling.contains(min + 12)) {
                    center = min + 12;
                    if ((center % 11) == 10) {
                        After.set(0, center - 1);
                        After.set(3, center - 2);
                    } else {
                        After.set(0, center - 1);
                        After.set(1, center + 1);
                        After.set(3, center - 10);
                    }
                } else if (falling.contains(min + 10)) {
                    center = min + 10;
                    After.set(0, center - 11);
                    After.set(1, center + 11);
                    After.set(3, center + 12);
                } else {
                    center = min + 1;
                    After.set(0, center - 11);
                    After.set(2, center + 11);
                    After.set(3, center - 12);
                }
                break;
            case 3:
                min = falling.get(0);
                if (falling.contains(min + 11) && falling.contains(min + 22)) {
                    center = min + 11;
                    if (falling.contains(min + 1)) {
                        if ((center % 11) == 0) {
                            After.set(0, center + 1);
                            After.set(1, center + 2);
                            After.set(3, center + 13);
                        } else {
                            After.set(0, center - 1);
                            After.set(1, center + 1);
                            After.set(3, center + 12);
                        }
                    } else {
                        if ((center % 11) == 10) {
                            After.set(0, center - 1);
                            After.set(2, center - 2);
                            After.set(3, center - 13);
                        } else {
                            After.set(0, center - 1);
                            After.set(2, center + 1);
                            After.set(3, center - 12);
                        }
                    }
                } else if (falling.contains(min + 11)) {
                    center = min + 12;
                    After.set(0, center - 11);
                    After.set(1, center + 11);
                    After.set(3, center - 10);
                } else {
                    center = min + 1;
                    After.set(0, center - 11);
                    After.set(2, center + 11);
                    After.set(3, center + 10);
                }
                break;
            case 4:
                min = falling.get(0);
                center = min + 11;
                if (falling.contains(min + 1)) {
                    After.set(1, center + 1);
                    After.set(2, center + 12);
                } else {
                    if ((center % 11) == 0) {
                        After.set(0, center - 9);
                        After.set(3, center - 10);
                    } else {
                        After.set(2, center - 1);
                        After.set(3, center - 10);
                    }
                }
                break;
            case 5:
                min = falling.get(0);
                if (falling.contains(min + 1)) {
                    center = min + 12;
                    After.set(0, center - 10);
                    After.set(1, center + 11);
                } else {
                    center = min + 10;
                    if ((center % 11) == 0) {
                        After.set(1, center - 11);
                        After.set(3, center + 2);
                    } else {
                        After.set(0, center - 11);
                        After.set(3, center - 12);
                    }
                }
                break;
            case 6:
                min = falling.get(0);
                if (falling.contains(min + 11) && falling.contains(min + 22)) {
                    center = min + 11;
                    if (falling.contains(center + 1)) {
                        if ((center % 11) == 0) {
                            After.set(0, center + 2);
                            After.set(3, center + 12);
                        } else {
                            After.set(0, center - 1);
                        }
                    } else {
                        if ((center % 11) == 10) {
                            After.set(0, center - 2);
                            After.set(3, center - 12);
                        } else {
                            After.set(3, center + 1);
                        }
                    }
                } else {
                    if (falling.contains(min + 11)) {
                        center = min + 11;
                        After.set(1, center + 11);
                    } else {
                        center = min + 1;
                        After.set(2, center - 11);
                    }
                }
                break;
            case 7: break;
            default: break;
        }
        boolean canRotate = true;
        for (Integer index : After) {
            if (stuck.contains(index)) {
                canRotate = false;
            }
        }

        if (canRotate) {
            for (Integer index : falling) {
                map.set(index, 0);
            }
            for (Integer index : After) {
                map.set(index, shape);
            }
            falling.clear();
            falling.addAll(After);
        }
    }

    public void removeCompleteLine() {
        ArrayList<Integer> completeList = new ArrayList<>();
        for (int i = 22; i > 3; i--) {
            boolean completeLine = true;
            for (int j = 0; j < 11; j++) {
                if (map.get(i * 11 + j) == 0) {
                    completeLine = false;
                    break;
                }
            }
            if (completeLine) {
                completeList.add(i);
            }
        }

        for (Integer line : completeList) {
            for (int i = 0; i < 11; i++) {
                map.remove(line * 11);
            }
        }

        for (int i = 0; i < (11 * completeList.size()); i++) {
            map.add(0, 0);
        }

        int numCompleteLine = completeList.size();
        switch(numCompleteLine) {
            case 1: score += 100;
                Toast.makeText(getApplicationContext(), "Good! (1 line was removed)", Toast.LENGTH_SHORT).show();
                break;
            case 2: score += 300;
                Toast.makeText(getApplicationContext(), "Great! (2 line was removed)", Toast.LENGTH_SHORT).show();
                break;
            case 3: score += 600;
                Toast.makeText(getApplicationContext(), "Excellent! (3 line was removed)", Toast.LENGTH_SHORT).show();
                break;
            case 4: score += 1000;
                Toast.makeText(getApplicationContext(), "Awesome! (4 line was removed)", Toast.LENGTH_SHORT).show();
                break;
            default: break;
        }
        scoreView.setText("Score : " + Integer.toString(score));
        updateStuck();
    }

    public void GameOver() {
        for (int i= 11*3; i<11*4; i++) {
            if (stuck.contains(i)) {
                gameOver = true;
                break;
            }
        }

        if (gameOver) {
            alertDialog.show();
            data = new String[2];
            new SendRanking().execute();
            data[0] = MainActivity.userName;
            data[1] = Integer.toString(score);

        }
    }

    public void updateStuck() {
        stuck.clear();
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i) != 0) {
                stuck.add(i);
            }
        }
        for (int i = 11 * 23; i < 11 * 24; i++) {
            stuck.add(i);
        }
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

    public void sendHttpWithContact(String url, String json) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            //Log.d("RESPONSE", response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String rankingStringArrayToJSON(String[] stringArray) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", stringArray[0]);
            jsonObject.put("score", stringArray[1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("JSON", jsonObject.toString());
        return jsonObject.toString();
    }


    public class SendRanking extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... none) {
            sendHttpWithContact("http://52.78.101.202:3000/api/ranks", rankingStringArrayToJSON(data));
            return null;
        }

        protected void onPostExecute(Void none){

        }
    }
}
