package com.mohaa.mazaya.dashboard.views;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.interfaces.OnTraderClickListener;
import com.mohaa.mazaya.dashboard.models.Trader;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Pdt_ImageAdapter extends RecyclerView.Adapter<Pdt_ImageAdapter.ViewHolder> {
    private List<Uri> images_list;
    public Context context;


    public Pdt_ImageAdapter(List<Uri> _images_list )
    {
        this.images_list = _images_list;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Uri image = images_list.get(i);

        Glide.with(context)
                .load(image) // image url
                .apply(new RequestOptions()
                        .override(512, 512) // resizing
                        .centerCrop())
                .into(viewHolder.src);  // imageview object


        //viewHolder.price.setText(price);//cant cast to float

    }


    @Override
    public int getItemCount() {
        return images_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;

        private ImageView src;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            src = mView.findViewById(R.id.imageView);





        }




    }

}
