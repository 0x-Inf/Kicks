package com.diablo.jayson.kicksv1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.ActivityFeedListAdapter;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.UI.Search.SearchActivity;
import com.diablo.jayson.kicksv1.UI.Settings.SettingsActivity;
import com.diablo.jayson.kicksv1.UI.SignUp.SignUpActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mProfileImage;
    private RecyclerView mRecyclerView;
    private ArrayList<Activity> mKicksData;
    private ActivityFeedListAdapter mAdapter;
    private FirebaseAuth mFirebaseAuth;

    private ImageButton mSearchButton, mSettingButton;
    private ImageView mProfilePicImageView;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        SharedPreferences preferences;

        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MaterialTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.signupfragment_container);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_kick_select, R.id.navigation_add_kick,
                R.id.navigation_active_kicks, R.id.navigation_map_view)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_add_kick) {
                    myToolbar.setVisibility(View.GONE);
                    navigationView.setVisibility(View.GONE);
                } else if (destination.getId() == R.id.navigation_map_view) {
                    myToolbar.setVisibility(View.GONE);
                } else {
                    myToolbar.setVisibility(View.VISIBLE);
                    navigationView.setVisibility(View.VISIBLE);
                }

            }
        });
        mFirebaseAuth = FirebaseAuth.getInstance();
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
//        if (id == R.id.action_settings) {
//            return true;
//        }
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_signOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setClickListeners() {

    }

    @Override
    public void onClick(View view) {

    }

}
