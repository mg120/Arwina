package com.tamiuz.arwina.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tamiuz.arwina.Models.ProductsModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.interfaces.MyProductItemClickLisnter;
import com.tamiuz.arwina.utils.Urls;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ViewHolder> {

    private Context mContext;
    private List<ProductsModel.ProductData> list;
    private MyProductItemClickLisnter productItemClickLisnter;

    public MyProductsAdapter(Context mContext, List<ProductsModel.ProductData> list) {
        this.mContext = mContext;
        this.list = list;
    }


    public void setOnItemClickListener(MyProductItemClickLisnter listener) {
        productItemClickLisnter = listener;
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public MyProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_product_item_row_layout, viewGroup, false);
        return new ViewHolder(view, productItemClickLisnter);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.product_name_txtV.setText(list.get(i).getTitle());
        viewHolder.product_price_txtV.setText(String.valueOf(list.get(i).getPrice()));
        viewHolder.product_qty_txtV.setText(String.valueOf(list.get(i).getQty()));
        Log.i("imagee: ", list.get(i).getImage());
        Glide.with(mContext)
                .load(Urls.imagesBase_Url + list.get(i).getImage())
                .error(R.drawable.product)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        viewHolder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        // image ready, hide progress now
                        viewHolder.progressBar.setVisibility(View.GONE);
                        return false;   // return false if you want Glide to handle everything else.
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .into(viewHolder.product_imageV);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.myProduct_item_imageV_id)
        ImageView product_imageV;
        @BindView(R.id.myProduct_edit_imageV_id)
        ImageView edit_imageV;
        @BindView(R.id.myProduct_delete_imageV_id)
        ImageView delete_imageV;
        @BindView(R.id.myProduct_name_txtV_is)
        TextView product_name_txtV;
        @BindView(R.id.myProduct_price_txtV_id)
        TextView product_price_txtV;
        @BindView(R.id.myProduct_quantity_txtV_id)
        TextView product_qty_txtV;
        @BindView(R.id.myProduct_image_progress_id)
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView, MyProductItemClickLisnter listner) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            edit_imageV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.OnEditClick(position);
                        }
                    }
                }
            });
            delete_imageV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.OnDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
