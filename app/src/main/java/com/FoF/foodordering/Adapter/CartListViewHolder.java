package com.FoF.foodordering.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.FoF.foodordering.MVP.CartProducts;
import com.FoF.foodordering.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class CartListViewHolder extends RecyclerView.ViewHolder {

    ImageView image1;
    ImageView delete;
    TextView productName1, price1, quantity, extraCount, extraPrice, txtGurantee,currency;
    CardView cardView1;
    @BindView(R.id.totalAmount)
    LinearLayout totalAmount;
    @BindViews({R.id.txtPrice, R.id.price, R.id.delivery,  R.id.tax,  R.id.amountPayable,  R.id.txtTax})
    List<TextView> textViews;
    @BindView(R.id.edit)
    TextView edit;

    public CartListViewHolder(final Context context, View itemView, List<CartProducts> newsListResponse) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        image1 = (ImageView) itemView.findViewById(R.id.productImage1);
        delete = (ImageView) itemView.findViewById(R.id.delete);
        productName1 = (TextView) itemView.findViewById(R.id.productName1);
        extraCount = (TextView) itemView.findViewById(R.id.extraCount);
        extraPrice = (TextView) itemView.findViewById(R.id.extraPrice);
        price1 = (TextView) itemView.findViewById(R.id.price1);
        currency = (TextView) itemView.findViewById(R.id.currency);
        quantity = (TextView) itemView.findViewById(R.id.quantity);
        txtGurantee = (TextView) itemView.findViewById(R.id.txtGurantee);
        cardView1 = (CardView) itemView.findViewById(R.id.cardView1);


    }
}
