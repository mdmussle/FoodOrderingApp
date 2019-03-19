package com.FoF.foodordering.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.FoF.foodordering.Fragments.ProductDetail;
import com.FoF.foodordering.MVP.Variants;
import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.R;

import java.util.List;

/**
 * Created by AbhiAndroid
 */

public class DetailPageVariantAdapter extends RecyclerView.Adapter<DetailPageVariantAdapter.DetailPageVariantViewHolder> {

    Context context;
    List<Variants> variants;
    public static int selectedPos;

    public DetailPageVariantAdapter(Context context,List<Variants> variants) {
        this.context = context;
        this.variants = variants;
        selectedPos=0;
    }

    @Override
    public DetailPageVariantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_variant_list_items, null);
        DetailPageVariantViewHolder detailPageVariantViewHolder = new DetailPageVariantViewHolder(view);
        return detailPageVariantViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailPageVariantViewHolder holder, int position) {
        Log.d("selectedList",selectedPos+"");
        holder.size.setText(variants.get(position).getVariantname());
        holder.price.setText(MainActivity.currency+" "+variants.get(position).getVarprice());
        if (position==selectedPos)
        {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.gray));
            holder.price.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.size.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.price.setTextColor(context.getResources().getColor(R.color.black));
            holder.size.setTextColor(context.getResources().getColor(R.color.light_black));
        }
    }

    @Override
    public int getItemCount() {
        return variants.size();
    }

    public class DetailPageVariantViewHolder extends RecyclerView.ViewHolder {

        TextView price, size;
        RelativeLayout relativeLayout;

        public DetailPageVariantViewHolder(View itemView) {
            super(itemView);
            size = (TextView) itemView.findViewById(R.id.size);
            price = (TextView) itemView.findViewById(R.id.price);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPos=getAdapterPosition();
                    ProductDetail productDetail=new ProductDetail();
                    productDetail.updatePrice(selectedPos);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
