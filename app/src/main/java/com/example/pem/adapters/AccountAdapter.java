package com.example.pem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pem.R;
import com.example.pem.models.Account;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private Context context;
    private List<Account> accounts;

    public AccountAdapter(Context context, List<Account> accounts) {
        this.context = context;
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_account, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account a = accounts.get(position);
        holder.tvAccountType.setText(a.getType());
        holder.tvDebtLabel.setText("Món nợ: " + (long) a.getDebt() + " (VND)");
        holder.tvAccountName.setText(a.getName());
        holder.tvBalance.setText(a.getFormattedBalance());
    }

    @Override
    public int getItemCount() { return accounts.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAccountType, tvDebtLabel, tvAccountName, tvBalance;

        ViewHolder(View itemView) {
            super(itemView);
            tvAccountType = itemView.findViewById(R.id.tv_account_type);
            tvDebtLabel = itemView.findViewById(R.id.tv_debt_label);
            tvAccountName = itemView.findViewById(R.id.tv_account_name);
            tvBalance = itemView.findViewById(R.id.tv_balance);
        }
    }
}
