package com.iiitd.onCampusUdhaar.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.other.Advertisement;
import com.iiitd.onCampusUdhaar.other.BookingOrder;
import com.iiitd.onCampusUdhaar.other.ConfigurationFirebase;
import com.iiitd.onCampusUdhaar.other.UserDetails;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class ProductDetailActivity extends AppCompatActivity {

    private CarouselView carouselView;
    private TextView title;
    private TextView description;
    private TextView value;
    private Button bookButton;
    private String advertisementID;
    private String sellerID;
    private FirebaseAuth firebaseAuth;
    private Advertisement advertisementSelect;
    private String advertisementtitle;
    private String advertisementtype;
    private Button chatButton;
    private String sellerEmailID;
    private Context context;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        initializeComponents();
        firebaseAuth= FirebaseAuth.getInstance();

        advertisementSelect =  (Advertisement) getIntent().
                getSerializableExtra("advertisementSelected");
        if(advertisementSelect != null){
            title.setText(advertisementSelect.getTitle());
            description.setText(advertisementSelect.getDescription());

            value.setText(advertisementSelect.getValue());

            advertisementID=advertisementSelect.getIdAdvertisement();
            sellerID=advertisementSelect.getSellerID();
            sellerEmailID = advertisementSelect.getEmail(); 
            advertisementtitle =advertisementSelect.getTitle();
            advertisementtype=advertisementSelect.getRentSell();
            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlString = advertisementSelect.getPhoto();
                    Picasso.get().load(urlString).into(imageView); //load image
                }
            };

            carouselView.setPageCount(1);
            carouselView.setImageListener(imageListener);
        }
        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified()) {


            String idUser = ConfigurationFirebase.getIdUser();
            if (sellerID.equalsIgnoreCase(idUser) || advertisementSelect.getStatus() != 1) {
                bookButton.setVisibility(View.INVISIBLE);
                //Toast.makeText(getApplicationContext(),"Hidden",Toast.LENGTH_LONG ).show();
            } else {
                //  Toast.makeText(getApplicationContext(),"hi5654645646",Toast.LENGTH_LONG ).show();
                bookButton.setVisibility(View.VISIBLE);
            }
            bookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String buyerID = ConfigurationFirebase.getIdUser();

                    BookingOrder bookingOrder = new BookingOrder();
                    bookingOrder.setBuyerId(buyerID);
                    bookingOrder.setSellerId(sellerID);
                    bookingOrder.setAdvertisementID(advertisementID);
                    bookingOrder.setAdvertisementtitle(advertisementtitle);
                    bookingOrder.setAdvertisementtype(advertisementtype);
                    bookingOrder.order();
                    DatabaseReference advertisementRef = ConfigurationFirebase.getFirebase()
                            .child("advertisement")
                            .child(advertisementSelect.getCategory())
                            .child(advertisementSelect.getIdAdvertisement());
                    advertisementRef.child("status").setValue(2);

                    DatabaseReference advertisementRef1 = ConfigurationFirebase.getFirebase()
                            .child("my_advertisement")
                            .child(advertisementSelect.getSellerID())
                            .child(advertisementSelect.getIdAdvertisement());
                    advertisementRef1.child("status").setValue(2);

//                advertisementSelect.setStatus(2);
//                advertisementSelect.save();
                    Intent i = new Intent(ProductDetailActivity.this, Category.class);
                    //i.putExtra("advertisementSelected", advertisementSelect);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "You have sucessfully booked your item and pending for seller approval", Toast.LENGTH_LONG).show();
                }
            });
        }



    }

    private void initializeComponents(){
        carouselView = findViewById(R.id.carouselView);
        title = findViewById(R.id.textTitleDetail);
        description = findViewById(R.id.textDescriptionDetail);
        value = findViewById(R.id.textValueDetail);
        bookButton=findViewById(R.id.buttonAccess2);
        chatButton = findViewById(R.id.chat);


//        salerent = findViewById(R.id.rentsellCategory);
//        rentTime = findViewById(R.id.rentTime);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



    public void openHangouts(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND,
                Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.talk"));
        intent.setType("text/plain");
        intent.setPackage("com.google.android.talk");
        startActivity(Intent.createChooser(intent, "Hangouts is not installed."));
    }
}
