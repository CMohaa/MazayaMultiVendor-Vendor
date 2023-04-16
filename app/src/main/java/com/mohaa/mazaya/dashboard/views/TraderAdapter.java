package com.mohaa.mazaya.dashboard.views;

import android.content.Context;
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

public class TraderAdapter extends RecyclerView.Adapter<TraderAdapter.ViewHolder> {
    private List<Trader> traderList;
    public Context context;

    private OnTraderClickListener onTraderClickListener;
    public TraderAdapter(List<Trader> _traderList, OnTraderClickListener onTraderClickListener )
    {
        this.traderList = _traderList;
        this.onTraderClickListener = onTraderClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trader_card_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Trader trader = traderList.get(i);
        String name = trader.getMerchant_name();
        String desc = trader.getMerchant_desc();
        String location = trader.getLocation();
        String img = trader.getThumb_image();

        //viewHolder.name.setText(name);
        //viewHolder.desc.setText(desc);


        viewHolder.name.setText(name);
        viewHolder.location.setText(location);
        Glide.with(context)
                .load(img) // image url
                .apply(new RequestOptions()
                        .override(512, 512) // resizing
                        .centerCrop())
                .into(viewHolder.src);  // imageview object
        viewHolder.src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTraderClickListener.onTraderClicked(trader, i);

            }
        });

        //viewHolder.price.setText(price);//cant cast to float

    }


    @Override
    public int getItemCount() {
        return traderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;
        private CardView cardView;
        private TextView name;
        private ImageView src;
        private TextView location;//discountedCardPrice


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            name = mView.findViewById(R.id.TraderName);
            src = mView.findViewById(R.id.TraderPoster);
            location = mView.findViewById(R.id.TraderLocation);




        }




    }

}
