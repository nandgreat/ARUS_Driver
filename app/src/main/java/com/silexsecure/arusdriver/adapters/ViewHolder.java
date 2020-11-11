package com.silexsecure.arusdriver.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.model.ProductSale;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

    }

    //set details to recycler view row
    public void setDetails(Context ctx, ProductSale productSale){
        //Views
        TextView tvOrderID = mView.findViewById(R.id.tvOrderID);
        TextView tvQtyLitres = mView.findViewById(R.id.tvQtyLitres);
        TextView tvAmount = mView.findViewById(R.id.tvAmount);
        TextView tvDateTime = mView.findViewById(R.id.tvDateTime);
        TextView tvDeliveryStatus = mView.findViewById(R.id.tvDeliveryStatus);
        //set data to views
        tvOrderID.setText(productSale.getId());
        tvQtyLitres.setText(String.valueOf(productSale.getQuantityInLitres()));
        tvAmount.setText(String.valueOf(productSale.getAmount()));
        tvDateTime.setText(String.valueOf(productSale.getOrderTime()));
        tvDeliveryStatus.setText(productSale.getDeliveryStatus());
    }

    private ClickListener mClickListener;

    //interface to send callbacks
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View  view, int position);
    }

    public void setOnClickListener(ClickListener clickListener){
        mClickListener = clickListener;
    }
}