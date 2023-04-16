package com.mohaa.mazaya.dashboard.Controllers.fragments_traders;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.EndlessRecyclerViewScrollListener;
import com.mohaa.mazaya.dashboard.Utils.reviewratings.RatingReviews;
import com.mohaa.mazaya.dashboard.interfaces.MyResultReceiver;
import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.CommentAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.ProductsAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.TraderAPIService;
import com.mohaa.mazaya.dashboard.manager.Call.Comments;
import com.mohaa.mazaya.dashboard.manager.Call.Traders;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.models.Comment;
import com.mohaa.mazaya.dashboard.views.CommentsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {


    public RatingFragment() {
        // Required empty public constructor
    }

    public MyResultReceiver resultreceiver;
    private RecyclerView commentsRecyclerView;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<Comment> commentsList;
    private RatingBar ratingbar;
    private int userPage = 1;
    private RatingReviews ratingReviews;
    private int traderID;
    private RatingBar ratingBar;
    private TextView rating_count;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        resultreceiver = (MyResultReceiver)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating , container , false);
        traderID  = resultreceiver.getResult();
        ratingbar=(RatingBar) view.findViewById(R.id.ratingBar);
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);
        ratingBar = view.findViewById(R.id.ratingBar);
        rating_count = view.findViewById(R.id.rating_count);
        updateRecycleView();
        loadComments(userPage);
        getTradersDetails(traderID);
        return view;
    }

    private void getTradersDetails(int traderID) {
        TraderAPIService service = RetrofitApi.retrofitRead().create(TraderAPIService.class);

        Call<Traders> call = service.getTraders(traderID);

        call.enqueue(new Callback<Traders>() {
            @Override
            public void onResponse(Call<Traders> call, Response<Traders> response) {
                if(response.body().getTraders().get(0).getRatecount() != 0) {
                    ratingBar.setRating(response.body().getTraders().get(0).getRate() / response.body().getTraders().get(0).getRatecount());

                }
                rating_count.setText(String.valueOf(response.body().getTraders().get(0).getRatecount()) + "  Reviews");
            }

            @Override
            public void onFailure(Call<Traders> call, Throwable t) {

            }
        });

    }

    private void updateRecycleView() {
        //RecyclerView Firebase List
        commentsList = new ArrayList<>();

        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);
        commentsRecyclerView.setHasFixedSize(true);
        commentsRecyclerView.setLayoutManager(mLayoutManager);
        commentsRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                loadComments(userPage);
            }
        });
        commentsRecyclerView.setAdapter(commentsRecyclerAdapter);
        //mUserDatabase.keepSynced(true);
    }
    private void loadComments(int pageno)
    {
        CommentAPIService service = RetrofitApi.retrofitRead().create(CommentAPIService.class);
        Call<Comments> call = service.getAllComments(traderID , pageno);
        call.enqueue(new Callback<Comments>() {
            @Override
            public void onResponse(Call<Comments> call, Response<Comments> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);
                //Toast.makeText(CommentsActivity.this, response.body().getComments().get(1).getContent(), Toast.LENGTH_SHORT).show();
                commentsList.addAll(response.body().getComments());
                commentsRecyclerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<Comments> call, Throwable t) {

            }
        });
    }

}
