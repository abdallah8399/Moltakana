package com.wingy.moltakana.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wingy.moltakana.Fragments.BlockAccountFragment;
import com.wingy.moltakana.Fragments.HistoryFragment;
import com.wingy.moltakana.Fragments.ListFragment;
import com.wingy.moltakana.Fragments.ManageAccountFragment;
import com.wingy.moltakana.Fragments.RoomInfoFragment;
import com.wingy.moltakana.Fragments.RoomSettingsFragment;
import com.wingy.moltakana.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class ManageRoomActivity extends AppCompatActivity {
    public static String position_major, position_minor, position_user, senderName;
    public static final int ALL=1, MEM_SUPERVISOR=2, SUPERVISOR=3, NO_ONE=4, OPEN=5, ENTRANCE_GATE=6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_room);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new RoomInfoFragment()).commit();

        position_major= getIntent().getStringExtra("position_major");
        position_minor= getIntent().getStringExtra("position_minor");
        position_user= getIntent().getStringExtra("position_user");
        senderName= getIntent().getStringExtra("senderName");

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment fragment = null;
                    switch (item.getItemId()){
                       // case R.id.navigation_roomInfo: fragment = new RoomInfoFragment();
                         //   break;
                        case R.id.navigation_blocked: fragment = new BlockAccountFragment();
                            break;
                        case R.id.navigation_manage_accounts: fragment = new ManageAccountFragment();
                            break;
                        case R.id.navigation_history: fragment = new HistoryFragment();
                            break;
                        case R.id.navigation_list: fragment = new ListFragment();
                            break;
                        case R.id.navigation_room_settings: fragment = new RoomSettingsFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            fragment).commit();
                    return true;

                }
            };

}
