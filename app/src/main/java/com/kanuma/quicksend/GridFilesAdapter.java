package com.kanuma.quicksend;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridFilesAdapter extends RecyclerView.Adapter<GridFilesViewHolder> {

    private List<File> fileDetailsList;
    private Context context;

    public GridFilesAdapter(Context context, List<File> fileDetailsList) {
        this.fileDetailsList = fileDetailsList;
        this.context =context;
    }

    @NonNull
    @Override
    public GridFilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(fileDetailsList.size() == 0){
            Toast.makeText(parent.getContext(),"List is empty",Toast.LENGTH_SHORT).show();
        }

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_individual_files, parent, false);
        return new GridFilesViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull GridFilesViewHolder holder, int position) {

        FileDetails details = FileDetails.getApkInfo(fileDetailsList.get(position).getAbsolutePath(),context);

        if(details.getName()!=null) {
            holder.fileTitle.setText(details.getName());
        }else {
            holder.fileTitle.setText(fileDetailsList.get(position).getName());
        }
        Drawable icon = details.getIcon();
        if(icon != null){
            holder.fileImage.setImageDrawable(icon);
        }else{
            holder.fileImage.setImageResource(R.drawable.ic_insert_drive_file_black_24dp);
        }

        holder.fileSize.setText("12mb");

    }

    @Override
    public int getItemCount() {
        return fileDetailsList.size();
    }
}
