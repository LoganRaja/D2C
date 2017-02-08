package com.compunetconnections.d2c;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class RegisterActivity extends ActionBarActivity {
    EditText username,phonenumber,email;
    Button submit;


    HttpPost httppost;
    HttpResponse response;
    String myJSON="",macAddress;
    ProgressDialog dialog = null;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    ArrayList<NameValuePair> nameValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeration);



        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        macAddress = wInfo.getMacAddress();


        username=(EditText)findViewById(R.id.username);
        email=(EditText)findViewById(R.id.email);
        phonenumber=(EditText)findViewById(R.id.phone);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username",username.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("phone_numer", phonenumber.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("mac_address", macAddress));
                nameValuePairs.add(new BasicNameValuePair("referral_id",intent.getStringExtra("sponserid")));
                new  Registraion().execute("");
            }
        });

    }

    private class Registraion extends AsyncTask<String, Void, String> {
        @Override
        protected  void onPreExecute()
        {
            dialog = ProgressDialog.show(RegisterActivity.this, "",
                    "Registering...", true);
        }
        @Override
        protected String doInBackground(String... params) {
            String responseStr="";

            try {
                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://d2c.compunet.in/register.php");
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
                    Intent mainintent = new Intent(RegisterActivity.this, UpdatePinActivity.class);
                    mainintent.putExtra("type","insert");
                    mainintent.putExtra("email",email.getText().toString());
                    startActivity(mainintent);
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
