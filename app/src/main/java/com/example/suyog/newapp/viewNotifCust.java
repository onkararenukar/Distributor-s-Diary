package com.example.suyog.newapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewNotifCust extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notif_cust);

        String distId=SaveSharedPreference.getFkOfDist(getApplicationContext());
        setMsg(distId);

    }

    public void setMsg(final String distId)
    {
        final TextView tv= findViewById(R.id.textv);
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("NotifDtoC");

        dbRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild(distId))
                    tv.setText(dataSnapshot.child(distId).getValue().toString());
                else
                    tv.setText("No any Notifications");
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }
}
