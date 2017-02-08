package com.compunetconnections.d2c;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
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

import java.util.ArrayList;

public class VerifyActivity extends ActionBarActivity {
    EditText refernce,setpin;
    LinearLayout setpinlayout,referencelayout;
    Button verify,submit;
    String type;
    String code="111";





    HttpPost httppost;
    HttpResponse response;
    String myJSON="";
    ProgressDialog dialog = null;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    EditText question;
    Button answer;
    ArrayList<NameValuePair> nameValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpin_update);
       /* verify=(Button)findViewById(R.id.verify);
        submit=(Button)findViewById(R.id.submit);
        setpinlayout=(LinearLayout)findViewById(R.id.setpin_layout);
        referencelayout=(LinearLayout)findViewById(R.id.referencelayout);
        refernce=(EditText)findViewById(R.id.reference);
        setpin=(EditText)findViewById(R.id.setpin);
        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        if(type.equals("existing"))
        {
            refernce.setHint("Enter Verification Code");
        }
        else
        {
            refernce.setHint("Enter Referral Code");
        }
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("existing")) {
                    if (refernce.getText().toString().equals(code)) {
                        refernce.setError(null);
                        referencelayout.setVisibility(View.GONE);
                        setpinlayout.setVisibility(View.VISIBLE);
                    } else {
                        refernce.setText("");
                        refernce.setError("some mistake your entered code");
                    }
                }
                else
                {
                    if(refernce.getText().toString().equals(code))
                    {
                        refernce.setError(null);
                        referencelayout.setVisibility(View.GONE);
                        setpinlayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        refernce.setText("");
                        refernce.setError("This referral id not exist");
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setpin.getText().toString().length() >= 4 && setpin.getText().toString().length() < 10) {
                    setpin.setError(null);
                    Intent intent = new Intent(VerifyActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    setpin.setText("");
                    setpin.setError("your pin limit 4 to 10 digit");
                }
            }
        });*/
    }
    private class ARTQuestionPost extends AsyncTask<String, Void, String> {
        @Override
        protected  void onPreExecute()
        {
            dialog = ProgressDialog.show(VerifyActivity.this, "",
                    "sending...", true);
        }
        @Override
        protected String doInBackground(String... params) {
            String responseStr="";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                httppost = new HttpPost("http://iyyam.compunet.in/ask_question.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response=httpclient.execute(httppost);
                myJSON = response.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
                myJSON="networkerror";
                dialog.dismiss();
            }
            return myJSON;
        }
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if(result.equals("networkerror")) {
            }
            else
            {
                Toast.makeText(getApplicationContext(), "your question is posted answer will get soon", Toast.LENGTH_LONG).show();
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
