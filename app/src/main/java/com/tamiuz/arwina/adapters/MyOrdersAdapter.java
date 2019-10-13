package com.tamiuz.arwina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tamiuz.arwina.Models.OrdersModel;
import com.tamiuz.arwina.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private Context mContext;
    private List<OrdersModel.OrderData> list;

    public MyOrdersAdapter(Context mContext, List<OrdersModel.OrderData> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_orders_row_item , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_item_imageV_id)
        ImageView item_image;
        @BindView(R.id.orderItem_name_txtV_id)
        TextView item_name;
        @BindView(R.id.orderItem_price_txtV_id)
        TextView item_price;
        @BindView(R.id.orderItem_quantity_txtV_id)
        TextView item_packages;
        @BindView(R.id.orderItem_address_txtV_id)
        TextView item_address;
        @BindView(R.id.orderItem_desc_txtV_id)
        TextView item_desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
