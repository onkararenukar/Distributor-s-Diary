package com.example.suyog.newapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void nav(android.view.View v)
    {
        startActivity(new Intent(getApplicationContext(), login.class));
        finish();
    }

    public void to_dist_reg_form(View view)
    {
        startActivity(new Intent(getApplicationContext(), distReg.class));
        finish();
    }

    public void to_new_cust_form(View view)
    {
        startActivity(new Intent(getApplicationContext(), cust_reg.class));
        finish();
    }
}
