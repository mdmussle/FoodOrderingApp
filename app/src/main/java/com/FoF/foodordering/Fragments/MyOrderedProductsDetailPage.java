package com.FoF.foodordering.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.FoF.foodordering.Adapter.DetailOrderProductListAdapter;
import com.FoF.foodordering.Activities.MainActivity;
import com.FoF.foodordering.Extras.Config;
import com.FoF.foodordering.MVP.Ordere;
import com.FoF.foodordering.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MyOrderedProductsDetailPage extends Fragment {

    View view;
    @BindView(R.id.orderedProductsRecyclerView)
    RecyclerView orderedProductsRecyclerView;
    public static List<Ordere> orderes;
    @BindViews({R.id.orderNo, R.id.orderdate, R.id.totalAmount, R.id.paymentMode, R.id.shippingAddress, R.id.orderStatus})
    List<TextView> textViews;
    public static int pos;
    public static String currency;
    @BindView(R.id.orderStateLayout)
    LinearLayout orderStateLayout;
    @BindViews({R.id.orderInProcess, R.id.orderDispatched, R.id.orderComplete})
    List<ImageView> imageViews;
    @BindView(R.id.txtOrderSteps)
    TextView txtOrderSteps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_ordered_products_detail, container, false);
        ButterKnife.bind(this, view);
        MainActivity.title.setText("");
        setData();
        setProductsData();
        return view;
    }

    private void setData() {
        if (orderes.get(pos).getStatuscode() == 0) {
            orderStateLayout.setVisibility(View.GONE);
            txtOrderSteps.setText("Order canceled by admin, Please contact our support for more details.");
        } else {
            orderStateLayout.setVisibility(View.VISIBLE);
            setState(orderes.get(pos).getStatuscode());
        }
        if (orderes.get(pos).getCurrency().equalsIgnoreCase("USD"))
            currency = "$";
        else
            currency = "₹";
        textViews.get(0).setText(orderes.get(pos).getOrderid());
        textViews.get(1).setText(orderes.get(pos).getOrderdate());
        textViews.get(3).setText(orderes.get(pos).getPaymode());
        textViews.get(4).setText(orderes.get(pos).getAddress());
        textViews.get(5).setText(orderes.get(pos).getStatus());
        textViews.get(2).setText(currency + " " + orderes.get(pos).getTotal());
    }

    private void setState(int statuscode) {
        for (int i = 0; i < statuscode; i++) {
            imageViews.get(i).setImageResource(R.drawable.status_done);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Config.getCartList(getActivity(), true);
    }

    private void setProductsData() {
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        orderedProductsRecyclerView.setLayoutManager(gridLayoutManager);
        DetailOrderProductListAdapter myOrdersAdapter = new DetailOrderProductListAdapter(getActivity(), orderes.get(pos).getVarients());
        orderedProductsRecyclerView.setAdapter(myOrdersAdapter);

    }
}
