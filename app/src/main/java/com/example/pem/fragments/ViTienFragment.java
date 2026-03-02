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
import com.example.pem.activities.NewAccountActivity;
import com.example.pem.adapters.AccountAdapter;
import com.example.pem.models.DataManager;

public class ViTienFragment extends Fragment {

    private TextView tvTaiSan, tvMonNo;
    private RecyclerView rvAccounts;
    private DataManager dataManager;
    private boolean isVisible = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vi_tien, container, false);

        dataManager = DataManager.getInstance();
        tvTaiSan = view.findViewById(R.id.tv_tai_san);
        tvMonNo = view.findViewById(R.id.tv_mon_no);
        rvAccounts = view.findViewById(R.id.rv_accounts);
        rvAccounts.setLayoutManager(new LinearLayoutManager(getContext()));

        view.findViewById(R.id.fab_add_account).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewAccountActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.btn_toggle_visibility).setOnClickListener(v -> {
            isVisible = !isVisible;
            updateVisibility();
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
        double taiSan = dataManager.getTotalAssets();
        double monNo = dataManager.getTotalDebt();

        tvTaiSan.setText(isVisible ? formatMoney(taiSan) : "****");
        tvMonNo.setText(isVisible ? formatMoney(monNo) : "****");

        AccountAdapter adapter = new AccountAdapter(getContext(), dataManager.getAccounts());
        rvAccounts.setAdapter(adapter);
    }

    private void updateVisibility() {
        updateUI();
    }

    private String formatMoney(double amount) {
        return (long) amount + " VND";
    }
}
