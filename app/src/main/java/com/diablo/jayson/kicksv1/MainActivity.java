package com.diablo.jayson.kicksv1;

import android.app.SearchManager;
import android.content.Context;
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
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.ActivityFeedListAdapter;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.UI.LoginActivity;
import com.diablo.jayson.kicksv1.UI.Settings.SettingsActivity;
import com.diablo.jayson.kicksv1.UI.SignUp.SignUpActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mProfileImage;
    private RecyclerView mRecyclerView;
    private ArrayList<Activity> mKicksData;
    private ActivityFeedListAdapter mAdapter;
    private FirebaseAuth mFirebaseAuth;

    private ImageButton mSearchButton, mSettingButton;
    private ImageView mProfilePicImageView;
    private Toolbar toolbar;

    SharedPreferences prefs = null;


    @Override
    protected void onStart() {
        super.onStart();
//        checkFirstRun();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MaterialTheme);
        super.onCreate(savedInstanceState);
        fixGoogleMapBug();
        setContentView(R.layout.activity_main);
        AppBarLayout appBarLayout = findViewById(R.id.mainAppBarlayout);
        checkFirstRun();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean darkMode = sharedPreferences.getBoolean("darkmode", false);


        if (savedInstanceState != null) {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.signupfragment_container);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_kick_select, R.id.navigation_add_kick,
                R.id.navigation_profile, R.id.navigation_map_view)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_add_kick) {
                    toolbar.setVisibility(View.GONE);
                    navigationView.setVisibility(View.GONE);
                } else if (destination.getId() == R.id.navigation_map_view) {
                    toolbar.setVisibility(View.GONE);
                } else if (destination.getId() == R.id.navigation_profile) {
                    toolbar.setVisibility(View.GONE);
                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                    navigationView.setVisibility(View.VISIBLE);
                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

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
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); //iconify the widget; do not expand it by default
        searchView.setQueryHint("Search tags");

        return true;
    }

    private void fixGoogleMapBug() {
        SharedPreferences googleBug = getSharedPreferences("google_bug", Context.MODE_PRIVATE);
        if (!googleBug.contains("fixed")) {
            File corruptedZoomTables = new File(getFilesDir(), "ZoomTables.data");
            corruptedZoomTables.delete();
            googleBug.edit().putBoolean("fixed", true).apply();
        }
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
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_signOut:
                FirebaseAuth.getInstance().signOut();
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
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

    private void checkFirstRun() {

        final String PREFS_NAME = "com.color.kicks";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            // This is just a normal run
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                return;
            } else {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }

        } else if (savedVersionCode == DOESNT_EXIST) {
            // TODO This is a new install (or the user cleared the shared preferences)
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        } else if (currentVersionCode > savedVersionCode) {
            // TODO This is an upgrade
            return;
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }


}
