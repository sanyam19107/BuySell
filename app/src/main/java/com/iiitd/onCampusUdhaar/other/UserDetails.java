package com.iiitd.onCampusUdhaar.other;

import com.google.firebase.database.DatabaseReference;
import com.iiitd.onCampusUdhaar.other.ConfigurationFirebase;

import java.io.Serializable;
import java.util.List;

public class UserDetails implements Serializable {
    private String idUser;
    private String name;
    private int rating;
    private int noOfRating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getNoOfRating() {
        return noOfRating;
    }

    public void setNoOfRating(int noOfRating) {
        this.noOfRating = noOfRating;
    }

    public UserDetails() {
        DatabaseReference userRef = ConfigurationFirebase.getFirebase()
                .child("users");
        setUserId(ConfigurationFirebase.getIdUser());
    }

    public void save(){

        String idUser = ConfigurationFirebase.getIdUser();

        // All advertisement
        DatabaseReference userRef = ConfigurationFirebase.getFirebase();

        userRef.child("users")
                .child(idUser)
                .setValue(this);

    }


    public String getUserId() {
        return idUser;
    }

    public void setUserId(String idUser) {
        this.idUser = idUser;
    }

}
