package com.FoF.foodordering.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.FoF.foodordering.Adapter.HomeCategoryAdapter;
import com.FoF.foodordering.Adapter.RecommendedItemsAdapter;
import com.FoF.foodordering.Extras.GPSManager;
import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.R;
import com.FoF.foodordering.Activities.SplashScreen;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends Fragment implements LocationListener {

    View view;
    @BindView(R.id.recommendedItemsRecyclerView)
    RecyclerView recommendedItemsRecyclerView;
    public static RecommendedItemsAdapter recommendedItemsAdapter;
    @BindView(R.id.categoriesRecyclerView)
    RecyclerView categoriesRecyclerView;
    @BindViews({R.id.phoneNo, R.id.distance})
    List<TextView> textViews;
    LinearLayout viewDistanceLayout, distanceLayout;
    boolean isGPSEnabled, isNetworkEnabled;
    LocationManager locationManager;
    Location location;
    public static double latitude, longitude;
    @BindView(R.id.bannerImage)
    ImageView bannerImage;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.txtRecommended)
    TextView txtRecommended;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        distanceLayout = (LinearLayout) view.findViewById(R.id.distanceLayout);
        viewDistanceLayout = (LinearLayout) view.findViewById(R.id.viewDistanceLayout);
        setRestaurantDetails();
        setRecommendedItemsData();
        setCategoriesData();
        return view;
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + SplashScreen.restaurantDetailResponseData.getPhone()));
        startActivity(intent);
    }

    @OnClick({R.id.restaurantDetails, R.id.viewDistanceLayout, R.id.distanceLayout, R.id.contactLayout, R.id.bannerImage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bannerImage:
            case R.id.restaurantDetails:
                ((MainActivity) getActivity()).loadFragment(new RestaurantDetails(), true);
                break;
            case R.id.viewDistanceLayout:
                checkConnection();
                break;
            case R.id.distanceLayout:
                String geoUri = "http://maps.google.com/maps?q=loc:" + SplashScreen.restaurantDetailResponseData.getLat() + "," + SplashScreen.restaurantDetailResponseData.getLon() + " (" + SplashScreen.restaurantDetailResponseData.getName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
                break;
            case R.id.contactLayout:
                call();
                break;
        }
    }

    private void setRestaurantDetails() {
        if (SplashScreen.restaurantDetailResponseData.getPhone() != null) {
            textViews.get(0).setText(SplashScreen.restaurantDetailResponseData.getPhone());
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                viewDistanceLayout.setVisibility(View.VISIBLE);
                distanceLayout.setVisibility(View.GONE);
            } else {
                viewDistanceLayout.setVisibility(View.GONE);
                distanceLayout.setVisibility(View.VISIBLE);
                checkConnection();
            }
        } else {
            cardView.setVisibility(View.GONE);
        }
        try {
            Log.d("imagess", SplashScreen.restaurantDetailResponseData.getImages().get(0));

            Picasso.with(getActivity())
                    .load(SplashScreen.restaurantDetailResponseData.getImages().get(0))
                    .resize(300, 300)
                    .placeholder(R.drawable.defaultimage)
                    .into(bannerImage);
        } catch (Exception e) {
            bannerImage.setVisibility(View.GONE);
        }
    }

    private void setCategoriesData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        categoriesRecyclerView.setLayoutManager(gridLayoutManager);
        categoriesRecyclerView.setNestedScrollingEnabled(false);
        HomeCategoryAdapter homeCategoryAdapter = new HomeCategoryAdapter(getActivity(), SplashScreen.categoryListResponseData);
        categoriesRecyclerView.setAdapter(homeCategoryAdapter);
    }


    public void setRecommendedItemsData() {
        int size;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recommendedItemsRecyclerView.setLayoutManager(gridLayoutManager);
        recommendedItemsRecyclerView.setNestedScrollingEnabled(false);
        size = SplashScreen.recommendedProductList.size();
        if (size > 0) {
            txtRecommended.setVisibility(View.VISIBLE);
            recommendedItemsAdapter = new RecommendedItemsAdapter(getActivity(), SplashScreen.recommendedProductList, size, this);
            recommendedItemsRecyclerView.setAdapter(recommendedItemsAdapter);
        } else {
            txtRecommended.setVisibility(View.GONE);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        // if GPS Enabled get lat/long using GPS Services
        if (isGPSEnabled) {
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                }
            }
        }
        if (location != null) {
            onLocationChanged(location);
        }
    }

    public void updateLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled) {
            Log.d("GPS", "Enabled");
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                return;
            }
            getLocation();

        } else {
            GPSManager gps = new GPSManager(getActivity());
            gps.start();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("requestCode", requestCode + "");
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                getLocation();
            }
        }
    }


    private void checkConnection() {
        if (isNetworkAvailable())
            updateLocation();

    }

    @Override
    public void onLocationChanged(Location location) {
        float distanceInMeters;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d("LocationCoordinates", latitude + "   " + longitude);
        viewDistanceLayout.setVisibility(View.GONE);
        distanceLayout.setVisibility(View.VISIBLE);
        try {
            Location loc1 = new Location("");
            loc1.setLatitude(latitude);
            loc1.setLongitude(longitude);

            Location loc2 = new Location("");
            loc2.setLatitude(Double.parseDouble(SplashScreen.restaurantDetailResponseData.getLat()));
            loc2.setLongitude(Double.parseDouble(SplashScreen.restaurantDetailResponseData.getLon()));

            distanceInMeters = loc1.distanceTo(loc2);
            Log.d("distance", distanceInMeters + "");
            distanceInMeters = distanceInMeters / 1000;
            textViews.get(1).setText(new DecimalFormat("##.#").format(distanceInMeters) + " km away");
        } catch (Exception e) {
            textViews.get(1).setText("Not Found");

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
