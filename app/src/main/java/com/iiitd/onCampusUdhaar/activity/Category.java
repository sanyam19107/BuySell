package com.iiitd.onCampusUdhaar.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.other.ConfigurationFirebase;

public class Category extends AppCompatActivity {

    private FirebaseAuth authentication;
    private GridLayout gridLayoutManager;
    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        authentication = ConfigurationFirebase.getFirebaseAuthentication();
        parentLayout = findViewById(android.R.id.content);

        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (!(conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED))
            Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
//        bottomNavigationSwitch();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.search_float);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                else

                    Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
//
//    private void bottomNavigationSwitch() {
//        BottomNavigationView bottom = (BottomNavigationView) findViewById(R.id.bottom_nav);
//        if (authentication.getCurrentUser() == null) {
//            bottom.getMenu().removeGroup(R.id.group_signin);
//        } else {
//            if (!authentication.getCurrentUser().isEmailVerified()) {
//                bottom.getMenu().removeGroup(R.id.group_signin);
//            } else {
//                String idUser = ConfigurationFirebase.getIdUser();
//                bottom.getMenu().removeGroup(R.id.group_signout);
//            }
//        }
//
//        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////                Toast.makeText(getApplicationContext(),this.getClass().getSimpleName(),Toast.LENGTH_LONG ).show();
////                Toast.makeText(getApplicationContext(),Category.class.getName(),Toast.LENGTH_LONG ).show();
//                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
//                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//                    switch (item.getItemId()) {
//                        case R.id.nav_home_logged_in:
//                        case R.id.nav_home_logged_out: {
//                            if (!getClass().getName().equals(Category.class.getName() + "$1")) {
//                                finish();
//                                startActivity(new Intent(getApplicationContext(), Category.class));
//                            }
//                        }
//                        break;
//                        case R.id.menu_advertisement: {
//                            if (!getClass().getName().equals(MyAdvertisement.class.getName() + "$1")) {
//                                startActivity(new Intent(getApplicationContext(), MyAdvertisement.class));
//                            }
//                        }
//                        break;
//                        case R.id.menu_register: {
//                            if (!getClass().getName().equals(LoginUser.class.getName() + "$1")) {
//                                startActivity(new Intent(getApplicationContext(), LoginUser.class));
//                            }
//                        }
//                        break;
//                        default:
//                    }
//                }
//                else
//                    Snackbar.make(parentLayout,R.string.switch_on,Snackbar.LENGTH_SHORT).show();
//                return false;
//            }});
//    }

    public void select(View view) {
        Intent i = new Intent(getApplicationContext(), ListAdvertisement.class);
        switch (view.getId()) {
            case R.id.c1:
                i.putExtra("filterCategory", "Electronics");
                break;
            case R.id.c2:
                i.putExtra("filterCategory", "Clothing");
                break;
            case R.id.c3:
                i.putExtra("filterCategory", "Sports Goods");
                break;
            case R.id.c4:
                i.putExtra("filterCategory", "Office Supplies");
                break;
            case R.id.c5:
                i.putExtra("filterCategory", "Miscellaneous");
                break;
        }
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            startActivity(i);
        else
            Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            switch (item.getItemId()) {
                case R.id.nav_home_logged_in: {
//                    authentication.signOut();
//                    invalidateOptionsMenu();
//                    finish();
//                    startActivity(new Intent(getApplicationContext(), Category.class));
//                    Toast.makeText(getApplicationContext(), R.string.logout_text, Toast.LENGTH_LONG).show();
                    if (authentication.getCurrentUser() == null) {
                        startActivity(new Intent(getApplicationContext(), LoginUser.class));
                    } else {
                        if (!authentication.getCurrentUser().isEmailVerified()) {
                            startActivity(new Intent(getApplicationContext(), LoginUser.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), MyAdvertisement.class));
                        }
                    }
                }
            }
        } else
            Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}