package com.FoF.foodordering.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.FoF.foodordering.Activities.AccountVerification;
import com.FoF.foodordering.Adapter.DetailPageSliderPagerAdapter;
import com.FoF.foodordering.Adapter.DetailPageVariantAdapter;
import com.FoF.foodordering.Adapter.DotsAdapter;
import com.FoF.foodordering.Adapter.MyPagerAdapter;
import com.FoF.foodordering.Extras.Config;
import com.FoF.foodordering.MVP.AddToWishlistResponse;
import com.FoF.foodordering.MVP.Product;
import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.Activities.ProductExtra;
import com.FoF.foodordering.R;
import com.FoF.foodordering.Retrofit.Api;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProductDetail extends Fragment {

    View view;
    private static ViewPager mPager;
    public static int position;
    @BindView(R.id.dotsRecyclerView)
    RecyclerView dotsRecyclerView;
    @BindView(R.id.variantRecyclerView)
    RecyclerView variantRecyclerView;

    public static DotsAdapter dotsAdapter;
    Activity activity;
    ArrayList<String> sliderImages = new ArrayList<>();
    public static TextView productName, price, currency;
    public static List<Product> productList = new ArrayList<>();
    @BindView(R.id.productDescWebView)
    WebView productDescWebView;
    TextView addToFavorite;
    @BindView(R.id.variantCardView)
    CardView variantCardView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    public static Button addToCart;
    public static String productQuantity;
    @BindView(R.id.noImageAdded)
    ImageView noImageAdded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ButterKnife.bind(this, view);
        activity = (Activity) view.getContext();
        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        addToCart = (Button) view.findViewById(R.id.addToCart);
        addToFavorite = (TextView) view.findViewById(R.id.addToFavorite);
        productName = (TextView) view.findViewById(R.id.productName);
        price = (TextView) view.findViewById(R.id.price);
        currency = (TextView) view.findViewById(R.id.currency);
        setData();
        checkWishList();
        return view;
    }

    @OnClick({R.id.addToWishListLayout, R.id.addToFavorite, R.id.addToCart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addToFavorite:
            case R.id.addToWishListLayout:
                if (!MainActivity.userId.equalsIgnoreCase("")) {
                    addToWishList();
                } else {
                    Config.showLoginCustomAlertDialog(getActivity(),
                            "Login To Continue",
                            "Please login to add item in your favorite",
                            SweetAlertDialog.WARNING_TYPE);
                }
                break;
            case R.id.addToCart:
                ProductExtra.product=productList.get(position);
                MainFragment.extraList = new ArrayList<>();
                MainFragment.extraList.addAll(productList.get(position).getExtra());
                Intent intent = new Intent(getActivity(), ProductExtra.class);
                intent.putExtra("productOrderLimit",productList.get(position).getPlimit());
                intent.putExtra("productName",productName.getText().toString());
                intent.putExtra("productPrice",price.getText().toString());
                intent.putExtra("productImage",productList.get(position).getProductPrimaryImage());
                startActivity(intent);
                break;
        }

    }

    private void addToWishList() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().addToWishList(
                productList.get(position).getProductId(),
                MainActivity.userId,
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                        pDialog.dismiss();


                        Log.d("addToWishListResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            Config.showCustomAlertDialog(getActivity(),
                                    addToWishlistResponse.getMessage(),
                                    "",
                                    SweetAlertDialog.SUCCESS_TYPE);
                            checkWishList();
                        } else {
                            final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText(addToWishlistResponse.getMessage());
                            alertDialog.setConfirmText("Verify Now");
                            alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    alertDialog.dismissWithAnimation();
                                    Config.moveTo(getActivity(), AccountVerification.class);

                                }
                            });
                            alertDialog.show();
                            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                            btn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
    }

    private void checkWishList() {
        progressBar.setVisibility(View.VISIBLE);
        addToFavorite.setVisibility(View.GONE);
        Api.getClient().checkWishList(
                productList.get(position).getProductId(),
                MainActivity.userId,
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {

                        progressBar.setVisibility(View.GONE);
                        addToFavorite.setVisibility(View.VISIBLE);
                        Log.d("checkListResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            addToFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorited_icon, 0, 0, 0);
                        } else
                            addToFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unfavorite_icon, 0, 0, 0);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressBar.setVisibility(View.GONE);
                        addToFavorite.setVisibility(View.VISIBLE);
                        Log.e("error", error.toString());
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.search.setVisibility(View.VISIBLE);
        MainActivity.cart.setVisibility(View.VISIBLE);
        MainActivity.title.setText("");
        Config.getCartList(getActivity(), true);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.search.setVisibility(View.GONE);
    }


    private void setData() {
        Log.d("productId", productList.get(position).getProductId());
        sliderImages = new ArrayList<>();
        try {
            sliderImages.add(productList.get(position).getProductPrimaryImage());
            sliderImages.addAll(productList.get(position).getImages());
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
        productQuantity = productList.get(position).getQuantity();
        productDescWebView.loadDataWithBaseURL(null, productList.get(position).getDescription(), "text/html", "utf-8", null);

        Log.d("detailPageProductId", productList.get(position).getProductId() + "");
        Log.d("variantList", productList.get(position).getVariants() + "");
        if (productList.get(position).getVariants().size() > 1) {
            setVariantListData();
        } else {
            variantCardView.setVisibility(View.GONE);
        }

        updatePrice(0);
    }

    public void updatePrice(int variantPosition) {
        Log.d("detailPageVariantSize", productList.get(position).getVariants().size() + "");

        productName.setText(productList.get(position).getProductName() + " - " +
                productList.get(position).getVariants().get(variantPosition).getVariantname());
        currency.setText(MainActivity.currency + " ");
        price.setText(productList.get(position).getVariants().get(variantPosition).getVarprice());

    }

    private void setVariantListData() {
        GridLayoutManager gridLayoutManager;
        int variantListSize = productList.get(position).getVariants().size();
        if (variantListSize <= 4) {
            gridLayoutManager = new GridLayoutManager(getActivity(), variantListSize, LinearLayoutManager.VERTICAL, false);
        } else
            gridLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.HORIZONTAL, false);

        variantRecyclerView.setLayoutManager(gridLayoutManager);
        DetailPageVariantAdapter detailPageVariantAdapter = new DetailPageVariantAdapter(getActivity(), productList.get(position).getVariants());
        variantRecyclerView.setAdapter(detailPageVariantAdapter);
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
}
