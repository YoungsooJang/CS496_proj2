package com.example.q.cs496_proj2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentA extends Fragment {

    public FragmentA() {
    }

    private ListView listView1;
    private ArrayList<String[]> phoneContacts;
    private ContactListViewAdapter contactListViewAdapter;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS  = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenta, container, false);

        listView1 = (ListView) view.findViewById(R.id.listView1);

        new GetPhoneContacts().execute();

        return view;
    }

    public class GetPhoneContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... none) {
            deleteAllContactsInServer("http://52.78.101.202:3000/api/contacts");
            getPermissionToGetPhoneContacts();
            return null;
        }

        protected void onPostExecute(Void none){
            contactListViewAdapter = new ContactListViewAdapter(getActivity(), phoneContacts);
            listView1.setAdapter(contactListViewAdapter);
        }
    }

    private void getPermissionToGetPhoneContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            getPhoneContacts();
        }
    }

    private void getPhoneContacts() {
        Uri contactsURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] reqCols = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        ContentResolver cr = getActivity().getContentResolver();

        Cursor contactCursor = cr.query(contactsURI, reqCols, null, null, sortOrder);

        phoneContacts = new ArrayList<>();
        String[] data;
        while(contactCursor.moveToNext()) {
            data = new String[3];
            data[0] = contactCursor.getString(0);
            data[1] = contactCursor.getString(1);
            data[2] = contactCursor.getString(2);
            phoneContacts.add(data);
            Log.d("contact", data[0] + "    " + data[1] + "    " + data[2]);
            sendHttpWithContact("http://52.78.101.202:3000/api/contacts", contactStringArrayToJSON(data));
        }
        contactCursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                getPhoneContacts();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we cannot get the phone's contacts", Toast.LENGTH_SHORT).show();
            }
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
            Log.d("RESPONSE", response.body().string());
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

    public void deleteAllContactsInServer(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            Log.d("RESPONSE", response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
