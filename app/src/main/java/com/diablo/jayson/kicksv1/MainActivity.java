package com.diablo.jayson.kicksv1;

import android.content.Intent;
import android.os.Bundle;

import com.diablo.jayson.kicksv1.Adapters.ActivityFeedListAdapter;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.UI.LoginActivity;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView mProfileImage;
    private RecyclerView mRecyclerView;
    private ArrayList<Activity> mKicksData;
    private ActivityFeedListAdapter mAdapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;


    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser  = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }else {
            Toast.makeText(this, mFirebaseUser.getEmail(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView,navController);
        mFirebaseAuth = FirebaseAuth.getInstance();



//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        int gridColumnCount = 1;
//
//        mRecyclerView = findViewById(R.id.recyclerview);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridColumnCount));
//        mKicksData = new ArrayList<>();
//
//        mAdapter = new ActivityFeedListAdapter(this, mKicksData);
//
//        mRecyclerView.setAdapter(mAdapter);
//
//
//        initializeData();


//        int imageDrawable = R.drawable.ic_main_nav_fab;

//        Picasso.get()
//                .load(imageDrawable)
//                .transform(new CircleTransform())
//                .into(mProfileImage);


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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

//    private void initializeData() {
//        String[] kicksTitles = getResources()
//                .getStringArray(R.array.kicks_titles);
//        String[] kicksDates = getResources()
//                .getStringArray(R.array.kicks_dates);
//        String[] kicksTimes = getResources()
//                .getStringArray(R.array.kicks_time);
//        String[] kicksLocations = getResources()
//                .getStringArray(R.array.kicks_location);
//        String[] alreadyAttending = getResources()
//                .getStringArray(R.array.already_attending_peeps);
//        String[] requiredAttending = getResources()
//                .getStringArray(R.array.required_peeps);
//        TypedArray kicksImageResources = getResources().obtainTypedArray(R.array.kicks_images);
//
//        mKicksData.clear();
//
//        for (int i = 0; i < kicksTitles.length; i++) {
//            mKicksData.add(new Activity(kicksTitles[i], kicksTimes[i], kicksDates[i],
//                    kicksLocations[i], alreadyAttending[i], requiredAttending[i],
//                    kicksImageResources.getResourceId(i, 0)));
//        }
//        kicksImageResources.recycle();
//
//        mAdapter.notifyDataSetChanged();
//    }

//    class CircleTransform implements Transformation {
//
//        boolean mCircleSeparator = false;
//
//        public CircleTransform() {
//
//        }
//
//        public CircleTransform(boolean circleSeparator) {
//            mCircleSeparator = circleSeparator;
//        }
//
//        @Override
//        public Bitmap transform(Bitmap source) {
//            int size = Math.min(source.getWidth(), source.getHeight());
//            int x = (source.getWidth() - size) / 2;
//            int y  = (source.getHeight()-size)/2;
//            Bitmap squaredBitmap = Bitmap.createBitmap(source,x,y,size,size);
//            if (squaredBitmap != source){
//                source.recycle();
//            }
//            Bitmap bitmap = Bitmap.createBitmap(size,size,source.getConfig());
//            Canvas canvas = new Canvas(bitmap);
//            BitmapShader shader = new BitmapShader(squaredBitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP);
//            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG|Paint.FILTER_BITMAP_FLAG);
//            paint.setShader(shader);
//            float r  = size/2f;
//            canvas.drawCircle(r,r,r-1,paint);
//
//
//            Paint paintBorder = new Paint();
//            paintBorder.setStyle(Paint.Style.STROKE);
//            paintBorder.setColor(Color.argb(84,0,0,0));
//            paintBorder.setAntiAlias(true);
//            paintBorder.setStrokeWidth(1);
//            canvas.drawCircle(r,r,r-1,paintBorder);
//
//            if (mCircleSeparator){
//                Paint paintBorderSeparator = new Paint();
//                paintBorderSeparator.setStyle(Paint.Style.STROKE);
//                paintBorderSeparator.setColor(Color.parseColor("#ffffff"));
//                paintBorderSeparator.setAntiAlias(true);
//                paintBorderSeparator.setStrokeWidth(4);
//                canvas.drawCircle(r,r,r+1,paintBorderSeparator);
//            }
//
//            squaredBitmap.recycle();
//
//
//            return bitmap;
//        }
//
//        @Override
//        public String key() {
//            return "String";
//        }
//    }
}
