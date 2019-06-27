package com.kanuma.quicksend.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kanuma.quicksend.GridFilesAdapter;
import com.kanuma.quicksend.Models.FileHelperMethods;
import com.kanuma.quicksend.R;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllFilesFragment extends Fragment {

    private  List<File> fileList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileList=FileHelperMethods.getFilesOfSpecificExtension(this.getActivity(),"pdf");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_files, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        GridFilesAdapter adapter = new GridFilesAdapter(this.getActivity(),fileList);
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.file_grid_spacing_large);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.file_grid_spacing_small);
        //recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
        return view;
    }


    /*
    public class GetFileAsync extends AsyncTask<Context,Void,List<File>>{

        @Override
        protected List<File> doInBackground(Context... contexts) {

            return FileHelperMethods.getFilesOfSpecificExtension(contexts[0],"apk");

        }
    }*/
}
