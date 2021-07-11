package com.example.suyog.newapp;

import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class sendNotifCust extends AppCompatActivity {

    TextView tvNewNotif,prevMsg;
    LinearLayout templateLL;
    String distId,custId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notif_cust);


        custId=SaveSharedPreference.getUserName(getApplicationContext());
        distId=SaveSharedPreference.getFkOfDist(getApplicationContext());


        tvNewNotif=findViewById(R.id.newNotif);
        prevMsg=findViewById(R.id.prevMsg);

        TextView tvt1=findViewById(R.id.template1);
        TextView tvt2=findViewById(R.id.template2);

        Date dt= new Date();

        Calendar c= Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE,1);

        dt=c.getTime();

        SimpleDateFormat fdtae=new SimpleDateFormat("dd-MMMM-yyyy");
        String date =fdtae.format(dt);

        tvt1.append(" "+date);
        tvt2.append(" "+date+" ltrs of  ");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder( this);

        alertDialog.setTitle("Note : ");

        alertDialog.setMessage("Sending current message will remove the previous one");
        AlertDialog dialog=alertDialog.create();
        dialog.show();

        templateLL=findViewById(R.id.templateLL);

        DatabaseReference dbRefNotDtoC= FirebaseDatabase.getInstance().getReference("NotifCtoD").child(distId);

        dbRefNotDtoC.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild(custId))
                    prevMsg.setText(dataSnapshot.child(custId).getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void load_template(View view)
    {
        TextView tv= findViewById(view.getId());

        String str=tv.getText().toString();

        tvNewNotif.setText(str);
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(getApplicationContext(),cust_homepage.class));
        finish();
    }


    public void disp(View view)
    {
        if(templateLL.getVisibility()==View.GONE)
            templateLL.setVisibility(View.VISIBLE);
        else
            templateLL.setVisibility(View.GONE);
    }

    public void sendMesaage(View view)
    {
        TextView tv=findViewById(R.id.newNotif);

        String msg=tv.getText().toString().trim();

        if(msg.isEmpty())
        {
            tv.setError("Message cannot be empty");
            tv.requestFocus();
            return;
        }

        msg+="\n\nSent On : ";

        Date dt=Calendar.getInstance().getTime();

        SimpleDateFormat fdtae=new SimpleDateFormat("dd-MMM-yyyy");
        String date =fdtae.format(dt);

        msg+=date;

        dt=new Date();
        fdtae= new SimpleDateFormat("hh:mm a");

        date=fdtae.format(dt);

        msg+=", "+date;

        FirebaseDatabase.getInstance().getReference("NotifCtoD").child(distId).child(custId).setValue(msg);

        Toast.makeText(getApplicationContext(),"Notification Sent",Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(),cust_homepage.class));
        finish();

    }
}
