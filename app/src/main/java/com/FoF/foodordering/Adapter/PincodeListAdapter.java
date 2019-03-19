package com.FoF.foodordering.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.FoF.foodordering.R;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PincodeListAdapter extends RecyclerView.Adapter<PincodeListAdapter.MyViewHolder> implements View.OnClickListener {

    List<String> listData;
    Context context;
    public static int pos;


    public PincodeListAdapter(Context context, List<String> listData) {
        this.context = context;
        this.listData = listData;
        pos = -1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pincode_list_items, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder

        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // set the data in items
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{5, 5, 5, 5, 5, 5, 5, 5});
        holder.pincode.setTextColor(Color.WHITE);
        shape.setColor(holder.colorPrimary);
        holder.pincode.setText(listData.get(position).trim());
        holder.pincode.setBackgroundDrawable(shape);
        holder.pincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", holder.pincode.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context,"Pincode copied to clipboard",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        @BindView(R.id.pincode)
        TextView pincode;
        @BindColor(R.color.gray)
        int gray;
        @BindColor(R.color.colorPrimary)
        int colorPrimary;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            ButterKnife.bind(this, itemView);

        }
    }
}