package com.example.eventfinder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventfinder.Activity.EventActivity;
import com.example.eventfinder.Activity.EventDetailsActivity;
import com.example.eventfinder.Model.Event;
import com.example.eventfinder.R;
import com.google.gson.Gson;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsHolderRetrofit> {

    Gson gson;
    List<Event> resultsModels;
    Context mContext;
    private final ItemClicks itemClicks;

    public ResultsAdapter(List<Event> resultsModels, Context mContext, ItemClicks itemClicks) {
        this.resultsModels = resultsModels;
        this.mContext = mContext;
        this.itemClicks = itemClicks;
    }

    @NonNull
    @Override
    public ResultsAdapter.ResultsHolderRetrofit onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_layout, parent, false);
        mContext = parent.getContext();
        gson = new Gson();
        return new ResultsHolderRetrofit(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsAdapter.ResultsHolderRetrofit holder, int position) {
        Log.e("name", resultsModels.get(position).getName());
        Event event = resultsModels.get(position);
        String id = resultsModels.get(position).getId();
        int pos = position;
        holder.name.setText(event.getName());
        holder.date.setText(event.getLocalDate());
        holder.category.setText(event.getGenre());
        holder.time.setText(event.getLocalTime());
        holder.stadium.setText(event.getVenue());
        Glide.with(mContext).
                load(event.getImgUrl()).placeholder(R.drawable.baseline_person_24).
                error(R.drawable.baseline_person_24).into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, EventDetailsActivity.class);
                i.putExtra("id", id);
                i.putExtra("event", gson.toJson(event));
                mContext.startActivity(i);
            }
        });

        if(event.isFavorite()){
            holder.fav.setImageDrawable(holder.fav.getContext().getDrawable(R.drawable.favorite_24));
        }else{
            holder.fav.setImageDrawable(holder.fav.getContext().getDrawable(R.drawable.favorite_border_24));
        }

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!event.isFavorite()){
                    itemClicks.likeClick(pos);
                }else{
                    itemClicks.dislikeClick(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultsModels.size();
    }

    public class ResultsHolderRetrofit extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardView cardView;
        ImageButton fav;
        TextView name, date, time, stadium, category;
        public ResultsHolderRetrofit(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.imageView);
            fav = itemView.findViewById(R.id.fav);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            stadium = itemView.findViewById(R.id.staduim);
            category = itemView.findViewById(R.id.category);

            name.setSelected(true);

            fav.setBackgroundResource(0);
        }
    }

    public interface ItemClicks {
        void likeClick(int position);

        void dislikeClick(int position);
    }
}
