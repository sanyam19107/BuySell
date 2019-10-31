package com.iiitd.onCampusUdhaar.other;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Advertisement implements Serializable {

    private String idAdvertisement;
    private String category;
    private String rentSell;
    private String rentTime;
    private String securityAmount;
    private String title;
    private String value;
    private String email;
    private String description;
    private String photo;
    private String sellerID;
    private int status;  //1-Available ,2-Booked,3-Completed
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public Advertisement() {
        DatabaseReference advertisementRef = ConfigurationFirebase.getFirebase()
                .child("my_advertisement");
        setIdAdvertisement(advertisementRef.push().getKey());
        setStatus(1);
    }


    public void save(){
        String idUser = ConfigurationFirebase.getIdUser();
        // All advertisement
        DatabaseReference advertisementRef = ConfigurationFirebase.getFirebase()
                .child("advertisement");
        advertisementRef
                .child(getCategory())
                .child(getIdAdvertisement())
                .setValue(this);

        // User-specific advertisement
        advertisementRef = ConfigurationFirebase.getFirebase()
                .child("my_advertisement");
        advertisementRef
                .child(idUser)
                .child(getIdAdvertisement())
                .setValue(this);
    }

    public void remove(){
        String idUser = ConfigurationFirebase.getIdUser();

        // All advertisement
        DatabaseReference advertisementRef = ConfigurationFirebase.getFirebase()
                .child("advertisement")
                .child(getCategory())
                .child(getIdAdvertisement());
        advertisementRef.removeValue();

        // User-specific advertisement
        advertisementRef = ConfigurationFirebase.getFirebase()
                .child("my_advertisement")
                .child(idUser)
                .child(getIdAdvertisement());
        advertisementRef.removeValue();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdAdvertisement() {
        return idAdvertisement;
    }

    public void setIdAdvertisement(String idAdvertisement) {
        this.idAdvertisement = idAdvertisement;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRentSell() {
        return rentSell;
    }

    public void setRentSell(String rentSell) {
        this.rentSell = rentSell;
    }

    public String getRentTime() {
        return rentTime;
    }

    public void setRentTime(String rentTime) {
        this.rentTime = rentTime;
    }

    public String getSecurityAmount() {
        return securityAmount;
    }

    public void setSecurityAmount(String securityAmount) {
        this.securityAmount = securityAmount;
    }
    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }
}
