package com.example.shopdienthoai.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.activity.DetailActivity;
import com.example.shopdienthoai.activity.ProductCategoryActivity;
import com.example.shopdienthoai.model.Category;
import com.example.shopdienthoai.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = this.categoryList.get(position);
        holder.txtName.setText(category.getName());
        String imageURL = "https://phoneshop-production.up.railway.app/api/v1/category/image-category/" + category.getId();
        if (holder.imgCate != null) {
            Picasso picasso = Picasso.get();
            picasso.load(imageURL).into(holder.imgCate);
        } else {
            Log.e("Picasso", "ImageView is null");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductCategoryActivity.class);
                intent.putExtra("category_slug", category.getSlug());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCate;
        TextView txtName;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgCate = itemView.findViewById(R.id.imgCate);
        }
    }
}
