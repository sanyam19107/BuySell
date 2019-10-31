package com.iiitd.onCampusUdhaar.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.other.AdapterOrder;
import com.iiitd.onCampusUdhaar.other.Advertisement;
import com.iiitd.onCampusUdhaar.other.BookingOrder;
import com.iiitd.onCampusUdhaar.other.ConfigurationFirebase;
import com.iiitd.onCampusUdhaar.other.RecyclerItemClickListener;
import com.iiitd.onCampusUdhaar.other.UserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyOrders extends AppCompatActivity {

    private FirebaseAuth authentication;
    private RecyclerView recyclerAdvertisementPublics;
    private AdapterOrder AdapterOrder;
    private List<BookingOrder> listMyorder = new ArrayList<>();
    private DatabaseReference advertisementPublicsRef;
    private AlertDialog dialog;
    private String filterCategory = "",idUser;
    private View parentLayout;
    private Button search_button;
    private EditText search_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        authentication = ConfigurationFirebase.getFirebaseAuthentication();
        parentLayout = findViewById(android.R.id.content);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout layout = (LinearLayout) findViewById(R.id.content);
        layout.removeView(findViewById(R.id.search_layout));

         idUser = ConfigurationFirebase.getIdUser();

        getSupportActionBar().setTitle("My Orders");
        recyclerAdvertisementPublics = findViewById(R.id.recyclerAdvertisementPublics);
        recyclerAdvertisementPublics.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdvertisementPublics.setHasFixedSize(true);
        AdapterOrder = new AdapterOrder(listMyorder, this);
        recyclerAdvertisementPublics.setAdapter(AdapterOrder);

        getAllOrders();

        recyclerAdvertisementPublics.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerAdvertisementPublics,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                                    BookingOrder bookingOrder = listMyorder.get(position);
                                  //  Toast.makeText(getApplicationContext(), String.valueOf(bookingOrder.getStatus()), Toast.LENGTH_LONG).show();
                                    if (bookingOrder.getStatus() == 1) {
                                        Toast.makeText(getApplicationContext(), "Item is pending for seller approval", Toast.LENGTH_LONG).show();
                                    }
                                    else if (bookingOrder.getStatus() == 2) {
                                        Intent i = new Intent(MyOrders.this, OrderDelivered.class);
                                        i.putExtra("orderDeliver",bookingOrder);
                                        startActivity(i);
//                                    i.putExtra("advertisementSelected", listMyorder);
//                                    startActivity(i);
//                                    Intent i = new Intent(ListAdvertisement.this, UnderConstruction.class);
//                                    startActivity(i);
                                    }else if (bookingOrder.getStatus() == 3) {
                                        Toast.makeText(getApplicationContext(), "Seller has rejected your request", Toast.LENGTH_LONG).show();
                                    } else if (bookingOrder.getStatus()==4){
                                        Toast.makeText(getApplicationContext(), "You need to return this product", Toast.LENGTH_LONG).show();
                                    }else if (bookingOrder.getStatus() == 5) {
                                        Toast.makeText(getApplicationContext(), "Order is already completed", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
                                }
                                }
                            }
                            @Override
                            public void onLongItemClick(View view, int position) {
                                //Add to Wishlist
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                ));
    }

    private void getAllOrders() {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching orders..");
        dialog.setCancelable(false);
        dialog.show();

        DatabaseReference usernamesRef = FirebaseDatabase.getInstance().getReference().child("my_orders").child(idUser);
     //   Toast.makeText(getApplicationContext(),"LLL", Toast.LENGTH_LONG).show();

        usernamesRef.addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                   for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                       final BookingOrder order = childSnapshot.getValue(BookingOrder.class);
                                                       DatabaseReference usernamesRef2 = FirebaseDatabase.getInstance().getReference().child("orders").child(order.getAdvertisementID());
                                                       usernamesRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                           @Override
                                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                                               for (DataSnapshot childSnapshot2 : dataSnapshot.getChildren()) {
                                                                   BookingOrder order1 = childSnapshot2.getValue(BookingOrder.class);
                                                                   if(order1.getBuyerId().equalsIgnoreCase(idUser) && order.getidOrder().equalsIgnoreCase(order1.getidOrder())){
                                                                           listMyorder.add(order1);
                                                                   }
                                                                   Collections.reverse(listMyorder);
                                                                   AdapterOrder.notifyDataSetChanged();
                                                               }
                                                           }
                                                           @Override
                                                           public void onCancelled(DatabaseError databaseError) {
                                                           }
                                                       });
                                                       }
                                                   dismissDialog();
                                                   }
                                               @Override
                                               public void onCancelled(DatabaseError databaseError) {
                                               }
                                           });
    }

    private void dismissDialog()
    {
        if (dialog!=null)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
