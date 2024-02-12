package com.example.eventfinder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventfinder.Activity.EventActivity;
import com.example.eventfinder.Model.Event;
import com.example.eventfinder.Model.EventDetailsModel;
import com.example.eventfinder.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistHolderRetrofit> {

    EventDetailsModel resultsModels;
    private List<String> mImageIds = new ArrayList<>();
    Context mContext;

    public ArtistAdapter(EventDetailsModel resultsModels, Context mContext) {
        this.resultsModels = resultsModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ArtistAdapter.ArtistHolderRetrofit onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.artist_list_layout, parent, false);
        mContext = parent.getContext();

        return new ArtistAdapter.ArtistHolderRetrofit(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ArtistHolderRetrofit holder, int position) {
        int pos = position;
        holder.artistName.setText(resultsModels.getSpotify().get(position).getArtist().getName());
        int follower = Integer.parseInt(resultsModels.getSpotify().get(position).getArtist().getFollowers().replace(",","")) / 1000000;
        holder.artistFollower.setText(String.valueOf(follower));
        holder.artistSpotify.setPaintFlags(holder.artistSpotify.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.artistSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(resultsModels.getSpotify().get(pos).getArtist().getURL());
            }
        });
        holder.percentage.setText(String.valueOf(resultsModels.getSpotify().get(position).getArtist().getPopularity()));
        holder.percentageCircle.setProgress((int) resultsModels.getSpotify().get(position).getArtist().getPopularity());
        Glide.with(mContext).
                load(resultsModels.getSpotify().get(position).getAlbum().get(0).getImg()).placeholder(R.drawable.baseline_person_24).
                error(R.drawable.baseline_person_24).into(holder.popAl1);
        Glide.with(mContext).
                load(resultsModels.getSpotify().get(position).getAlbum().get(1).getImg()).placeholder(R.drawable.baseline_person_24).
                error(R.drawable.baseline_person_24).into(holder.popAl2);
        Glide.with(mContext).
                load(resultsModels.getSpotify().get(position).getAlbum().get(2).getImg()).placeholder(R.drawable.baseline_person_24).
                error(R.drawable.baseline_person_24).into(holder.popAl3);

        Glide.with(mContext).
                load(resultsModels.getSpotify().get(position).getArtist().getImg()).placeholder(R.drawable.baseline_person_24).
                error(R.drawable.baseline_person_24).into(holder.imageView);

//        for (int i = 0; i <= resultsModels.getSpotify().get(position).getAlbum().size(); i++) {
//            mImageIds.add(resultsModels.getSpotify().get(i).getAlbum().get(i).getImg());
//        }
    }

    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mContext.startActivity(browserIntent);
    }

    @Override
    public int getItemCount() {
        return resultsModels.getSpotify().size();
    }

    public class ArtistHolderRetrofit extends RecyclerView.ViewHolder {

        ImageView imageView, popAl1, popAl2, popAl3;
        TextView artistName, artistFollower, artistSpotify, percentage;
        CircularProgressIndicator percentageCircle;
        public ArtistHolderRetrofit(@NonNull View itemView) {
            super(itemView);
            popAl1 = itemView.findViewById(R.id.popAl1);
            popAl2 = itemView.findViewById(R.id.popAl2);
            popAl3 = itemView.findViewById(R.id.popAl3);
            imageView = itemView.findViewById(R.id.artistImageView);
            artistName = itemView.findViewById(R.id.artistName);
            artistFollower = itemView.findViewById(R.id.artistFollower);
            artistSpotify = itemView.findViewById(R.id.artistSpotify);
            percentage = itemView.findViewById(R.id.percentage);
            percentageCircle = itemView.findViewById(R.id.percentageCircle);
        }
    }
}
