package com.example.minigame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.minigame.databinding.ActivityScreenBinding;

public class ScreenActivity extends AppCompatActivity {

    public String phone, sum;
    private ActivityScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_screen);
        phone = getIntent().getStringExtra("PHONE");
        sum = getIntent().getStringExtra("SUM")+" руб.";
        binding.setActivity(this);
    }


}