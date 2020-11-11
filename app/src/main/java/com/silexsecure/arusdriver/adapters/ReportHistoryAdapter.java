package com.silexsecure.arusdriver.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.model.HistoryData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportHistoryAdapter extends RecyclerView.Adapter<ReportHistoryAdapter.MyViewHolder> {

    private List<HistoryData> historyDatas;

    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView historyUserImage;

        ImageView ivServiceType;
        TextView tvServiceType;
        TextView tvDescription;
        TextView tvStatus;
        TextView tvDate;
        RelativeLayout rlViewReport;

        MyViewHolder(View view) {
            super(view);

            ivServiceType = view.findViewById(R.id.ivServiceType);
            tvServiceType = view.findViewById(R.id.tvServiceType);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvDate = view.findViewById(R.id.tvDate);
            rlViewReport = view.findViewById(R.id.rlViewReport);
        }
    }

    public ReportHistoryAdapter(Context context, List<HistoryData> historyDatas) {
        this.historyDatas = historyDatas;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_model, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HistoryData history = historyDatas.get(position);

        Log.d("Position", position + "");

        holder.tvServiceType.setText(history.getServiceTypeName());
        holder.tvDescription.setText(history.getDescription());
        holder.tvDate.setText(history.getDateSubmitted());

    }

    @Override
    public int getItemCount() {

        return historyDatas.size();
    }
}
