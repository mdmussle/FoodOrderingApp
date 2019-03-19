package com.FoF.foodordering.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.FoF.foodordering.Adapter.EditExtrasAdapter;
import com.FoF.foodordering.Extras.Config;
import com.FoF.foodordering.Fragments.MainFragment;
import com.FoF.foodordering.MVP.AddToWishlistResponse;
import com.FoF.foodordering.MVP.CartProducts;
import com.FoF.foodordering.R;
import com.FoF.foodordering.Retrofit.Api;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EditCart extends AppCompatActivity {

    RecyclerView recyclerView;
    String productPrice, productName, orderLimit;
    @BindViews({R.id.dialogProductName, R.id.productPrice, R.id.currency, R.id.currency1, R.id.productQuantity})
    List<TextView> textViewList;
    @BindView(R.id.productImage)
    ImageView productImage;
    public static int quantity = 1;
    public static TextView totalAmount;
    public static double productPriceWithQuantity;
    public static CartProducts product;
    String variantId, variantName, variantPrice;
    public static JSONArray extraJsonArray;
    @BindView(R.id.txtExtras)
    TextView txtExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart);
        ButterKnife.bind(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        extraJsonArray = new JSONArray();
        quantity = Integer.parseInt(product.getVariants().getVarquantity());
        setExtraList();
        if (product.getExtra().size() == 0)
            txtExtras.setVisibility(View.INVISIBLE);
        getIntentData();
        overridePendingTransition(R.anim.slide_up_dialog, R.anim.slide_out_down);
    }

    private void setTotalAmount() {
        double totalAmount = productPriceWithQuantity;
        for (int i = 0; i < EditExtrasAdapter.extraPriceList.size(); i++) {
            totalAmount = totalAmount + EditExtrasAdapter.extraPriceList.get(i);
        }
        EditCart.totalAmount.setText(String.format("%.2f",totalAmount));
    }

    private void getIntentData() {
        Intent intent = getIntent();
        productName = intent.getStringExtra("productName");
        productPrice = intent.getStringExtra("productPrice");
        orderLimit = intent.getStringExtra("productOrderLimit");
        textViewList.get(0).setText(productName);
        textViewList.get(2).setText("Price: " + MainActivity.currency + " ");
        textViewList.get(3).setText(MainActivity.currency + " ");
        Picasso.with(EditCart.this)
                .load(intent.getStringExtra("productImage"))
                .placeholder(R.drawable.item_not_added)
                .into(productImage);
        setPrice();
    }

    public void setPrice() {
        textViewList.get(4).setText(quantity + "");
        productPriceWithQuantity = (Double.parseDouble(productPrice)) * quantity;
        textViewList.get(1).setText(String.format("%.2f", productPriceWithQuantity));
        setTotalAmount();
    }

    @OnClick({R.id.close, R.id.plus, R.id.minus, R.id.addToCart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.plus:
                if (quantity < Integer.parseInt(orderLimit)) {

                    quantity = quantity + 1;
                    setPrice();
                }
                break;
            case R.id.minus:
                if (quantity != 1) {
                    quantity = quantity - 1;
                }
                setPrice();
                break;
            case R.id.addToCart:

                if (!MainActivity.userId.equalsIgnoreCase("")) {
                    addToCart();
                } else {
                    Config.showLoginCustomAlertDialog(EditCart.this,
                            "Login To Continue",
                            "Please login to add product in your wishlist",
                            SweetAlertDialog.WARNING_TYPE);
                }
                Log.d("extraJSONArray", extraJsonArray.toString());
                break;
        }
    }


    private void addToCart() {

        variantId = product.getVariants().getVarientid();
        variantName = product.getVariants().getVariantname();
        variantPrice = product.getVariants().getVarprice();
        final SweetAlertDialog pDialog = new SweetAlertDialog(EditCart.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().addToCart(product.getProductId(),
                MainActivity.userId,
                variantId,
                quantity + "",
                extraJsonArray.toString(),
                variantName,
                variantPrice,
                product.getProductName(),
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                        pDialog.dismiss();

                        Log.d("addToCartResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {

                            showCustomAlertDialog(EditCart.this,
                                    addToWishlistResponse.getMessage(),
                                    "",
                                    SweetAlertDialog.SUCCESS_TYPE);
                        } else {
                            final SweetAlertDialog alertDialog = new SweetAlertDialog(EditCart.this, SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText(addToWishlistResponse.getMessage());
                            alertDialog.setConfirmText("Verify Now");
                            alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    alertDialog.dismissWithAnimation();
                                    Config.moveTo(EditCart.this, AccountVerification.class);

                                }
                            });
                            alertDialog.show();
                            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                            btn.setBackgroundColor(ContextCompat.getColor(EditCart.this, R.color.colorPrimary));
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                        Log.d("addToCart", error.toString());
                    }
                });

    }
    public void showCustomAlertDialog(final Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setExtraList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EditCart.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        EditExtrasAdapter EditExtrasAdapter = new EditExtrasAdapter(EditCart.this, MainFragment.extraList);
        recyclerView.setAdapter(EditExtrasAdapter);
    }
}
