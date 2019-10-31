package com.iiitd.onCampusUdhaar.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.other.AdapterAdvertisement;
import com.iiitd.onCampusUdhaar.other.Advertisement;
import com.iiitd.onCampusUdhaar.other.ConfigurationFirebase;
import com.iiitd.onCampusUdhaar.other.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListAdvertisement extends AppCompatActivity {

    private FirebaseAuth authentication;
    private RecyclerView recyclerAdvertisementPublics;
    private AdapterAdvertisement adapterAdvertisement;
    private List<Advertisement> listAdvertisements = new ArrayList<>();
    private DatabaseReference advertisementPublicsRef;
    private AlertDialog dialog;
    private String filterCategory = "";
    private View parentLayout;
    private Button search_button;
    private EditText search_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_advertisement);

        authentication = ConfigurationFirebase.getFirebaseAuthentication();
        parentLayout = findViewById(android.R.id.content);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout layout = (LinearLayout) findViewById(R.id.content);
        layout.removeView(findViewById(R.id.search_layout));

        Bundle extras = getIntent().getExtras();
        filterCategory = extras.getString("filterCategory");
        getSupportActionBar().setTitle(filterCategory);
        recyclerAdvertisementPublics = findViewById(R.id.recyclerAdvertisementPublics);
        recyclerAdvertisementPublics.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdvertisementPublics.setHasFixedSize(true);
        adapterAdvertisement = new AdapterAdvertisement(listAdvertisements, this);
        recyclerAdvertisementPublics.setAdapter(adapterAdvertisement);

        getAllAdvertisements();

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
                                        Advertisement advertisementSelect = listAdvertisements.get(position);
                                    Intent i = new Intent(ListAdvertisement.this, ProductDetailActivity.class);
                                    i.putExtra("advertisementSelected", advertisementSelect);
                                    startActivity(i);
//                                    Intent i = new Intent(ListAdvertisement.this, UnderConstruction.class);
//                                    startActivity(i);
                                } else
                                    Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
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

    private void getAllAdvertisements() {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching ads..");
        dialog.setCancelable(false);
        dialog.show();



        advertisementPublicsRef = ConfigurationFirebase.getFirebase()
                .child("advertisement")
                .child(filterCategory);

        //listAdvertisements.clear();
        advertisementPublicsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAdvertisements.clear();
                for (DataSnapshot advertisements : dataSnapshot.getChildren()) {
                    Advertisement advertisement =
                            advertisements.getValue(Advertisement.class);
                    if (advertisements!=null && advertisements.child("status").getValue((Integer.class)) == 1)
                        listAdvertisements.add(advertisement);
                    //listAdvertisements.add(advertisement);

                }

                Collections.reverse(listAdvertisements);
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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
