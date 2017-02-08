package com.compunetconnections.d2c;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends ActionBarActivity {
    EditText email,sponsor_id;
    Button submit;
    HttpPost httppost;
    HttpResponse response;
    String myJSON="",randomPassword;
    ProgressDialog dialog = null;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    ArrayList<NameValuePair> nameValuePairs;
    SqlliteDB pindb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pindb=new SqlliteDB(this);
       Cursor cursor= pindb.select();
        if(cursor.getCount()>0)
        {
            while(cursor.moveToNext()){
                Intent intenthome=new Intent(MainActivity.this,PinverifyActivity.class);
                startActivity(intenthome);
                finish();
            }
        }
        else{
            setContentView(R.layout.reffral_userid);
            email = (EditText) findViewById(R.id.email);
            sponsor_id = (EditText) findViewById(R.id.sponsor_id);
            submit = (Button) findViewById(R.id.submit);


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (email.getText().toString().isEmpty() && sponsor_id.getText().toString().isEmpty()) {
                        Intent mainintent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(mainintent);
                    }
                    if (!email.getText().toString().isEmpty() && !sponsor_id.getText().toString().isEmpty()) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("INFORMATION")
                                .setMessage("Please fill any one filed")
                                .setIcon(android.R.drawable.stat_notify_error)
                                .setNegativeButton(android.R.string.ok, null).show();
                    } else {
                        if (!email.getText().toString().isEmpty()) {
                            randomPassword= randomPassword();
                            nameValuePairs = new ArrayList<NameValuePair>();
                            nameValuePairs.add(new BasicNameValuePair("verification", randomPassword));
                            nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
                            new  VerificationEmail().execute("");

                        } else {
                            nameValuePairs = new ArrayList<NameValuePair>();
                            nameValuePairs.add(new BasicNameValuePair("sponserid", sponsor_id.getText().toString()));
                            StrictMode.setThreadPolicy(policy);
                            new VerifySponcerExit().execute("");
                        }
                    }
                }
            });
        }

            }

    public String randomPassword() {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String password="";//remember to declare $pass as an array//put the length -1 in cache
        for (int i = 0; i < 8; i++) {
            Random r = new Random();
            int i1 = r.nextInt(alphabet.length()-1) ;
            String df = alphabet.substring(i1, i1 + 1);
            password=password.concat(df);
            Log.e("password",password);
        }
        return password;
    }
    private class VerifySponcerExit extends AsyncTask<String, Void, String> {
        @Override
        protected  void onPreExecute()
        {
            dialog = ProgressDialog.show(MainActivity.this, "",
                    "verifying...", true);
        }
        @Override
        protected String doInBackground(String... params) {
            String responseStr="";

            try {
                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://d2c.compunet.in/verify_sponser_id.php");
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
                    Toast.makeText(getApplicationContext(), "failed1", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Intent mainintent = new Intent(MainActivity.this, RegisterActivity.class);
                    mainintent.putExtra("type","insert");
                    mainintent.putExtra("sponserid",result);
                    startActivity(mainintent);
                    finish();
                }
            }
        }

    }

    private class VerificationEmail extends AsyncTask<String, Void, String> {
        @Override
        protected  void onPreExecute()
        {
            dialog = ProgressDialog.show(MainActivity.this, "",
                    "verifying...", true);
        }
        @Override
        protected String doInBackground(String... params) {
            String responseStr="";

            try {
                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://d2c.compunet.in/forgotpassword.php");
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
                if (result.equals("user_email_not_exist")) {
                    Toast.makeText(getApplicationContext(), "user email id not exist", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Intent intent=new Intent(MainActivity.this,ForgotPinActivity.class);
                    intent.putExtra("randomPassword",randomPassword);
                    intent.putExtra("type","insert");
                    intent.putExtra("email",result);
                    startActivity(intent);
                    finish();
                }
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.other_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
