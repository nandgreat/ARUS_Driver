package com.silexsecure.arusdriver.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.service.APIService;
import com.silexsecure.arusdriver.service.ClientAPI;
import com.silexsecure.arusdriver.adapters.ReportHistoryAdapter;
import com.silexsecure.arusdriver.model.ReportsHistoryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private RecyclerView historyRecyclerView;

    private ReportHistoryAdapter reportHistoryAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        historyRecyclerView = view.findViewById(R.id.report_history_recycler_view);

        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setRefreshing(true);

//        getAllHistory(MainActivity.token, Integer.parseInt(MainActivity.userId));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

//                getAllHistory(MainActivity.token, Integer.parseInt(MainActivity.userId));

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void getAllHistory(String token, int userId) {

        APIService service = new APIService();
        ClientAPI api = APIService.createDriverService(ClientAPI.class);

        Call<ReportsHistoryResponse> call = api.ReportHistory(token, userId);

        call.enqueue(new Callback<ReportsHistoryResponse>() {
            @Override
            public void onResponse(Call<ReportsHistoryResponse> call, Response<ReportsHistoryResponse> response) {

                if (response.isSuccessful()) {

                    swipeRefreshLayout.setRefreshing(false);

                    reportHistoryAdapter = new ReportHistoryAdapter(getContext(), response.body().getData());
                    historyRecyclerView.setAdapter(reportHistoryAdapter);

                    Log.d("HistoryFragment", response.body().getData().size() + " " + response.body().getData().toArray());

                }
            }

            @Override
            public void onFailure(Call<ReportsHistoryResponse> call, Throwable t) {


            }
        });

    }


}
