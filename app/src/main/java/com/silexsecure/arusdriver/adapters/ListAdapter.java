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
import com.silexsecure.arusdriver.model.CarMake;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    Context c;
    ArrayList<CarMake> itemsList;
    String from;

    public ListAdapter(Context c, ArrayList<CarMake> items) {
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

        holder.tvListItem.setText(itemsList.get(position).getCarMake());

        holder.tvListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("carmake", itemsList.get(position).getCarMake());
                intent.putExtra("carmakeid", itemsList.get(position).getCarMakeID());
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
        return itemsList != null ? itemsList.size() : 0;
    }
}