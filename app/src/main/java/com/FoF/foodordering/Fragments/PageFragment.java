package com.FoF.foodordering.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.FoF.foodordering.Adapter.ProductsByCategoryAdapter;
import com.FoF.foodordering.R;
import com.FoF.foodordering.Activities.SplashScreen;

import java.util.ArrayList;

public class PageFragment extends Fragment {
    RecyclerView recommendedItemsRecyclerView;
    public static ProductsByCategoryAdapter productsByCategoryAdapter;
    public static ArrayList<Integer> selectedPosList;
    public static int position;

    public static PageFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        selectedPosList = new ArrayList<>();
        for (int i = 0; i < SplashScreen.categoryListResponseData.get(position).getProducts().size(); i++) {
            selectedPosList.add(0);
        }
        return fragment;
    }

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        position = getArguments().getInt("position");
        int layout = R.layout.fragment_pbyc;
        root = inflater.inflate(layout, container, false);
        root.setTag(position);
        Log.d("PageFragmentPos", position + "");
        setRecommendedItemsData();
        return root;
    }

    public void setRecommendedItemsData() {
        recommendedItemsRecyclerView = (RecyclerView) root.findViewById(R.id.recommendedItemsRecyclerView);
        int size;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recommendedItemsRecyclerView.setLayoutManager(gridLayoutManager);
        recommendedItemsRecyclerView.setNestedScrollingEnabled(true);

        productsByCategoryAdapter = new ProductsByCategoryAdapter(getActivity(), SplashScreen.categoryListResponseData.get(position).getProducts(),
                SplashScreen.categoryListResponseData.get(position).getProducts().size());
        recommendedItemsRecyclerView.setAdapter(productsByCategoryAdapter);
    }

}
