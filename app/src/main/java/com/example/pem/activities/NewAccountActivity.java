package com.example.pem.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pem.R;
import com.example.pem.models.Account;
import com.example.pem.models.DataManager;
import java.util.UUID;

public class NewAccountActivity extends AppCompatActivity {

    private Spinner spinnerAccountType, spinnerSo;
    private EditText etAccountName, etNote;
    private TextView tvNoteCount;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        dataManager = DataManager.getInstance();

        spinnerAccountType = findViewById(R.id.spinner_account_type);
        spinnerSo = findViewById(R.id.spinner_so);
        etAccountName = findViewById(R.id.et_account_name);
        etNote = findViewById(R.id.et_note);
        tvNoteCount = findViewById(R.id.tv_note_count);

        // Account types
        String[] accountTypes = {"Tiền mặt", "Thẻ tín dụng", "Thẻ ngân hàng", "Tiết kiệm"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, accountTypes);
        spinnerAccountType.setAdapter(typeAdapter);

        // Books
        String[] bookNames = new String[dataManager.getBooks().size()];
        for (int i = 0; i < dataManager.getBooks().size(); i++) {
            bookNames[i] = dataManager.getBooks().get(i).getName();
        }
        ArrayAdapter<String> bookAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, bookNames);
        spinnerSo.setAdapter(bookAdapter);

        // Note character counter
        etNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNoteCount.setText(s.length() + "/30");
                if (s.length() > 30) {
                    etNote.setText(s.subSequence(0, 30));
                    etNote.setSelection(30);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Back
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Save
        findViewById(R.id.btn_save).setOnClickListener(v -> saveAccount());
    }

    private void saveAccount() {
        String name = etAccountName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Nhập tên tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = spinnerAccountType.getSelectedItem().toString();
        String bookName = spinnerSo.getSelectedItem() != null
                ? spinnerSo.getSelectedItem().toString() : "sổ 1";

        // Find book id
        String bookId = "so1";
        for (com.example.pem.models.Book b : dataManager.getBooks()) {
            if (b.getName().equals(bookName)) {
                bookId = b.getId();
                break;
            }
        }

        Account account = new Account(UUID.randomUUID().toString(), name, type, 0, bookId);
        dataManager.addAccount(account);

        Toast.makeText(this, "Đã tạo tài khoản!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
