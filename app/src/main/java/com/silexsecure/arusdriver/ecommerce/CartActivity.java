package com.silexsecure.arusdriver.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.adapters.CartAdapter;
import com.silexsecure.arusdriver.model.Cart;
import com.silexsecure.arusdriver.util.Utils;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    RecyclerView cart_recyclerview;
    CartAdapter cartAdapter;

    Button bCheckout;
    TextView textCartItemCount;
    int mCartItemCount = 0;


    ArrayList<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cartList = new ArrayList<>();

        mCartItemCount = Utils.getCartCount(this);


        bCheckout = findViewById(R.id.bCheckout);
        cart_recyclerview = findViewById(R.id.cart_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        cart_recyclerview.setLayoutManager(mLayoutManager);
        cart_recyclerview.setItemAnimator(new DefaultItemAnimator());
        cartAdapter = new CartAdapter(this, cartList);
        cart_recyclerview.setAdapter(cartAdapter);

        prepareAlbums();

        bCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, ShippingInfoActivity.class));

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.battery,
                R.drawable.shock_absorber,
                R.drawable.sensor,
                R.drawable.ignition,
                R.drawable.break_pad,
                R.drawable.gasket,
                R.drawable.tires,
                R.drawable.oil_filter,
                R.drawable.door_mirror,
                R.drawable.sensor,
                R.drawable.ignition
        };

        Cart a = new Cart("Battery", 1300.00, covers[0], 1, 1300.00);
        cartList.add(a);

        a = new Cart("Shock Absorber", 8000.00, covers[1], 1, 8000.00);
        cartList.add(a);

        cartAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_car_parts, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }


    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
