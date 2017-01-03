package com.example.q.cs496_proj2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static int contactCount = 1;

    public static ArrayList<String[]> totalContacts = new ArrayList<>();
    public static ArrayList<String[]> phoneContacts = new ArrayList<>();
    public static ArrayList<String[]> facebookContacts = new ArrayList<>();
    public static ArrayList<String[]> rankingList = new ArrayList<>();
    public FragmentA fragmentA;
    public static String userName = "Anonymous";

    private CallbackManager callbackManager;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("result", "************************************** " + object.toString());
                        try {
                            String userID = object.getString("id");
                            userName = object.getString("name");
                            Log.d("USERID", "*********************************** user ID : " + userID);
                            getFacebookContacts(userID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr",error.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    fragmentA = new FragmentA();
                    return fragmentA;
                case 1:
                    FragmentB fragmentB = new FragmentB();
                    return fragmentB;
                case 2:
                    FragmentC fragmentC = new FragmentC();
                    return fragmentC;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Contacts";
                case 1:
                    return "Gallery";
                case 2:
                    return "TETRIS";
            }
            return null;
        }
    }

    public void getFacebookContacts(String userID) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + userID + "/taggable_friends?limit=50",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("RESPONSE", "********************************************** " + response.toString());
                        try {
                            JSONArray friendList = response.getJSONObject().getJSONArray("data");
                            String[] data;
                            for (int i = 0; i < friendList.length(); i++) {
                                data = new String[3];
                                data[0] = Integer.toString(contactCount);
                                contactCount += 1;
                                data[1] = friendList.getJSONObject(i).getString("name");
                                data[2] = "facebook";
                                facebookContacts.add(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new PostFacebookContacts().execute();
                    }
                }
        ).executeAsync();
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

    public String contactStringArrayToJSON(String[] stringArray) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", stringArray[1]);
            jsonObject.put("phoneNumber", stringArray[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("JSON", jsonObject.toString());
        return jsonObject.toString();
    }

    public class PostFacebookContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... none) {
            for (String[] FBcontact : facebookContacts) {
                sendHttpWithContact("http://52.78.101.202:3000/api/contacts", contactStringArrayToJSON(FBcontact));
            }
            return null;
        }

        protected void onPostExecute(Void none){
            totalContacts = phoneContacts;
            totalContacts.addAll(facebookContacts);
            Collections.sort(totalContacts, new Comparator<String[]>() {
                @Override
                public int compare(String[] o1, String[] o2) {
                    return o1[1].compareTo(o2[1]);
                }
            });
            fragmentA.updateListView(totalContacts);
        }
    }
}
