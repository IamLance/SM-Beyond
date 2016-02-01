package com.dlsu.ccs.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Jarrette on 12/5/2015.
 */
public class LoggedInActivity extends AppCompatActivity {
    private TextView message;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);

        String username = getIntent().getExtras().getString("Name");

        message = (TextView)findViewById(R.id.textView);
        message.append(" " + username + "!");

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutPost();
                Intent intent = new Intent(LoggedInActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void logoutPost(){
        new LogoutActivity(this).execute();
    }

}
