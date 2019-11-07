package com.tbg.thebutterflycorneradmin;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public ArrayList<Coupon> scannedCoupons;
    Context context;

    Adapter(Context context,ArrayList<Coupon> scannedCoupons){
        this.context=context;
        this.scannedCoupons=scannedCoupons;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        Coupon coupon = scannedCoupons.get(position);
        holder.email.setText("Email: "+scannedCoupons.get(position).getUserEmail());
        String timestamp=scannedCoupons.get(position).getTimestamp();
        String time = DateUtils.formatDateTime(context, Long.parseLong(timestamp), DateUtils.FORMAT_SHOW_TIME);
        String date =  DateUtils.formatDateTime(context, Long.parseLong(timestamp), DateUtils.FORMAT_SHOW_DATE);
        holder.date.setText("Date: "+date);
        holder.time.setText("Time: "+time);
    }

    @Override
    public int getItemCount() {
        return scannedCoupons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView email, date, time;

        public ViewHolder(View view) {
            super(view);
            email = (TextView) view.findViewById(R.id.email);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);
        }
    }
}
