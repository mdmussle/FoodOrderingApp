package com.FoF.foodordering.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.FoF.foodordering.MVP.OrderVariants;
import com.FoF.foodordering.R;

import java.util.List;

import butterknife.ButterKnife;

public class OrderedProductsListViewHolder extends RecyclerView.ViewHolder {

    ImageView image1;
    TextView productName1;

    public OrderedProductsListViewHolder(final Context context, View itemView, List<OrderVariants> newsListResponse) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        image1 = (ImageView) itemView.findViewById(R.id.productImage1);
        productName1 = (TextView) itemView.findViewById(R.id.productName1);



    }
}
