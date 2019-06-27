package com.kanuma.quicksend;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class FileDetails {

    private String name;
    private String version;
    private Drawable icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
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
            details.setVersion(packageInfo.versionName);
            details.setIcon(pm.getApplicationIcon(applicationInfo));
        }

        return details;





    }

}
