package com.tamiuz.arwina.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tamiuz.arwina.Models.ProductsModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.activities.LogInActivity;
import com.tamiuz.arwina.activities.MainActivity;
import com.tamiuz.arwina.activities.OrderActivity;
import com.tamiuz.arwina.utils.Urls;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyProductsAdapter extends RecyclerView.Adapter<CompanyProductsAdapter.ViewHolder> {

    private Context mContext;
    private List<ProductsModel.ProductData> list;

    public CompanyProductsAdapter(Context mContext, List<ProductsModel.ProductData> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public CompanyProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.company_product_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyProductsAdapter.ViewHolder holder, int position) {
        holder.productName_txtV.setText(list.get(position).getTitle());
        holder.product_price_txtV.setText(String.valueOf(list.get(position).getPrice()));
        holder.product_content_txtV.setText(String.valueOf(list.get(position).getQty()));
        holder.product_maxContent_txtV.setText(String.valueOf(list.get(position).getMaxqty()));
        Log.i("imagee: ", list.get(position).getImage());
        Glide.with(mContext)
                .load(Urls.imagesBase_Url + list.get(position).getImage())
                .error(R.drawable.product)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        // image ready, hide progress now
                        holder.progressBar.setVisibility(View.GONE);
                        return false;   // return false if you want Glide to handle everything else.
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .into(holder.product_imageV);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_row_imageV_id)
        ImageView product_imageV;
        @BindView(R.id.product_name_txtV_id)
        TextView productName_txtV;
        @BindView(R.id.product_price_txtV_id)
        TextView product_price_txtV;
        @BindView(R.id.product_content_txtV_id)
        TextView product_content_txtV;
        @BindView(R.id.product_maxContent_txtV_id)
        TextView product_maxContent_txtV;
        @BindView(R.id.product_image_progress_id)
        ProgressBar progressBar;
        @BindView(R.id.product_order_btn_id)
        Button product_order_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            product_order_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainActivity.userModel.getRole() != 0) {
                        if (MainActivity.userModel.getRole() == 2) {
                            Toast.makeText(mContext, "هذه الخدمة غير متاحة!", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(mContext, OrderActivity.class);
                            intent.putExtra("product_data", list.get(getAdapterPosition()));
                            mContext.startActivity(intent);
                        }
                    } else {
                        mContext.startActivity(new Intent(mContext, LogInActivity.class));
                    }
                }
            });
    }
}
}
