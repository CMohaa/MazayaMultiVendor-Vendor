package com.mohaa.mazaya.dashboard.Controllers.activities_products;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.mohaa.mazaya.dashboard.Controllers.BaseActivity;
import com.mohaa.mazaya.dashboard.Controllers.activities_popup.CommentsActivity;
import com.mohaa.mazaya.dashboard.Controllers.activities_popup.ImagePopupDetailsActivity;
import com.mohaa.mazaya.dashboard.Controllers.activities_traders.TraderActivity;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.ExpandableDescTextView;
import com.mohaa.mazaya.dashboard.Utils.GridSpacingItemDecoration;
import com.mohaa.mazaya.dashboard.Utils.ProductsUI;
import com.mohaa.mazaya.dashboard.Utils.multisnaprecyclerview.MultiSnapRecyclerView;
import com.mohaa.mazaya.dashboard.interfaces.OnProductClickListener;
import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.CommentAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.ProductsAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.VariantsAPIService;
import com.mohaa.mazaya.dashboard.manager.Call.Comments;
import com.mohaa.mazaya.dashboard.manager.Call.Products;
import com.mohaa.mazaya.dashboard.manager.Response.ProductResponse;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.models.Comment;
import com.mohaa.mazaya.dashboard.models.Product;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;
import com.mohaa.mazaya.dashboard.views.CommentsRecyclerAdapter;
import com.mohaa.mazaya.dashboard.views.ProductsSnapAdapter;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IndividualProductActivity extends BaseActivity implements OnProductClickListener {
    private int quantity = 1;
    TextView quantityProductPage;
    int selectedItemQuantity = 1;
    int selectedItemVariantId = 0;

    private ImageView minus, plus;
    Button btn_buy;
    Button btn_add_to_cart;
    private ImageView productimage;
    private TextView productname;
    private TextView productprice;
    //private TextView productdesc;

    //ProgressDialog
    private ProgressDialog mLoginProgress;


    private String products_id;
    private Product products;
    double discount;
    double new_price;

    private int ads_id;
    private String ads_name , ads_thumb_image , ads_desc , ads_trader;
    private double ads_discount , ads_price ;
    private long ads_barcode;

    private TextView products_desc , product_trader;


    Toolbar toolbar;
    private TextView count;

    private TextView desc_panel , spec_panel , products_details;
    private LinearLayout desc , spec;
    private ExpandableDescTextView desc_message;

    private ArrayList<Product> sponsored_list;
    private ArrayList<Product> suggest_list;
    private ArrayList<Product> viewed_list;

    private ProductsSnapAdapter sponsoredAdapter;
    private ProductsSnapAdapter suggestAdapter;
    private ProductsSnapAdapter viewedAdapter;

    //=========== Comments =============
    private RecyclerView commentsRecyclerView;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<Comment> commentsList;
    private LinearLayout newCommentContainer;
    private EditText commentEditText;
    private TextView see_all_comments;

    private RatingBar ratingBar;
    private TextView rating_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product);



        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
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


        init_main();
        initListeners();



    }



    private void init_main() {

        count = findViewById(R.id.count);


        products = (Product) getIntent().getExtras().getSerializable(ProductsUI.BUNDLE_PRODUCTS_LIST);
        if(products != null)
        {
            ads_thumb_image = getIntent().getStringExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE);
            //type = getIntent().getStringExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE);
            ads_id = products.getId();
            products_id = getIntent().getStringExtra(ProductsUI.BUNDLE_PRODUCTS_ID);
            ads_name = products.getProduct_name();
            ads_barcode = products.getBarcode();
            ads_discount = products.getDiscount();
            ads_price = products.getPrice();

            ads_desc = products.getProduct_desc();
            ads_trader = products.getMerchant_name();

            init();

        }
    }

    /**
     * method to initialize the listeners
     */
    private void initListeners() {

        // ExpandableListView on child click listener

        updateRecycleView();
        loadComments(1);

    }
    private void updateRecycleView() {
        //RecyclerView Firebase List
        commentsList = new ArrayList<>();
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList);
        commentsRecyclerView.setHasFixedSize(true);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
    private void init() {

        mLoginProgress = new ProgressDialog(this);
        //Firebase
        productimage = findViewById(R.id.productimage);
        productname = findViewById(R.id.productname);
        productprice = findViewById(R.id.productprice);

        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);

        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        newCommentContainer = findViewById(R.id.newCommentContainer);
        commentEditText = findViewById(R.id.commentEditText);
        see_all_comments = findViewById(R.id.see_all_comments);
        ratingBar = findViewById(R.id.ratingBar);
        rating_count = findViewById(R.id.rating_count);

        products_desc = findViewById(R.id.productdescription);
        product_trader = findViewById(R.id.product_trader);
        desc_message = findViewById(R.id.desc_message);
        products_details = findViewById(R.id.products_details);
        desc_panel = findViewById(R.id.desc_panel);
        spec_panel = findViewById(R.id.spec_panel);
        desc = findViewById(R.id.desc);
        spec = findViewById(R.id.spec);
        desc_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desc.setVisibility(View.VISIBLE);
                spec.setVisibility(View.GONE);

                desc.setBackground(getResources().getDrawable(R.drawable.border_blue));
                spec.setBackground(getResources().getDrawable(R.drawable.border_white));

            }
        });


        spec_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desc.setVisibility(View.GONE);
                spec.setVisibility(View.VISIBLE);

                desc.setBackground(getResources().getDrawable(R.drawable.border_white));
                spec.setBackground(getResources().getDrawable(R.drawable.border_blue));
            }
        });

        //productdesc = findViewById(R.id.productdesc);

        quantityProductPage = findViewById(R.id.quantityProductPage);
        quantityProductPage.setText("1");

        discount = ads_discount;
        new_price = ads_price  - ((ads_price *discount) / 100);


        fillComment(ads_desc , desc_message);
        //products_desc.setText(ads_desc);
        product_trader.setText(ads_trader);
        if(products.getRatecount() != 0) {
            ratingBar.setRating(products.getRate() / products.getRatecount());

        }
        rating_count.setText(String.valueOf(products.getRatecount()) + "  Reviews");
        product_trader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(IndividualProductActivity.this, TraderActivity.class);
                loginIntent.putExtra(ProductsUI.BUNDLE_TRADERS_ID, products.getMerchant_id());

                startActivity(loginIntent);
            }
        });
        //setValues(products);
        setDetails();
        btn_buy = findViewById(R.id.buy_now);
        btn_add_to_cart = findViewById(R.id.add_to_cart);

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        productimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndividualProductActivity.this, ImagePopupDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE, ads_thumb_image);
                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_NAME, ads_name);
                startActivity(intent);
            }
        });
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItemQuantity != 1) {
                    selectedItemQuantity--;
                    quantityProductPage.setText(String.valueOf(selectedItemQuantity));
                }
            }
        });

        // Increment Listener
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItemQuantity++;
                quantityProductPage.setText(String.valueOf(selectedItemQuantity));
            }
        });

        sponsored_list = new ArrayList<>();
        suggest_list = new ArrayList<>();
        viewed_list = new ArrayList<>();

        sponsoredAdapter = new ProductsSnapAdapter(sponsored_list , this);
        suggestAdapter = new ProductsSnapAdapter(suggest_list , this);
        viewedAdapter = new ProductsSnapAdapter(viewed_list , this);


        MultiSnapRecyclerView firstRecyclerView = (MultiSnapRecyclerView) findViewById(R.id.first_recycler_view);
        LinearLayoutManager firstManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        firstRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        firstRecyclerView.setItemAnimator(new DefaultItemAnimator());
        firstRecyclerView.setLayoutManager(firstManager);
        firstRecyclerView.setAdapter(sponsoredAdapter);


        MultiSnapRecyclerView secondRecyclerView =(MultiSnapRecyclerView)findViewById(R.id.second_recycler_view);
        LinearLayoutManager secondManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        secondRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        secondRecyclerView.setItemAnimator(new DefaultItemAnimator());
        secondRecyclerView.setLayoutManager(secondManager);
        secondRecyclerView.setAdapter(suggestAdapter);

        MultiSnapRecyclerView thirdRecyclerView =(MultiSnapRecyclerView)findViewById(R.id.third_recycler_view);
        LinearLayoutManager thirdManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        thirdRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        thirdRecyclerView.setItemAnimator(new DefaultItemAnimator());
        thirdRecyclerView.setLayoutManager(thirdManager);
        thirdRecyclerView.setAdapter(viewedAdapter);

        getSponsoredProducts(products.getCat_parent_id() , 1 , 3);
        getSuggestProducts(products.getCat_parent_id() , 1 , 2);
        getMostViewedProducts(products.getCat_parent_id() , 1 , 4);
        IncViewCount(products.getId());
        commentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(IndividualProductActivity.this, CommentsActivity.class);
                loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) products);

                startActivity(loginIntent);
            }
        });
        see_all_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(IndividualProductActivity.this, CommentsActivity.class);
                loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) products);

                startActivity(loginIntent);
            }
        });

    }

    private void getMostViewedProducts(int cat_parent_id, int pagenumbr, int sort_type) {
        ProductsAPIService service = RetrofitApi.retrofitRead().create(ProductsAPIService.class);
        Call<Products> call = service.getProductsAlike(cat_parent_id , pagenumbr , sort_type );
        call.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                viewed_list.addAll(response.body().getProducts());
                viewedAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {

            }
        });
    }
    private void getSuggestProducts(int cat_parent_id, int pagenumbr, int sort_type) {
        ProductsAPIService service = RetrofitApi.retrofitRead().create(ProductsAPIService.class);
        Call<Products> call = service.getProductsAlike(cat_parent_id , pagenumbr , sort_type );
        call.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                suggest_list.addAll(response.body().getProducts());
                suggestAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {

            }
        });
    }
    private void getSponsoredProducts(int cat_parent_id, int pagenumbr, int sort_type) {
        ProductsAPIService service = RetrofitApi.retrofitRead().create(ProductsAPIService.class);
        Call<Products> call = service.getProductsAlike(cat_parent_id , pagenumbr , sort_type );
        call.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                sponsored_list.addAll(response.body().getProducts());
                sponsoredAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {

            }
        });
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, r.getDisplayMetrics()));
    }
    private void fillComment(String desc , ExpandableDescTextView commentTextView) {
        Spannable contentString = new SpannableStringBuilder(getResources().getString(R.string.details) + "   " + desc);
        contentString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(IndividualProductActivity.this, R.color.highlight_text)),
                0, products_details.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        commentTextView.setText(contentString);

    }

    private void setDetails() {
        //Toast.makeText(this, thumb_image, Toast.LENGTH_SHORT).show();
        Glide.with(this)
                .load(ads_thumb_image) // image url
                .apply(new RequestOptions()


                        .override(200, 200) // resizing
                        .centerCrop())
                .into(productimage);  // imageview object
        productname.setText(ads_name);



        productprice.setText( String.valueOf(new_price)  +" LE");
        TextView Discount = findViewById(R.id.ProductDiscount);
        TextView old_price = findViewById(R.id.ProductOldPrice);
        if(discount > 0)
        {
           old_price.setVisibility(View.VISIBLE);
           Discount.setVisibility(View.VISIBLE);
           Discount.setText(String.valueOf(discount)+ "% OFF");
           old_price.setText(String.valueOf(ads_price)+ " LE");
           old_price.setPaintFlags( old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        //productdesc.setText(sellProducts.getName());

    }

    public void decrement(View view) {
        if (quantity > 1) {
            quantity--;
            quantityProductPage.setText(String.valueOf(quantity));
        }
    }

    public void increment(View view) {
        if (quantity < 500) {
            quantity++;
            quantityProductPage.setText(String.valueOf(quantity));
        } else {

        }
    }
    //check that product count must not exceed 500
    TextWatcher productcount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (quantityProductPage.getText().toString().equals("")) {
                quantityProductPage.setText("0");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //none
            if (Integer.parseInt(quantityProductPage.getText().toString()) >= 500) {

            }
        }

    };





    protected void onResume() {
        super.onResume();

        new CheckInternetConnection(this).checkConnection();

    }

    @Override
    public void onProductClicked(Product contact, int position) {
        Intent loginIntent = new Intent(IndividualProductActivity.this, IndividualProductActivity.class);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) contact);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE, contact.getThumb_image());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_ID, contact.getId());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE, contact.getType());
        startActivity(loginIntent);

    }
    public void IncViewCount(int product_id)
    {
        ProductsAPIService service = RetrofitApi.retrofitWrite().create(ProductsAPIService.class);

        Product user = new Product(product_id, 1);

        Call<ProductResponse> call = service.updateProductView(
                user.getId(),
                user.getView_count()

        );

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

            }
        });
    }
}
