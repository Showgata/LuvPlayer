package com.kanuma.quicksend;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsHandling {


    private static int PERMISSION_REQUEST_CODE =1;

    public static void getPermission(Context context) {
        //Check whether the app is installed on Android 6.0 or higher//
        if (Build.VERSION.SDK_INT >= 23) {
            //Check whether your app has access to the READ permission//
            if (checkPermission(context)) {
                //If your app has access to the device’s storage, then print the following message to Android Studio’s Logcat//
                Log.e("permission", "Permission already granted.");
            } else {

                //If your app doesn’t have permission to access external storage, then call requestPermission//
                requestPermission(context);
            }
        }
    }

    private static boolean checkPermission(Context context) {
        //Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If the app does have this permission, then return true//
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {//If the app doesn’t have this permission, then return false//
            return false;
        }
    }


    private static void requestPermission(Context context) {

        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST_CODE);

    }









}
