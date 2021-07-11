package com.example.suyog.newapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addProduct extends AppCompatActivity {

    String distId;
    DatabaseReference dbRefProduct,myref;
    int backPressCount=0,proCount=0;

    EditText etCompName,etPrice,etDelCharges,etDescript,etExtraPros;

    CheckBox cbButter,cbGhee,cbCream,cbCheese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        distId= getIntent().getStringExtra("id");

        dbRefProduct= FirebaseDatabase.getInstance().getReference("product");

        etDelCharges= findViewById(R.id.delCharges);
        etCompName=findViewById(R.id.companyName);
        etPrice=findViewById(R.id.price);
        etDescript=findViewById(R.id.description);
        etExtraPros=findViewById(R.id.moreProdcts);

        cbButter=findViewById(R.id.butter);
        cbGhee=findViewById(R.id.ghee);
        cbCream=findViewById(R.id.cream);
        cbCheese=findViewById(R.id.cheese);

    }

    @Override
    public void onBackPressed() {
        if(backPressCount==0)
        {
            Toast.makeText(getApplicationContext(),"Touch Again to Exit\nProduct details will not be saved !!",Toast.LENGTH_SHORT).show();
            backPressCount++;
            return;
        }


        startActivity(new Intent(getApplicationContext(), Registration.class));
        finish();
    }

    public void add_milk(View view) {

        String name="milk",compName,price,description;
        compName=etCompName.getText().toString().trim();
        price=etPrice.getText().toString().trim();
        description=etDescript.getText().toString().trim();

        if(compName.isEmpty())
        {
            etCompName.setError("Enter Company Name");
            etCompName.requestFocus();
            return;
        }

        if( ! compName.matches("^[A-Za-z \\\\s]{0,}[\\\\.]{0,1}[A-Za-z \\\\s]{0,}$") )
        {
            etCompName.setError("Only Alphabets and space allowed");
            etCompName.requestFocus();
            return ;
        }

        if(price.isEmpty())
        {
            etPrice.setError("Enter price for this product");
            etPrice.requestFocus();
            return;
        }

        Product product=new Product(name,compName,price,description);

        String id=dbRefProduct.child(distId).push().getKey();

        myref=dbRefProduct.child(distId);
        myref.child(id).setValue(product);

        etCompName.setText("");
        etPrice.setText("");
        etDescript.setText("");

        proCount++;
        Toast.makeText(getApplicationContext(),"Product Added",Toast.LENGTH_SHORT).show();
    }

    public void complete_reg(View view) {
        String delCharges,compName,price,otherPro;

        delCharges= etDelCharges.getText().toString().trim();

        if(delCharges.isEmpty())
        {
            etDelCharges.setError("Please fill out this field");
            etDelCharges.requestFocus();
            return;
        }

        compName=etCompName.getText().toString().trim();
        price=etPrice.getText().toString().trim();


        if(proCount==0 && compName.isEmpty() && price.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Add Atleast One Product",Toast.LENGTH_SHORT).show();
            return;
        }

        String extraPros=etExtraPros.getText().toString().trim();

        if( ! extraPros.matches("^[A-Za-z \\\\s]{0,}[\\\\.]{0,1}[A-Za-z \\\\s]{0,}$") )
        {
            etExtraPros.setError("Only Alphabets and space allowed");
            etExtraPros.requestFocus();
            return ;
        }

        if(!compName.isEmpty() && !price.isEmpty())
            add_milk(view);

        otherPro="";
        if(cbButter.isChecked())
            otherPro="Butter";

        if(cbCream.isChecked())
            otherPro+=" Cream";

        if(cbGhee.isChecked())
            otherPro+=" Ghee";

        if(cbCheese.isChecked())
            otherPro+=" Cheese";
        if(!extraPros.isEmpty())
            otherPro+=" "+extraPros;

        myref=dbRefProduct.child(distId);

        myref.child("delCharges").setValue(delCharges);
        myref.child("others").setValue(otherPro);

        Toast.makeText(getApplicationContext(),"Registration Complete",Toast.LENGTH_LONG).show();
        SaveSharedPreference.setUserName(getApplicationContext(),distId,"Distributor");
        startActivity(new Intent(getApplicationContext(),dist_homepage.class));
        finish();
        //startActivity(new Intent(getApplicationContext(),));
    }
}
