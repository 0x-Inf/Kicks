package com.diablo.jayson.kicksv1;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Adapters.ActivityFeedListAdapter;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.UI.LoginActivity;
import com.diablo.jayson.kicksv1.UI.Search.SearchActivity;
import com.diablo.jayson.kicksv1.UI.UserProfile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mProfileImage;
    private RecyclerView mRecyclerView;
    private ArrayList<Activity> mKicksData;
    private ActivityFeedListAdapter mAdapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private ImageButton mSearchButton,mSettingButton;
    private ImageView mProfilePicImageView;


    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, mFirebaseUser.getEmail(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchButton = findViewById(R.id.searchImageButton);
        mSettingButton = findViewById(R.id.settingsImageButton);
        mProfilePicImageView = findViewById(R.id.profilePicImageView);
        getSupportActionBar().hide();
        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_kick_select, R.id.navigation_add_kick,
                R.id.navigation_active_kicks,R.id.navigation_map_view)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        mFirebaseAuth = FirebaseAuth.getInstance();
        String photoUrl = "https://comps.canstockphoto.com/bowling-eps-vectors_csp2647543.jpg";

        Glide.with(this)
                .load(photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(mProfilePicImageView);

        setClickListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setClickListeners(){
        mSearchButton.setOnClickListener(this);
        mSettingButton.setOnClickListener(this);
        mProfilePicImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(mSearchButton)){
            startActivity(new Intent(this, SearchActivity.class));
        }else if (view.equals(mSettingButton)){
            startActivity(new Intent(this,SearchActivity.class));
        }else if (view.equals(mProfilePicImageView)){
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

}
