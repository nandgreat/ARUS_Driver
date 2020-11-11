package com.silexsecure.arusdriver.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.silexsecure.arusdriver.model.ProductSale;

import java.util.ArrayList;

public class FirebaseHelper {

    DatabaseReference db;
    Boolean saved=null;
    ArrayList<ProductSale> productSales=new ArrayList<>();

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //SAVE
    public Boolean save(ProductSale productSale)
    {
        if(productSale==null)
        {
            saved=false;
        }else {

            try
            {
                db.child("orders").push().setValue(productSale);
                saved=true;
            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }

        }

        return saved;
    }

    //READ
    public ArrayList<ProductSale> retrieve()
    {
        db.child("orders").child("").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return productSales;
    }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        productSales.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
//            String name=ds.getValue(ProductSale.class).getName();
            productSales.add(ds.getValue(ProductSale.class));
        }
    }

}