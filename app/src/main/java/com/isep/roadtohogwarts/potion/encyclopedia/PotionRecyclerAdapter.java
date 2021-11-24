package com.isep.roadtohogwarts.potion.encyclopedia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isep.roadtohogwarts.Potion;
import com.isep.roadtohogwarts.R;

import java.util.List;

public class PotionRecyclerAdapter extends RecyclerView.Adapter<PotionRecyclerAdapter.ViewHolder>  {

    List<Potion> potions;


    public PotionRecyclerAdapter(List<Potion> potions) {
        this.potions = potions;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookDescription;
        TextView bookVolume;

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);
            bookVolume = itemView.findViewById(R.id.bookVolume);
            bookDescription = itemView.findViewById(R.id.bookDescription);
        }

    }

    @NonNull
    @Override
    public PotionRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.potion_recyclerview_item,parent,false);

        return new PotionRecyclerAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull PotionRecyclerAdapter.ViewHolder holder, int position) {
        holder.bookVolume.setText( potions.get(position).getName());
        holder.bookDescription.setText( potions.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return potions.size();
    }


}



