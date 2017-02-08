package com.compunetconnections.d2c;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class HomeActivity extends ActionBarActivity  {
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0, buttonclear, buttonverify, buttonback;
    EditText pin_get_verify;
    String email;
    TextView label;
    SqlliteDB pindb;

    String[][] myStringArray ;
    HttpPost httppost;
    HttpResponse response;
    String myJSON="",randomPassword;
    ProgressDialog dialog = null;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    ArrayList<NameValuePair> nameValuePairs;
    ListView listview;
    ArrayList<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        listview = (ListView) findViewById(R.id.listview);
        pindb=new SqlliteDB(this);
        Cursor cursor= pindb.select();
        while(cursor.moveToNext()){
            email = cursor.getString(cursor.getColumnIndex("email"));
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email",email));
           new SelectReferrals().execute("");
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite:
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Invite");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.setData(Uri.parse("mailto:")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }









    private class SelectReferrals extends AsyncTask<String, Void, String> {
        @Override
        protected  void onPreExecute()
        {
            dialog = ProgressDialog.show(HomeActivity.this, "",
                    "verifying...", true);
        }
        @Override
        protected String doInBackground(String... params) {
            String responseStr="";

            try {
                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://d2c.compunet.in/select_myferrals.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response=httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                myJSON= responseStr.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("error", String.valueOf(ex));
                myJSON="networkerror";
                dialog.dismiss();
            }
            return myJSON;
        }
        @Override
        protected void onPostExecute(String result) {
            result= result.replaceAll("\\s+", "");
            dialog.dismiss();
            Log.e("result",result);
            if(result.equals("networkerror")) {
                Toast.makeText(getApplicationContext(), "your network is not connected please verify", Toast.LENGTH_LONG).show();
            }
            else {
                if (result.equals("failed")) {
                    Toast.makeText(getApplicationContext(), "user does not have any referral member", Toast.LENGTH_LONG).show();

                }
                else
                {
                    try {
                        JSONArray mJsonArray = new JSONArray(result);
                        JSONObject mJsonObject = new JSONObject();
                        myStringArray=new String[mJsonArray.length()][5];
                        for (int i = 0; i < mJsonArray.length(); i++) {
                            mJsonObject = mJsonArray.getJSONObject(i);
                            myStringArray[i][0] = mJsonObject.getString("username");
                            myStringArray[i][1] = mJsonObject.getString("email");
                            list.add(mJsonObject.getString("username"));
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("errror", String.valueOf(e));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(HomeActivity.this,android.R.layout.simple_list_item_1, list);
                    listview.setAdapter(adapter);
                }
            }
        }

    }
}
