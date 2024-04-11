package com.example.shopdienthoai.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.adapter.CategoryAdapter;
import com.example.shopdienthoai.adapter.ProductAdapter;
import com.example.shopdienthoai.api.category.CategoryClient;
import com.example.shopdienthoai.api.category.CategoryData;
import com.example.shopdienthoai.api.category.CategoryInterface;
import com.example.shopdienthoai.api.product.ProductClient;
import com.example.shopdienthoai.api.product.ProductData;
import com.example.shopdienthoai.api.product.ProductInterface;
import com.example.shopdienthoai.model.Category;
import com.example.shopdienthoai.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView recyclerView, categoryView;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    ProgressBar progressBar;
    ImageView imgCart, imgSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ViewFlipper viewFlipper = view.findViewById(R.id.viewFlipper);
        recyclerView = view.findViewById(R.id.productView);
        categoryView = view.findViewById(R.id.categoryView);
        progressBar = view.findViewById(R.id.progressBar);
        imgSearch = view.findViewById(R.id.imgSearch);
        imgCart = view.findViewById(R.id.imgCart);
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


        List<String> banner = new ArrayList<>();
        //banner.add("https://cdn.discordapp.com/attachments/1112389672002396282/1189847814621110333/banner.jpg?ex=659fa721&is=658d3221&hm=6f3ae2648f853120160580b6500aff1fc905b97b7cce0bb2a585b0faa0d7ee0e&");
        banner.add("https://img.freepik.com/free-vector/realistic-cyber-monday-horizontal-sale-banner_23-2149099067.jpg");
        banner.add("https://img.freepik.com/free-vector/flat-design-shopping-center-facebook-cover_23-2149337410.jpg");
        for (int i = 0; i < banner.size(); i++) {
            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            Picasso.get().load(banner.get(i)).into(imageView);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();

        getProducts();
        getCategory();

        return view;
    }

    private void getProducts(){
        progressBar.setVisibility(View.VISIBLE);
        ProductClient productClient = new ProductClient(getContext());
        ProductInterface productInterface = productClient.getProductInterface();
        Call<ProductData.ProductResponse> call = productInterface.getAllProduct();
        call.enqueue(new Callback<ProductData.ProductResponse>() {
            @Override
            public void onResponse(Call<ProductData.ProductResponse> call, Response<ProductData.ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductData.ProductResponse productResponse = response.body();
                    List<Product> productList = productResponse.getProducts();
                    productAdapter = new ProductAdapter(getActivity(), productList);
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    recyclerView.setAdapter(productAdapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    // Handle errors
                    Toast.makeText(getActivity(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProductData.ProductResponse> call, Throwable t) {
                // Handle failures
                Toast.makeText(getActivity(), "Request failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void getCategory(){
        CategoryClient categoryClient = new CategoryClient();
        CategoryInterface categoryInterface = categoryClient.getCategoryInterface();
        Call<CategoryData.CategoryResponse> call = categoryInterface.getAllCategory();
        call.enqueue(new Callback<CategoryData.CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryData.CategoryResponse> call, Response<CategoryData.CategoryResponse> response) {
                if (response.isSuccessful()) {
                    CategoryData.CategoryResponse categoryResponse = response.body();
                    List<Category> categoryList = categoryResponse.getCategory();
                    categoryAdapter = new CategoryAdapter(getActivity(), categoryList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    categoryView.setLayoutManager(layoutManager);
                    categoryView.setAdapter(categoryAdapter);
                } else {
                    Toast.makeText(getActivity(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryData.CategoryResponse> call, Throwable t) {
                // Handle failures
                Toast.makeText(getActivity(), "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}