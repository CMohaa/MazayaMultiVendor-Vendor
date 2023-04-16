package com.mohaa.mazaya.dashboard.Controllers.fragments_traders;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mohaa.mazaya.dashboard.Controllers.activities_products.IndividualProductActivity;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.EndlessRecyclerViewScrollListener;
import com.mohaa.mazaya.dashboard.Utils.GridSpacingItemDecoration;
import com.mohaa.mazaya.dashboard.Utils.ProductsUI;
import com.mohaa.mazaya.dashboard.interfaces.MyResultReceiver;
import com.mohaa.mazaya.dashboard.interfaces.OnProductClickListener;
import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.ProductsAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.VariantsAPIService;
import com.mohaa.mazaya.dashboard.manager.Call.Products;
import com.mohaa.mazaya.dashboard.manager.Call.Variants;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.models.Product;
import com.mohaa.mazaya.dashboard.models.Variant;
import com.mohaa.mazaya.dashboard.views.FilterItemListAdapter;
import com.mohaa.mazaya.dashboard.views.Products_ListAdapter;
import com.mohaa.mazaya.dashboard.views.SortItemListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment implements OnProductClickListener {



    public ProductsFragment() {

        // Required empty public constructor
    }
    public MyResultReceiver resultreceiver;
    private RecyclerView recList;
    private ArrayList<Product> products_list;
    private RecyclerView products_recyclerView;
    private StaggeredGridLayoutManager products_staggeredGridLayoutManager;
    private Products_ListAdapter products_listAdapter;

    private int traderID;
    private int userPage = 1;
    RelativeLayout sort, filter;
    TextView sortByText;
    String[] sortByArray = {"Most Recent", "Most Orders", "Top Rated", "Most Viewed"};
    int sortById = 0;
    List<String> departmentFilter = new ArrayList<>();
    List<String> companyFilter = new ArrayList<>();
    List<String> packFilter = new ArrayList<>();
    List<String> statusFilter = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        resultreceiver = (MyResultReceiver)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products , container , false);
        //traderID =  getArguments().getInt("traderID");
        traderID  = resultreceiver.getResult();

        recList = (RecyclerView) view.findViewById(R.id.recyclerview);
        products_list = new ArrayList<>();

        products_listAdapter = new Products_ListAdapter(products_list , this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recList.setLayoutManager(mLayoutManager);
        recList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recList.setItemAnimator(new DefaultItemAnimator());
        recList.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                getData(userPage , sortByText.getText().toString());
            }
        });
        recList.setAdapter(products_listAdapter);


        //
        sort = view.findViewById(R.id.sortLay);
        filter = view.findViewById(R.id.filterLay);
        sortByText = view.findViewById(R.id.sortBy);
        setSortListener();
        setFilterListener();
        sortByText.setText(sortByArray[0]);
        getData(1 , sortByText.getText().toString());
        // Inflate the layout for this fragment
        return view;
    }
    //Responsible For Adding the 3 tabs : Camera  , Home , Messages
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    public void getAllPosts(int userPage , String sortById)
    {



        ProductsAPIService service = RetrofitApi.retrofitRead().create(ProductsAPIService.class);
        Call<Products> call = service.getProducts(traderID , userPage , 1 );
        // String[] sortByArray = {"Most Recent", "Most Orders", "Most Shares", "Most Viewed"};
        switch (sortById) {
            case "Most Recent":

                call = service.getProducts(traderID , userPage , 1 );
                break;

            case "Most Orders":

                call = service.getProducts(traderID , userPage , 2 );
                break;

            case "Top Rated":

                call = service.getProducts(traderID , userPage , 3 );
                break;
            case "Most Viewed":

                call = service.getProducts(traderID , userPage , 4 );
                break;


            default:
            {
                call = service.getProducts(traderID , userPage , 1 );
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

    public void getAllPosts_Filter(String name ,String value)
    {

    }


    public void getData(int userPage , String _sortById){
        try {
            //swipeRefreshLayout.setRefreshing(true);
            getAllPosts(userPage ,_sortById );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }









    @Override
    public void onProductClicked(Product contact, int position) {
        //Toast.makeText(this, contact.getProduct_name(), Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent(getContext(), IndividualProductActivity.class);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) contact);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE, contact.getThumb_image());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_ID, contact.getId());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE, contact.getType());
        startActivity(loginIntent);

    }

    // Set Sort Listener
    private void setSortListener() {
        sort.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.sort_listview);

                ListView listView = dialog.findViewById(R.id.sort_listview);
                listView.setAdapter(new SortItemListAdapter(getContext(), sortByArray, sortById));
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
                        getData( 1, sortByText.getText().toString());
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

                final Dialog dialog = new Dialog(getContext());
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
                final FilterItemListAdapter filterItemListAdapter = new FilterItemListAdapter(getContext(), headers, listHashMap, departmentFilter, companyFilter, packFilter, statusFilter);
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
                            getData(1, sortByText.getText().toString());
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

    private List<String> getNames(List<Variant> type) {
        // Toast.makeText(this, type.size(), Toast.LENGTH_SHORT).show();
        List<String> type_values = new ArrayList<>();
        for (int i = 0; i < type.size(); i++) {
            type_values.add(type.get(i).getName());
            Toast.makeText(getContext(), type.get(i).getName(), Toast.LENGTH_SHORT).show();
        }
        return type_values;
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
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        return vairent;

    }

}
