package com.example.cleaningconsultancy.ui.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleaningconsultancy.CleanerDetail;
import com.example.cleaningconsultancy.R;

import java.util.ArrayList;

public class CleanerAdapter  extends RecyclerView.Adapter<CleanerAdapter.CleanerViewHolder> {

    private ArrayList<SearchViewModel> cleaner; //change to model
    Context context;
    public CleanerAdapter(ArrayList<SearchViewModel> cleaner, Context context){
        this.cleaner = cleaner;
        this.context = context;
    }

    @NonNull
    @Override
    public CleanerAdapter.CleanerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.cleaner_list,parent,false);//create layout
        return new CleanerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CleanerAdapter.CleanerViewHolder holder, int position) {
        final SearchViewModel selectCleaner = cleaner.get(position);
        final String name = selectCleaner.getName();
        final String phoneNumber = selectCleaner.getPhoneNumber();
        final String price = selectCleaner.getPrice().toString();
        final String description = selectCleaner.getDescription();
        final String availableFrom = selectCleaner.getAvailableFrom();
        final String availableTo = selectCleaner.getAvailableTo();
        final String currentUserId = selectCleaner.getCurrentUserId();

        holder.txtName.setText(name);

        holder.layout_cleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CleanerDetail.class);
                intent.putExtra("currentUserId", currentUserId);
                intent.putExtra("name", name);
                intent.putExtra("price", price);
                intent.putExtra("description", description);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("availableFrom", availableFrom);
                intent.putExtra("availableTo", availableTo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cleaner.size();
    }

    public class CleanerViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtType;
        LinearLayout layout_cleaner;

        public CleanerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.cleanerName);
            layout_cleaner = (LinearLayout)itemView.findViewById(R.id.layout_cleaner);
        }
    }
}
