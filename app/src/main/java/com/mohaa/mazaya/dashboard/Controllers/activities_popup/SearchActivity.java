package com.mohaa.mazaya.dashboard.Controllers.activities_popup;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mohaa.mazaya.dashboard.Controllers.BaseActivity;
import com.mohaa.mazaya.dashboard.Controllers.activities_products.IndividualProductActivity;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.EndlessRecyclerViewScrollListener;
import com.mohaa.mazaya.dashboard.Utils.GridSpacingItemDecoration;
import com.mohaa.mazaya.dashboard.Utils.ProductsUI;
import com.mohaa.mazaya.dashboard.Utils.searchbar.MaterialSearchBar;
import com.mohaa.mazaya.dashboard.interfaces.OnProductClickListener;
import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.ProductsAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.VariantsAPIService;
import com.mohaa.mazaya.dashboard.manager.Call.Products;
import com.mohaa.mazaya.dashboard.manager.Call.Variants;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.models.Product;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;
import com.mohaa.mazaya.dashboard.views.FilterItemListAdapter;
import com.mohaa.mazaya.dashboard.views.Products_ListAdapter;
import com.mohaa.mazaya.dashboard.views.SortItemListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This Activity class is used to display a grid of search results for recipe searches.
 */
public class SearchActivity extends BaseActivity implements MaterialSearchBar.OnSearchActionListener, OnProductClickListener {

    RelativeLayout sort, filter;
    TextView sortByText;
    String[] sortByArray = {"Most Recent", "Most Orders", "Top Rated", "Most Viewed"};
    int sortById = 0;
    List<String> departmentFilter = new ArrayList<>();
    List<String> companyFilter = new ArrayList<>();
    List<String> packFilter = new ArrayList<>();
    List<String> statusFilter = new ArrayList<>();

    MaterialSearchBar searchBar;
    private DrawerLayout drawer;

    private ArrayList<Product> products_list;
    private RecyclerView recList;
    private StaggeredGridLayoutManager products_staggeredGridLayoutManager;
    private Products_ListAdapter products_listAdapter;
    Toolbar toolbar;
    private RelativeLayout search_panel ;
    private String searched_TXT;

    private int userPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


        drawer = findViewById(R.id.drawer_layout);
        search_panel = findViewById(R.id.search_panel);

        sort = findViewById(R.id.sortLay);
        filter = findViewById(R.id.filterLay);
        sortByText = findViewById(R.id.sortBy);
        setSortListener();
        setFilterListener();
        sortByText.setText(sortByArray[0]);
        recList = (RecyclerView) findViewById(R.id.recyclerView);
        products_list = new ArrayList<>();
        products_listAdapter = new Products_ListAdapter(products_list , this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recList.setLayoutManager(mLayoutManager);
        recList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recList.setItemAnimator(new DefaultItemAnimator());
        recList.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                getAllPosts(getSearched_TXT()  , userPage , sortByText.getText().toString());
            }
        });
        recList.setAdapter(products_listAdapter);

        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);

        Log.d("LOG_TAG", getClass().getSimpleName() + ": text " + searchBar.getText());
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!searchBar.getText().equals("")) {//&& type != null
                    //String A = Character.toUpperCase(text.charAt(0)) + text.substring(1);
                    //Toast.makeText(this, "" + A, Toast.LENGTH_SHORT).show();
                    setSearched_TXT(searchBar.getText());
                    products_list.clear();
                    userPage = 1;
                    products_listAdapter.notifyDataSetChanged();
                    getAllPosts(getSearched_TXT()  , userPage , sortByText.getText().toString());


                }
                else {
                    userPage = 1;
                    products_list.clear();
                    products_listAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });






        //loadProducts();
    }
    public void getAllPosts(String query , int userPage , String sortById)
    {


        ProductsAPIService service = RetrofitApi.retrofitRead().create(ProductsAPIService.class);
        Call<Products> call = service.getProducts(query , userPage , 1 );
        // String[] sortByArray = {"Most Recent", "Most Orders", "Most Shares", "Most Viewed"};
        switch (sortById) {
            case "Most Recent":

                call = service.getProducts(query , userPage , 1 );
                break;

            case "Most Orders":

                call = service.getProducts(query , userPage , 2 );
                break;

            case "Top Rated":

                call = service.getProducts(query , userPage , 3 );
                break;
            case "Most Viewed":

                call = service.getProducts(query , userPage , 4 );
                break;


            default:
            {
                call = service.getProducts(query , userPage , 1 );
                break;
            }
        }





        call.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                products_list.addAll(response.body().getProducts());
                products_listAdapter.notifyDataSetChanged();



            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {

            }
        });

    }
    //Responsible For Adding the 3 tabs : Camera  , Home , Messages
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    protected void onResume() {
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(String text) {
        if(!text.equals("")) {//&& type != null
            //String A = Character.toUpperCase(text.charAt(0)) + text.substring(1);
            //Toast.makeText(this, "" + A, Toast.LENGTH_SHORT).show();
            setSearched_TXT(text);
            products_list.clear();
            userPage = 1;
            products_listAdapter.notifyDataSetChanged();
            getAllPosts(getSearched_TXT()  , userPage , sortByText.getText().toString());
            recList.setVisibility(View.VISIBLE);

        }
        else {
            userPage = 1;
            products_list.clear();
            products_listAdapter.notifyDataSetChanged();
            recList.setVisibility(View.GONE);
        }
    }


    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_SPEECH:
                break;
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                break;
        }
    }



    private void load(String query) {


    }
    static String convert(String str)
    {

        // Create a char array of given String
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {

            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char)(ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')

                // Convert into Lower-Case
                ch[i] = (char)(ch[i] + 'a' - 'A');
        }

        // Convert the char array to equivalent String
        String st = new String(ch);
        return st;
    }

    @Override
    public void onProductClicked(Product contact, int position) {
        Intent loginIntent = new Intent(SearchActivity.this, IndividualProductActivity.class);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) contact);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE, contact.getThumb_image());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_ID, contact.getId());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE, contact.getType());
        startActivity(loginIntent);
    }

    public String getSearched_TXT() {
        return searched_TXT;
    }

    public void setSearched_TXT(String searched_TXT) {
        this.searched_TXT = searched_TXT;
    }
    // Set Sort Listener
    private void setSortListener() {
        sort.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog
                final Dialog dialog = new Dialog(SearchActivity.this);
                dialog.setContentView(R.layout.sort_listview);

                ListView listView = dialog.findViewById(R.id.sort_listview);
                listView.setAdapter(new SortItemListAdapter(SearchActivity.this, sortByArray, sortById));
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();

                // ListView Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        sortById = i;
                        sortByText.setText(sortByArray[sortById]);

                        // Reload Products List
                        userPage = 1;
                        products_list.clear();
                        products_listAdapter.notifyDataSetChanged();
                        getAllPosts( getSearched_TXT() , 1, sortByText.getText().toString());
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    private void setFilterListener() {
        filter.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog

                final Dialog dialog = new Dialog(SearchActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filterlayout);

                final List<String> department = getVarients(1);
                final List<String> company = getVarients(2);
                final List<String> pack = getVarients(3);
                final List<String> status= getVarients(4);

                // Add into hash map
                HashMap<String, List<String>> listHashMap = new HashMap<>();
                listHashMap.put("department", department);
                listHashMap.put("company", company);
                listHashMap.put("pack", pack);
                listHashMap.put("status", status);

                // Add Headers
                List<String> headers = new ArrayList<>();
                headers.add("department");
                headers.add("company");
                headers.add("pack");
                headers.add("status");

                final ExpandableListView listView = dialog.findViewById(R.id.expandableList);
                final FilterItemListAdapter filterItemListAdapter = new FilterItemListAdapter(SearchActivity.this, headers, listHashMap, departmentFilter, companyFilter, packFilter, statusFilter);
                listView.setAdapter(filterItemListAdapter);
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();

                // ListView Click Listener
                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                        switch (groupPosition) {
                            case 0: // department
                                if (!departmentFilter.contains(department.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    departmentFilter.add(department.get(childPosition));
                                } else {
                                    departmentFilter.remove(department.get(childPosition));
                                }
                                break;

                            case 1: // occasion
                                if (!companyFilter.contains(company.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    companyFilter.add(company.get(childPosition));
                                } else {
                                    companyFilter.remove(company.get(childPosition));
                                }
                                break;
                            case 2: // material
                                if (!packFilter.contains(pack.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    packFilter.add(pack.get(childPosition));
                                } else {
                                    packFilter.remove(pack.get(childPosition));
                                }
                                break;
                            case 3: // occasion
                                if (!statusFilter.contains(status.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    statusFilter.add(status.get(childPosition));
                                } else {
                                    statusFilter.remove(status.get(childPosition));
                                }
                                break;
                        }
                        filterItemListAdapter.notifyDataSetChanged();
                        return false;
                    }
                });

                // Filter Apply Button Click
                Button apply = dialog.findViewById(R.id.apply);
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (companyFilter.size() > 0) {
                            getAllPosts_Filter("company", companyFilter.get(0));
                        } else if (departmentFilter.size() > 0) {
                            getAllPosts_Filter("department", departmentFilter.get(0));
                        } else if (packFilter.size() > 0) {
                            getAllPosts_Filter("pack", packFilter.get(0));
                        } else if (statusFilter.size() > 0) {
                            getAllPosts_Filter("status", statusFilter.get(0));
                        } else {
                            getAllPosts(getSearched_TXT() ,1, sortByText.getText().toString());
                        }
                        // Reload Products List By Filter
                        // fillGridView();
                        dialog.dismiss();
                    }
                });

                // Clear All Button Click
                Button clear = dialog.findViewById(R.id.clear);
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            departmentFilter.clear();
                        } catch (NullPointerException ignore) {

                        }

                        try {
                            companyFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        try {
                            packFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        try {
                            statusFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        filterItemListAdapter.notifyDataSetChanged();
                    }
                });

                // Close Button
                final ImageView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }
    public void getAllPosts_Filter(String name ,String value)
    {

    }
    private List<String> getVarients(int type) {
        List<String> vairent = new ArrayList<>();

        VariantsAPIService service = RetrofitApi.retrofitRead().create(VariantsAPIService.class);

        Call<Variants> call = service.getVariants(type);

        call.enqueue(new Callback<Variants>() {
            @Override
            public void onResponse(Call<Variants> call, Response<Variants> response) {

                for (int i = 0; i < response.body().getVariants().size(); i++) {
                    vairent.add(response.body().getVariants().get(i).getName());
                    /*
                    if(response.body().getVariants().get(i).getType() == type)
                    {

                        vairent.add(response.body().getVariants().get(i).getName());

                    }

                     */

                }


            }

            @Override
            public void onFailure(Call<Variants> call, Throwable t) {
                Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        return vairent;

    }
}
