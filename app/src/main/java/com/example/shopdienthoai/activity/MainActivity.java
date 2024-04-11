package com.example.shopdienthoai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        replaceFragment(new HomeFragment());

        activityMainBinding.bottomMenu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menuHome) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.menuProduct) {
                replaceFragment(new ProductFragment());
            } else if (item.getItemId() == R.id.menuNoti) {
                replaceFragment(new NotificationFragment());
            } else if (item.getItemId() == R.id.menuProfile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }
}