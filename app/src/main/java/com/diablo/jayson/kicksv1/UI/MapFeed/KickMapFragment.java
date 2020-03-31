package com.diablo.jayson.kicksv1.UI.MapFeed;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class KickMapFragment extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = KickMapFragment.class.getSimpleName();

    private GoogleMap mMap;
    private Location mLastLocation;
    private LatLng mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    private ArrayList<Activity> allActivities;
    private Marker Marker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();
                } else {
                    Toast.makeText(getActivity(), R.string.location_permission_denied,
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kick_map_fragment, container, false);
        loadActiveActivitiesFromDb();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return view;
    }

    private void loadActiveActivitiesFromDb() {

        allActivities = new ArrayList<Activity>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                allActivities.add(new Activity(snapshot.toObject(Activity.class).getHost(),
                                        snapshot.toObject(Activity.class).getkickTitle(),
                                        snapshot.toObject(Activity.class).getkickTime(),
                                        snapshot.toObject(Activity.class).getKickEndTime(),
                                        snapshot.toObject(Activity.class).getkickDate(),
                                        snapshot.toObject(Activity.class).getkickLocation(),
                                        snapshot.toObject(Activity.class).getKickLocationCordinates(),
                                        snapshot.toObject(Activity.class).getMinRequiredPeople(),
                                        snapshot.toObject(Activity.class).getMaxRequiredPeeps(),
                                        snapshot.toObject(Activity.class).getMinAge(),
                                        snapshot.toObject(Activity.class).getMaxAge(),
                                        snapshot.toObject(Activity.class).getimageUrl(),
                                        snapshot.toObject(Activity.class).getTags(),
                                        snapshot.toObject(Activity.class).getUploadedTime(),
                                        snapshot.toObject(Activity.class).getUploaderId(),
                                        snapshot.toObject(Activity.class).getActivityId(),
                                        snapshot.toObject(Activity.class).getTag(),
                                        snapshot.toObject(Activity.class).getMattendees(),
                                        snapshot.toObject(Activity.class).getActivityCost()));
                            }
                        }
                    }
                });
    }

    public void enableLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);

        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLastLocation = location;

                                mCurrentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            Objects.requireNonNull(getContext()), R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        allActivities = new ArrayList<Activity>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                allActivities.add(new Activity(snapshot.toObject(Activity.class).getHost(),
                                        snapshot.toObject(Activity.class).getkickTitle(),
                                        snapshot.toObject(Activity.class).getkickTime(),
                                        snapshot.toObject(Activity.class).getKickEndTime(),
                                        snapshot.toObject(Activity.class).getkickDate(),
                                        snapshot.toObject(Activity.class).getkickLocation(),
                                        snapshot.toObject(Activity.class).getKickLocationCordinates(),
                                        snapshot.toObject(Activity.class).getMinRequiredPeople(),
                                        snapshot.toObject(Activity.class).getMaxRequiredPeeps(),
                                        snapshot.toObject(Activity.class).getMinAge(),
                                        snapshot.toObject(Activity.class).getMaxAge(),
                                        snapshot.toObject(Activity.class).getimageUrl(),
                                        snapshot.toObject(Activity.class).getTags(),
                                        snapshot.toObject(Activity.class).getUploadedTime(),
                                        snapshot.toObject(Activity.class).getUploaderId(),
                                        snapshot.toObject(Activity.class).getActivityId(),
                                        snapshot.toObject(Activity.class).getTag(),
                                        snapshot.toObject(Activity.class).getMattendees(),
                                        snapshot.toObject(Activity.class).getActivityCost()));
                            }
                            mMap = googleMap;
                            Log.e(TAG, String.valueOf(allActivities.get(0).getKickLocationCordinates()));
                            for (int i = 0; i < allActivities.size(); i++) {
                                Marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(allActivities.get(i).getKickLocationCordinates().getLatitude(), allActivities.get(i).getKickLocationCordinates().getLongitude()))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                        .title(allActivities.get(i).getTag().getTagName()));
                                Marker.setTag(allActivities.get(i).getActivityId());
                            }

                        }

                    }
                });


//        for (Activity activity : allActivities) {
//            Marker = mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(activity.getKickLocationCordinates().getLatitude(), activity.getKickLocationCordinates().getLongitude()))
//                    .title(activity.getTag().getTagName()));
//            Marker.setTag(activity.getActivityId());
//        }

//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
//        mFusedLocationClient.getLastLocation().addOnSuccessListener(
//                new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                            mMap.addMarker(new MarkerOptions().position(mCurrentLocation).title("You Were/Are Here"));
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 4));
//                        } else {
//                            Toast.makeText(getActivity(), "Location Not Found", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//        );


//        enableLocation();

    }


    public static Bitmap getBitmapFromURl(String urlsrc) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

            }
        });


        try {
            URL url = new URL(urlsrc);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }


    public static class DownloadFilesTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            Bitmap networkBitmap = null;


            String networkUrl = urls[0];//Load the first element
            URL url = null;
            try {
                url = new URL(networkUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                networkBitmap = BitmapFactory.decodeStream(
                        url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return networkBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }

    AsyncTask<String, Void, Bitmap> loadImageTask = new AsyncTask<String, Void, Bitmap>() {
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bmImg = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
                bmImg = null;
            }

            return bmImg;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            // TODO: do what you need with resulting bitmap - add marker to map


        }
    };


}
