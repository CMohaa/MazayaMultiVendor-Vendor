package com.mohaa.mazaya.dashboard.views;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.interfaces.OnProductClickListener;
import com.mohaa.mazaya.dashboard.models.Product;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsSnapAdapter extends RecyclerView.Adapter<ProductsSnapAdapter.ViewHolder>   {
    private List<Product> productsList;
    public Context context;


    private OnProductClickListener onProductClickListener;
    public ProductsSnapAdapter(List<Product> _productList , OnProductClickListener onProductClickListener )
    {
        this.productsList = _productList;

        this.onProductClickListener = onProductClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_mini_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Product products = productsList.get(i);

        if(products.getProduct_shortname() != null) {
            String name = products.getProduct_shortname();
            viewHolder.name.setText(name);

        }
        else
        {
            String name = products.getProduct_name();
            viewHolder.name.setText(name);
        }

        String desc = products.getProduct_desc();
        double price = products.getPrice();
        double discount = products.getDiscount();
        String img = products.getThumb_image();

        //viewHolder.name.setText(name);
        //viewHolder.desc.setText(desc);

        double new_price = price  - ((price *discount) / 100);

        if(discount > 0)
        {
            viewHolder.old_price.setVisibility(View.VISIBLE);
            viewHolder.Discount.setVisibility(View.VISIBLE);
            viewHolder.Discount.setText(String.valueOf(discount)+ "%"+ context.getResources().getString(R.string.off));
            viewHolder.old_price.setText(String.valueOf(price)+ " "+ context.getResources().getString(R.string.egypt_currency));
            viewHolder.old_price.setPaintFlags( viewHolder.old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        viewHolder.price.setText(String.valueOf(new_price)+ " "+ context.getResources().getString(R.string.egypt_currency));
        Glide.with(context)
                .load(img) // image url
                .apply(new RequestOptions()

                        .override(125, 125) // resizing
                        .centerCrop())
                .into(viewHolder.src);  // imageview object
        viewHolder.src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductClickListener.onProductClicked(products, i);

            }
        });

        //viewHolder.price.setText(price);//cant cast to float

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;

        private TextView name;
        private ImageView src;
        private TextView price;//discountedCardPrice
        private TextView old_price;//discountedCardPrice
        private TextView Discount;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            name = mView.findViewById(R.id.ProductName);
            src = mView.findViewById(R.id.ProductPoster);
            price = mView.findViewById(R.id.ProductPrice);

            old_price = mView.findViewById(R.id.ProductOldPrice);
            Discount = mView.findViewById(R.id.ProductDiscount);

        }
    }
}
