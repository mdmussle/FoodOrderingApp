package com.FoF.foodordering.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import com.FoF.foodordering.Activities.AccountVerification;
import com.FoF.foodordering.Adapter.MyWishListAdapter;
import com.FoF.foodordering.Extras.Config;
import com.FoF.foodordering.Activities.Login;
import com.FoF.foodordering.MVP.Product;
import com.FoF.foodordering.MVP.WishlistResponse;
import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.R;
import com.FoF.foodordering.Retrofit.Api;
import com.FoF.foodordering.Activities.SignUp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FavoriteList extends Fragment {

    View view;

    @BindView(R.id.categoryRecyclerView)
    RecyclerView productsRecyclerView;
    public static List<Product> productsData = new ArrayList<>();
    @BindView(R.id.emptyWishlistLayout)
    LinearLayout emptyWishlistLayout;
    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;
    @BindView(R.id.verifyEmailLayout)
    LinearLayout verifyEmailLayout;
    @BindView(R.id.continueShopping)
    Button continueShopping;
public static MyWishListAdapter wishListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        ButterKnife.bind(this, view);
        MainActivity.title.setText("My Favorite");
        if (!MainActivity.userId.equalsIgnoreCase("")) {
            getWishList();
        } else {
            loginLayout.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @OnClick({R.id.continueShopping, R.id.loginNow, R.id.txtSignUp, R.id.verfiyNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShopping:
                Config.moveTo(getActivity(), MainActivity.class);
                getActivity().finish();
                break;
            case R.id.loginNow:
                Config.moveTo(getActivity(), Login.class);
                break;
            case R.id.txtSignUp:
                Config.moveTo(getActivity(), SignUp.class);
                break;
            case R.id.verfiyNow:
                Config.moveTo(getActivity(), AccountVerification.class);
                break;
        }
    }

    public void getWishList() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().getWishList(MainActivity.userId, new Callback<WishlistResponse>() {
            @Override
            public void success(WishlistResponse wishlistResponse, Response response) {
                pDialog.dismiss();
                try {
                    if (wishlistResponse.getSuccess().equalsIgnoreCase("true")) {

                        Log.d("cartId", wishlistResponse.getProducts().size() + "");
                        productsData.clear();
                        productsData = wishlistResponse.getProducts();
                        setProductsData();

                    } else {
                        verifyEmailLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.d("wishList", "Not available");
                    emptyWishlistLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                emptyWishlistLayout.setVisibility(View.VISIBLE);
                pDialog.dismiss();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Config.getCartList(getActivity(), true);
    }

    private void setProductsData() {
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        productsRecyclerView.setLayoutManager(gridLayoutManager);
        wishListAdapter = new MyWishListAdapter(getActivity(), productsData);
        productsRecyclerView.setAdapter(wishListAdapter);

    }
}
