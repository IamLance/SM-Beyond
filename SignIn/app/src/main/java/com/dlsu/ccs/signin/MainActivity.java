package com.dlsu.ccs.signin;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private EditText usernameField,passwordField;
    private TextView status,role,method;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameField = (EditText)findViewById(R.id.editText1);
        passwordField = (EditText)findViewById(R.id.editText2);
            //insert null validation and other filters


        status = (TextView)findViewById(R.id.textView6);

    }


    public void loginPost(View view) throws ExecutionException, InterruptedException {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        Intent i = new Intent(MainActivity.this, DeviceAdminBean.class);
        String res = new SigninActivity(this, status).execute(username, password).get();
        if (res != null) {
            i.putExtra("token", res);
            MainActivity.this.startActivity(i);
            finish();
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
}
