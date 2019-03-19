package com.FoF.foodordering.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.FoF.foodordering.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by AbhiAndroid
 */

class VariantsAdapter extends RecyclerView.Adapter<VariantsAdapter.VariantsViewHolder> {

    Context context;
    public static int selectedPos = 0;


    public VariantsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public VariantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.variants_list_items, null);
        VariantsViewHolder variantsViewHolder = new VariantsViewHolder(view);
        return variantsViewHolder;
    }

    @Override
    public void onBindViewHolder(VariantsViewHolder holder, int position) {
        if (selectedPos == position) {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.textViewList.get(0).setTextColor(context.getResources().getColor(R.color.white));
            holder.textViewList.get(1).setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.textViewList.get(0).setTextColor(context.getResources().getColor(R.color.light_black));
            holder.textViewList.get(1).setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class VariantsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.relativeLayout)
        RelativeLayout relativeLayout;
        @BindViews({R.id.size, R.id.price})
        List<TextView> textViewList;

        public VariantsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPos = getAdapterPosition();
                    RecommendedItemsAdapter.popupwindow_obj.dismiss();
                    notifyItemChanged(selectedPos);
                }
            });
        }
    }
}
