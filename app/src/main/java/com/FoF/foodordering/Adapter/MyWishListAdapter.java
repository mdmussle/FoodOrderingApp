package com.FoF.foodordering.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.FoF.foodordering.Fragments.MainFragment;
import com.FoF.foodordering.Fragments.ProductDetail;
import com.FoF.foodordering.MVP.Product;
import com.FoF.foodordering.MVP.Variants;
import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.Activities.ProductExtra;
import com.FoF.foodordering.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AbhiAndroid
 */
public class MyWishListAdapter extends RecyclerView.Adapter<MyWishListAdapter.MyWishListViewHolder> {
    Context context;
    List<Product> productList;
    public static PopupWindow popupwindow_obj;
    public static int index = 0;
    public static ArrayList<Integer> selectedList;

    public MyWishListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        selectedList=new ArrayList<>();
        for (int i=0;i<productList.size();i++)
        {
            selectedList.add(0);
        }
    }

    @Override
    public MyWishListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wishlist_list_items, null);
        MyWishListViewHolder myWishListViewHolder = new MyWishListViewHolder(context, view);
        return myWishListViewHolder;
    }

    public PopupWindow popupDisplay(int position, List<Variants> variants) {

        final PopupWindow popupWindow = new PopupWindow(context);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.mylayout, null);

        RecyclerView variantsRecyclerView = (RecyclerView) view.findViewById(R.id.variantsRecyclerView);
        setVariantData(variantsRecyclerView, position, variants);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        return popupWindow;
    }

    private void setVariantData(RecyclerView variantsRecyclerView, int position, List<Variants> variants) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        variantsRecyclerView.setLayoutManager(linearLayoutManager);
        WishlistProductVariantsAdapter variantsAdapter = new WishlistProductVariantsAdapter(context, variants, position);
        variantsRecyclerView.setAdapter(variantsAdapter);
    }

    @Override
    public void onBindViewHolder(final MyWishListViewHolder holder, final int position) {

        holder.currency.setText("$");
        Log.d("posIS", selectedList + "");
        holder.price.setText(productList.get(position).getVariants().get(selectedList.get(position)).getVarprice());
        holder.productName.setText(productList.get(position).getProductName() + " - " +
                productList.get(position).getVariants().get(selectedList.get(position)).getVariantname());

        try {
            Log.d("productListIs", productList.get(position).getProductPrimaryImage());
            Picasso.with(context)
                    .load(productList.get(position).getProductPrimaryImage())
                    .placeholder(R.drawable.defaultimage)
                    .resize(200, 200)
                    .into(holder.image);
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }


        if (productList.get(position).getVariants().size() < 2) {
            holder.variantCount.setText("No more variant");
        } else {
            holder.variantCount.setText((productList.get(position).getVariants().size() - 1) + " more variant");

        }
        holder.variantsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < productList.get(position).getVariants().size(); i++) {
                    if (productList.get(position).getVariants().get(i).getVarprice().equalsIgnoreCase(holder.price.getText().toString().trim())) {
                        index = i;
                    }
                }
                Log.d("recommendedindexIs", index + "");

                popupwindow_obj = popupDisplay(position, productList.get(position).getVariants());
                Rect location = locateView(holder.variantsLayout);
                popupwindow_obj.showAtLocation(holder.variantsLayout, Gravity.TOP | Gravity.LEFT, location.left, location.bottom);
            }
        });
    }

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyWishListViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView productName, price, variantCount, currency;
        @BindView(R.id.variantsLayout)
        RelativeLayout variantsLayout;
        @BindView(R.id.add)
        ImageView add;
        Animation slideUpAnimation,slideDownAnimation;

        public MyWishListViewHolder(final Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            slideUpAnimation = AnimationUtils.loadAnimation(context,
                    R.anim.slide_up_dialog);

            slideDownAnimation = AnimationUtils.loadAnimation(context,
                    R.anim.slide_out_down);
            image = (ImageView) itemView.findViewById(R.id.productImage);
            productName = (TextView) itemView.findViewById(R.id.productName);
            currency = (TextView) itemView.findViewById(R.id.currency);
            price = (TextView) itemView.findViewById(R.id.price);
            variantCount = (TextView) itemView.findViewById(R.id.variantCount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetail.productList.clear();
                    ProductDetail.productList.addAll(productList);
                    ProductDetail productDetail = new ProductDetail();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", getAdapterPosition());
                    productDetail.setArguments(bundle);
                    ((MainActivity) context).loadFragment(productDetail, true);
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductExtra.product=productList.get(getAdapterPosition());
                    MainFragment.extraList = new ArrayList<>();
                    MainFragment.extraList.addAll(productList.get(getAdapterPosition()).getExtra());
                    Intent intent = new Intent(context, ProductExtra.class);
                    intent.putExtra("productOrderLimit",productList.get(getAdapterPosition()).getPlimit());
                    intent.putExtra("productName",productName.getText().toString());
                    intent.putExtra("productPrice",price.getText().toString());
                    intent.putExtra("productImage",productList.get(getAdapterPosition()).getProductPrimaryImage());
                    context.startActivity(intent);


                }
            });
        }
    }
}
