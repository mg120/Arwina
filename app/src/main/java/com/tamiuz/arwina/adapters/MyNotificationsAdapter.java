package com.tamiuz.arwina.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamiuz.arwina.Models.NotifationsModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.interfaces.RecyclerItemClickListner;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyNotificationsAdapter  extends RecyclerView.Adapter<MyNotificationsAdapter.ViewHolder> {

    private Context mContext;
    private List<NotifationsModel.messageData> list;
    private RecyclerItemClickListner recyclerItemClickListner;

    public MyNotificationsAdapter(Context mContext, List<NotifationsModel.messageData> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void setOnItemClickListener(RecyclerItemClickListner listener) {
        recyclerItemClickListner = listener;
    }

    @NonNull
    @Override
    public MyNotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_row_item , parent, false);
        return new ViewHolder(view, recyclerItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNotificationsAdapter.ViewHolder holder, int position) {
        holder.item_title_txtV.setText(list.get(position).getNotification());
        holder.item_desc_txtV.setText(list.get(position).getCreated_at());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.notification_item_title_txtV_id)
        TextView item_title_txtV;
        @BindView(R.id.notification_item_desc_txtV_id)
        TextView item_desc_txtV;
        @BindView(R.id.notification_item_time_txtV_id)
        TextView item_time_txtV;

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
