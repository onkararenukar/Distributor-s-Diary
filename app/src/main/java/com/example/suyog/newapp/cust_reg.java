package com.example.suyog.newapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class cust_reg extends AppCompatActivity {

    EditText etname,etaddr,etpincode,etemail,etcontact,etpasswd,etconfpass;
    int proceed;
    ValueEventListener custl,buffl,distl;

    DatabaseReference custdbref,bufcustdbref,distdbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_reg);

        custdbref= FirebaseDatabase.getInstance().getReference("Customer");
        bufcustdbref=FirebaseDatabase.getInstance().getReference("Buffcust");
        distdbref=FirebaseDatabase.getInstance().getReference("Distributor");

        etname= (EditText) findViewById(R.id.cname);
        etaddr= (EditText) findViewById(R.id.caddress);
        etpincode=(EditText) findViewById(R.id.cpincode);
        etemail= (EditText) findViewById(R.id.cemail);
        etcontact= (EditText) findViewById(R.id.cmobile);
        etpasswd= (EditText) findViewById(R.id.cpassword);
        etconfpass=(EditText) findViewById(R.id.cconfpwd);

    }

    public void to_search_dist(View view) {

        proceed=1;

        String name=etname.getText().toString().trim();
        final String email=etemail.getText().toString().trim();
        String addr=etaddr.getText().toString().trim();
        String contact=etcontact.getText().toString().trim();
        String passwd=etpasswd.getText().toString().trim();
        String confpass=etconfpass.getText().toString().trim();
        final String pincode=etpincode.getText().toString().trim();

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


        if(!confpass.equalsIgnoreCase(passwd))
        {
            etconfpass.setError("Password don't Match");
            etconfpass.requestFocus();
            return;
        }


       distl=distdbref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for ( DataSnapshot dist : dataSnapshot.getChildren())
               {
                   String str=dist.child("distEmail").getValue().toString();
                   if(email.equalsIgnoreCase(str))
                   {
                       Toast.makeText(getApplicationContext(),"Email Id Already Registered",Toast.LENGTH_LONG).show();
                       etemail.setText("");
                       proceed=0;
                   }
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

        custl=custdbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for ( DataSnapshot dist : dataSnapshot.getChildren())
                {
                    for (DataSnapshot cust : dist.getChildren())
                    {
                        String str = cust.child("custEmail").getValue().toString();
                            if (email.equalsIgnoreCase(str))
                            {
                                Toast.makeText(getApplicationContext(), "Email Id Already Registered", Toast.LENGTH_LONG).show();
                                etemail.setText("");
                                proceed = 0;
                            }
                     }
                }
                //Toast.makeText(getApplicationContext(),"check 1 exit",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });

        buffl=bufcustdbref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for ( DataSnapshot cust : dataSnapshot.getChildren())
                {
                    String str=cust.child("custEmail").getValue().toString();
                    if(email.equalsIgnoreCase(str))
                    {
                        Toast.makeText(getApplicationContext(),"Email Id Already Registered",Toast.LENGTH_LONG).show();
                        etemail.setText("");
                        proceed=0;
                    }
                }

                add();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void add()
    {
        custdbref.removeEventListener(custl);
        bufcustdbref.removeEventListener(buffl);

        //Toast.makeText(getApplicationContext(),"Check complete, Proceed= "+proceed,Toast.LENGTH_SHORT).show();
        if(proceed==1)
        {

            String name=etname.getText().toString().trim();
            String email=etemail.getText().toString().trim();
            String addr=etaddr.getText().toString().trim();
            String contact=etcontact.getText().toString().trim();
            String passwd=etpasswd.getText().toString().trim();
            String pincode=etpincode.getText().toString().trim();

            String id=bufcustdbref.push().getKey();

            Customer cust= new Customer(id,name,addr,pincode,email,contact,passwd);

            bufcustdbref.child(id).setValue(cust);

            Toast.makeText(getApplicationContext(),"Registered",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getBaseContext(), search_dist.class);
            intent.putExtra("pincode", pincode);
            intent.putExtra("id",id);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(getApplicationContext(),Registration.class));
        finish();
    }
}
