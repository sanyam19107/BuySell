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

public class OrderConfirmation extends AppCompatActivity {
    private Advertisement orderSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

    }
    public void Accept(View view) {
        orderSelect =  (Advertisement) getIntent().
                getSerializableExtra("advertisementSelected");
        if(orderSelect != null) {
            DatabaseReference orderRef = ConfigurationFirebase.getFirebase()
                    .child("orders")
                    .child(orderSelect.getIdAdvertisement());
            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BookingOrder order = snapshot.getValue(BookingOrder.class);
                        if (order.getStatus() == 1) {
                            String orderid = order.getidOrder();
                            DatabaseReference orderRef = ConfigurationFirebase.getFirebase()
                                    .child("orders")
                                    .child(orderSelect.getIdAdvertisement())
                                    .child(orderid);
                            orderRef.child("status").setValue(2);
                            Intent intent=new Intent(getApplicationContext(),MyAdvertisement.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),"Order Request Accepted", Toast.LENGTH_SHORT).show();

                        }

                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
    public void Decline(View view) {
        //Toast.makeText(getApplicationContext(), "entered", Toast.LENGTH_SHORT).show();

        orderSelect =  (Advertisement) getIntent().
                getSerializableExtra("advertisementSelected");

        if(orderSelect != null) {

            DatabaseReference orderRef = ConfigurationFirebase.getFirebase()
                    .child("orders")
                    .child(orderSelect.getIdAdvertisement());
            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BookingOrder order = snapshot.getValue(BookingOrder.class);

                        if (order.getStatus() == 1) {

                            String orderid = order.getidOrder();
                            DatabaseReference orderRef = ConfigurationFirebase.getFirebase()
                                    .child("orders")
                                    .child(orderSelect.getIdAdvertisement())
                                    .child(orderid);
                            orderRef.child("status").setValue(3);

                            DatabaseReference orderRef1 = ConfigurationFirebase.getFirebase()
                                    .child("advertisement")
                                    .child(orderSelect.getCategory())
                                    .child(orderSelect.getIdAdvertisement());
                            orderRef1.child("status").setValue(1);
                            DatabaseReference orderRef2 = ConfigurationFirebase.getFirebase()
                                    .child("my_advertisement")
                                    .child(orderSelect.getSellerID())
                                    .child(orderSelect.getIdAdvertisement());
                            orderRef2.child("status").setValue(1);
                            Intent intent=new Intent(getApplicationContext(),MyAdvertisement.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(),"Order Request Declined", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "object not found", Toast.LENGTH_SHORT).show();
        }
    }
}
