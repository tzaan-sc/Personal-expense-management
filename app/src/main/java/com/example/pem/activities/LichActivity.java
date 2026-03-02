package com.example.pem.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import com.example.pem.R;
import com.example.pem.models.DataManager;
import com.example.pem.models.Transaction;
import java.util.Calendar;
import java.util.List;

public class LichActivity extends AppCompatActivity {

    private int currentMonth, currentYear;
    private GridLayout calendarGrid;
    private DataManager dataManager;
    private String filterType = "thu"; // chi, thu, tong
    private TextView tabChi, tabThu, tabTong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lich);

        dataManager = DataManager.getInstance();
        Calendar cal = Calendar.getInstance();
        currentMonth = cal.get(Calendar.MONTH) + 1;
        currentYear = cal.get(Calendar.YEAR);

        calendarGrid = findViewById(R.id.calendar_grid);
        tabChi = findViewById(R.id.tab_chi);
        tabThu = findViewById(R.id.tab_thu);
        tabTong = findViewById(R.id.tab_tong);

        findViewById(R.id.btn_prev_cal).setOnClickListener(v -> {
            currentMonth--;
            if (currentMonth < 1) { currentMonth = 12; currentYear--; }
            buildCalendar();
        });

        findViewById(R.id.btn_next_cal).setOnClickListener(v -> {
            currentMonth++;
            if (currentMonth > 12) { currentMonth = 1; currentYear++; }
            buildCalendar();
        });

        tabChi.setOnClickListener(v -> setFilter("chi"));
        tabThu.setOnClickListener(v -> setFilter("thu"));
        tabTong.setOnClickListener(v -> setFilter("tong"));

        buildCalendar();
    }

    private void setFilter(String type) {
        filterType = type;
        int blue = getResources().getColor(R.color.blue_primary);
        int gray = getResources().getColor(R.color.text_gray);
        int white = Color.WHITE;

        tabChi.setBackgroundColor(Color.TRANSPARENT);
        tabThu.setBackgroundColor(Color.TRANSPARENT);
        tabTong.setBackgroundColor(Color.TRANSPARENT);
        tabChi.setTextColor(gray);
        tabThu.setTextColor(gray);
        tabTong.setTextColor(gray);

        switch (type) {
            case "chi":
                tabChi.setBackgroundResource(R.drawable.bg_type_selected);
                tabChi.setTextColor(white);
                break;
            case "thu":
                tabThu.setBackgroundResource(R.drawable.bg_type_selected);
                tabThu.setTextColor(white);
                break;
            case "tong":
                tabTong.setBackgroundResource(R.drawable.bg_type_selected);
                tabTong.setTextColor(white);
                break;
        }
        buildCalendar();
    }

    private void buildCalendar() {
        calendarGrid.removeAllViews();

        Calendar cal = Calendar.getInstance();
        cal.set(currentYear, currentMonth - 1, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1; // 0=Sun
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Previous month fill
        Calendar prevCal = (Calendar) cal.clone();
        prevCal.add(Calendar.MONTH, -1);
        int daysInPrev = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int totalCells = 42;
        List<Transaction> txList = dataManager.getTransactionsByMonth(currentMonth, currentYear);

        for (int i = 0; i < totalCells; i++) {
            int col = i % 7;
            int row = i / 7;

            TextView cell = new TextView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.columnSpec = GridLayout.spec(col, 1, 1f);
            params.rowSpec = GridLayout.spec(row, 1, 1f);
            params.setMargins(1, 1, 1, 1);
            cell.setLayoutParams(params);
            cell.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            cell.setPadding(0, 4, 0, 4);
            cell.setTextSize(12f);

            int dayNum;
            boolean isCurrentMonth;

            if (i < firstDayOfWeek) {
                dayNum = daysInPrev - firstDayOfWeek + i + 1;
                isCurrentMonth = false;
            } else if (i >= firstDayOfWeek + daysInMonth) {
                dayNum = i - firstDayOfWeek - daysInMonth + 1;
                isCurrentMonth = false;
            } else {
                dayNum = i - firstDayOfWeek + 1;
                isCurrentMonth = true;
            }

            cell.setText(String.valueOf(dayNum));
            cell.setTextColor(isCurrentMonth ? Color.BLACK : Color.LTGRAY);

            // Check if today
            Calendar today = Calendar.getInstance();
            if (isCurrentMonth && dayNum == today.get(Calendar.DAY_OF_MONTH)
                    && currentMonth == today.get(Calendar.MONTH) + 1
                    && currentYear == today.get(Calendar.YEAR)) {
                cell.setBackgroundResource(R.drawable.bg_type_selected);
                cell.setTextColor(Color.WHITE);
            }

            // Show transaction amount for this day
            if (isCurrentMonth) {
                final int finalDay = dayNum;
                double dayAmount = 0;
                for (Transaction t : txList) {
                    Calendar tCal = Calendar.getInstance();
                    tCal.setTime(t.getDate());
                    if (tCal.get(Calendar.DAY_OF_MONTH) == finalDay) {
                        if ("tong".equals(filterType)) {
                            dayAmount += "thu".equals(t.getType()) ? t.getAmount() : -t.getAmount();
                        } else if (filterType.equals(t.getType())) {
                            dayAmount += t.getAmount();
                        }
                    }
                }
                if (dayAmount != 0) {
                    String amtStr = formatMoney(dayAmount);
                    cell.setText(dayNum + "\n" + amtStr);
                    cell.setTextSize(10f);
                }
            }

            calendarGrid.addView(cell);
        }
    }

    private String formatMoney(double amount) {
        long val = (long) Math.abs(amount);
        String prefix = amount < 0 ? "-" : "";
        if (val >= 1000) return prefix + (val / 1000) + "k";
        return prefix + val + "đ";
    }
}
