package com.iiitd.onCampusUdhaar.other;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;


public class BookingOrder implements Serializable {

    private String idOrder;
    private String buyerId;
    private String sellerId;
    private Integer status; //1-Requested 2 Seller Accepted 3 Seller Rejected 4 Delivered 5 Complete
    private String advertisementID;
    private String  advertisementtitle;
    private String  advertisementtype;

    public String getAdvertisementtype() {
        return advertisementtype;
    }

    public void setAdvertisementtype(String advertisementtype) {
        this.advertisementtype = advertisementtype;
    }

    public String getAdvertisementtitle() {
        return advertisementtitle;
    }

    public void setAdvertisementtitle(String advertisementtitle) {
        this.advertisementtitle = advertisementtitle;
    }
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAdvertisementID() {
        return advertisementID;
    }

    public void setAdvertisementID(String advertisementID) {
        this.advertisementID = advertisementID;
    }

    public BookingOrder() {
        DatabaseReference advertisementRef = ConfigurationFirebase.getFirebase()
                .child("orders");
        setidOrder(advertisementRef.push().getKey());
        setStatus(1);
    }

   
    public void order(){
        String idUser2 = ConfigurationFirebase.getIdUser();
        // User-specific advertisement Orders
        DatabaseReference advertisementRefOrder = ConfigurationFirebase.getFirebase()
                .child("orders");
        //advertisementRefOrder = ConfigurationFirebase.getFirebase()
        //       .child("my_orders");
        advertisementRefOrder
                .child(this.advertisementID)
                .child(getidOrder())
                .setValue(this);
        DatabaseReference advertisementMyOrder = ConfigurationFirebase.getFirebase()
                .child("my_orders");

        advertisementMyOrder
                .child(idUser2).push()
                .setValue(this);
    }


    public String getidOrder() {
        return idOrder;
    }

    public void setidOrder(String idOrder) {
        this.idOrder = idOrder;
    }

}
