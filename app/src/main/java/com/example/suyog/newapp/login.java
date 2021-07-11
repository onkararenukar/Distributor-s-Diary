package com.example.suyog.newapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    EditText etEmail, etMobile, etPasswd;
    DatabaseReference dbRefDist, dbRefCust, dbRefBuffcust;
    int success;
    String logId;
    AlertDialog.Builder builder;
    ValueEventListener listner1, listner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        builder = new AlertDialog.Builder(login.this);

        etEmail = (EditText) findViewById(R.id.email);
        etMobile = (EditText) findViewById(R.id.mobile);
        etPasswd = (EditText) findViewById(R.id.password);

        dbRefDist = FirebaseDatabase.getInstance().getReference("Distributor");
        dbRefCust = FirebaseDatabase.getInstance().getReference("Customer");
        dbRefBuffcust = FirebaseDatabase.getInstance().getReference("Buffcust");

        logId = "";
        success = 0;
    }

    public void login(View view) {

        success = 0;

        final String email, mobile, passwd;

        email = etEmail.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();
        passwd = etPasswd.getText().toString().trim();

        if (email.isEmpty() && mobile.isEmpty()) {
            etEmail.setError("Email Id or Mobile No. is Required");
            etMobile.requestFocus();
            etEmail.requestFocus();

            return;
        }

        if (email.isEmpty() && !mobile.isEmpty() && mobile.length() < 10) {
            etMobile.setError("Mobile No. should be 10 Digits");
            etMobile.requestFocus();
            return;
        }
        if (passwd.isEmpty()) {
            etPasswd.setError("Please enter a password");
            etPasswd.requestFocus();
            return;
        }

        if (!email.isEmpty()) {
            dbRefDist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dist : dataSnapshot.getChildren()) {
                        String dbemail, dbpasswd;

                        dbemail = dist.child("distEmail").getValue().toString();
                        dbpasswd = dist.child("distPasswd").getValue().toString();

                        if (email.equals(dbemail) && passwd.equalsIgnoreCase(dbpasswd)) {
                            logId = dist.child("distId").getValue().toString();
                            SaveSharedPreference.setUserName(getApplicationContext(), logId, "Distributor");
                            startActivity(new Intent(getApplicationContext(), dist_homepage.class));
                            finish();
                            success = 1;
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            dbRefCust.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dist : dataSnapshot.getChildren()) {
                        for (DataSnapshot cust : dist.getChildren()) {
                            String dbemail, dbpasswd;

                            dbemail = cust.child("custEmail").getValue().toString();
                            dbpasswd = cust.child("custPasswd").getValue().toString();

                            if (email.equals(dbemail) && passwd.equalsIgnoreCase(dbpasswd)) {
                                logId = cust.getKey();
                                String distId = cust.child("fkDistId").getValue().toString();
                                SaveSharedPreference.setUserName(getApplicationContext(), logId, "Customer");
                                SaveSharedPreference.setFkOfDist(getApplicationContext(), distId);

                                startActivity(new Intent(getApplicationContext(), cust_homepage.class));
                                finish();
                                success = 1;
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            listner1 = dbRefBuffcust.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot bCust : dataSnapshot.getChildren()) {
                        String dbemail, dbpasswd;

                        dbemail = bCust.child("custEmail").getValue().toString();
                        dbpasswd = bCust.child("custPasswd").getValue().toString();
                        final String id = bCust.child("custId").getValue().toString();

                        if (email.equals(dbemail) && passwd.equalsIgnoreCase(dbpasswd) && bCust.hasChild("fkDistId")) {
                            //Toast.makeText(getApplicationContext(),"Waiting for Distributor response",Toast.LENGTH_SHORT).show();
                            final String did = bCust.child("fkDistId").getValue().toString();
                            final String pcode = bCust.child("custPincode").getValue().toString();


                            builder.setMessage("Request to previous distributor is still pending for approval,Would you like to " +
                                    "send request to another Distributor ? \n(This will discard previous " +
                                    "request automatically !! )").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    FirebaseDatabase.getInstance().getReference("requests").child(did).child(id).setValue(null);
                                    FirebaseDatabase.getInstance().getReference("Buffcust").child(id).child("fkDistId").setValue(null);
                                    FirebaseDatabase.getInstance().getReference("Buffcust").child(id).child("defaults").setValue(null);

                                    Intent intent = new Intent(getBaseContext(), search_dist.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("pincode", pcode);
                                    startActivity(intent);
                                    finish();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                            success = 1;

                        } else if (email.equals(dbemail) && passwd.equalsIgnoreCase(dbpasswd) && !bCust.hasChild("fkDistId")) {
                            final String pcode = bCust.child("custPincode").getValue().toString();

                            builder.setMessage("Sorry, Your previous request is rejected by requestor" +
                                    "\nWould You like to send request to another Distributor").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getBaseContext(), search_dist.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("pincode", pcode);
                                    startActivity(intent);
                                    finish();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });

                            AlertDialog alert = builder.create();

                            alert.show();

                            success = 1;
                        }
                    }

                    if (success == 0)
                        Toast.makeText(getApplicationContext(), "Invalid Credentials1", Toast.LENGTH_LONG).show();

                    removeListner1();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {

            dbRefDist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dist : dataSnapshot.getChildren()) {
                        String dbcontact, dbpasswd, logId;
                        dbcontact = dist.child("distContact").getValue().toString();
                        dbpasswd = dist.child("distPasswd").getValue().toString();

                        if (mobile.equals(dbcontact) && passwd.equalsIgnoreCase(dbpasswd)) {
                            logId = dist.child("distId").getValue().toString();
                            SaveSharedPreference.setUserName(getApplicationContext(), logId, "Distributor");
                            startActivity(new Intent(getApplicationContext(), dist_homepage.class));
                            finish();
                            success = 1;

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            dbRefCust.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dist : dataSnapshot.getChildren()) {
                        for (DataSnapshot cust : dist.getChildren()) {
                            String dbcontact, dbpasswd;

                            dbcontact = cust.child("custContact").getValue().toString();
                            dbpasswd = cust.child("custPasswd").getValue().toString();

                            if (mobile.equals(dbcontact) && passwd.equalsIgnoreCase(dbpasswd)) {
                                logId = cust.child("custId").getValue().toString();
                                String distId = cust.child("fkDistId").getValue().toString();
                                SaveSharedPreference.setUserName(getApplicationContext(), logId, "Customer");
                                SaveSharedPreference.setFkOfDist(getApplicationContext(), distId);
                                startActivity(new Intent(getApplicationContext(), cust_homepage.class));
                                finish();
                                success = 1;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            listner2 = dbRefBuffcust.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot bCust : dataSnapshot.getChildren()) {
                        String dbContact, dbpasswd;

                        final String id = bCust.child("custId").getValue().toString();
                        dbContact = bCust.child("custContact").getValue().toString();
                        dbpasswd = bCust.child("custPasswd").getValue().toString();

                        if (mobile.equals(dbContact) && passwd.equalsIgnoreCase(dbpasswd) && bCust.hasChild("fkDistId")) {
                            final String did = bCust.child("fkDistId").getValue().toString();
                            final String pcode = bCust.child("custPincode").getValue().toString();

                            builder.setMessage("Request to previous distributor is still pending for approval,Would you like to " +
                                    "send request to another Distributor ? \n(This will discard previous " +
                                    "request automatically !! )").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseDatabase.getInstance().getReference("requests").child(did).child(id).setValue(null);
                                    FirebaseDatabase.getInstance().getReference("Buffcust").child(id).child("fkDistId").setValue(null);
                                    FirebaseDatabase.getInstance().getReference("Buffcust").child(id).child("defaults").setValue(null);

                                    Intent intent = new Intent(getBaseContext(), search_dist.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("pincode", pcode);
                                    startActivity(intent);
                                    finish();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });

                            AlertDialog alert = builder.create();

                            alert.show();

                            success = 1;
                        }

                        if (mobile.equals(dbContact) && passwd.equalsIgnoreCase(dbpasswd) && !bCust.hasChild("fkDistId")) {
                            final String pcode = bCust.child("custPincode").getValue().toString();

                            builder.setMessage("Sorry, Your previous request is rejected by distributor" +
                                    "\nWould You like to send request to another Distributor").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(getBaseContext(), search_dist.class);

                                    intent.putExtra("id", id);
                                    intent.putExtra("pincode", pcode);
                                    startActivity(intent);
                                    finish();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();
                            success = 1;
                        }
                    }

                    if (success == 0)
                        Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();

                    removeListner2();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    public void removeListner1() {
        dbRefBuffcust.removeEventListener(listner1);
    }

    public void removeListner2() {
        dbRefBuffcust.removeEventListener(listner2);
    }

    public void to_regist_page(View view) {
        startActivity(new Intent(getApplicationContext(), Registration.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Registration.class));
        finish();
    }
}
