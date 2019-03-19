package com.FoF.foodordering.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.FoF.foodordering.Adapter.MyPagerAdapter;
import com.FoF.foodordering.Adapter.ProductsByCategoryAdapter;
import com.FoF.foodordering.Extras.Config;
import com.FoF.foodordering.Extras.DetectConnection;
import com.FoF.foodordering.MVP.CategoryListResponse;
import com.FoF.foodordering.MVP.Extra;
import com.FoF.foodordering.MVP.Product;
import com.FoF.foodordering.MVP.RecommendedProductsResponse;
import com.FoF.foodordering.MVP.RestaurantDetailResponse;
import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.R;
import com.FoF.foodordering.Retrofit.Api;
import com.FoF.foodordering.Activities.SplashScreen;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainFragment extends Fragment {

    View view;
    public static Activity activity;
    private String TAG = "testing";
    @BindString(R.string.app_name)
    String app_name;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static ViewPager viewPager;
    public static TabLayout simpleTabLayout;
    ViewPager.OnPageChangeListener mOnPageChangeListener;
    public static LinearLayout tabLinearlayout;
    public static ArrayList<Integer> selectedPosList;
    public static LinkedHashMap<Integer, ArrayList<Integer>> selectedPosHashMap = new LinkedHashMap<>();
    public static int viewPagerCurrentPos = 0;
    public static ViewPagerAdapter adapter;
    public static List<ProductsByCategoryAdapter> instances = new ArrayList();
    public static MyPagerAdapter mAdapter;
    public static List<Extra> extraList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        MainActivity.title.setText(app_name);
        simpleTabLayout = (TabLayout) view.findViewById(R.id.simpleTabLayout);
        tabLinearlayout = (LinearLayout) view.findViewById(R.id.tabLinearlayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        activity = (Activity) view.getContext();
        MainActivity.cart.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_UNLOCKED);
        MainActivity.drawerLayout.closeDrawers();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);

        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    getCategoryList();

                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart", "called " + viewPagerCurrentPos);
        MainActivity.search.setVisibility(View.VISIBLE);
        selectedPosList = new ArrayList<>();
        if (viewPagerCurrentPos == 0) {
            for (int i = 0; i < SplashScreen.recommendedProductList.size(); i++) {
                selectedPosList.add(0);
            }
        } else {
            for (int i = 0; i < SplashScreen.categoryListResponseData.get(viewPagerCurrentPos - 1).getProducts().size(); i++) {
                selectedPosList.add(0);
            }
        }
        selectedPosHashMap.put(viewPagerCurrentPos, selectedPosList);
        instances = new ArrayList<>();
        createTabs(); // create custom tabs
        Config.getCartList(getActivity(), true);


    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.search.setVisibility(View.GONE);
    }

    private void createTabs() {
        setupViewPager(viewPager);
        simpleTabLayout.setupWithViewPager(viewPager);
    }

    public static List<ProductsByCategoryAdapter> getInstances() {
        return instances;
    }

    private void setupViewPager(final ViewPager viewPager) {
        ProductsByCategoryAdapter.isFirstTime = true;
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Home(), "Home");
        for (int i = 0; i < SplashScreen.categoryListResponseData.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("pos", i);
            ProductsByCategory categoryWiseNewsList = new ProductsByCategory();
            categoryWiseNewsList.setArguments(bundle);
            adapter.addFragment(categoryWiseNewsList, SplashScreen.categoryListResponseData.get(i).getCategory_name());
        }
        viewPager.setOffscreenPageLimit(SplashScreen.categoryListResponseData.size() + 1);
        Log.d("viewPagerPosition", viewPagerCurrentPos + "");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(viewPagerCurrentPos);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("pagePosition", position + "");
                viewPagerCurrentPos = position;
                selectedPosList = new ArrayList<>();
                try {
                    Log.d("selectedHashMap", selectedPosHashMap.get(position).toString());
                    selectedPosList.addAll(selectedPosHashMap.get(position));
                } catch (Exception e) {
                    if (position != 0) {
                        for (int i = 0; i < SplashScreen.categoryListResponseData.get(position - 1).getProducts().size(); i++) {
                            selectedPosList.add(0);
                        }
                    } else {
                        for (int i = 0; i < SplashScreen.recommendedProductList.size(); i++) {
                            selectedPosList.add(0);
                        }
                    }
                    selectedPosHashMap.put(position, selectedPosList);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);

            }
        });
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private void enableDisableSwipeRefresh(boolean enable) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enable);
        }
    }


    // API Methods

    public void getCategoryList() {
        // getting category list news data
        Api.getClient().getCategoryList(new Callback<List<CategoryListResponse>>() {
            @Override
            public void success(List<CategoryListResponse> categoryListResponses, Response response) {

                try {
                    SplashScreen.categoryListResponseData = new ArrayList<>();
                    SplashScreen.categoryListResponseData.addAll(categoryListResponses);
                    Log.d("categoryData", categoryListResponses.get(0).getCategory_name());

                    getRestaurantDetail();
                } catch (Exception e) {
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    public void getRestaurantDetail() {

        // getting slider list data
        Api.getClient().getRestaurantDetail(new Callback<RestaurantDetailResponse>() {
            @Override
            public void success(RestaurantDetailResponse restaurantDetailResponse, Response response) {
                SplashScreen.restaurantDetailResponseData = restaurantDetailResponse;
                getRecommendedList();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    public void getRecommendedList() {
        // getting slider list data
        Api.getClient().getRecommendedProducts(new Callback<RecommendedProductsResponse>() {
            @Override
            public void success(RecommendedProductsResponse recommendedProductsResponse, Response response) {
                if (recommendedProductsResponse.getSuccess().equalsIgnoreCase("true")) {
                    SplashScreen.recommendedProductList = new ArrayList<>();
                    SplashScreen.recommendedProductList.addAll(recommendedProductsResponse.getProductList());
                } else {
                    SplashScreen.recommendedProductList = new ArrayList<>();
                }
                getAllProducts();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    public void getAllProducts() {
        // getting news list data
        Api.getClient().getAllProducts(new Callback<List<Product>>() {
            @Override
            public void success(List<Product> allProducts, Response response) {

                swipeRefreshLayout.setRefreshing(false);

                try {
                    SplashScreen.allProductsData = new ArrayList<>();
                    SplashScreen.allProductsData.addAll(allProducts);
                } catch (Exception e) {

                }

                selectedPosList = new ArrayList<>();
                if (viewPagerCurrentPos == 0) {
                    for (int i = 0; i < SplashScreen.recommendedProductList.size(); i++) {
                        selectedPosList.add(0);
                    }
                } else {
                    for (int i = 0; i < SplashScreen.categoryListResponseData.get(viewPagerCurrentPos - 1).getProducts().size(); i++) {
                        selectedPosList.add(0);
                    }
                }
                selectedPosHashMap.put(viewPagerCurrentPos, selectedPosList);
                instances = new ArrayList<>();
                createTabs(); // create custom tabs


            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }
}
