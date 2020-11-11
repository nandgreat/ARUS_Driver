package com.silexsecure.arusdriver.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.model.OrderStatus;
import com.silexsecure.arusdriver.model.ProductSale;

import java.text.DecimalFormat;
import java.util.Date;

public class PendingOrdersAdapter extends FirebaseRecyclerAdapter<ProductSale, PendingOrdersAdapter.PendingOrderViewHolder> {

    Context context;

    public PendingOrdersAdapter(@NonNull FirebaseRecyclerOptions<ProductSale> options, Context context) {
        super(options);
        this.context = context;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull PendingOrderViewHolder holder, int i, @NonNull final ProductSale productSale) {

        String dateString = "";

        if (productSale.getOrderTime().contentEquals("orderTime")) {
            dateString = "orderTime";
        } else {
            long millisecond = Long.parseLong(productSale.getOrderTime());
            dateString = DateFormat.format("MM/dd/yyyy, H:m:s", new Date(millisecond)).toString();
        }

        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        String moneyString = formatter.format(productSale.getAmount());
        //set data to views
        holder.tvOrderID.setText(productSale.getId());
        if (productSale.getQuantityInKg() != null)
            holder.tvQtyLitres.setText(productSale.getQuantityInKg().toString().concat(" Kg(s)"));

        if (productSale.getDeliveryStatus().contentEquals(OrderStatus.DriverAssigned.toString())) {
            holder.tvDriverNotAssigned.setEnabled(false);
            holder.tvDriverNotAssigned.setVisibility(View.GONE);

            holder.tvCallDriver.setVisibility(View.VISIBLE);
            holder.tvTrackOrder.setVisibility(View.VISIBLE);

            holder.tvCallDriver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + productSale.getDriverPhone()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    context.startActivity(callIntent);
                }
            });

            holder.tvTrackOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Tracking your Order", Toast.LENGTH_SHORT).show();

                }
            });
        }


        holder.tvAmount.setText("₦" + moneyString + "");
        holder.tvDateTime.setText(dateString);
        holder.tvDeliveryStatus.setText(productSale.getDeliveryStatus());
    }

    @NonNull
    @Override
    public PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pending_order_item, parent, false);
        return new PendingOrderViewHolder(view);
    }

    static class PendingOrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderID;
        TextView tvQtyLitres;
        TextView tvAmount;
        TextView tvDateTime;
        TextView tvDeliveryStatus;
        TextView tvDriverNotAssigned, tvCallDriver,tvTrackOrder;

        PendingOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderID = itemView.findViewById(R.id.tvOrderID);
            tvQtyLitres = itemView.findViewById(R.id.tvQtyLitres);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvDeliveryStatus = itemView.findViewById(R.id.tvDeliveryStatus);
            tvDriverNotAssigned = itemView.findViewById(R.id.tvDriverNotAssigned);
            tvCallDriver = itemView.findViewById(R.id.tvCallDriver);
            tvTrackOrder = itemView.findViewById(R.id.tvTrackOrder);

        }
    }
}