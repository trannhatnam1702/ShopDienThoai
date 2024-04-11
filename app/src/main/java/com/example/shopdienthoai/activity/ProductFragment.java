package com.example.shopdienthoai.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.adapter.ProductAdapter;
import com.example.shopdienthoai.api.product.ProductClient;
import com.example.shopdienthoai.api.product.ProductData;
import com.example.shopdienthoai.api.product.ProductInterface;
import com.example.shopdienthoai.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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

    ProgressBar progressBar;
    private ProductAdapter productAdapter;
    private RecyclerView allProductView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        allProductView = view.findViewById(R.id.allProductView);
        getProducts();

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
                    allProductView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    allProductView.setAdapter(productAdapter);
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
}