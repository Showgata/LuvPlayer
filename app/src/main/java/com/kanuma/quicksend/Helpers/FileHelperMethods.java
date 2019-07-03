package com.kanuma.quicksend.Helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.kanuma.quicksend.Models.FileDetails;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileHelperMethods {

    private static final String EXTERNAL_URI ="external";
    private static final String TAG = "FileHelperMethods" ;

    public static List<File> getFilesOfSpecificExtension(Context context,String extension){

        List<String> uriFiles = new ArrayList<>();

        //Instantiate the content resolver
        ContentResolver cr = context.getContentResolver();

        // Get from the memory
        Uri fileUri = MediaStore.Files.getContentUri(EXTERNAL_URI);

        //Same as select of sql / filter
        String[] projection =new String[]{MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.SIZE
        };


        String selectionMimeType=null;
        String mimeType=null;
        String[] selectionArgs=null;
        String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED;;


        if(extension != null) {
            selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            selectionArgs = new String[]{mimeType};
        }

        //Get the cursor
        Cursor cursor = cr.query(fileUri,projection,selectionMimeType,selectionArgs,sortOrder);
        if (cursor == null)
            return null;

        //Add the uris in the array
        if (cursor.moveToLast()) {
            do {

                String data = cursor.getString(0);
                Log.d(TAG, "getFilesOfSpecificExtension: "+data);
                uriFiles.add(data);

            } while (cursor.moveToPrevious());
        }
        cursor.close();

        //from the uris, the file object and add them to the list
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < uriFiles.size(); i++) {
            File file = new File(uriFiles.get(i));
            fileList.add(file);
        }
        Log.d(TAG, "getSpecificTypeOfFile: " + fileList.size());
        return fileList;
    }




    public static FileDetails getApkInfo(String path, Context context){
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES);
        FileDetails details = new FileDetails();

        if(packageInfo !=null){
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            applicationInfo.sourceDir = path;
            applicationInfo.publicSourceDir=path;

            details.setName(pm.getApplicationLabel(applicationInfo).toString());
            details.setSize(getSizeOfTheFile(applicationInfo));
            details.setIcon(pm.getApplicationIcon(applicationInfo));
        }

        return details;

    }

    private static String getSizeOfTheFile(ApplicationInfo applicationInfo){
        File file = new File(applicationInfo.publicSourceDir);
        long size = file.length();
        Log.d(TAG, "getSizeOfTheFile: "+size);
        return size+"";
    }

}
