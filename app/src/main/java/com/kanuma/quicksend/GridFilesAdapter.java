package com.kanuma.quicksend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kanuma.quicksend.Models.FileDetails;
import com.kanuma.quicksend.Helpers.FileHelperMethods;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridFilesAdapter extends RecyclerView.Adapter<GridFilesViewHolder> {

    private List<File> fileDetailsList;
    private Context context;
    public QueueTrackerInterface qti;

    private static final String TAG = "GridFilesAdapter";


    public GridFilesAdapter(Context context, List<File> fileDetailsList,QueueTrackerInterface qti) {
        this.fileDetailsList = fileDetailsList;
        this.context =context;
        this.qti = qti;
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
    public void onBindViewHolder(@NonNull final GridFilesViewHolder holder, int position) {

        final File mFile=fileDetailsList.get(position);
        final FileDetails details = FileHelperMethods.getApkInfo(mFile.getAbsolutePath(),context);

        if(details.getName()!=null) {
            holder.fileTitle.setText(details.getName());
        }else {
            holder.fileTitle.setText(fileDetailsList.get(position).getName());
        }
        Drawable icon = details.getIcon();
        if(icon != null){
            holder.fileImage.setImageDrawable(icon);
        }else{
            holder.fileImage.setImageResource(R.drawable.ic_music_note_black_24dp);
        }

        if(details.getSize()==null) {
            holder.fileSize.setText("-");
        }else{
            holder.fileSize.setText(details.getSize());
        }

        //For single taps
        holder.fileImage.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qti.navigateAndPlay(mFile,fileDetailsList);

            }
        });




        //For Long Clicks
        holder.fileImage.getRootView().setOnLongClickListener((new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                details.setSelected(!details.isSelected());
                holder.fileSelected.setImageResource(details.isSelected() ?
                        R.drawable.ic_check_circle_black_24dp : Color.TRANSPARENT);


                if(details.isSelected()) {
                    Log.d(TAG, "onClick: Yes");
                    qti.insertInQueue(mFile);
                }else{
                    qti.deleteFromQueue(mFile);
                }

                return true;
            }
        }));

    }

    @Override
    public int getItemCount() {
        return fileDetailsList.size();
    }

    public interface QueueTrackerInterface{
        void insertInQueue(File f);
        void deleteFromQueue(File f);
        void navigateAndPlay(File f,List<File> fileDetailsList);
    }
}
