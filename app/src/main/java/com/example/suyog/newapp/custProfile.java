package com.example.suyog.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class custProfile extends AppCompatActivity{

        public TextView tvName, tvAddress, tvPincode, tvMobile, tvEmail;
        DatabaseReference dbRefDist;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cust_profile);

            tvName = (TextView) findViewById(R.id.txtName);
            tvAddress = (TextView) findViewById(R.id.txtAddress);
            tvPincode = (TextView) findViewById(R.id.txtPincode);
            tvMobile = (TextView) findViewById(R.id.txtMobile);
            tvEmail = (TextView) findViewById(R.id.txtEmail);

            String custId= SaveSharedPreference.getUserName(getApplicationContext());

            dbRefDist= FirebaseDatabase.getInstance().getReference("Customer").child(custId);


            dbRefDist.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    tvName.setText(dataSnapshot.child("custName").getValue().toString());
                    tvAddress.setText(dataSnapshot.child("custAddr").getValue().toString());
                    tvPincode.setText(dataSnapshot.child("custPincode").getValue().toString());
                    tvEmail.setText(dataSnapshot.child("custEmail").getValue().toString());
                    tvMobile.setText(dataSnapshot.child("custContact").getValue().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }

        @Override
        public void onBackPressed()
        {
            finish();
            startActivity(new Intent(getApplicationContext(),cust_homepage.class));
        }

        public void to_cust_home(View view)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),cust_homepage.class));
        }

}
