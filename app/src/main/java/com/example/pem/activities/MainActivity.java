package com.example.pem.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.pem.R;
import com.example.pem.fragments.SoFragment;
import com.example.pem.fragments.ViTienFragment;

public class MainActivity extends AppCompatActivity {

    private TextView tabSo, tabViTien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabSo = findViewById(R.id.tab_so);
        tabViTien = findViewById(R.id.tab_vi_tien);

        tabSo.setOnClickListener(v -> selectTab(0));
        tabViTien.setOnClickListener(v -> selectTab(1));

        // Load default fragment
        selectTab(0);
    }

    private void selectTab(int index) {
        Fragment fragment;
        if (index == 0) {
            fragment = new SoFragment();
            tabSo.setBackgroundResource(R.drawable.bg_tab_selected);
            tabSo.setTextColor(getResources().getColor(R.color.blue_primary));
            tabViTien.setBackgroundColor(Color.TRANSPARENT);
            tabViTien.setTextColor(getResources().getColor(R.color.text_dark));
        } else {
            fragment = new ViTienFragment();
            tabViTien.setBackgroundResource(R.drawable.bg_tab_selected);
            tabViTien.setTextColor(getResources().getColor(R.color.blue_primary));
            tabSo.setBackgroundColor(Color.TRANSPARENT);
            tabSo.setTextColor(getResources().getColor(R.color.text_dark));
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
