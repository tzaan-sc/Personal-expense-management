package com.example.pem.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pem.R;
import com.example.pem.models.Transaction;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private List<Transaction> transactions;
    private OnItemClickListener listener;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/M", Locale.getDefault());

    public interface OnItemClickListener {
        void onEdit(Transaction t, int position);
    }

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction t = transactions.get(position);

        // Show date header if first item or different date from previous
        boolean showHeader = position == 0 || !isSameDay(transactions.get(position - 1).getDate(), t.getDate());
        if (showHeader) {
            holder.rlDateHeader.setVisibility(View.VISIBLE);
            holder.tvDateHeader.setText(getDayLabel(t.getDate()));

            // Calculate daily totals
            double dayThu = 0, dayChi = 0;
            for (Transaction tx : transactions) {
                if (isSameDay(tx.getDate(), t.getDate())) {
                    if ("thu".equals(tx.getType())) dayThu += tx.getAmount();
                    else dayChi += tx.getAmount();
                }
            }
            holder.tvDateThu.setText("Thu: " + formatMoney(dayThu));
            holder.tvDateChi.setText("Chi: " + formatMoney(dayChi));
        } else {
            holder.rlDateHeader.setVisibility(View.GONE);
        }

        holder.tvName.setText(t.getCategory());
        holder.tvNote.setText(t.getNote().isEmpty() ? " " : t.getNote());
        holder.tvAmount.setText(t.getFormattedAmount());
        holder.tvAccount.setText(t.getAccountId());
        holder.tvAmount.setTextColor("chi".equals(t.getType())
                ? Color.parseColor("#F44336")
                : Color.parseColor("#4CAF50"));

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(t, holder.getAdapterPosition());
        });
    }

    private boolean isSameDay(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance(); c1.setTime(d1);
        Calendar c2 = Calendar.getInstance(); c2.setTime(d2);
        return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }

    private String getDayLabel(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String[] days = {"CN", "T2", "T3", "T4", "T5", "T6", "T7"};
        String dayName = days[cal.get(Calendar.DAY_OF_WEEK) - 1];
        return "▼  " + dayName + ", " + SDF.format(date);
    }

    private String formatMoney(double amount) {
        long val = (long) amount;
        if (val >= 1000) return (val / 1000) + "k";
        return val + "đ";
    }

    @Override
    public int getItemCount() { return transactions.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View rlDateHeader;
        TextView tvDateHeader, tvDateThu, tvDateChi;
        View ivCategory;
        TextView tvName, tvNote, tvAmount, tvAccount;
        View btnEdit;

        ViewHolder(View itemView) {
            super(itemView);
            rlDateHeader = itemView.findViewById(R.id.rl_date_header);
            tvDateHeader = itemView.findViewById(R.id.tv_date_header);
            tvDateThu = itemView.findViewById(R.id.tv_date_thu);
            tvDateChi = itemView.findViewById(R.id.tv_date_chi);
            ivCategory = itemView.findViewById(R.id.iv_category);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNote = itemView.findViewById(R.id.tv_note);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvAccount = itemView.findViewById(R.id.tv_account);
            btnEdit = itemView.findViewById(R.id.btn_edit);
        }
    }
}
