package com.example.minigame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.minigame.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;

public class MainActivity extends AppCompatActivity {

   protected ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setActivity(this);
        MobileAds.initialize(this, initializationStatus -> { });

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.invalidateAll();
    }

    public void outClick(View v) {
        Intent intent = new Intent(this,SendActivity.class);
        startActivity(intent);
    }
    public void show(View v) {
        findViewById(R.id.button).setEnabled(false);
        AdRequest request = new AdRequest.Builder().build();
          RewardedAd.load(this,"ca-app-pub-2304943461766296/1291837734", request,new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                findViewById(R.id.button).setEnabled(true);
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        Completable completable = Completable.create(emitter -> {
                            findViewById(R.id.button).setEnabled(true);
                            emitter.onComplete();
                        }).subscribeOn(AndroidSchedulers.mainThread()).delaySubscription(30, TimeUnit.SECONDS);
                        completable.subscribe();
                    }
                });
                rewardedAd.show(MainActivity.this, rewardItem -> {
                    SharedPreferences preferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    double ans = Double.parseDouble(preferences.getString("BALANCE","0.00").replace(',','.'))+0.02;
                    preferences.edit().putString("BALANCE",String.format("%.2f",ans)).commit();
                    binding.invalidateAll();
                            Completable completable = Completable.create(emitter -> {
                                findViewById(R.id.button).setEnabled(true);
                                emitter.onComplete();
                            }).subscribeOn(AndroidSchedulers.mainThread()).delaySubscription(30, TimeUnit.SECONDS);
                            completable.subscribe();
                }
                );
            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void game(View v) {
        if(!isNetworkConnected()) return;
        DialogFragment dialogFragment = new Dialog(this);
        dialogFragment.show(getSupportFragmentManager(),"tag");
        findViewById(R.id.button2).setEnabled(false);
    }

    protected void setEnabled() {
        Completable completable = Completable.create(emitter -> {
            findViewById(R.id.button2).setEnabled(true);
            emitter.onComplete();
        }).subscribeOn(AndroidSchedulers.mainThread()).delaySubscription(30, TimeUnit.SECONDS);
        completable.subscribe();
    }
    public String getBalance() {
        return "Баланс: "+getSharedPreferences("PREFS",MODE_PRIVATE).getString("BALANCE","0.00")+" руб.";
    }
    public void telegram(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://t.me/+-9cZ3VPSVWZhNmJi"));
        intent.setPackage("org.telegram.messenger");
        startActivity(Intent.createChooser(intent,"title"));
    }

}