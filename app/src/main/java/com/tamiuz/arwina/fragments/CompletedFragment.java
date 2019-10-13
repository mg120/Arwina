package com.tamiuz.arwina.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tamiuz.arwina.Models.MakeOrderResponseModel;
import com.tamiuz.arwina.activities.MainActivity;
import com.tamiuz.arwina.adapters.OrderProcessAdapter;
import com.tamiuz.arwina.Models.OrdersModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.interfaces.RecyclerItemClickListner;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;

import java.util.List;

public class CompletedFragment extends Fragment {

    private List<OrdersModel.OrderData> complete_list;
    RecyclerView complete_recyclerV;
    OrderProcessAdapter ordersAdapter;
    ProgressBar progressBar;
    TextView no_data_txtV;
    int order_status = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            complete_list = getArguments().getParcelableArrayList("completed_list");
            Log.i("complete: ", complete_list.size() + "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        complete_recyclerV = view.findViewById(R.id.completed_recyclerV_id);
        no_data_txtV = view.findViewById(R.id.completed_no_orders_txtV_id);
        if (complete_list.size() > 0) {
            no_data_txtV.setVisibility(View.GONE);
            complete_recyclerV.setVisibility(View.VISIBLE);

            buildProcessRecyclerV(complete_list);
        } else {
            no_data_txtV.setVisibility(View.VISIBLE);
            complete_recyclerV.setVisibility(View.GONE);
        }
        return view;
    }

    private void buildProcessRecyclerV(List<OrdersModel.OrderData> complete_list) {
        complete_recyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        complete_recyclerV.setHasFixedSize(true);
        if (complete_list.size() > 0) {
            complete_recyclerV.setVisibility(View.VISIBLE);
            no_data_txtV.setVisibility(View.GONE);
            ordersAdapter = new OrderProcessAdapter(getActivity(), complete_list);
            complete_recyclerV.setAdapter(ordersAdapter);

            ordersAdapter.setOnItemClickListener(new RecyclerItemClickListner() {
                @Override
                public void OnItemClick(int position) {
                    showPopUp(complete_list.get(position).getStatus(), position);
                }
            });
        } else {
            complete_recyclerV.setVisibility(View.GONE);
            no_data_txtV.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    private void showPopUp(Integer status, int position) {
        final Dialog pass_Dialog = new Dialog(getActivity());
        pass_Dialog.setContentView(R.layout.order_state_popup);
        final TextView order_state = pass_Dialog.findViewById(R.id.orderState_val_txtV);
        final Button process_btn = pass_Dialog.findViewById(R.id.order_inProcess_btn_id);
        final Button onWay_btn = pass_Dialog.findViewById(R.id.order_inWay_btn_id);
        final Button complete_btn = pass_Dialog.findViewById(R.id.order_completed_btn_id);
        pass_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        pass_Dialog.show();
        pass_Dialog.setCancelable(true);

        if (MainActivity.userModel.getRole() == 0) {
            process_btn.setVisibility(View.GONE);
            onWay_btn.setVisibility(View.GONE);
        }
        if (status == 2){
            process_btn.setEnabled(false);
            complete_btn.setEnabled(false);
            order_state.setText("فى الطريق");
        } else if (status == 0 || status == 1){
            onWay_btn.setEnabled(false);
            complete_btn.setEnabled(false);
            order_state.setText("قيد التجهيز");
        }else if (status == 3){
            process_btn.setEnabled(false);
            onWay_btn.setEnabled(false);
            order_state.setText("فى الطريق");
        } else if (status == 4){
            process_btn.setEnabled(false);
            onWay_btn.setEnabled(false);
            complete_btn.setEnabled(false);
            order_state.setText("تم التلسيم");
        }
        process_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_status = 2;
                pass_Dialog.dismiss();
                process_btn.setEnabled(false);
                updateOrder(order_status, position);
            }
        });
        onWay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_status = 3;
                pass_Dialog.dismiss();
                process_btn.setEnabled(false);
                onWay_btn.setEnabled(false);
                updateOrder(order_status , position);
            }
        });

        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle cliclk Here ....
                order_status = 4;
                pass_Dialog.dismiss();
                updateOrder(order_status, position);
            }
        });
    }

    private void updateOrder(int orderStatus, int pos) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<MakeOrderResponseModel> call = serviceInterface.update_order(complete_list.get(pos).getId(), MainActivity.userModel.getId());
        call.enqueue(new Callback<MakeOrderResponseModel>() {
            @Override
            public void onResponse(Call<MakeOrderResponseModel> call, Response<MakeOrderResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().getMessage()){
                    Toast.makeText(getActivity(), response.body().getData(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), response.body().getData(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MakeOrderResponseModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
