package com.iiitd.onCampusUdhaar.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iiitd.onCampusUdhaar.R;
import com.iiitd.onCampusUdhaar.other.ConfigurationFirebase;
import com.iiitd.onCampusUdhaar.other.Permission;
import com.iiitd.onCampusUdhaar.other.Advertisement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class PostAdvertisement extends AppCompatActivity
{
    private EditText fieldTitle, fieldDescription,fieldSecurityAmount;
    private ImageView image1;
    private Spinner fieldState, fieldCategory,rentSellCategory,rentTimeCategory;
    private EditText fieldValue;
    private EditText fieldEmail;
    private Advertisement advertisement;
    private StorageReference storage;
    private AlertDialog dialog;
    private View parentLayout;
    public final int CAMERA_PIC_REQUEST=1;
    public final int GALLERY_PIC_REQUEST = 2;


    private String[] permission = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private LinearLayout rentTimelayout;
    private String listPhotoRecovery;
    private String listUrlPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_advertisement);
        rentTimelayout = (LinearLayout) this.findViewById(R.id.RentLayout);
        rentTimelayout.setVisibility(LinearLayout.GONE);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        parentLayout = findViewById(android.R.id.content);

        storage = ConfigurationFirebase.getFirebaseStorage();

        Permission.validatePermission(permission, this, 1);

        initializeComponents();
        loadingDataSpinner();
    }

    public void saveAdvertisement() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Posting ad..");
        dialog.setCancelable(false);
        dialog.show();

        savePhotoStorage(listPhotoRecovery);
    }

    private void savePhotoStorage(String urlString){

        StorageReference imageAdvertisement = storage.child("images")
                .child("advertisement")
                .child(advertisement.getIdAdvertisement())
                .child("image");

        // make upload of file
        UploadTask uploadTask = imageAdvertisement.putFile( Uri.parse(urlString) );

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri firebaseUrl = taskSnapshot.getDownloadUrl();
                listUrlPhotos = firebaseUrl.toString();

                advertisement.setPhoto(listUrlPhotos);
                advertisement.save();

                dismissDialog();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessageError("Failed to upload");
                Log.i("INFO", "Failed to upload: " + e.getMessage());
                dialog.dismiss();
            }
        });
    }

    private Advertisement configurateAdvertisement(){
        String category = fieldCategory.getSelectedItem().toString();
        String rentSell=rentSellCategory.getSelectedItem().toString();
        String rentTime=rentTimeCategory.getSelectedItem().toString();
        String securityAmount=fieldSecurityAmount.getText().toString();
        String title = fieldTitle.getText().toString();
        String value = fieldValue.getText().toString();
        String email = fieldEmail.getText().toString();
        String description = fieldDescription.getText().toString();

        Advertisement advertisement = new Advertisement();
        advertisement.setSellerID(ConfigurationFirebase.getIdUser());
        advertisement.setCategory(category);
        advertisement.setRentSell(rentSell);

        if(rentSell.equals("Rent"))
        {
            advertisement.setRentTime(rentTime);
            advertisement.setSecurityAmount(securityAmount);
        }

        advertisement.setTitle(title);
        advertisement.setValue(value);
        advertisement.setEmail(email);
        advertisement.setDescription(description);

        return advertisement;
    }

    public void validate(View view){
        String email = "";
        advertisement = configurateAdvertisement();
        String value = String.valueOf(fieldValue.getText());
        if(fieldEmail.getText() != null){
            email = fieldEmail.getText().toString();
        }

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            if(listPhotoRecovery != NULL){
                if(true){
                    if(!advertisement.getCategory().isEmpty()){
                        if(!advertisement.getTitle().isEmpty()){
                            if(!value.isEmpty() && !value.equals("0")){
                                if(!advertisement.getEmail().isEmpty() && email.endsWith("@iiitd.ac.in")){
                                    if(!advertisement.getDescription().isEmpty()){

                                        saveAdvertisement();

                                    }else{
                                        showMessageError("Fill in the description field!");
                                    }
                                }else{
                                    showMessageError("Fill in the email field with valid IIITD Email ID");
                                }
                            }else{
                                showMessageError("Fill in the value field\n!");
                            }
                        }else{
                            showMessageError("Fill in the title field!");
                        }
                    }else{
                        showMessageError("Fill in the category field!");
                    }
                }else{
                    showMessageError("Fill in the state field!");
                }
            }else {
                showMessageError("Select at least one photo");
            }
        }else
            Snackbar.make(parentLayout, R.string.switch_on, Snackbar.LENGTH_SHORT).show();
    }

    private void showMessageError(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode == Activity.RESULT_OK){
//
//            listPhotoRecovery = data.getData().toString();
//
//            if(requestCode == 1){
//                image1.setImageURI(data.getData());
//            }
//        }
//
//    }

    private void showPictureDialog()
    {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();


    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY_PIC_REQUEST);
    }
    private void takePhotoFromCamera() {
        Log.i("TAG1","cAMERA IS STARTED");
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name = "camera";
        String ImagePath;
        Uri URI1, URI2;
        if (resultCode == RESULT_OK)
        {
            System.out.println("RQ = " + requestCode);

            if (requestCode == CAMERA_PIC_REQUEST) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                File filesDir = getApplicationContext().getFilesDir();
                System.out.println("in cameraaaa");
                Log.i("TAG1", "In camerrraa");
                OutputStream os;
                try
                {
                    File imageFile = new File(filesDir, name + ".jpg");
                    os = new FileOutputStream(imageFile);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);

                    System.out.println("hello camera");
                    //check for permissions
                    if (ContextCompat.checkSelfPermission(PostAdvertisement.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        // No explanation needed; request the permission
                        System.out.println("permission NOT granted");
                        ActivityCompat.requestPermissions(PostAdvertisement.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                    else
                    {
                        // Permission has already been granted
                        System.out.println("permission granted");

                        ImagePath = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "demo_image", "demo_image");

                        URI1 = Uri.parse(ImagePath);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), URI1);
                        listPhotoRecovery = URI1.toString();
                        image1.setImageBitmap(bitmap);
                        Toast.makeText(PostAdvertisement.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();
                        os.flush();
                        os.close();
                    }
                }
                catch (Exception e)
                {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }
            }
        }
        if (requestCode == GALLERY_PIC_REQUEST && null != data) {
            URI2 = data.getData();
            listPhotoRecovery = data.getData().toString();
            try {
                Log.i("TAG2", "In galleryy");

                System.out.println("in galleryyyyy");
                Bitmap imageBitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), URI2);
                image1.setImageBitmap(imageBitmap2);
            }
            catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }
        }
    }

    private void initializeComponents(){
        fieldTitle = findViewById(R.id.editTitle);
        fieldDescription = findViewById(R.id.editDescription);
        fieldValue = findViewById(R.id.editValue);
        fieldEmail = findViewById(R.id.editEmail);
        fieldCategory = findViewById(R.id.spinnerCategory);
        rentSellCategory=findViewById(R.id.rentsellCategory);
        rentTimeCategory=findViewById(R.id.rentTime);
        fieldSecurityAmount=findViewById(R.id.securityPrice);
        image1 = findViewById(R.id.imageRegister1);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, 1);
            }
        });

        rentSellCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
            {
                if(rentSellCategory.getSelectedItem().toString().equals("Rent")){
                    rentTimelayout.setVisibility(LinearLayout.VISIBLE);
                }
                else {
                    rentTimelayout.setVisibility(LinearLayout.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void loadingDataSpinner(){
        String[] category = getResources().getStringArray(R.array.category);
        String[] rentsellCategory = getResources().getStringArray(R.array.RentSell);
        String[] rentTime=getResources().getStringArray(R.array.RentTime);

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(
                this,R.layout.spinner_format,
                category
        );
        ArrayAdapter<String> rentsellAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_format,
                rentsellCategory
        );
        ArrayAdapter<String> renttimeAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_format,
                rentTime
        );
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldCategory.setAdapter(adapterCategory);

        renttimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rentTimeCategory.setAdapter(renttimeAdapter);

        rentsellAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rentSellCategory.setAdapter(rentsellAdapter);

        if(rentSellCategory.getSelectedItem().toString().equals("Rent")){
            rentTimelayout.setVisibility(LinearLayout.VISIBLE);
        }
        else {
            rentTimelayout.setVisibility(LinearLayout.GONE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissionResult : grantResults){

            if (permissionResult == PackageManager.PERMISSION_DENIED){
                AlertValidationPermission();
            }
        }
    }

    private void AlertValidationPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions Denied");
        builder.setMessage("To use the app you must accept the permissions");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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

}
