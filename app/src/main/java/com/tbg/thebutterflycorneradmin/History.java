package com.tbg.thebutterflycorneradmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class History extends AppCompatActivity {

    ArrayList<Coupon> scannedCoupons;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db=FirebaseFirestore.getInstance();
        scannedCoupons=new ArrayList<>();
        getScannedCoupons();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        adapter=new Adapter(this,scannedCoupons);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }


    public void getScannedCoupons(){
        db.collection("scannedCoupons")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> coupon=document.getData();
                                scannedCoupons.add(new Coupon(String.valueOf(coupon.get("userEmail")),String.valueOf(coupon.get("timestamp")),String.valueOf(coupon.get("userId")),String.valueOf(coupon.get("couponId"))));

                            }
                            Log.i("xxxxx", String.valueOf(scannedCoupons.size())) ;
                            Log.i("xxxxxxx",scannedCoupons.get(0).getCouponId()+"  "+scannedCoupons.get(0).getUserEmail());
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("bhenchod", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
