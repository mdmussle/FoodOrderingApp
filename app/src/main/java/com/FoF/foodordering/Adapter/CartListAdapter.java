package com.FoF.foodordering.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.FoF.foodordering.Activities.EditCart;
import com.FoF.foodordering.Fragments.MainFragment;
import com.FoF.foodordering.MVP.CartProducts;
import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.Extras.Config;
import com.FoF.foodordering.Fragments.MyCartList;
import com.FoF.foodordering.MVP.AddToWishlistResponse;
import com.FoF.foodordering.R;
import com.FoF.foodordering.Retrofit.Api;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by AbhiAndroid
 */
public class CartListAdapter extends RecyclerView.Adapter<CartListViewHolder> {
    Context context;
    List<CartProducts> cartProducts;
    public static double totalAmount = 0f, amountPayable;
    public static String totalAmountPayable;
    double tax = 0f;

    public CartListAdapter(Context context, List<CartProducts> cartProducts) {
        this.context = context;
        this.cartProducts = cartProducts;
        totalAmount = 0f;
    }

    @Override
    public CartListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_items, null);
        CartListViewHolder CartListViewHolder = new CartListViewHolder(context, view, cartProducts);
        return CartListViewHolder;
    }

    @Override
    public void onBindViewHolder(final CartListViewHolder holder, final int position) {
        totalAmount = totalAmount + (Double.parseDouble(cartProducts.get(position).getVariants().getVarprice()) * Double.parseDouble(cartProducts.get(position).getVariants().getVarquantity()));
        double extraAmount = 0;

        try {
            for (int i = 0; i < cartProducts.get(position).getExtra().size(); i++) {
                Log.d("extraAmount", cartProducts.get(position).getExtra().get(i).getExtraprice());
                extraAmount = extraAmount +
                        (Double.parseDouble(cartProducts.get(position).getExtra().get(i).getExtraprice()) * Double.parseDouble(cartProducts.get(position).getExtra().get(i).getExtraquantity()));
            }
            totalAmount = totalAmount + extraAmount;
        } catch (Exception e) {

        }

        holder.productName1.setText(cartProducts.get(position).getProductName() + " - " + cartProducts.get(position).getVariants().getVariantname());
        holder.currency.setText(MainActivity.currency + " ");
        holder.price1.setText(String.format("%.2f",(Double.parseDouble(cartProducts.get(position).getVariants().getVarquantity()) * Double.parseDouble(cartProducts.get(position).getVariants().getVarprice()))));
        Picasso.with(context)
                .load(cartProducts.get(position).getProductPrimaryImage())
                .resize(100,100)
                .into(holder.image1);
        if (cartProducts.get(position).getExtra().size() == 0) {
            holder.extraPrice.setVisibility(View.INVISIBLE);
            holder.extraCount.setVisibility(View.INVISIBLE);
        } else {
            holder.extraPrice.setVisibility(View.VISIBLE);
            holder.extraCount.setVisibility(View.VISIBLE);
            holder.extraCount.setText(cartProducts.get(position).getExtra().size() + " Extra:");
            holder.extraPrice.setText(MainActivity.currency + " " + String.format("%.2f",extraAmount));
        }

        holder.quantity.setText(cartProducts.get(position).getVariants().getVarquantity() + " Quantity:");

        if (position == cartProducts.size() - 1) {
            holder.totalAmount.setVisibility(View.VISIBLE);
            holder.txtGurantee.setText(Html.fromHtml(context.getResources().getString(R.string.secure_payment_text)));

            holder.textViews.get(0).setText(" Total Price (" + cartProducts.size() + " items)");
            holder.textViews.get(1).setText(MainActivity.currency + " " + String.format("%.2f",totalAmount));
            if (MyCartList.cartistResponseData.getShipping().length() > 0) {

                holder.textViews.get(2).setText(MainActivity.currency + " " + String.format("%.2f",Double.parseDouble(MyCartList.cartistResponseData.getShipping())));
                amountPayable = totalAmount +
                        Double.parseDouble(MyCartList.cartistResponseData.getShipping());
            } else {
                amountPayable = totalAmount;
                holder.textViews.get(2).setText(MainActivity.currency + " 0.0");

            }
            if (MyCartList.cartistResponseData.getTax().length() > 0) {
                tax = (totalAmount / 100) * Double.parseDouble(MyCartList.cartistResponseData.getTax());
                holder.textViews.get(5).setText("Tax (" + MyCartList.cartistResponseData.getTax() + "%)");
            }
            tax = Double.parseDouble(String.format("%.2f", tax));
            Log.d("floatTax", tax + "");
            holder.textViews.get(3).setText(MainActivity.currency + " " + String.format("%.2f",tax));
            holder.textViews.get(4).setText(MainActivity.currency + " " + (String.format("%.2f", (amountPayable + tax))));
            totalAmountPayable = (String.format("%.2f", (amountPayable + tax)));
            Log.d("totalAmountPayable", totalAmountPayable);
        } else
            holder.totalAmount.setVisibility(View.GONE);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditCart.product = cartProducts.get(position);
                MainFragment.extraList = new ArrayList<>();
                MainFragment.extraList.addAll(cartProducts.get(position).getExtra());
                Intent intent = new Intent(context, EditCart.class);
                intent.putExtra("productOrderLimit", cartProducts.get(position).getPlimit());
                intent.putExtra("productName", holder.productName1.getText().toString());
                intent.putExtra("productPrice", cartProducts.get(position).getVariants().getVarprice());
                intent.putExtra("productImage", cartProducts.get(position).getProductPrimaryImage());
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCart(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartProducts.size();
    }

    private void deleteCart(final int position) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().deleteCartItem(
                MainActivity.userId,
                cartProducts.get(position).getVariants().getVarientid(),
                cartProducts.get(position).getProductId(),
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                        pDialog.dismiss();
                        Log.d("deleteCartResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            Config.getCartList(context, false);
                            MainActivity.cartCount.setVisibility(View.GONE);
                            ((MainActivity) context).loadFragment(new MyCartList(), true);
                            ((MainActivity) context).removeCurrentFragmentAndMoveBack();

                            Config.showCustomAlertDialog(context,
                                    "Your Cart status",
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.SUCCESS_TYPE);
                        } else {

                            Config.showCustomAlertDialog(context,
                                    "Your Cart status",
                                    addToWishlistResponse.getMessage(),
                                    SweetAlertDialog.NORMAL_TYPE);
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
    }
}
