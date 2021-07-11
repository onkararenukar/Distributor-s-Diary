package com.example.suyog.newapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class search_cust extends AppCompatActivity {

    EditText etName,etEmail,etPincode,etContact;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cust);

        etName= findViewById(R.id.cname);
        etEmail=findViewById(R.id.cemail);
        etPincode=findViewById(R.id.cpincode);
        etContact=findViewById(R.id.cmobile);

    }

    public void view_cust_list(View view)
    {
        String name,email,contact,pincode;

        name=etName.getText().toString().trim();
        email=etEmail.getText().toString().trim();
        contact=etContact.getText().toString().trim();
        pincode=etPincode.getText().toString().trim();

        Intent intent = new Intent(getApplicationContext(),cust_list.class);
        intent.putExtra("name",name);
        intent.putExtra("email",email);
        intent.putExtra("contact",contact);
        intent.putExtra("pincode",pincode);

        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(getApplicationContext(),dist_homepage.class));
        finish();
    }

}
