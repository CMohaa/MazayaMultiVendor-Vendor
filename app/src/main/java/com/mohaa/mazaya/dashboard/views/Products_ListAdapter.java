package com.mohaa.mazaya.dashboard.views;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.interfaces.OnProductClickListener;
import com.mohaa.mazaya.dashboard.models.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Products_ListAdapter extends RecyclerView.Adapter<Products_ListAdapter.ViewHolder>   {
    private List<Product> productList;
    public Context context;


    private OnProductClickListener onProductClickListener;
    public Products_ListAdapter(List<Product> _tradersList , OnProductClickListener onProductClickListener )
    {
        this.productList = _tradersList;

        this.onProductClickListener = onProductClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_card_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Product product = productList.get(i);
        String name = product.getProduct_name();
        String desc = product.getProduct_desc();
        double price = product.getPrice();
        double discount = product.getDiscount();
        String img = product.getThumb_image();
        int sp = product.getSponsored();
        //viewHolder.name.setText(name);
        //viewHolder.desc.setText(desc);

        double new_price = price  - ((price *discount) / 100);
        viewHolder.name.setText(name);
        viewHolder.Discount.setText(context.getResources().getString(R.string.best_value));
        if(discount > 0)
        {

            viewHolder.old_price.setVisibility(View.VISIBLE);
            viewHolder.Discount.setText(String.valueOf(discount)+ "% OFF");
            viewHolder.old_price.setText(String.valueOf(price)+ " LE");
            viewHolder.old_price.setPaintFlags( viewHolder.old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        //viewHolder.sponsored.setVisibility(View.GONE);
        if(sp == 1)
        {
            viewHolder.sponsored.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.sponsored.setVisibility(View.INVISIBLE);
        }

        viewHolder.price.setText(String.valueOf(new_price)+ " LE");
        Glide.with(context)
                .load(img) // image url
                .apply(new RequestOptions()
                        .override(512, 512) // resizing
                        .centerCrop())
                .into(viewHolder.src);  // imageview object
        viewHolder.src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductClickListener.onProductClicked(product, i);

            }
        });

        //viewHolder.price.setText(price);//cant cast to float

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;

        private TextView name;
        private ImageView src;
        private TextView price;//discountedCardPrice
        private TextView old_price;//discountedCardPrice
        private TextView sponsored;//discountedCardPrice

        private TextView Discount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            name = mView.findViewById(R.id.ProductName);
            src = mView.findViewById(R.id.ProductPoster);
            price = mView.findViewById(R.id.ProductPrice);
            old_price = mView.findViewById(R.id.ProductOldPrice);
            sponsored = mView.findViewById(R.id.ProductSponsored);
            Discount = mView.findViewById(R.id.ProductDiscount);



        }




    }

}
