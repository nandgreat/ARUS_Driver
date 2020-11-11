package com.silexsecure.arusdriver.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.silexsecure.arusdriver.R;

public class MyViewHolder extends RecyclerView.ViewHolder  {

    TextView nameTxt;

    public TextView tvOrderID;
    public TextView tvQtyLitres;
    public TextView tvAmount;
    public TextView tvDateTime;
    public TextView tvDeliveryStatus;


    public MyViewHolder(View itemView) {
        super(itemView);

        tvOrderID = itemView.findViewById(R.id.tvOrderID);
        tvQtyLitres = itemView.findViewById(R.id.tvQtyLitres);
        tvAmount = itemView.findViewById(R.id.tvAmount);
        tvDateTime = itemView.findViewById(R.id.tvDateTime);
        tvDeliveryStatus = itemView.findViewById(R.id.tvDeliveryStatus);

//        nameTxt=(TextView) itemView.findViewById(R.id.nameTxt);
    }
}