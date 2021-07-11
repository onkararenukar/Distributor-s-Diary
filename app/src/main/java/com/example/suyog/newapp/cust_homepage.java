package com.example.suyog.newapp;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class cust_homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_homepage);
    }

    public void action_LogOut(View view) {
        SaveSharedPreference.clearUserName(getApplicationContext());
        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }

    public void notify(android.view.View v)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder( this);

        alertDialog.setTitle("Notification");

        alertDialog.setMessage("Please Select one of the following\nView notifications/Send Notification");
        alertDialog.setNegativeButton("Send", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                startActivity(new Intent(getApplicationContext(),sendNotifCust.class));
                finish();
            }
        });

        alertDialog.setPositiveButton("View",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                startActivity(new Intent(getApplicationContext(),viewNotifCust.class));
            }
        });
        AlertDialog dialog=alertDialog.create();
        dialog.show();

    }

    public void view_bills(View view)
    {
        startActivity(new Intent(getApplicationContext(),viewBillsCust.class));
    }

    public void to_cust_profile(View view){
        startActivity(new Intent(getApplicationContext(),custProfile.class));
    }
}
