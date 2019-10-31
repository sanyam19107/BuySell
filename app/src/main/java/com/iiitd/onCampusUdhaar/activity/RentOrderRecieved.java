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
public class RentOrderRecieved extends AppCompatActivity {
    private BookingOrder orderDeliver;
    private Advertisement orderSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_order_recieved);

    }
    public void Accept3(View view) {
        orderSelect = (Advertisement) getIntent().
                getSerializableExtra("advertisementSelected");
        if (orderSelect != null) {
            DatabaseReference orderRef = ConfigurationFirebase.getFirebase()
                    .child("orders")
                    .child(orderSelect.getIdAdvertisement());
            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BookingOrder order = snapshot.getValue(BookingOrder.class);
                        if (order.getStatus() == 4) {
                            String orderid = order.getidOrder();
                            DatabaseReference orderRef = ConfigurationFirebase.getFirebase()
                                    .child("orders")
                                    .child(orderSelect.getIdAdvertisement())
                                    .child(orderid);
                            orderRef.child("status").setValue(5);
                            Intent intent = new Intent(getApplicationContext(), MyAdvertisement.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "Order Complete", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }}
