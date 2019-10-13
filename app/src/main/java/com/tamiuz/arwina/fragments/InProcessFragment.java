package com.tamiuz.arwina.fragments;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tamiuz.arwina.Models.MakeOrderResponseModel;
import com.tamiuz.arwina.activities.MainActivity;
import com.tamiuz.arwina.activities.MandoopNewOrderActivity;
import com.tamiuz.arwina.adapters.OrderProcessAdapter;
import com.tamiuz.arwina.Models.OrdersModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.interfaces.RecyclerItemClickListner;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;

import java.util.List;


public class InProcessFragment extends Fragment {

    private List<OrdersModel.OrderData> process_list;
    RecyclerView process_recyclerV;
    OrderProcessAdapter ordersAdapter;
    ProgressBar progressBar;
    TextView no_data_txtV;
    int order_status = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            process_list = getArguments().getParcelableArrayList("process_list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_process, container, false);
        process_recyclerV = view.findViewById(R.id.inProcess_recyclerV_id);
        progressBar = view.findViewById(R.id.inProcess_progressBar_id);
        no_data_txtV = view.findViewById(R.id.inProcess_no_orders_txtV_id);

        progressBar.setVisibility(View.GONE);
        if (process_list.size() > 0) {
            no_data_txtV.setVisibility(View.GONE);
            process_recyclerV.setVisibility(View.VISIBLE);
            buildProcessRecyclerV(process_list);
        } else {
            no_data_txtV.setVisibility(View.VISIBLE);
            process_recyclerV.setVisibility(View.GONE);
        }
        return view;
    }

    private void buildProcessRecyclerV(List<OrdersModel.OrderData> process_list) {
        process_recyclerV.setLayoutManager(new LinearLayoutManager(getActivity()));
        process_recyclerV.setHasFixedSize(true);
        if (process_list.size() > 0) {
            process_recyclerV.setVisibility(View.VISIBLE);
            no_data_txtV.setVisibility(View.GONE);
            ordersAdapter = new OrderProcessAdapter(getActivity(), process_list);
            process_recyclerV.setAdapter(ordersAdapter);

            ordersAdapter.setOnItemClickListener(new RecyclerItemClickListner() {
                @Override
                public void OnItemClick(int position) {
                    if (process_list.get(position).getStatus() == 0) {
                        Intent intent = new Intent(getActivity(), MandoopNewOrderActivity.class);
                        intent.putExtra("order_id" , process_list.get(position).getId());
                        getActivity().startActivity(intent);
                    } else {
                        showPopUp(process_list.get(position).getStatus(), position);
                    }
                }
            });
        } else {
            process_recyclerV.setVisibility(View.GONE);
            no_data_txtV.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    private void showPopUp(int status, int pos) {
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

        if (status == 2) {
            process_btn.setEnabled(false);
            complete_btn.setEnabled(false);
            order_state.setText("فى الطريق");
        }
        if (status == 0 || status == 1) {
            onWay_btn.setEnabled(false);
            complete_btn.setEnabled(false);
            order_state.setText("قيد التجهيز");
        } else if (status == 3) {
            process_btn.setEnabled(false);
            onWay_btn.setEnabled(false);
            order_state.setText("فى الطريق");
        } else if (status == 4) {
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
                updateOrder(order_status, pos);
            }
        });
        onWay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_status = 3;
                pass_Dialog.dismiss();
                process_btn.setEnabled(false);
                onWay_btn.setEnabled(false);
                updateOrder(order_status, pos);
            }
        });

        complete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle cliclk Here ....
                order_status = 4;
                pass_Dialog.dismiss();
                updateOrder(order_status, pos);
            }
        });
    }

    private void updateOrder(int orderStatus, int pos) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<MakeOrderResponseModel> call = serviceInterface.update_order(process_list.get(pos).getId(), MainActivity.userModel.getId());
        call.enqueue(new Callback<MakeOrderResponseModel>() {
            @Override
            public void onResponse(Call<MakeOrderResponseModel> call, Response<MakeOrderResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().getMessage()) {
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
