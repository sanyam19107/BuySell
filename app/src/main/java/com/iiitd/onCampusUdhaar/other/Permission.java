package com.iiitd.onCampusUdhaar.other;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permission {

    public static boolean validatePermission(String[] Permission, Activity activity, int requestCode){

        if (Build.VERSION.SDK_INT >= 23 ){

            List<String> listPermission = new ArrayList<>();
            for ( String permission : Permission ){
                Boolean temppermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if ( !temppermission ) listPermission.add(permission);
            }
            if ( listPermission.isEmpty() ) return true;
            String[] navePermission = new String[ listPermission.size() ];
            listPermission.toArray( navePermission );
            ActivityCompat.requestPermissions(activity, navePermission, requestCode );


        }

        return true;

    }

}
