package com.FoF.foodordering.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.FoF.foodordering.Extras.Common;
import com.FoF.foodordering.Extras.Config;
import com.FoF.foodordering.Fragments.AppInfo;
import com.FoF.foodordering.Fragments.FAQ;
import com.FoF.foodordering.Fragments.FavoriteList;
import com.FoF.foodordering.Fragments.MainFragment;
import com.FoF.foodordering.Fragments.MyCartList;
import com.FoF.foodordering.Fragments.MyOrders;
import com.FoF.foodordering.Fragments.MyProfile;
import com.FoF.foodordering.Fragments.ProductDetail;
import com.FoF.foodordering.Fragments.RestaurantDetails;
import com.FoF.foodordering.Fragments.SearchProducts;
import com.FoF.foodordering.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    public static ImageView menu, back, cart,search;
    public static DrawerLayout drawerLayout;
    public static TextView title, cartCount;
    boolean doubleBackToExitPressedOnce = false;
    public static ProgressBar progressBar;
    public static String currency="$", userId;
    public static LinearLayout toolbarContainer;
    String from;

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbarContainer = (LinearLayout) findViewById(R.id.toolbar_container);
        initViews();
        getUserId();
        getCurrency();
        loadFragment(new MainFragment(),false);
        MainFragment.viewPagerCurrentPos=0;
        MainFragment.selectedPosHashMap.clear();
        Intent intent = getIntent();
        try {
            from = intent.getStringExtra("from");
            if (from.equalsIgnoreCase("signUp")) {
                Config.showCustomAlertDialog(MainActivity.this,
                        "Verification email sent successfully",
                        "Please check your inbox and confirm your email address. The email may take upto 5 minutes to reach your inbox\n\nIf you didn't receive email from us, make sure to check your spam folder.",
                        SweetAlertDialog.WARNING_TYPE);
            }
        } catch (Exception e) {
            Log.e("errorOccur", "Error");
        }
        displayFirebaseRegId();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.myFavorite:
                        loadFragment(new FavoriteList(), true);

                        break;
                    case R.id.myCart:
                        loadFragment(new MyCartList(), true);
                        break;
                    case R.id.myOrders:
                        loadFragment(new MyOrders(), true);
                        break;
                    case R.id.myProfile:
                        loadFragment(new MyProfile(), true);
                        break;
                    case R.id.faq:
                        loadFragment(new FAQ(), true);
                        break;
                    case R.id.appInfo:
                        loadFragment(new AppInfo(), true);
                        break;
                    case R.id.share:
                        shareApp();
                        break;
                    case R.id.rateApp:
                        // perform click on Rate Item
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                        break;
                    case R.id.email:
                        openGmail();
                        break;
                    case R.id.call:
                        call();
                        break;
                    case R.id.whatsapp:
                        openWhatsApp();
                        break;
                }
                return false;
            }
        });

        if (getIntent().getBooleanExtra("isFromNotification", false)) {
            ProductDetail.productList.clear();
            ProductDetail.productList.addAll(SplashScreen.productList);
            ProductDetail productDetail = new ProductDetail();
            Bundle bundle = new Bundle();
            bundle.putInt("position", 0);
            productDetail.setArguments(bundle);
            loadFragment(productDetail, true);
        }
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("FCM", "Firebase reg id: " + regId);
        if (!TextUtils.isEmpty(regId)) {
        } else
            Log.d("Firebase", "Firebase Reg Id is not received yet!");
    }
    @OnClick({R.id.menu, R.id.back, R.id.cart, R.id.cartCount,R.id.search,R.id.drawerTopLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.back:
                removeCurrentFragmentAndMoveBack();
                break;
            case R.id.cart:
            case R.id.cartCount:
                loadFragment(new MyCartList(), true);

                break;
            case R.id.search:
                loadFragment(new SearchProducts(), true);
                break;
            case R.id.drawerTopLayout:
                loadFragment(new RestaurantDetails(), true);
                break;

        }
    }
    public void openWhatsApp(){
        try {

            String toNumber = SplashScreen.restaurantDetailResponseData.getPhone(); // Replace with mobile phone number without +Sign or leading zeros.


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        title = (TextView) findViewById(R.id.title);
        cartCount = (TextView) findViewById(R.id.cartCount);
        menu = (ImageView) findViewById(R.id.menu);
        cart = (ImageView) findViewById(R.id.cart);
        back = (ImageView) findViewById(R.id.back);
        search = (ImageView) findViewById(R.id.search);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }
    @Override
    public void onBackPressed() {
        // double press to exit
        if (menu.getVisibility() == View.VISIBLE) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
        } else {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back once more to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }
    public void lockUnlockDrawer(int lockMode) {
        drawerLayout.setDrawerLockMode(lockMode);
        if (lockMode == DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            menu.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);

        } else {
            menu.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);

        }

    }

    private void getUserId() {
        if (Common.getSavedUserData(MainActivity.this, "userId").equalsIgnoreCase("")) {
            userId = "";
        } else {
            userId = Common.getSavedUserData(MainActivity.this, "userId");
            Log.d("userId", userId);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivityStart","Called");

    }

    private void getCurrency() {
        try {
            if (SplashScreen.restaurantDetailResponseData.getCurrency().equalsIgnoreCase("USD"))
                currency = "$";
            else
                currency = "₹";
        } catch (Exception e) {
            Log.d("currencyError",e.toString());
        }
    }

    public void removeCurrentFragmentAndMoveBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


    private void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getResources().getString(R.string.contactNo)));
        startActivity(intent);
    }

    private void openGmail() {
        // perform click on Email ID
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        String className = null;
        for (final ResolveInfo info : matches) {
            if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                className = info.activityInfo.name;

                if (className != null && !className.isEmpty()) {
                    break;
                }
            }
        }
        emailIntent.setData(Uri.parse("mailto:" + getResources().getString(R.string.emailId)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback Of " + getResources().getString(R.string.app_name));
        emailIntent.setClassName("com.google.android.gm", className);
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException ex) {
            // handle error
        }

    }

    public void shareApp() {
        // share app with your friends
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Try this " + getResources().getString(R.string.app_name) + " App: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share Using"));
    }
}
