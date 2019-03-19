package com.FoF.foodordering.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.FoF.foodordering.Adapter.DetailPageSliderPagerAdapter;
import com.FoF.foodordering.Adapter.DotsAdapter;
import com.FoF.foodordering.Adapter.MyPagerAdapter;
import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.Activities.MapViewActivity;
import com.FoF.foodordering.R;
import com.FoF.foodordering.Activities.SplashScreen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestaurantDetails extends Fragment {

    View view;
    ArrayList<String> sliderImages = new ArrayList<>();

    private static ViewPager mPager;
    @BindView(R.id.noImageAdded)
    ImageView noImageAdded;
    @BindViews({R.id.name, R.id.address, R.id.phoneNo, R.id.website, R.id.openingHours, R.id.description})
    List<TextView> textViews;
    @BindView(R.id.dotsRecyclerView)
    RecyclerView dotsRecyclerView;
    public static DotsAdapter dotsAdapter;
    Activity activity;
    SupportMapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        ButterKnife.bind(this, view);
        activity = (Activity) view.getContext();
        MainActivity.title.setText("About Us");
        setData();
        return view;
    }

    @OnClick({R.id.viewMap, R.id.navigate, R.id.address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.address:
            case R.id.navigate:
                String geoUri = "http://maps.google.com/maps?q=loc:" + SplashScreen.restaurantDetailResponseData.getLat() + "," + SplashScreen.restaurantDetailResponseData.getLon() + " (" + textViews.get(0).getText().toString() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
                break;
            case R.id.viewMap:
                startActivity(new Intent(getActivity(), MapViewActivity.class).putExtra("lat", SplashScreen.restaurantDetailResponseData.getLat()).putExtra("long", SplashScreen.restaurantDetailResponseData.getLon()));
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.search.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.search.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        Config.getCartList(getActivity(), true);
        try {
            integrateMap();
        } catch (Exception e) {

        }
    }

    private void setData() {
        sliderImages = new ArrayList<>();
        try {
            sliderImages.addAll(SplashScreen.restaurantDetailResponseData.getImages());
            if (sliderImages.size() > 0) {
                init();
                noImageAdded.setVisibility(View.GONE);
            } else {
                noImageAdded.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            noImageAdded.setVisibility(View.VISIBLE);
        }
        setDots(0);
        textViews.get(0).setText(SplashScreen.restaurantDetailResponseData.getName());
        textViews.get(1).setText(SplashScreen.restaurantDetailResponseData.getAddress());
        textViews.get(2).setText(SplashScreen.restaurantDetailResponseData.getPhone());
        textViews.get(3).setText(SplashScreen.restaurantDetailResponseData.getWeb());
        textViews.get(4).setText(SplashScreen.restaurantDetailResponseData.getTime());
        textViews.get(5).setText(Html.fromHtml(SplashScreen.restaurantDetailResponseData.getDescription()));
    }


    private void init() {
        mPager = (ViewPager) view.findViewById(R.id.pager);
        DetailPageSliderPagerAdapter mAdapter = new DetailPageSliderPagerAdapter(getChildFragmentManager(), sliderImages);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(mPager.getChildCount() * MyPagerAdapter.LOOPS_COUNT / 2, false); // set current item in the adapter to middle
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % sliderImages.size();
                Log.d("onPageSelected", position + "");
                setDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setDots(int selectedPos) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        dotsRecyclerView.setLayoutManager(linearLayoutManager);
        dotsAdapter = new DotsAdapter(activity, sliderImages.size(), selectedPos);
        dotsRecyclerView.setAdapter(dotsAdapter);

    }


    private void integrateMap() {
        mapFragment = new SupportMapFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.framelayout, mapFragment).commit();
        Bundle args = new Bundle();
        args.putString("longitude", SplashScreen.restaurantDetailResponseData.getLon());
        args.putString("latitude", SplashScreen.restaurantDetailResponseData.getLat());
        mapFragment.setArguments(args);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng citylocation = new LatLng(Double.parseDouble(SplashScreen.restaurantDetailResponseData.getLat()), Double.parseDouble(SplashScreen.restaurantDetailResponseData.getLon()));

                googleMap.addMarker(new MarkerOptions().position(citylocation).title("Marker Title").snippet("Marker Description"));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(citylocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }
}
