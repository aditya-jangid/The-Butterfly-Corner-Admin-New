package com.tbg.thebutterflycorneradmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //

    Button button,historyButton;
    FirebaseFirestore db;
    String docId;
    Map<String,Object> coupon;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        JSONObject jsonObject = null;
        String userId = null;
        String userEmail=null;
        String timeStamp=null;
        String couponId=null;
        if(scanResult!=null){
            Log.i("GetContents",scanResult.getContents());
            try {

                jsonObject=new JSONObject(scanResult.getContents());
                couponId=jsonObject.getString("couponId");
                userId=jsonObject.getString("userId");
                userEmail=jsonObject.getString("userEmail");
                Long tsLong = System.currentTimeMillis();
                timeStamp = tsLong.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                final String finalUserId = userId;
                final String finalCouponId = couponId;
                final String finalUserEmail = userEmail;
                final String finalTimeStamp = timeStamp;
                db.collection("coupons")
                        .whereEqualTo("couponId",jsonObject.get("couponId"))
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                docId=document.getId();
                                DocumentReference Ref = db.collection("coupons").document(docId);


                                Ref.update("used", "1")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Status", "DocumentSnapshot successfully updated!");
                                                coupon = new HashMap<>();
                                                coupon.put("userId", finalUserId);
                                                coupon.put("couponId", finalCouponId);
                                                coupon.put("userEmail", finalUserEmail);
                                                coupon.put("timestamp", finalTimeStamp);
                                                storeNewScannedCoupon(coupon);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Status", "Error updating document", e);
                                            }
                                        });
                            }
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void storeNewScannedCoupon(Map<String,Object> coupon){
        db.collection("scannedCoupons")
                .add(coupon)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("bc", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("bc", "Error adding document", e);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        historyButton=findViewById(R.id.historyButton);

        db=FirebaseFirestore.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator=new IntentIntegrator(MainActivity.this);
                intentIntegrator.initiateScan();
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,History.class);
                startActivity(intent);
            }
        });

    }
}
