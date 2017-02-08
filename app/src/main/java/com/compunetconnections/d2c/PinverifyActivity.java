package com.compunetconnections.d2c;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import java.util.ArrayList;
import java.util.Random;

public class PinverifyActivity extends ActionBarActivity implements View.OnClickListener {
    Button button1,button2,button3,button4,button5,button6,button7,button8,button9,button0,buttonclear,buttonverify,buttonback;
    EditText pin_get_verify;
    String pin_content=null;
    TextView label;
    SqlliteDB pindb;


    HttpPost httppost;
    HttpResponse response;
    String myJSON="",randomPassword;
    ProgressDialog dialog = null;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    ArrayList<NameValuePair> nameValuePairs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpin_update);












        button0=(Button)findViewById(R.id.id0);
        button1=(Button)findViewById(R.id.id1);
        button2=(Button)findViewById(R.id.id2);
        button3=(Button)findViewById(R.id.id3);
        button4=(Button)findViewById(R.id.id4);
        button5=(Button)findViewById(R.id.id5);
        button6=(Button)findViewById(R.id.id6);
        button7=(Button)findViewById(R.id.id7);
        button8=(Button)findViewById(R.id.id8);
        button9=(Button)findViewById(R.id.id9);
        buttonclear=(Button)findViewById(R.id.idclear);
        buttonverify=(Button)findViewById(R.id.idverify);
        buttonback=(Button)findViewById(R.id.buttonback);

        pin_get_verify=(EditText)findViewById(R.id.pin_get_text);


        label=(TextView)findViewById(R.id.labletext);
        label.setText("Enter Pin");
        pindb=new SqlliteDB(this);

        buttonback.setText("Forgot Password ?");

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonclear.setOnClickListener(this);
        buttonverify.setOnClickListener(this);
        buttonback.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
       pin_get_verify.setError(null);
        switch(view.getId()){
            case R.id.id0:
                pin_get_verify.setText(pin_get_verify.getText().toString().concat("0"));
                break;
            case R.id.id1:
                pin_get_verify.setText(pin_get_verify.getText().toString().concat("1"));
                break;
            case R.id.id2:
                pin_get_verify.setText(pin_get_verify.getText().toString().concat("2"));
                break;
            case R.id.id3:
                pin_get_verify.setText(pin_get_verify.getText().toString().concat("3"));
                break;
            case R.id.id4:
                pin_get_verify.setText(pin_get_verify.getText().toString().concat("4"));
                break;
            case R.id.id5:
                pin_get_verify.setText(pin_get_verify.getText().toString().concat("5"));
                break;
            case R.id.id6:
                pin_get_verify.setText(pin_get_verify.getText().toString().concat("6"));
                break;
            case R.id.id7:
                pin_get_verify.setText(pin_get_verify.getText().toString().concat("7"));
                break;
            case R.id.id8:
                pin_get_verify.setText(pin_get_verify.getText().toString().concat("8"));
                break;
            case R.id.id9:
                pin_get_verify.setText(pin_get_verify.getText().toString().concat("9"));
                break;
            case R.id.idclear:
                if(!pin_get_verify.getText().toString().isEmpty())
                pin_get_verify.setText(pin_get_verify.getText().toString().substring(0, pin_get_verify.getText().toString().length() - 1));
                break;
            case R.id.idverify:
                pindb=new SqlliteDB(this);
                Cursor cursor= pindb.select();

                    while(cursor.moveToNext()){
                        Log.e("count", String.valueOf(cursor.getCount()));
                        Log.e("id", cursor.getString(cursor.getColumnIndex("id")));
                        Log.e("pin", cursor.getString(cursor.getColumnIndex("pin")));
                        Log.e("email", cursor.getString(cursor.getColumnIndex("email")));
                        if(pin_get_verify.getText().toString().equals(cursor.getString(cursor.getColumnIndex("pin")).toString()))
                        {
                            Intent intenthome=new Intent(PinverifyActivity.this,HomeActivity.class);
                            startActivity(intenthome);
                            finish();
                        }
                        else
                        {
                            pin_get_verify.setError("Incorrect");
                            buttonback.setVisibility(View.VISIBLE);
                        }
                    }

                break;
            case R.id.buttonback:
                buttonback.setEnabled(false);
                Cursor cursor1 = pindb.select();
                String email = null;
                while (cursor1.moveToNext()) {
                    email = cursor1.getString(cursor1.getColumnIndex("email")).toString();
                }
                randomPassword=randomPassword();
                nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("verification",randomPassword));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                new  VerificationEmail().execute("");
                break;
            default:
                break;
        }
    }



    private class VerificationEmail extends AsyncTask<String, Void, String> {
        @Override
        protected  void onPreExecute()
        {
            dialog = ProgressDialog.show(PinverifyActivity.this, "",
                    "Email sending...", true);
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
                    Toast.makeText(getApplicationContext(), "verfication code send your mail please verify", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(PinverifyActivity.this,ForgotPinActivity.class);
                    intent.putExtra("randomPassword",randomPassword);
                    intent.putExtra("type", "update");
                    startActivity(intent);
                    finish();
                }
            }
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
