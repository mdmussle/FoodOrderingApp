package com.FoF.foodordering.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.Fragments.MyOrderedProductsDetailPage;
import com.FoF.foodordering.MVP.Ordere;
import com.FoF.foodordering.R;

import java.util.List;


/**
 * Created by AbhiAndroid
 */
public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersViewHolder> {

    Context context;
    List<Ordere> orderes;

    public MyOrdersAdapter(Context context, List<Ordere> orderes) {
        this.context = context;
        this.orderes = orderes;
    }

    @Override
    public MyOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_orders_list_items, null);
        MyOrdersViewHolder MyOrdersViewHolder = new MyOrdersViewHolder(context, view);
        return MyOrdersViewHolder;
    }

    @Override
    public void onBindViewHolder(MyOrdersViewHolder holder, final int position) {
        setProductsData(holder, position);
        holder.date.setText("Date: " + orderes.get(position).getOrderdate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOrderedProductsDetailPage.orderes = orderes;
                MyOrderedProductsDetailPage.pos = position;
                ((MainActivity) context).loadFragment(new MyOrderedProductsDetailPage(), true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderes.size();
    }


    private void setProductsData(MyOrdersViewHolder holder, int position) {
        Log.d("orderProducts", orderes.get(position).getVarients().size() + "");
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(context, 1);
        holder.orderedProductsRecyclerView.setLayoutManager(gridLayoutManager);
        OrderProductListAdapter myOrdersAdapter = new OrderProductListAdapter(context, orderes.get(position).getVarients());
        holder.orderedProductsRecyclerView.setAdapter(myOrdersAdapter);

    }
}
