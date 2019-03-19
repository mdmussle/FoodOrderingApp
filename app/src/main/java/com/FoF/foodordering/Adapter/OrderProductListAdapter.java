package com.FoF.foodordering.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.FoF.foodordering.MVP.OrderVariants;
import com.FoF.foodordering.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by AbhiAndroid
 */
public class OrderProductListAdapter extends RecyclerView.Adapter<OrderedProductsListViewHolder> {
    Context context;
    List<OrderVariants> newsListResponse;

    public OrderProductListAdapter(Context context, List<OrderVariants> newsListResponse) {
        this.context = context;
        this.newsListResponse = newsListResponse;
    }

    @Override
    public OrderedProductsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ordered_products_list_items1, null);
        OrderedProductsListViewHolder OrderedProductsListViewHolder = new OrderedProductsListViewHolder(context, view, newsListResponse);
        return OrderedProductsListViewHolder;
    }

    @Override
    public void onBindViewHolder(final OrderedProductsListViewHolder holder, final int position) {

        holder.productName1.setText(newsListResponse.get(position).getProductname()+" - "+newsListResponse.get(position).getVariantname());

        try {
            Log.d("image",newsListResponse.get(position).getImage());
            Picasso.with(context)
                    .load(newsListResponse.get(position).getImage())
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.image1);
        } catch (Exception e) {
            holder.image1.setImageResource(R.drawable.defaultimage);
        }

    }

    @Override
    public int getItemCount() {
        return newsListResponse.size();
    }

}
