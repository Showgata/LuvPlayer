package com.kanuma.quicksend.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kanuma.quicksend.GridFilesAdapter;
import com.kanuma.quicksend.Helpers.FileHelperMethods;
import com.kanuma.quicksend.MainActivity;
import com.kanuma.quicksend.R;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllFilesFragment extends Fragment implements GridFilesAdapter.QueueTrackerInterface {

    private  List<File> fileList;
    private Queue<File> selectedItems;
    //private BottomSheetDialog bottomSheetView;
    private Button shareButton;

    //In BottomSheetDialog
    /* private Button bsClearButton;
    private Button bsSendButton;
    private TextView bsText; */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileList=FileHelperMethods.getFilesOfSpecificExtension(this.getActivity(),"mp3");
        selectedItems = new LinkedList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_files, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        GridFilesAdapter adapter = new GridFilesAdapter(this.getActivity(),fileList,this);
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.file_grid_spacing_large);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.file_grid_spacing_small);
        //recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

        shareButton=view.findViewById(R.id.share_files);
        shareButton.setOnClickListener(shareButtonFun);
        /* bottomSheetView = new BottomSheetDialog(getActivity());
        initBottomSheetView(bottomSheetView,inflater,container);*/

        return view;
    }

    private View.OnClickListener shareButtonFun = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),"Ready to share",Toast.LENGTH_SHORT).show();


            Fragment nextFragment = MusicFragment.getMusicQueue(selectedItems);
            ((MainActivity)getActivity()).navigateTo(nextFragment,true);


        }
    };


/*

    private void initBottomSheetView(BottomSheetDialog bottomSheetView, LayoutInflater inflater, ViewGroup container){

        View v = inflater.inflate(R.layout.snippet_selected_and_send_view,container, false);
        bsClearButton =v.findViewById(R.id.bsClear);
        bsSendButton=v.findViewById(R.id.bsSend);
        bsText=v.findViewById(R.id.bsText);

        bsSendButton.setOnClickListener(bsSendFun);
        bsClearButton.setOnClickListener(bsClearFun);
        bottomSheetView.setContentView(v);
        bottomSheetView.setCancelable(false);



    }

    //when bsSend is clicked
    private View.OnClickListener bsSendFun = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Toast.makeText(getActivity(),"Sending....",Toast.LENGTH_SHORT).show();


        }
    };

    //when bsClear is clicked
    private View.OnClickListener bsClearFun = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Toast.makeText(getActivity(),"Clear",Toast.LENGTH_SHORT).show();


        }
    };
*/


    @Override
    public void insertInQueue(File f) {
        selectedItems.add(f);

        if(selectedItems.size()==1) {
            shareButton.setClickable(true);
        }

        String label = selectedItems.size() + " is selected to share";
        shareButton.setText(label);

    }

    @Override
    public void deleteFromQueue(File f) {
        selectedItems.remove(f);

        if(selectedItems.size()<=0){
            shareButton.setClickable(false);
            String label = "Select Something";
            shareButton.setText(label);
        }else {
            shareButton.setClickable(true);
            String label = selectedItems.size() + " is selected to share";
            shareButton.setText(label);
        }

    }

    @Override
    public void navigateAndPlay(File f) {

        Fragment nextFragment = MusicFragment.playMusic(f);
        ((MainActivity)getActivity()).navigateTo(nextFragment,true);


    }

    /*
    public class GetFileAsync extends AsyncTask<Context,Void,List<File>>{

        @Override
        protected List<File> doInBackground(Context... contexts) {

            return FileHelperMethods.getFilesOfSpecificExtension(contexts[0],"apk");

        }
    }*/
}
