package com.tamiuz.arwina.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tamiuz.arwina.Models.AllCompaniesModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.interfaces.RecyclerItemClickListner;
import com.tamiuz.arwina.utils.Urls;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<AllCompaniesModel.CompanyData> list;
    private RecyclerItemClickListner recyclerItemClickListner;

    public MainRecyclerAdapter(Context mContext, List<AllCompaniesModel.CompanyData> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void setOnItemClickListener(RecyclerItemClickListner listener) {
        recyclerItemClickListner = listener;
    }

    @NonNull
    @Override
    public MainRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_company_row_item, viewGroup, false);
        return new ViewHolder(view, recyclerItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainRecyclerAdapter.ViewHolder viewHolder, int position) {
        viewHolder.company_name.setText(list.get(position).getName());
        viewHolder.company_address.setText(list.get(position).getAddress());
        viewHolder.company_ratingBar.setRating(list.get(position).getRate());
        Log.i("imagee: " , list.get(position).getImage());
        Glide.with(mContext)
                .load(Urls.imagesBase_Url + list.get(position).getImage())
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
                .into(viewHolder.company_imageV);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.company_row_imageV_id)
        ImageView company_imageV;
        @BindView(R.id.company_name_txtV_id)
        TextView company_name;
        @BindView(R.id.company_address_txtV_id)
        TextView company_address;
        @BindView(R.id.company_ratingBar_item_id)
        RatingBar company_ratingBar;
        @BindView(R.id.company_image_progress_id)
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
