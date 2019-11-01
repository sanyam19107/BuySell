package com.iiitd.onCampusUdhaar.other;

import com.google.firebase.database.DatabaseReference;
import com.iiitd.onCampusUdhaar.other.ConfigurationFirebase;

import java.io.Serializable;
import java.util.List;

public class UserDetails implements Serializable {
    private String idUser;
    private String name;
    private String rating;
    private String noOfRating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getNoOfRating() {
        return noOfRating;
    }

    public void setNoOfRating(String noOfRating) {
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
