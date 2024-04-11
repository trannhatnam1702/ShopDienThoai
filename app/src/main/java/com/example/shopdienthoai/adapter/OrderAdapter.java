package com.example.shopdienthoai.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.model.Category;
import com.example.shopdienthoai.model.Order;
import com.example.shopdienthoai.model.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList){
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView txtName, txtDescription, txtStatus;
        public OrderViewHolder(View itemView){
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            productImage = itemView.findViewById(R.id.productImage);
        }
        public void bind(Order order) {
            Product product = order.getProducts().get(0);
            if (product.getCategory() instanceof Category) {
                Category category = (Category) product.getCategory();
                String categoryName = category.getName();

                txtName.setText(product.getName());
                txtDescription.setText(product.getDescription());
                txtStatus.setText(order.getStatus());
                String imageURL = "https://picked-primate-poorly.ngrok-free.app/api/v1/product/image-product/" + product.getId();
                if (productImage != null) {
                    Picasso picasso = Picasso.get();
                    picasso.load(imageURL).into(productImage);
                } else {
                    Log.e("Picasso", "ImageView is null");
                }
            }
        }
    }
}
