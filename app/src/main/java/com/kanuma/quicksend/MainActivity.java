package com.kanuma.quicksend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.kanuma.quicksend.Fragments.AllFilesFragment;

public class MainActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionsHandling.getPermission(this);

        if(savedInstanceState ==null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container,new AllFilesFragment())
                    .commit();
        }
    }


    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {

        FragmentTransaction transaction =
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,fragment);

        if(addToBackstack){
            transaction.addToBackStack(null);
        }

        transaction.commit();


    }
}
