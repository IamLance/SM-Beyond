package com.dlsu.ccs.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jarrette on 12/6/2015.
 */
public class TermsActivity extends AppCompatActivity {
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_conditions);

        prefs = getSharedPreferences("com.dlsu.ccs.signin", MODE_PRIVATE);


    }

    @Override
    protected void onResume(){
        super.onResume();

        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);

        if (prefs.getBoolean("firstrun", true)) {
            // First
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TermsActivity.this, MainActivity.class);
                    prefs.edit().putBoolean("firstrun", false).commit();
                    TermsActivity.this.startActivity(intent);
                    TermsActivity.this.finish();
                }
            });

            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TermsActivity.this.finish();
                /*
                Intent intent = new Intent(TermsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                */
                }
            });
        }
        else{
            //Second
            Intent intent = new Intent(TermsActivity.this, MainActivity.class);
            TermsActivity.this.startActivity(intent);
            TermsActivity.this.finish();
        }


    }
}
