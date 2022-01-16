package com.example.minigame;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;

public class Dialog extends DialogFragment {

    private View root;
    private RecyclerView view;
    private boolean k;
    private MainActivity activity;

    public Dialog(MainActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        root = getLayoutInflater().inflate(R.layout.dialog,null);
        view = ((RecyclerView)root.findViewById(R.id.list));
        LinearLayoutManager manager = new MyManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        view.setLayoutManager(manager);
        setCancelable(false);
        ((RecyclerView)root.findViewById(R.id.list)).setAdapter(new Adapter(getContext()));
        AdRequest adRequest = new AdRequest.Builder().build();
        ((AdView)root.findViewById(R.id.adView2)).loadAd(adRequest);
        builder.setView(root);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        int pos = 198 + new Random().nextInt(9);
        ((Adapter)view.getAdapter()).pos = pos;
        view.smoothScrollToPosition(pos);
        k = true;
        view.getAdapter().notifyDataSetChanged();
        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                view.getAdapter().notifyDataSetChanged();
                Log.d("TAG",newState+"");
                if(newState==0) {
                    if(k) {
                        k = false;
                        view.scrollToPosition(pos);
                    }
                    Completable completable = Completable.create(emitter -> {
                        ((TextView)root.findViewById(R.id.ans)).setText("Вы получили: "+String.format("%.2f",((Adapter)view.getAdapter()).data.get(pos)));
                        add(String.format("%.2f",((Adapter)view.getAdapter()).data.get(pos)));
                        setCancelable(true);
                        activity.setEnabled();
                        activity.binding.invalidateAll();
                        emitter.onComplete();
                    });
                    completable.subscribeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                        Completable completable1 = Completable.create(emitter -> {
                            dismiss();
                            emitter.onComplete();
                        }).delaySubscription(5000,TimeUnit.MILLISECONDS);
                        completable1.subscribe();
                    });
                }
            }
        });

    }


    private class MyManager extends LinearLayoutManager {

        private float ms = 60f;

        public MyManager(Context context) {
            super(context);
        }

        @Override
        public void setOrientation(int orientation) {
            super.setOrientation(orientation);
        }

        @Override
        public boolean canScrollHorizontally() {
            return false;
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                public PointF computeScrollVectorForPosition(int targetPosition) {
                    return super.computeScrollVectorForPosition(targetPosition);
                }
                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return ms / displayMetrics.densityDpi;
                }
            };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }


    }
    private void add(String add) {
        SharedPreferences preferences = getContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE);
        double ans = Double.parseDouble(preferences.getString("BALANCE","0.00").replace(',','.'))+Double.parseDouble(add.replace(',','.'));
        preferences.edit().putString("BALANCE",String.format("%.2f",ans)).commit();
    }
}
