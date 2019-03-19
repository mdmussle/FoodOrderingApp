package com.FoF.foodordering.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.FoF.foodordering.Fragments.MainFragment;
import com.FoF.foodordering.MVP.CategoryListResponse;
import com.FoF.foodordering.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AbhiAndroid
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.HomeCategoryViewHolder> {

    Context context;
    List<CategoryListResponse> categoryList;

    public HomeCategoryAdapter(Context context, List<CategoryListResponse> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public HomeCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_category_list_items, null);
        HomeCategoryAdapter.HomeCategoryViewHolder homeCategoryViewHolder = new HomeCategoryViewHolder(view);
        return homeCategoryViewHolder;
    }

    @Override
    public void onBindViewHolder(HomeCategoryViewHolder holder, int position) {
        holder.categoryName.setText(categoryList.get(position).getCategory_name());
        try {
            Picasso.with(context)
                    .load(categoryList.get(position).getCategory_image())
                    .placeholder(R.drawable.defaultimage)
                    .resize(200, 200)
                    .into(holder.categoryImage);
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.categoryImage)
        ImageView categoryImage;

        @BindView(R.id.categoryName)
        TextView categoryName;

        public HomeCategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainFragment.viewPager.setCurrentItem(getAdapterPosition()+1);
                }
            });
        }
    }
}
