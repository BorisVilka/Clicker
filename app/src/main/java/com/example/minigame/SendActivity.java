package com.example.minigame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.minigame.databinding.ActivitySendBinding;

public class SendActivity extends AppCompatActivity {

    private ActivitySendBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_send);
        binding.setActivity(this);
        binding.phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.textInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.sum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.textInputLayout2.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.invalidateAll();
    }

    public String getBalance() {
        return "Баланс: "+getSharedPreferences("PREFS",MODE_PRIVATE).getString("BALANCE","0.00")+" руб.";
    }
    public void send(View view) {
        if(binding.sum.getText().toString().isEmpty()) binding.textInputLayout.setError("Введите сумму для вывода");
        else {
            double money = Double.parseDouble(binding.sum.getText().toString().replace(',','.'));
            if(money>Double.parseDouble(getSharedPreferences("PREFS",MODE_PRIVATE).getString("BALANCE","0.00").replace(',','.'))) {
                binding.textInputLayout.setError("Недостаточно средств");
                if(binding.phone.getText().toString().isEmpty()) binding.textInputLayout2.setError("Введите номер");
            } else if(binding.phone.getText().toString().isEmpty()) binding.textInputLayout2.setError("Введите номер");
            else {
                SharedPreferences preferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                double ans = Double.parseDouble(preferences.getString("BALANCE","0.00").replace(',','.'))-money;
                preferences.edit().putString("BALANCE",String.format("%.2f",ans)).commit();
                Intent intent = new Intent(this,ScreenActivity.class);
                intent.putExtra("PHONE",binding.phone.getText().toString());
                intent.putExtra("SUM",binding.sum.getText().toString());
                startActivity(intent);
            }
        }
    }
}