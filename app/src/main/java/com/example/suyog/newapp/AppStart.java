package com.example.suyog.newapp;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AppStart extends AppCompatActivity {

    private static int delay=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);

        if((SaveSharedPreference.getUserName(AppStart.this)).length()==0)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), Registration.class));
                    finish();
                }
            }, 500);
        }
        else
        {
            if((SaveSharedPreference.getUtype(AppStart.this)).equalsIgnoreCase("distributor"))
            {
                startActivity(new Intent(getApplicationContext(),dist_homepage.class));
                finish();
            }
            else
                if((SaveSharedPreference.getUtype(AppStart.this)).equalsIgnoreCase("customer"))
                {
                    startActivity(new Intent(getApplicationContext(),cust_homepage.class));
                    finish();
                }
        }

    }
}
