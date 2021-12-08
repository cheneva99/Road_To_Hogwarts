package com.isep.roadtohogwarts.spell.encyclopedia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isep.roadtohogwarts.Spell;
import com.isep.roadtohogwarts.R;

import java.util.List;

public class SpellRecyclerAdapter extends RecyclerView.Adapter<SpellRecyclerAdapter.ViewHolder>  {

    List<Spell> spells;

    public SpellRecyclerAdapter(List<Spell> spells) {
        this.spells = spells;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView spellType;
        TextView spellName;
        TextView spellDescription;
        Button moreButton;
        boolean isExpanded = false;

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);
            spellName = itemView.findViewById(R.id.spellName);
            spellType = itemView.findViewById(R.id.spellType);
            spellDescription = itemView.findViewById(R.id.spellDescription);
            moreButton = itemView.findViewById(R.id.expandButton);
        }
    }

    @NonNull
    @Override
    public SpellRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spell_recyclerview_item,parent,false);

        return new SpellRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpellRecyclerAdapter.ViewHolder holder, int position) {
        holder.spellName.setText( spells.get(position).getName());

        String spellType = spells.get(position).getType();
        if (spellType != "null") {
            holder.spellType.setText(spellType);
        }

        if (spells.get(position).getDescription().equals("null")) {
            holder.moreButton.setVisibility(View.INVISIBLE);

        } else {

            holder.moreButton.setOnClickListener(view -> {
                if (holder.isExpanded) {
                    holder.spellDescription.setText("");
                    holder.moreButton.setText("More");
                } else {
                    holder.spellDescription.setText(spells.get(position).getDescription());
                    holder.moreButton.setText("Less");
                }
                holder.isExpanded = !holder.isExpanded;
            });

        }
    }

    @Override
    public int getItemCount() {
        return spells.size();
    }

}



