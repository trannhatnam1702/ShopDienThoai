package com.example.shopdienthoai.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.activity.DetailActivity;
import com.example.shopdienthoai.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.SearchProductViewHolder> implements Filterable {
    private Context context;
    private List<Product> productList;
    private List<Product> productListOld;

    public SearchProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.productListOld = productList;
    }

    @Override
    public SearchProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_product_item, parent, false);
        return new SearchProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchProductViewHolder holder, int position) {
        Product product = this.productList.get(position);
        holder.txtName.setText(product.getName());
        holder.txtPrice.setText("$"+product.getPrice().toString());
        String imageURL = "https://phoneshop-production.up.railway.app/api/v1/product/image-product/" + product.getId();
        if (holder.productImage != null) {
            Picasso picasso = Picasso.get();
            picasso.load(imageURL).into(holder.productImage);
        } else {
            Log.e("Picasso", "ImageView is null");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("product_slug", product.getSlug());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class SearchProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView txtName, txtPrice;
        public SearchProductViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if(search.isEmpty())
                    productList = productListOld;
                else{
                    List<Product> list = new ArrayList<>();
                    for(Product product : productListOld){
                        if(product.getName().toLowerCase().contains(search.toLowerCase()))
                            list.add(product);
                    }
                    productList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productList = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
