
package com.silexsecure.arusdriver.adapters;

import android.content.Context;
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
import com.silexsecure.arusdriver.model.Cart;
import com.silexsecure.arusdriver.util.Utils;

import java.util.List;

/**
 * Created by Nandom Kumchi on 21/05/20.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private Context mContext;
    private List<Cart> albumList;

    private static String TAG = "AlbumAdapterTAG";

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvBrand, tvAmount;
        public ImageView ivThumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvBrand = view.findViewById(R.id.tvBrand);
            ivThumbnail = view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);
            tvAmount = view.findViewById(R.id.tvAmount);
        }
    }


    public CartAdapter(Context mContext, List<Cart> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Cart album = albumList.get(position);
        holder.tvName.setText(album.getName());
//        holder.tvBrand.setText(album.getMake());
        holder.tvAmount.setText("₦".concat(Utils.moneyFormat(String.valueOf(album.getAmount()))));

        Log.d(TAG, "onBindViewHolder: "+ album.getName());
 
        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.ivThumbnail);

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