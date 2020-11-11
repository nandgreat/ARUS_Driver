
package com.silexsecure.arusdriver.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.ecommerce.ProductSingleActivity;
import com.silexsecure.arusdriver.model.ProductSingle;
import com.silexsecure.arusdriver.util.Utils;

import java.util.List;

/**
 * Created by Nandom Kumchi on 21/05/20.
 */
public class ProductSingleAdapter extends RecyclerView.Adapter<ProductSingleAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductSingle> albumList;

    private static String TAG = "AlbumAdapterTAG";

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, tvBrand, tvPrice;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            tvBrand = view.findViewById(R.id.tvBrand);
            thumbnail = view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);
            tvPrice = view.findViewById(R.id.tvPrice);
        }
    }


    public ProductSingleAdapter(Context mContext, List<ProductSingle> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductSingle album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.tvBrand.setText(album.getMake());
        holder.tvPrice.setText("₦".concat(Utils.moneyFormat(String.valueOf(album.getAmount()))));

        Log.d(TAG, "onBindViewHolder: "+ album.getName());
 
        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ProductSingleActivity.class));
            }
        });
    }
 
    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_car_parts, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
 
    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
 
        public MyMenuItemClickListener() {
        }
 
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_cart:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_search:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
 
    @Override
    public int getItemCount() {
        return albumList.size();
    }
}