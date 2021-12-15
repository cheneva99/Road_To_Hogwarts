package com.isep.roadtohogwarts.character.encyclopedia;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isep.roadtohogwarts.Character;
import com.isep.roadtohogwarts.OnItemListener;
import com.isep.roadtohogwarts.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CharacterRecyclerAdapter extends RecyclerView.Adapter<CharacterRecyclerAdapter.ViewHolder>  {

    private List<Character> characters;
    private OnItemListener onItemListener;



    public CharacterRecyclerAdapter(List<Character> characters,OnItemListener onItemListener) {
        this.characters = characters;
        this.onItemListener= onItemListener;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView characterName;
        private final ImageView characterCard;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener ) {
            super(itemView);
            this.characterName = itemView.findViewById(R.id.characterName);
            this.characterCard = itemView.findViewById(R.id.characterCard);
            itemView.setOnClickListener(this);
            this.onItemListener= onItemListener;

        }
        @Override
        public void onClick(View v) {
            this.onItemListener.onItemClick(getAdapterPosition());
        }

    }

    @NonNull
    @Override
    public CharacterRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.character_recyclerview_item,parent,false);

        return new CharacterRecyclerAdapter.ViewHolder(view,this.onItemListener);


    }

    @Override
    public void onBindViewHolder(@NonNull CharacterRecyclerAdapter.ViewHolder holder, int position) {
        holder.characterName.setText( characters.get(position).getName());
        try {
            String url = characters.get(position).getImageUrl();
            if(!url.equals("")) {

                Picasso.get().load(url).resize(150,200).into(holder.characterCard);
            }else{
                Picasso.get().load(R.drawable.noimage).resize(150,200).into(holder.characterCard);

            }

        }catch(Exception e){
            Log.d("image", "onBindViewHolder: "+e);



        }
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }



}



