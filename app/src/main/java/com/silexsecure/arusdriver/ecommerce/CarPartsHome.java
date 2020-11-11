package com.silexsecure.arusdriver.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.adapters.AlbumsAdapter;
import com.silexsecure.arusdriver.adapters.RecyclerViewAdapter;
import com.silexsecure.arusdriver.model.Album;
import com.silexsecure.arusdriver.model.Brands;
import com.silexsecure.arusdriver.util.GridSpacingItemDecoration;
import com.silexsecure.arusdriver.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CarPartsHome extends AppCompatActivity {

    private RecyclerView recyclerView, brandsRecyclerView;
    private AlbumsAdapter adapter;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Album> albumList;
    private List<Brands> brandsList;
    TextView textCartItemCount;
    int mCartItemCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_parts_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Empire Store");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        initCollapsingToolbar();
        mCartItemCount = Utils.getCartCount(this);

        recyclerView = findViewById(R.id.recycler_view);
        brandsRecyclerView = findViewById(R.id.brands_recyclerview);

        albumList = new ArrayList<>();
        brandsList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);
        recyclerViewAdapter = new RecyclerViewAdapter(this, brandsList);

        initRecyclerView();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        prepareBrands();

//        try {
//            Glide.with(this).load(R.drawable.car_parts_home).into((ImageView) findViewById(R.id.backdrop));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Empire Store");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
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
                R.drawable.ignition};

        Album a = new Album("Battery", 13, covers[0]);
        albumList.add(a);

        a = new Album("Shock Absorber", 8, covers[1]);
        albumList.add(a);

        a = new Album("Sensor", 11, covers[2]);
        albumList.add(a);

        a = new Album("Ignition Module", 12, covers[3]);
        albumList.add(a);

        a = new Album("Break pads", 14, covers[4]);
        albumList.add(a);

        a = new Album("Top Gasket", 1, covers[5]);
        albumList.add(a);

        a = new Album("Tires", 11, covers[6]);
        albumList.add(a);

        a = new Album("Oil Filter", 14, covers[7]);
        albumList.add(a);

        a = new Album("Door Mirror", 11, covers[8]);
        albumList.add(a);

        a = new Album("Greatest Hits", 17, covers[9]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * Adding few albums for testing
     */
    private void prepareBrands() {
        int[] covers = new int[]{
                R.drawable.toyota_logo,
                R.drawable.honda_logo,
                R.drawable.peugeot_logo,
                R.drawable.lexus_logo,
                R.drawable.nissan_logo,
                R.drawable.innoson_logo,
                R.drawable.acura_logo,
                R.drawable.hyundai
        };

        Brands a = new Brands("Toyota", covers[0]);
        brandsList.add(a);

        a = new Brands("Honda", covers[1]);
        brandsList.add(a);

        a = new Brands("Sensor", covers[2]);
        brandsList.add(a);

        a = new Brands("Peugeot", covers[3]);
        brandsList.add(a);

        a = new Brands("Break pads", covers[4]);
        brandsList.add(a);

        a = new Brands("Top Gasket", covers[5]);
        brandsList.add(a);

        a = new Brands("Tires", covers[6]);
        brandsList.add(a);

        a = new Brands("Oil Filter", covers[7]);
        brandsList.add(a);


        recyclerViewAdapter.notifyDataSetChanged();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_cart) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
//        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        brandsRecyclerView = findViewById(R.id.brands_recyclerview);
        brandsRecyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, brandsList);
        brandsRecyclerView.setAdapter(adapter);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}