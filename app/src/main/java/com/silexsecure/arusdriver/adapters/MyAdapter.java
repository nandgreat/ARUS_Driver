package com.silexsecure.arusdriver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.adapters.MyViewHolder;import com.silexsecure.arusdriver.model.ProductSale;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context c;
    ArrayList<ProductSale> productSale;

    public MyAdapter(Context c, ArrayList<ProductSale> spacecrafts) {
        this.c = c;
        this.productSale = spacecrafts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.order_history_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.nameTxt.setText(spacecrafts.get(position));

        //set data to views
        holder.tvOrderID.setText(productSale.get(position).getId());
        holder.tvQtyLitres.setText(String.valueOf(productSale.get(position).getQuantityInLitres()));
        holder.tvAmount.setText(String.valueOf(productSale.get(position).getAmount()));
        holder.tvDateTime.setText(String.valueOf(productSale.get(position).getOrderTime()));
        holder.tvDeliveryStatus.setText(productSale.get(position).getDeliveryStatus());

    }

    @Override
    public int getItemCount() {
        return productSale.size();
    }
}