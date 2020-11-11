package com.silexsecure.arusdriver.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.model.CarModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CarModelAdapter extends RecyclerView.Adapter<CarModelAdapter.MyViewHolder> {

    Context c;
    ArrayList<CarModel> itemsList;
    String from;

    public CarModelAdapter(Context c, ArrayList<CarModel> items) {
        this.c = c;
        this.from = from;
        this.itemsList = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.nameTxt.setText(items.get(position));

        holder.tvListItem.setText(itemsList.get(position).getCarModel());

        holder.tvListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("carmodel", itemsList.get(position).getCarModel());
                intent.putExtra("carmodelid", itemsList.get(position).getCarModelID());
                ((Activity) c).setResult(Activity.RESULT_OK, intent);
                ((Activity) c).finish();

            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView historyUserImage;

        TextView tvListItem;

        MyViewHolder(View view) {
            super(view);

            tvListItem = view.findViewById(R.id.tvListItem);
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}