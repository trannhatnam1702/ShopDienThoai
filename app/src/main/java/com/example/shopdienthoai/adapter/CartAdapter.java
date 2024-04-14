package com.example.shopdienthoai.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.activity.CartActivity;
import com.example.shopdienthoai.activity.DetailActivity;
import com.example.shopdienthoai.dao.CartDAO;
import com.example.shopdienthoai.model.Cart;
import com.example.shopdienthoai.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private Context context;
    private List<Cart> cartList;

    public interface OnQuantityChangeListener {
        void onQuantityChange(int position, int newQuantity);
    }

    private OnQuantityChangeListener onQuantityChangeListener;

    public CartAdapter(Context context, List<Cart> cartList, OnQuantityChangeListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.onQuantityChangeListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Cart cart = this.cartList.get(position);
        int quantity = cart.getQuantity();
        double sumProductPrice = quantity * (cart.getPrice());
        holder.txtName.setText(cart.getName());
        holder.txtProductPrice.setText("$"+cart.getPrice());
        holder.txtSumProductPrice.setText(String.valueOf("$"+quantity * (cart.getPrice())));
        holder.txtQuantity.setText(String.valueOf(quantity));
        String imageURL = "https://phoneshop-production.up.railway.app/api/v1/product/image-product/" + cart.getProductId();
        if (holder.productImage != null) {
            Picasso picasso = Picasso.get();
            picasso.load(imageURL).into(holder.productImage);
        } else {
            Log.e("Picasso", "ImageView is null");
        }

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("product_slug", cart.getSlug());
                context.startActivity(intent);
            }
        });

        holder.txtTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cart.getQuantity() + 1;
                cart.setQuantity(quantity);
                float totalPrice = 0;
                totalPrice += quantity * cart.getPrice();
                onQuantityChangeListener.onQuantityChange(position, quantity);
                CartDAO cartDAO = new CartDAO(v.getContext());
                cartDAO.updateCartItemQuantity(cart.getId(),quantity);
                cartDAO.updateCartItemPrice(cart.getId(),totalPrice);
                notifyItemChanged(position);
            }
        });

        holder.txtGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = cart.getQuantity() - 1;
                if (quantity > 0) {
                    cart.setQuantity(quantity);
                    holder.txtQuantity.setText(String.valueOf(quantity));

                    float totalPrice = 0;
                    totalPrice += quantity * cart.getPrice();

                    onQuantityChangeListener.onQuantityChange(position, quantity);
                    CartDAO cartDAO = new CartDAO(v.getContext());
                    cartDAO.updateCartItemQuantity(cart.getId(),quantity);
                    cartDAO.updateCartItemPrice(cart.getId(),totalPrice);
                    notifyItemChanged(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView txtName, txtProductPrice, txtSumProductPrice, txtQuantity, txtTang, txtGiam;
        public CartViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtSumProductPrice = itemView.findViewById(R.id.txtSumProductPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtTang = itemView.findViewById(R.id.txtTang);
            txtGiam = itemView.findViewById(R.id.txtGiam);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
