package com.example.pem.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pem.R;
import com.example.pem.activities.AddTransactionActivity;
import com.example.pem.activities.LichActivity;
import com.example.pem.adapters.TransactionAdapter;
import com.example.pem.models.DataManager;
import com.example.pem.models.Transaction;
import java.util.List;

public class SoFragment extends Fragment {

    private int currentMonth;
    private int currentYear;
    private TextView tvMonth, tvTong, tvThu, tvChi;
    private RecyclerView rvTransactions;
    private TransactionAdapter adapter;
    private DataManager dataManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_so, container, false);

        dataManager = DataManager.getInstance();

        // Default to current month
        java.util.Calendar cal = java.util.Calendar.getInstance();
        currentMonth = cal.get(java.util.Calendar.MONTH) + 1;
        currentYear = cal.get(java.util.Calendar.YEAR);

        tvMonth = view.findViewById(R.id.tv_month);
        tvTong = view.findViewById(R.id.tv_tong);
        tvThu = view.findViewById(R.id.tv_thu);
        tvChi = view.findViewById(R.id.tv_chi);
        rvTransactions = view.findViewById(R.id.rv_transactions);

        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));

        // Navigation
        view.findViewById(R.id.btn_prev_month).setOnClickListener(v -> {
            currentMonth--;
            if (currentMonth < 1) { currentMonth = 12; currentYear--; }
            updateUI();
        });

        view.findViewById(R.id.btn_next_month).setOnClickListener(v -> {
            currentMonth++;
            if (currentMonth > 12) { currentMonth = 1; currentYear++; }
            updateUI();
        });

        // Calendar button opens calendar view
        view.findViewById(R.id.btn_calendar).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LichActivity.class);
            startActivity(intent);
        });

        // chi tiết button
        view.findViewById(R.id.btn_chi_tiet).setOnClickListener(v -> {
            // Show detail/filter options
        });

        // Add button
        view.findViewById(R.id.btn_add_so).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
            startActivity(intent);
        });

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        tvMonth.setText(currentMonth + "/" + currentYear);

        double thu = dataManager.getTotalThuByMonth(currentMonth, currentYear);
        double chi = dataManager.getTotalChiByMonth(currentMonth, currentYear);
        double tong = thu - chi;

        tvThu.setText(formatMoney(thu));
        tvChi.setText(formatMoney(chi));
        tvTong.setText(formatMoney(tong));

        List<Transaction> txList = dataManager.getTransactionsByMonth(currentMonth, currentYear);
        // Sort by date descending
        txList.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        adapter = new TransactionAdapter(getContext(), txList);
        rvTransactions.setAdapter(adapter);
    }

    private String formatMoney(double amount) {
        long val = (long) Math.abs(amount);
        String prefix = amount < 0 ? "-" : "";
        if (val >= 1000) return prefix + (val / 1000) + "k";
        return prefix + val + "đ";
    }
}
