package com.tamiuz.arwina.adapters;

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
import com.tamiuz.arwina.Models.OrdersModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.interfaces.RecyclerItemClickListner;
import com.tamiuz.arwina.utils.Urls;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderProcessAdapter extends RecyclerView.Adapter<OrderProcessAdapter.ViewHolder> {

    private Context mContext;
    private List<OrdersModel.OrderData> list;
    private RecyclerItemClickListner recyclerItemClickListner;

    public OrderProcessAdapter(Context mContext, List<OrdersModel.OrderData> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void setOnItemClickListener(RecyclerItemClickListner listener) {
        recyclerItemClickListner = listener;
    }

    @NonNull
    @Override
    public OrderProcessAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_orders_row_item, parent, false);
        return new ViewHolder(view, recyclerItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProcessAdapter.ViewHolder holder, int position) {
        holder.order_num_txtV.setText(list.get(position).getOrder_number());
        holder.orderItem_name_txtV.setText(list.get(position).getItem_name());
        holder.orderItem_price_txtV.setText(list.get(position).getPrice());
        holder.orderItem_quantity_txtV.setText(String.valueOf(list.get(position).getQty()));
        holder.orderItem_address_txtV.setText(list.get(position).getPlaceaddress());
        holder.orderItem_desc_txtV.setText(list.get(position).getDesc());
        holder.orderItem_time_txtV.setText(list.get(position).getCreated_at());
        Log.i("imagee: ", list.get(position).getItem_image());
        Glide.with(mContext)
                .load(Urls.imagesBase_Url + list.get(position).getItem_image())
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
                .into(holder.item_imageV);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.order_item_imageV_id)
        ImageView item_imageV;
        @BindView(R.id.order_num_txtV_id)
        TextView order_num_txtV;
        @BindView(R.id.orderItem_name_txtV_id)
        TextView orderItem_name_txtV;
        @BindView(R.id.orderItem_price_txtV_id)
        TextView orderItem_price_txtV;
        @BindView(R.id.orderItem_quantity_txtV_id)
        TextView orderItem_quantity_txtV;
        @BindView(R.id.orderItem_address_txtV_id)
        TextView orderItem_address_txtV;
        @BindView(R.id.orderItem_desc_txtV_id)
        TextView orderItem_desc_txtV;
        @BindView(R.id.orderItem_time_txtV_id)
        TextView orderItem_time_txtV;
        @BindView(R.id.orderItem_image_progress_id)
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView, final RecyclerItemClickListner listner) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
