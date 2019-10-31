package com.iiitd.onCampusUdhaar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.other.Advertisement;
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
                Intent intent=new Intent(getApplicationContext(),MyOrders.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Product Received and Order Completed Successfully", Toast.LENGTH_SHORT).show();
                finish();
//                DatabaseReference orderRef1 = ConfigurationFirebase.getFirebase()
//                        .child("advertisement")
//                        .child(orderSelect.getCategory())
//                        .child(orderSelect.getIdAdvertisement());
//                orderRef1.child("status").setValue(1);
//                DatabaseReference orderRef2 = ConfigurationFirebase.getFirebase()
//                        .child("my_advertisement")
//                        .child(orderDeliver.getSellerId())
//                        .child(orderDeliver.getAdvertisementID());
//                        orderRef2.child("status").setValue(3);
//                        BookingOrder bookingOrder = new BookingOrder();
//                         String orderType = orderRef2.child("Category");
//
//
//                orderRef2.child("status").setValue(1);
            }
        }
    }
//    public void Decline(View view) {
//        //Toast.makeText(getApplicationContext(), "entered", Toast.LENGTH_SHORT).show();
//
//        orderDeliver =  (Advertisement) getIntent().
//                getSerializableExtra("advertisementSelected");
//
//        if(orderDeliver != null) {
//
//            DatabaseReference orderRef = ConfigurationFirebase.getFirebase()
//                    .child("orders")
//                    .child(orderDeliver.getIdAdvertisement());
//            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        BookingOrder order = snapshot.getValue(BookingOrder.class);
//
//                        if (order.getStatus() == 1) {
//
//                            String orderid = order.getidOrder();
//                            DatabaseReference orderRef = ConfigurationFirebase.getFirebase()
//                                    .child("orders")
//                                    .child(orderDeliver.getIdAdvertisement())
//                                    .child(orderid);
//                            orderRef.child("status").setValue(3);
//
//                            DatabaseReference orderRef1 = ConfigurationFirebase.getFirebase()
//                                    .child("advertisement")
//                                    .child(orderDeliver.getCategory())
//                                    .child(orderDeliver.getIdAdvertisement());
//                            orderRef1.child("status").setValue(1);
//                            DatabaseReference orderRef2 = ConfigurationFirebase.getFirebase()
//                                    .child("my_advertisement")
//                                    .child(orderDeliver.getSellerID())
//                                    .child(orderDeliver.getIdAdvertisement());
//                            orderRef2.child("status").setValue(1);
//                            Intent intent=new Intent(getApplicationContext(),MyAdvertisement.class);
//                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),"Order Request Declined", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            });
//        }else{
//            Toast.makeText(getApplicationContext(), "object not found", Toast.LENGTH_SHORT).show();
//        }
    }


