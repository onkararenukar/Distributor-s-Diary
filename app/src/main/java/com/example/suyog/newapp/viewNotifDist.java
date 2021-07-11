package com.example.suyog.newapp;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewNotifDist extends AppCompatActivity {

    String distId;
    DatabaseReference dbRefNotifCtoD;
    LinearLayout notifll;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notif_dist);

        distId=SaveSharedPreference.getUserName(getApplicationContext());

        dbRefNotifCtoD= FirebaseDatabase.getInstance().getReference("NotifCtoD").child(distId);

        notifll=findViewById(R.id.notifLL);

        dbRefNotifCtoD.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(notifll.getChildCount()>0)
                    notifll.removeAllViews();

                if(dataSnapshot.getChildrenCount()==0)
                {
                    TextView tv= new TextView(notifll.getContext());

                    tv.setTextColor(Color.BLACK);
                    tv.setBackgroundColor(Color.WHITE);
                    tv.setPadding(0,0,0,10);

                    LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(DpToPx(280), ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,0,DpToPx(10));

                    tv.setText("No Notifications");
                    notifll.addView(tv);
                }
                else
                {
                    for(DataSnapshot custNotif : dataSnapshot.getChildren())
                    {
                        String custId= custNotif.getKey();

                        final TextView tv= new TextView(notifll.getContext());

                        tv.setTextColor(Color.BLACK);
                        tv.setTextSize(13);
                        tv.setBackgroundColor(Color.WHITE);
                        tv.setPadding(15,0,0,5);

                        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(DpToPx(280), ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,0,0,DpToPx(10));

                        DatabaseReference dbRefCust=FirebaseDatabase.getInstance().getReference("Customer").child(distId).child(custId);

                        dbRefCust.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                tv.append("\nFrom "+dataSnapshot.child("custName").getValue()+"\n");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        tv.append(custNotif.getValue().toString());
                        notifll.addView(tv,params);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public int DpToPx(int dp)
    {
        DisplayMetrics dm= getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp*(dm.xdpi/dm.DENSITY_DEFAULT));
    }

}
