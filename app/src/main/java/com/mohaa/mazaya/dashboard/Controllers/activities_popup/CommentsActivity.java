package com.mohaa.mazaya.dashboard.Controllers.activities_popup;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mohaa.mazaya.dashboard.Controllers.BaseActivity;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.EndlessRecyclerViewScrollListener;
import com.mohaa.mazaya.dashboard.Utils.ProductsUI;
import com.mohaa.mazaya.dashboard.Utils.reviewratings.RatingReviews;
import com.mohaa.mazaya.dashboard.helper.SharedPrefManager;
import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.CommentAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.ProductsAPIService;
import com.mohaa.mazaya.dashboard.manager.Call.Comments;
import com.mohaa.mazaya.dashboard.manager.Response.CommentResponse;
import com.mohaa.mazaya.dashboard.manager.Response.ProductResponse;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.models.Comment;
import com.mohaa.mazaya.dashboard.models.Product;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;
import com.mohaa.mazaya.dashboard.views.CommentsRecyclerAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends BaseActivity {

    //=========== Comments =============
    private RecyclerView commentsRecyclerView;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<Comment> commentsList;
    private EditText commentEditText;
    private Button sendButton;
    private RatingBar ratingbar;
    private Product products;
    private int userPage = 1;
    private RatingReviews ratingReviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide Title
        TextView titleToolbar = findViewById(R.id.appname);
        titleToolbar.setVisibility(View.GONE);

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        products = (Product) getIntent().getExtras().getSerializable(ProductsUI.BUNDLE_PRODUCTS_LIST);
        init();
        initListeners();


    }




    private void init() {


        ratingbar=(RatingBar)findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.commentEditText);
        sendButton = findViewById(R.id.sendButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
    }
    /**
     * method to initialize the listeners
     */
    private void initListeners() {

        // ExpandableListView on child click listener
        ratingbar.setRating(5);
        updateRecycleView();
        loadComments(userPage);
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sendButton.setEnabled(charSequence.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingbar.getRating()!= 0 && commentEditText.getText()!= null)
                {
                    sendComment();
                }

            }
        });

    }

    private void sendComment() {

        showProgressDialog();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int owner_id = SharedPrefManager.getInstance(this).getUser().getId();
        String owner_name = SharedPrefManager.getInstance(this).getUser().getName();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        CommentAPIService api = RetrofitApi.retrofitWrite().create(CommentAPIService.class);

        //Defining the trader object as we need to pass it with the call
        Comment trader = new Comment(owner_id , owner_name, commentEditText.getText().toString(), products.getId() , products.getMerchant_id() , (int)ratingbar.getRating() ,timestamp.getTime());

        // Log.d("helloo",encodedImage +"   >>"+imgname);
        //Log.d("imggggg","   >>"+imgname);

        //defining the call
        Call<CommentResponse> call = api.createComment(
                trader.getUsr_id(),
                trader.getUsr_name(),
                trader.getContent(),
                trader.getPdt_id(),
                trader.getMerchant_id(),
                trader.getRate(),
                trader.getCreated_at()


        );
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                IncRate(products.getId() , (int)ratingbar.getRating());
                hideProgressDialog();
                Toast.makeText(CommentsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(CommentsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateRecycleView() {
        //RecyclerView Firebase List
        commentsList = new ArrayList<>();

        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL, false);
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
        Call<Comments> call = service.getComments(products.getId() , pageno);
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
    public void IncRate(int product_id , int rate)
    {
        ProductsAPIService service = RetrofitApi.retrofitWrite().create(ProductsAPIService.class);

        Product user = new Product(product_id ,products.getMerchant_id() , rate , 1);

        Call<ProductResponse> call = service.updateRate(
                user.getId(),
                user.getMerchant_id(),
                user.getRate(),
                user.getRatecount()

        );

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                onBackPressed();
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

            }
        });
    }
}
