package com.example.cleaningconsultancy.ui.order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleaningconsultancy.CleanerDetail;
import com.example.cleaningconsultancy.R;
import com.example.cleaningconsultancy.ui.search.CleanerAdapter;
import com.example.cleaningconsultancy.ui.search.SearchViewModel;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<OrderHistoryViewModel> orders; //change to model
    Context context;
    public OrderAdapter(ArrayList<OrderHistoryViewModel> orders, Context context){
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.order_list,parent,false);//create layout
        return new OrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        final OrderHistoryViewModel order = orders.get(position);
        final String name = order.getName();
        final String price = order.getPrice();
        final String date = order.getDate();
        final String time = order.getTime();

        holder.cleanerName.setText(holder.cleanerName.getText().toString() +" "+name);
        holder.price.setText(holder.price.getText().toString() +" " + price);
        holder.date.setText(holder.date.getText().toString() + " " + date);
        holder.time.setText(holder.time.getText().toString() + " "+ time);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{

        TextView cleanerName, price, date, time;
        LinearLayout layout_order;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            cleanerName = (TextView)itemView.findViewById(R.id.cleanerName);
            price = (TextView)itemView.findViewById(R.id.price);
            date = (TextView)itemView.findViewById(R.id.date);
            time = (TextView)itemView.findViewById(R.id.time);
            layout_order = (LinearLayout)itemView.findViewById(R.id.layout_orders);
        }
    }
}
