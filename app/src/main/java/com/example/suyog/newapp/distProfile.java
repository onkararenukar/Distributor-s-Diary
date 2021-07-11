package com.example.suyog.newapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class distProfile extends AppCompatActivity
{

    public TextView tvName, tvAddress, tvPincode, tvMobile, tvEmail;
    DatabaseReference dbRefDist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dist_profile);

        tvName = (TextView) findViewById(R.id.txtName);
        tvAddress = (TextView) findViewById(R.id.txtAddress);
        tvPincode = (TextView) findViewById(R.id.txtPincode);
        tvMobile = (TextView) findViewById(R.id.txtMobile);
        tvEmail = (TextView) findViewById(R.id.txtEmail);

        final String distId= SaveSharedPreference.getUserName(getApplicationContext());
        System.out.println("++++++DIST+++++++"+distId);
        dbRefDist=FirebaseDatabase.getInstance().getReference("Distributor").child(distId);
        System.out.println("+++++++DIST++++++"+dbRefDist);

        dbRefDist.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                System.out.println("++++++DIST+++++++"+dataSnapshot);
                tvName.setText(dataSnapshot.child("distName").getValue().toString());
                tvAddress.setText(dataSnapshot.child("distAddr").getValue().toString());
                tvPincode.setText(dataSnapshot.child("distPincode").getValue().toString());
                tvEmail.setText(dataSnapshot.child("distEmail").getValue().toString());
                tvMobile.setText(dataSnapshot.child("distContact").getValue().toString());
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
        startActivity(new Intent(getApplicationContext(),dist_homepage.class));
    }

    public void to_dist_home(View view)
    {
        finish();
        startActivity(new Intent(getApplicationContext(),dist_homepage.class));
    }
}
