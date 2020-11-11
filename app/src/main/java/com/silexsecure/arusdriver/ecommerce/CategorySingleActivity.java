package com.silexsecure.arusdriver.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.adapters.ProductSingleAdapter;
import com.silexsecure.arusdriver.model.ProductSingle;
import com.silexsecure.arusdriver.util.GridSpacingItemDecoration;
import com.silexsecure.arusdriver.util.Helper;
import com.silexsecure.arusdriver.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CategorySingleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductSingleAdapter productSingleAdapter;
    private List<ProductSingle> productSingleList;
    private Helper helper;
    TextView textCartItemCount;
    int mCartItemCount = 0;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_single);

        helper = new Helper(this);

        mCartItemCount = Utils.getCartCount(this);


        getSupportActionBar().setTitle("Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);

        productSingleList = new ArrayList<>();

        productSingleAdapter = new ProductSingleAdapter(this, productSingleList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, helper.dpToPx(10, this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productSingleAdapter);

        prepareAlbums();
    }


    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.battery,
                R.drawable.ttao_logo,
                R.drawable.britmarine,
                R.drawable.blue_ray,
                R.drawable.exide,
                R.drawable.varta,
                R.drawable.bosch,
                R.drawable.duracell,
                };

        ProductSingle a = new ProductSingle("12V/75a First Class Motor Battery", "Die Hard", 3000, covers[0]);
        productSingleList.add(a);

        a = new ProductSingle("18V/76a Car Battery", "TTAO", 2500, covers[1]);
        productSingleList.add(a);

        a = new ProductSingle("12V/75a First Class ", "Britmarine", 5300, covers[2]);
        productSingleList.add(a);

        a = new ProductSingle("18V/60a Car Battery", "Blue Ray", 3300, covers[3]);
        productSingleList.add(a);

        a = new ProductSingle("12V/75a Motor Battery", "Exide", 3950, covers[4]);
        productSingleList.add(a);

        a = new ProductSingle("18V/60a Car Battery", "Varta", 4200, covers[5]);
        productSingleList.add(a);

        a = new ProductSingle("18V/76a Car Battery", "Bosch", 4000, covers[6]);
        productSingleList.add(a);

        a = new ProductSingle("12V/75a First Class", "Duracell", 3999, covers[7]);
        productSingleList.add(a);

        productSingleAdapter.notifyDataSetChanged();
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



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_cart:
                startActivity(new Intent(this, CartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
