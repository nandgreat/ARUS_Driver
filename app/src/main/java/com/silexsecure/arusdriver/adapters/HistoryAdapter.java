package com.silexsecure.arusdriver.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.model.TowingRequest;
import com.silexsecure.arusdriver.util.NotificationConfig;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryAdapter extends FirebaseRecyclerAdapter<TowingRequest, HistoryAdapter.HistoryViewHolder> {


    Context context;
    private static final String TAG = "HistoryAdapterTAG";

    DatabaseReference reference;

    public HistoryAdapter(@NonNull FirebaseRecyclerOptions<TowingRequest> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final HistoryViewHolder holder, int i, @NonNull final TowingRequest towingRequest) {

        String dateString = "";

//        if (towingRequest.getOrderTime().contentEquals("orderTime")) {
//            dateString = "orderTime";
//        } else {
//            long millisecond = Long.parseLong(towingRequest.getOrderTime());
//            dateString = DateFormat.format("MM/dd/yyyy, H:m:s", new Date(millisecond)).toString();
//        }

        String staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=13&size=600x300&maptype=roadmap&markers=color:green%7Clabel:G%7C"
                +towingRequest.getPickupLatitude()+","+towingRequest.getPickupLongitude() +"&markers=color:red%7Clabel:C%7C" +
                towingRequest.getDestinationLatitude()+","+towingRequest.getDestinationLongitude()+"&key=AIzaSyC8D7H6ILeIvJX13IOKBKAU3VZbBhalIU8";

        holder.tvDateTime.setText(towingRequest.getOrderTime());

        Glide.with(context).load(staticMapUrl).into(holder.ivMapView);

        holder.tvCarMakeNType.setText(towingRequest.getCarMake().concat("(").concat(towingRequest.getCarType()).concat(")"));

        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        String moneyString = formatter.format(towingRequest.getAmount());

        Log.d(TAG, "onBindViewHolder: "+ towingRequest.getOrderTime());
        holder.tvDateTime.setText(towingRequest.getOrderTime());
        //set data to views

        if (towingRequest.getDriverName() != null) {
            if (towingRequest.getDriverName().isEmpty())
                holder.tvRideDriver.setText(R.string.driver_not_assigned);
            else {
                String[] splitStr = towingRequest.getCustomerName().split("\\s+");
                holder.tvRideDriver.setText("Your Towing with " + splitStr[0]);
            }
        } else
            holder.tvRideDriver.setText(R.string.driver_not_assigned);

        reference = FirebaseDatabase.getInstance().getReference();

        Glide.with(context)
                .load(towingRequest.getDriverImage() == null ? R.drawable.default_user : towingRequest.getDriverImage())
                .into(holder.ivDriverImage);

//            ClientAPI mAPIService = ApiUtils.getAPIService();
//
//            Notification notification = new Notification("Gas Delivery Progress", "Your Gas Delivery is on the Way", "individual", displayFirebaseRegId());
//
//            mAPIService.sendNotification(notification).enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//                    Toast.makeText(context, String.format("OK"), Toast.LENGTH_SHORT).show();
//                    String allRepos = response.body();
//                    Log.d("HistoryTAG", "onResponse: " + allRepos);
//
//                    Toast.makeText(context, "Driver has been assigned", Toast.LENGTH_SHORT).show();
//
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//                    Log.d("HistoryTAG", "onFailure: " + t.getCause());
//                    Toast.makeText(context, "Network Error. Please try again", Toast.LENGTH_SHORT).show();
//                }
//
//            });


        holder.tvTowingStatus.setText(towingRequest.getTowingStatus());
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.towing_history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private String displayFirebaseRegId() {
        SharedPreferences pref = context.getSharedPreferences(NotificationConfig.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("HistoryTAG", "Firebase reg id: " + regId);

        return regId;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvRideDriver;
        public TextView tvCarMakeNType;
        public TextView tvAmount;
        public TextView tvDateTime;
        public ImageView ivMapView;
        public CircleImageView ivDriverImage;
        public TextView tvTowingStatus;
        public TextView tvPaymentStatus;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRideDriver = itemView.findViewById(R.id.tvRideDriver);
            tvCarMakeNType = itemView.findViewById(R.id.tvCarMakeNType);
            ivMapView = itemView.findViewById(R.id.ivMapView);
//            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            ivDriverImage = itemView.findViewById(R.id.ivDriverImage);
            tvTowingStatus = itemView.findViewById(R.id.tvTowingStatus);
            tvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
        }
    }



}