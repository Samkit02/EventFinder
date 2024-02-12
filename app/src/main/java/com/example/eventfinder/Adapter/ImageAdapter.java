package com.example.eventfinder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventfinder.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> mImageIds;
    Context mContext;

    public ImageAdapter(List<String> imageIds, Context mContext) {
        mImageIds = imageIds;
        this.mContext = mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageItemView);
        }
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        Glide.with(mContext).
                load(mImageIds.get(position)).placeholder(R.drawable.baseline_person_24).
                error(R.drawable.baseline_person_24).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImageIds.size();
    }
}

