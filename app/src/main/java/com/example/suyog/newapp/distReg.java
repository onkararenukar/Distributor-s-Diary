package com.example.suyog.newapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class distReg extends AppCompatActivity {

    EditText etname,etaddr,etemail,etcontact,etpasswd,etconfpass,etpincode;

    DatabaseReference dbRefDist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dist_reg);

        dbRefDist = FirebaseDatabase.getInstance().getReference("Distributor");

        etname= (EditText) findViewById(R.id.dname);
        etaddr= (EditText) findViewById(R.id.daddress);
        etemail= (EditText) findViewById(R.id.demail);
        etcontact= (EditText) findViewById(R.id.dmobile);
        etpasswd= (EditText) findViewById(R.id.dpassword);
        etconfpass=(EditText) findViewById(R.id.confpass);
        etpincode=(EditText) findViewById(R.id.pincode);

    }


    public void add_dist(View view) {

        String name=etname.getText().toString().trim();
        String email=etemail.getText().toString().trim();
        String addr=etaddr.getText().toString().trim();
        String contact=etcontact.getText().toString().trim();
        String passwd=etpasswd.getText().toString().trim();
        String confpass=etconfpass.getText().toString().trim();
        String pincode=etpincode.getText().toString().trim();

        if(name.isEmpty())
        {
            etname.setError("Please enter a Name");
            etname.requestFocus();
            return ;
        }

        if( ! name.matches("^[A-Za-z \\\\s]{1,}[\\\\.]{0,1}[A-Za-z \\\\s]{0,}$") )
        {
            etname.setError("Only Alphabets and space allowed");
            etname.requestFocus();
            return ;
        }

        if(addr.isEmpty()){
            etaddr.setError("Please enter address");
            etaddr.requestFocus();
            return;
        }

        if(pincode.isEmpty())
        {
            etpincode.setError("Please enter pincode");
            etpincode.requestFocus();
            return;
        }

        if(pincode.length()!=6)
        {
            etpincode.setError("Pincode must be 6 digit long");
            etpincode.requestFocus();
            return;
        }

        if(email.isEmpty())
        {
            etemail.setError("Please enter a Email Id");
            etemail.requestFocus();
            return ;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            etemail.setError("Invalid Email Id");
            etemail.requestFocus();
            return;
        }


        if(contact.isEmpty())
        {
            etcontact.setError("Please enter a mobile No.");
            etcontact.requestFocus();
            return ;
        }

        if(!Patterns.PHONE.matcher(contact).matches() || contact.length()<10)
        {
            etcontact.setError(" Invalid mobile no. ");
            etcontact.requestFocus();
            return;
        }

        if(passwd.isEmpty())
        {
            etpasswd.setError("Please enter a Password");
            etpasswd.requestFocus();
            return ;
        }

        if(! passwd.matches("^[A-Za-z0-9!@#$%^&*\\\\s]{1,}[\\\\.]{0,1}[A-Za-z!@#$%^&*\\\\s]{0,}$"))
        {
            etpasswd.setError("Password should contain alphabets,digits and special symbols only");
            etpasswd.requestFocus();
            return;
        }

        if(passwd.length()<6 || passwd.length()>12)
        {
            etpasswd.setError("Password length must be between 6 to 12");
            etpasswd.requestFocus();
            return;
        }

        if(confpass.isEmpty())
        {
            etconfpass.setError("Please enter a Password");
            etconfpass.requestFocus();
            return ;
        }


        if(!confpass.equals(passwd))
        {
            etconfpass.setError("Password don't Match");
            etconfpass.requestFocus();
            return;
        }


        String id = dbRefDist.push().getKey();

        Distributor distobj= new Distributor(id,name,addr,pincode,email,contact,passwd);

        dbRefDist.child(id).setValue(distobj);

        Toast.makeText(getApplicationContext(),"Registered",Toast.LENGTH_LONG).show();

        Intent intent=new Intent(getBaseContext(),addProduct.class);
        intent.putExtra("id",id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(getApplicationContext(),Registration.class));
        finish();
    }

}
