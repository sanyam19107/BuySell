package com.iiitd.onCampusUdhaar.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.other.AdapterAdvertisement;
import com.iiitd.onCampusUdhaar.other.BookingOrder;
import com.iiitd.onCampusUdhaar.other.ConfigurationFirebase;
import com.iiitd.onCampusUdhaar.other.RecyclerItemClickListener;
import com.iiitd.onCampusUdhaar.other.Advertisement;


public class MyAdvertisement extends AppCompatActivity {

    //configurate recyclerView
    private RecyclerView recyclerAdvertisement;
    private List<Advertisement> advertisements = new ArrayList<>();
    private AdapterAdvertisement adapterAdvertisement;
    private DatabaseReference advertisementUserRef;
    private AlertDialog dialog;
    private int pos,flag=0,declined=0,length=0;
    private View parentLayout;
    private FirebaseAuth authentication;
    private Advertisement advertisementSelect;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_advertisement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        parentLayout = findViewById(android.R.id.content);
        authentication = ConfigurationFirebase.getFirebaseAuthentication();

        //Configurations initial
        advertisementUserRef = ConfigurationFirebase.getFirebase()
                .child("my_advertisement")
                .child(ConfigurationFirebase.getIdUser());


        recyclerAdvertisement = findViewById(R.id.recyclerAdvertisement);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PostAdvertisement.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Configurate ReyclerView
        recyclerAdvertisement.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdvertisement.setHasFixedSize(true);
        //Configure Adapter
        adapterAdvertisement = new AdapterAdvertisement(advertisements, this);
        recyclerAdvertisement.setAdapter(adapterAdvertisement);

        recoveryAdvertisement();

        //add event of click in recyclerview
        recyclerAdvertisement.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this, recyclerAdvertisement,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                                    final Advertisement advertisementSelect = advertisements.get(position);
//                                    advertisementUserRef = ConfigurationFirebase.getFirebase()
//                                            .child("my_advertisement")
//                                            .child(advertisementSelect.getIdAdvertisement());
                                    int status = advertisementSelect.getStatus();
                                   // Toast.makeText(getApplicationContext(), String.valueOf(status), Toast.LENGTH_SHORT).show();


                                    if(advertisementSelect != null) {
                                            declined=0;
                                        DatabaseReference orderRef = ConfigurationFirebase.getFirebase()
                                                .child("orders")
                                                .child(advertisementSelect.getIdAdvertisement());
                                       // flag=0;
                                        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                length=0;
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    length++;
                                                    BookingOrder order = snapshot.getValue(BookingOrder.class);
                                                    if (order.getStatus() == 1) {
                                                        if (advertisementSelect.getStatus() == 2 ) {
                                                            Intent i = new Intent(MyAdvertisement.this, OrderConfirmation.class);
                                                            i.putExtra("advertisementSelected", advertisementSelect);
                                                            startActivity(i);
                                                            break;
                                                        }
                                                    }else if (order.getStatus() == 3) {
                                                        length--;
                                                    }
                                                    else if (order.getStatus() == 2) {
                                                        Snackbar.make(parentLayout, R.string.orderstatus2, Snackbar.LENGTH_SHORT).show();
                                                    }
                                                    else if (order.getStatus() == 4) {
                                                        Intent i = new Intent(getApplicationContext(), RentOrderRecieved.class);
                                                        i.putExtra("advertisementSelected",advertisementSelect);
                                                        startActivity(i);
                                                        break;
                                                       // Snackbar.make(parentLayout, "Product Delivered but pending for return", Snackbar.LENGTH_SHORT).show();
                                                    }
                                                    else if (order.getStatus() == 5) {
                                                        Snackbar.make(parentLayout,"Order  Complete", Snackbar.LENGTH_SHORT).show();
                                                    }
                                                   // break;
                                                }
                                                if(length==0) {
                                                    Snackbar.make(parentLayout, R.string.orderstatus1, Snackbar.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });

                                    }
                                //   Toast.makeText(getApplicationContext(), "ishu"+String.valueOf(flag)+"sanyam"+String.valueOf(advertisementSelect.getStatus()), Toast.LENGTH_SHORT).show();



                                } else
                                    Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
                            }


                            @Override
                            public void onLongItemClick(View view, int position) {
                                pos = position;
                                registerForContextMenu(view);
                            }
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_ads, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        switch(item.getItemId())
        {
            case R.id.edit: // For Edit option
            {
                if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                {
                    advertisementSelect = advertisements.get(pos);
                    Intent i = new Intent(MyAdvertisement.this, EditAdvertisement.class);
                    i.putExtra("advertisementSelected", advertisementSelect);
                    startActivity(i);
                }
                else
                    Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();

            }
            return true;

            case R.id.delete: // For Delete option
            {
                if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                {
                    advertisementSelect = advertisements.get(pos);
                    deleteAdvertisement();
                }

                else
                    Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
            }
            return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void deleteAdvertisement(){

        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(advertisementSelect.getPhoto());
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.i("INFO", "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.i("INFO", "onFailure: did not delete file");
            }
        });

        advertisementSelect.remove();
    }

    private void recoveryAdvertisement() {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching ads..");
        dialog.setCancelable(false);
        dialog.show();


        advertisementUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                advertisements.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    advertisements.add(ds.getValue(Advertisement.class));
                }

                Collections.reverse(advertisements);
                adapterAdvertisement.notifyDataSetChanged();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            switch (item.getItemId()) {
                case R.id.menu_signout: {
                    authentication.signOut();
                    invalidateOptionsMenu();
                    finish();
                    signOut();
                    startActivity(new Intent(getApplicationContext(), Category.class));
                    Toast.makeText(getApplicationContext(), R.string.logout_text, Toast.LENGTH_LONG).show();
                }
                break;
                case R.id.my_orders:{
                    invalidateOptionsMenu();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MyOrders.class));
                   // Intent i = new Intent(MyAdvertisement.this, EditAdvertisement.class);
                    //i.putExtra("advertisementSelected", advertisementSelect);
                    //startActivity(i);
                    //advertisementUserRef
                }
                 break;
            }
        } else
            Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
}