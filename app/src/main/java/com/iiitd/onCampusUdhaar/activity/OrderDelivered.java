package com.iiitd.onCampusUdhaar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.other.BookingOrder;
import com.iiitd.onCampusUdhaar.other.ConfigurationFirebase;

// Code for  the Order Delivered confirmation from seller/renter and giving the raating to the buyer.


public class OrderDelivered extends AppCompatActivity {
    private BookingOrder orderDeliver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_delivered);

    }
    public void Accept2(View view) {
        orderDeliver =  (BookingOrder) getIntent().
                getSerializableExtra("orderDeliver");
        if(orderDeliver != null) {
            DatabaseReference orderRef = ConfigurationFirebase.getFirebase()
                    .child("orders")
                    .child(orderDeliver.getAdvertisementID());

            if(orderDeliver.getAdvertisementtype()!=null && orderDeliver.getAdvertisementtype().equalsIgnoreCase("Rent")){
                orderRef.child(orderDeliver.getidOrder()).child("status").setValue(4);
                Intent intent=new Intent(getApplicationContext(),MyOrders.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Product Received", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                orderRef.child(orderDeliver.getidOrder()).child("status").setValue(5);
                Intent intent=new Intent(getApplicationContext(), RatingActivity.class);
                startActivity(intent);
                intent.putExtra("orderDeliver",orderDeliver);
                Toast.makeText(getApplicationContext(), "Product Received and Order Completed Successfully", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }
    }


