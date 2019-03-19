package com.FoF.foodordering.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.FoF.foodordering.Activities.EditCart;
import com.FoF.foodordering.MVP.Extra;
import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AbhiAndroid
 */

public class EditExtrasAdapter extends RecyclerView.Adapter<EditExtrasAdapter.RecommededExtrasViewHolder> {

    Context context;
    List<Extra> extraList;
    int quantity;
    public static LinkedList<Double> extraPriceList;
    JSONObject extraJsonObject;

    public EditExtrasAdapter(Context context, List<Extra> extraList) {
        this.context = context;
        this.extraList = extraList;
        extraPriceList = new LinkedList<>();
    }

    @Override
    public RecommededExtrasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.extras_list_items, null);
        RecommededExtrasViewHolder recommededExtrasViewHolder = new RecommededExtrasViewHolder(view);
        return recommededExtrasViewHolder;
    }

    @Override
    public void onBindViewHolder(RecommededExtrasViewHolder holder, int position) {
        holder.addItem.setVisibility(View.GONE);
        holder.quantityLayout.setVisibility(View.VISIBLE);
        extraPriceList.add(Double.parseDouble(extraList.get(position).getExtraprice())*Double.parseDouble(extraList.get(position).getExtraquantity()));
        holder.textViewList.get(0).setText(extraList.get(position).getExtraname());
        holder.textViewList.get(1).setText(String.format("%.2f", (Double.parseDouble(extraList.get(position).getExtraprice())*Double.parseDouble(extraList.get(position).getExtraquantity()))));
        holder.textViewList.get(2).setText(MainActivity.currency + " ");
        holder.textViewList.get(3).setText(extraList.get(position).getExtraquantity());

        extraJsonObject = new JSONObject();
        try {
            extraJsonObject.put("extraid", extraList.get(position).getExtraid());
            extraJsonObject.put("extraquantity", extraList.get(position).getExtraquantity());
            extraJsonObject.put("extraname", extraList.get(position).getExtraname());
            extraJsonObject.put("extraprice", extraList.get(position).getExtraprice());
            EditCart.extraJsonArray.put(extraJsonObject);
            Log.d("extraJsonArray", EditCart.extraJsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTotalAmount();

    }

    @Override
    public int getItemCount() {
        return extraList.size();
    }

    public class RecommededExtrasViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.extraName, R.id.extraPrice, R.id.currency, R.id.productQuantity})
        List<TextView> textViewList;
        @BindView(R.id.quantityLayout)
        LinearLayout quantityLayout;
        @BindView(R.id.addItem)
        ImageView addItem;

        public RecommededExtrasViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            addItem.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {
                    extraPriceList.set(getAdapterPosition(), Double.parseDouble(extraList.get(getAdapterPosition()).getExtraprice()));
                    addItem.setVisibility(View.GONE);
                    quantityLayout.setVisibility(View.VISIBLE);
                    setTotalAmount();

                    for (int i = 0; i < EditCart.extraJsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = EditCart.extraJsonArray.getJSONObject(i);
                            if (jsonObject.getString("extraid").equalsIgnoreCase(extraList.get(getAdapterPosition()).getExtraid())) {
                                EditCart.extraJsonArray.remove(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    extraJsonObject = new JSONObject();
                    try {
                        extraJsonObject.put("extraid", extraList.get(getAdapterPosition()).getExtraid());
                        extraJsonObject.put("extraquantity", "1");
                        extraJsonObject.put("extraname", extraList.get(getAdapterPosition()).getExtraname());
                        extraJsonObject.put("extraprice", extraList.get(getAdapterPosition()).getExtraprice());
                        EditCart.extraJsonArray.put(extraJsonObject);
                        Log.d("extraJsonArray", EditCart.extraJsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @OnClick({R.id.plus, R.id.minus})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.plus:
                    quantity = (Integer.parseInt(textViewList.get(3).getText().toString())) + 1;
                    textViewList.get(3).setText(quantity + "");
                    textViewList.get(1).setText(String.format("%.2f", ((Double.parseDouble(extraList.get(getAdapterPosition()).getExtraprice())) * quantity)));
                    extraPriceList.set(getAdapterPosition(), ((Double.parseDouble(extraList.get(getAdapterPosition()).getExtraprice())) * quantity));
                    setTotalAmount();
                    Log.d("extraPriceList", extraPriceList.toString());
                    for (int i = 0; i < EditCart.extraJsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = EditCart.extraJsonArray.getJSONObject(i);
                            if (jsonObject.getString("extraid").equalsIgnoreCase(extraList.get(getAdapterPosition()).getExtraid())) {
                                EditCart.extraJsonArray.remove(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    extraJsonObject = new JSONObject();
                    try {
                        extraJsonObject.put("extraid", extraList.get(getAdapterPosition()).getExtraid());
                        extraJsonObject.put("extraquantity", quantity + "");
                        extraJsonObject.put("extraname", extraList.get(getAdapterPosition()).getExtraname());
                        extraJsonObject.put("extraprice", extraList.get(getAdapterPosition()).getExtraprice());
                        EditCart.extraJsonArray.put(extraJsonObject);
                        Log.d("extraJsonArray", EditCart.extraJsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.minus:
                    if ((Integer.parseInt(textViewList.get(3).getText().toString())) != 1) {
                        quantity = (Integer.parseInt(textViewList.get(3).getText().toString())) - 1;
                        textViewList.get(3).setText(quantity + "");
                        textViewList.get(1).setText(String.format("%.2f",((Double.parseDouble(extraList.get(getAdapterPosition()).getExtraprice())) * quantity)));
                        extraPriceList.set(getAdapterPosition(), ((Double.parseDouble(extraList.get(getAdapterPosition()).getExtraprice())) * quantity));
                        setTotalAmount();
                    } else {
                        quantity = 0;
                        extraPriceList.set(getAdapterPosition(), (double) 0);
                        setTotalAmount();
                        quantityLayout.setVisibility(View.GONE);
                        addItem.setVisibility(View.VISIBLE);
                    }
                    Log.d("extraPriceList", extraPriceList.toString());

                    for (int i = 0; i < EditCart.extraJsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = EditCart.extraJsonArray.getJSONObject(i);
                            if (jsonObject.getString("extraid").equalsIgnoreCase(extraList.get(getAdapterPosition()).getExtraid())) {
                                EditCart.extraJsonArray.remove(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (quantity != 0) {
                        extraJsonObject = new JSONObject();
                        try {
                            extraJsonObject.put("extraid", extraList.get(getAdapterPosition()).getExtraid());
                            extraJsonObject.put("extraquantity", quantity + "");
                            extraJsonObject.put("extraname", extraList.get(getAdapterPosition()).getExtraname());
                            extraJsonObject.put("extraprice", extraList.get(getAdapterPosition()).getExtraprice());
                            EditCart.extraJsonArray.put(extraJsonObject);
                            Log.d("extraJsonArray", EditCart.extraJsonArray.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    private void setTotalAmount() {
        double totalAmount = EditCart.productPriceWithQuantity;
        for (int i = 0; i < extraPriceList.size(); i++) {
            totalAmount = totalAmount + extraPriceList.get(i);
        }
        EditCart.totalAmount.setText(String.format("%.2f",totalAmount));
    }
}
