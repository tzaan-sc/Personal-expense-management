package com.example.pem.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pem.R;
import com.example.pem.models.DataManager;
import com.example.pem.models.Transaction;
import java.util.Calendar;
import java.util.Date;

public class AddTransactionActivity extends AppCompatActivity {

    private String currentType = "chi"; // "chi" or "thu"
    private StringBuilder inputExpression = new StringBuilder();
    private TextView tvAmount, tabChi, tabThu;
    private EditText etNote;
    private String selectedCategory = "";
    private Date selectedDate;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        dataManager = DataManager.getInstance();
        selectedDate = new Date();

        tvAmount = findViewById(R.id.tv_amount);
        tabChi = findViewById(R.id.tab_chi);
        tabThu = findViewById(R.id.tab_thu);
        etNote = findViewById(R.id.et_note);

        // Close button
        findViewById(R.id.btn_close).setOnClickListener(v -> finish());

        // Type switch
        tabChi.setOnClickListener(v -> switchType("chi"));
        tabThu.setOnClickListener(v -> switchType("thu"));

        // Save
        findViewById(R.id.btn_save).setOnClickListener(v -> saveTransaction());

        // Date picker
        findViewById(R.id.btn_ngay).setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (dp, y, m, d) -> {
                Calendar picked = Calendar.getInstance();
                picked.set(y, m, d);
                selectedDate = picked.getTime();
                ((TextView) v).setText(d + "/" + (m + 1));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // OK = evaluate expression
        findViewById(R.id.btn_ok).setOnClickListener(v -> evaluateExpression());

        // Calculator keys
        setupCalcKey(R.id.btn_0, "0");
        setupCalcKey(R.id.btn_1, "1");
        setupCalcKey(R.id.btn_2, "2");
        setupCalcKey(R.id.btn_3, "3");
        setupCalcKey(R.id.btn_4, "4");
        setupCalcKey(R.id.btn_5, "5");
        setupCalcKey(R.id.btn_6, "6");
        setupCalcKey(R.id.btn_7, "7");
        setupCalcKey(R.id.btn_8, "8");
        setupCalcKey(R.id.btn_9, "9");
        setupCalcKey(R.id.btn_dot, ".");
        setupCalcKey(R.id.btn_add, "+");
        setupCalcKey(R.id.btn_sub, "-");
        setupCalcKey(R.id.btn_mul, "*");
        setupCalcKey(R.id.btn_div, "/");

        // Delete
        findViewById(R.id.btn_del).setOnClickListener(v -> {
            if (inputExpression.length() > 0) {
                inputExpression.deleteCharAt(inputExpression.length() - 1);
                tvAmount.setText(inputExpression.length() > 0 ? inputExpression.toString() : "0");
            }
        });

        // Category buttons
        setupCategory(R.id.btn_add_category, "");
        // You can add more category click listeners here
    }

    private void setupCalcKey(int id, String val) {
        View btn = findViewById(id);
        if (btn != null) {
            btn.setOnClickListener(v -> {
                inputExpression.append(val);
                tvAmount.setText(inputExpression.toString());
            });
        }
    }

    private void setupCategory(int id, String name) {
        View btn = findViewById(id);
        if (btn != null) {
            btn.setOnClickListener(v -> {
                selectedCategory = name;
                // Visually highlight selected category
            });
        }
    }

    private void switchType(String type) {
        currentType = type;
        if ("chi".equals(type)) {
            tabChi.setBackgroundResource(R.drawable.bg_tab_selected);
            tabThu.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        } else {
            tabThu.setBackgroundResource(R.drawable.bg_tab_selected);
            tabChi.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        }
    }

    private void evaluateExpression() {
        try {
            String expr = inputExpression.toString().trim();
            if (expr.isEmpty()) return;
            double result = evalSimple(expr);
            inputExpression = new StringBuilder(String.valueOf((long) result));
            tvAmount.setText(inputExpression.toString());
        } catch (Exception e) {
            Toast.makeText(this, "Biểu thức không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    // Simple expression evaluator for +, -, *, /
    private double evalSimple(String expr) {
        expr = expr.replaceAll("\\s+", "");
        // Handle + and - (lowest precedence, left to right)
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if ((c == '+' || c == '-') && i > 0) {
                double left = evalSimple(expr.substring(0, i));
                double right = evalSimple(expr.substring(i + 1));
                return c == '+' ? left + right : left - right;
            }
        }
        // Handle * and /
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if ((c == '*' || c == '/') && i > 0) {
                double left = evalSimple(expr.substring(0, i));
                double right = evalSimple(expr.substring(i + 1));
                return c == '*' ? left * right : left / right;
            }
        }
        return Double.parseDouble(expr);
    }

    private void saveTransaction() {
        String expr = inputExpression.toString().trim();
        double amount = 0;
        try {
            amount = evalSimple(expr.isEmpty() ? "0" : expr);
        } catch (Exception e) {
            Toast.makeText(this, "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount <= 0) {
            Toast.makeText(this, "Nhập số tiền > 0", Toast.LENGTH_SHORT).show();
            return;
        }

        String note = etNote.getText().toString().trim();
        String category = selectedCategory.isEmpty() ? "Khác" : selectedCategory;

        Transaction t = new Transaction(currentType, category, amount, note, selectedDate, "acc1", "so1");
        dataManager.addTransaction(t);

        Toast.makeText(this, "Đã lưu giao dịch!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
