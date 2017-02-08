package com.compunetconnections.d2c;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ForgotPinActivity extends ActionBarActivity {
    EditText verificationcode;
    Button submit;
    String code,type,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_pin);
        verificationcode=(EditText)findViewById(R.id.verificatio_code);
        submit=(Button)findViewById(R.id.submit);
        Intent intent=getIntent();
        code= intent.getStringExtra("randomPassword");
        type=intent.getStringExtra("type");
        if(intent.getStringExtra("email")!=null)
        {
           type="reset";
            username=intent.getStringExtra("email");
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificationcode.getText().toString().equals(code))
                {
                    verificationcode.setError(null);
                    Intent intent=new Intent(ForgotPinActivity.this,UpdatePinActivity.class);
                    intent.putExtra("type",type);
                    intent.putExtra("email",username);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    verificationcode.setError("Not Match");
                }
            }
        });
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
