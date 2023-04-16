package com.mohaa.mazaya.dashboard.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.interfaces.OnCataClickListener;
import com.mohaa.mazaya.dashboard.models.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>   {
    private List<Category> productsList;
    public Context context;


    private OnCataClickListener onCataClickListener;
    public CategoriesAdapter(List<Category> _tradersList , OnCataClickListener onCataClickListener )
    {
        this.productsList = _tradersList;

        this.onCataClickListener = onCataClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_category_item , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Category category = productsList.get(i);
        String userName =category.getName();
        int id =category.getId();
        viewHolder.setUserData(userName);
        viewHolder.category_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCataClickListener.onProductClicked(category, i);

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

        private ConstraintLayout category_panel;
        private StyleTextView StoriesUserName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;


            StoriesUserName = mView.findViewById(R.id.status_name);
            category_panel = mView.findViewById(R.id.category_panel);


        }
        public void setUserData(String name  ){



            StoriesUserName.setText(name);




        }



    }

}
