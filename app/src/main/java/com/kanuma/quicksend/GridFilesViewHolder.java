package com.kanuma.quicksend;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridFilesViewHolder extends RecyclerView.ViewHolder {

        public ImageView fileImage;
        public TextView fileTitle;
        public TextView fileSize;

public GridFilesViewHolder(@NonNull View itemView) {
        super(itemView);

        fileImage = itemView.findViewById(R.id.file_image);
        fileTitle = itemView.findViewById(R.id.file_title);
        fileSize = itemView.findViewById(R.id.file_size);
        }
}