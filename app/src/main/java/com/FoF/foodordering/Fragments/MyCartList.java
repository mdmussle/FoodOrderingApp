package com.FoF.foodordering.Fragments;

import android.content.Context;
import android.content.Intent;
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
import com.FoF.foodordering.Adapter.CartListAdapter;
import com.FoF.foodordering.Extras.Config;
import com.FoF.foodordering.Activities.Login;
import com.FoF.foodordering.MVP.CartProducts;
import com.FoF.foodordering.MVP.CartistResponse;
import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.R;
import com.FoF.foodordering.Retrofit.Api;
import com.FoF.foodordering.Activities.SignUp;
import com.FoF.foodordering.Activities.SplashScreen;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyCartList extends Fragment {

    View view;

    @BindView(R.id.categoryRecyclerView)
    RecyclerView productsRecyclerView;
    public static List<CartProducts> productsData = new ArrayList<>();
    public static CartistResponse cartistResponseData;
    @BindView(R.id.proceedToPayment)
    Button proceedToPayment;
    public static Context context;
    @BindView(R.id.emptyCartLayout)
    LinearLayout emptyCartLayout;
    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;
    @BindView(R.id.continueShopping)
    Button continueShopping;

    @BindView(R.id.verifyEmailLayout)
    LinearLayout verifyEmailLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart_list, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        MainActivity.title.setText("My Cart");
        proceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int)Double.parseDouble(CartListAdapter.totalAmountPayable) >= Integer.parseInt(SplashScreen.restaurantDetailResponseData.getMinorder()))
                    ((MainActivity) getActivity()).loadFragment(new ChoosePaymentMethod(), true);
                else
                    Config.showCustomAlertDialog(getActivity(),
                            "",
                            "Minimum order value must be atleast " + SplashScreen.restaurantDetailResponseData.getMinorder(),
                            SweetAlertDialog.WARNING_TYPE);

            }
        });

        return view;
    }

    @OnClick({R.id.continueShopping, R.id.loginNow, R.id.txtSignUp, R.id.verfiyNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShopping:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.cart.setVisibility(View.VISIBLE);
    }

    public void getCartList() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().getCartList(MainActivity.userId, new Callback<CartistResponse>() {
            @Override
            public void success(CartistResponse cartistResponse, Response response) {

                cartistResponseData = cartistResponse;
                pDialog.dismiss();
                productsData = new ArrayList<>();
                productsData = cartistResponse.getProducts();
                if (cartistResponse.getSuccess().equalsIgnoreCase("false")) {
                    verifyEmailLayout.setVisibility(View.VISIBLE);
                    proceedToPayment.setVisibility(View.GONE);
                } else {
                    try {
                        Log.d("cartId", cartistResponse.getCartid());
                        cartistResponse.getProducts().size();
                        proceedToPayment.setVisibility(View.VISIBLE);
                        setProductsData();
                    } catch (Exception e) {
                        proceedToPayment.setVisibility(View.GONE);
                        emptyCartLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("errorInCartList", error.toString());

                pDialog.dismiss();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.cart.setVisibility(View.GONE);
        MainActivity.cartCount.setVisibility(View.GONE);
        Config.getCartList(getActivity(), false);
        if (!MainActivity.userId.equalsIgnoreCase("")) {
            getCartList();
        } else {
            proceedToPayment.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setProductsData() {
        CartListAdapter wishListAdapter;
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        productsRecyclerView.setLayoutManager(gridLayoutManager);
        wishListAdapter = new CartListAdapter(getActivity(), productsData);
        productsRecyclerView.setAdapter(wishListAdapter);

    }
}
