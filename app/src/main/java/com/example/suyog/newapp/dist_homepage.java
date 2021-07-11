package com.example.suyog.newapp;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class dist_homepage extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dist_homepage);
    }

    public void to_view_requests(View view) {
        startActivity(new Intent(getApplicationContext(),view_requests.class));
    }

    @Override
    public void onBackPressed()
    {
        this.finishAffinity();
    }

    public void action_LogOut(View view) {
        SaveSharedPreference.clearUserName(getApplicationContext());
        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }

    public void to_manage_cust(View view)
    {
        AlertDialog.Builder builder;
        builder=new AlertDialog.Builder(dist_homepage.this);

        builder.setMessage("Please select one of the following, View existing " + "Customers/ " +
                "Add new Customer").setCancelable(true).setPositiveButton("View", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(),search_cust.class));
                finish();
            }
        }).setNegativeButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(),addCustomer.class));
                finish();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();

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
                startActivity(new Intent(getApplicationContext(),sendNotifDist.class));
                finish();
            }
        });
        alertDialog.setPositiveButton("View", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                startActivity(new Intent(getApplicationContext(),viewNotifDist.class));
            }
        });
        AlertDialog dialog=alertDialog.create();
        dialog.show();

    }

    public void to_todays_sales(View view)
    {
        startActivity( new Intent(getApplicationContext(),addTodaysSales.class));
        finish();
    }

    public void to_view_bills(View view)
    {
        startActivity( new Intent(getApplicationContext(),viewBillsDist.class));
        finish();

    }

    public void to_dist_profile(View view)
    {
        startActivity(new Intent(getApplicationContext(),distProfile.class));
        finish();
    }
    //ADD intent call for PRODUCTS
    public void to_products(View view)
    {
        startActivity(new Intent(getApplicationContext(),addProduct.class));
        finish();
    }
}
