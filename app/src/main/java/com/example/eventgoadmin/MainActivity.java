package com.example.eventgoadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.eventgoadmin.databinding.ActivityMainBinding;
import com.example.eventgoadmin.request.ApiRequest;
import com.example.eventgoadmin.request.RetrofitRequest;
import com.example.eventgoadmin.ui.buat_event.BuatEventActivity;
import com.example.eventgoadmin.ui.home.HomeFragment;
import com.example.eventgoadmin.ui.home.InboxFragment;
import com.example.eventgoadmin.ui.home.ProfilFragment;
import com.example.eventgoadmin.ui.home.UserFragment;
import com.example.eventgoadmin.util.Utils;
import com.irfaan008.irbottomnavigation.SpaceItem;
import com.irfaan008.irbottomnavigation.SpaceOnClickListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public ActivityMainBinding binding;
    ApiRequest apiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiRequest = RetrofitRequest.getInstance().create(ApiRequest.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.spaceNav.initWithSaveInstanceState(savedInstanceState);
        binding.spaceNav.addSpaceItem(new SpaceItem("", R.drawable.nav_home));
        binding.spaceNav.addSpaceItem(new SpaceItem("", R.drawable.download));
        binding.spaceNav.addSpaceItem(new SpaceItem("", R.drawable.group));
        binding.spaceNav.addSpaceItem(new SpaceItem("", R.drawable.user));
        binding.spaceNav.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Intent intent = new Intent(MainActivity.this, BuatEventActivity.class);
                intent.putExtra("type_intent", Utils.TYPE_ADD);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex == 0){
                    setFragment(new HomeFragment());
                }else if(itemIndex == 1) {
                    setFragment(new InboxFragment());
                }else if(itemIndex == 2) {
                    setFragment(new UserFragment());
                }else if(itemIndex == 3) {
                    setFragment(new ProfilFragment());
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
            }
        });

        setFragment(new HomeFragment());
    }


    public void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameHome.getId(), fragment);
        transaction.commit();
    }



}