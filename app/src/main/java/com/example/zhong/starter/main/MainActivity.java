package com.example.zhong.starter.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.zhong.starter.R;
import com.example.zhong.starter.adopt.AdoptFragment;
import com.example.zhong.starter.adopt.FosterFragment;


public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    private MineFragment accountFragment;
    private AdoptFragment adoptFragment;
    private FosterFragment fosterFragment;
    private MyReceiver myReceiver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        /** 导航基础设置 包括按钮选中效果 导航栏背景色等 */
        bottomNavigationBar
                .setTabSelectedListener(this)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor("#007AFF") //选中颜色
                .setBarBackgroundColor(R.color.colorWhite);//导航栏背景色

        accountFragment=new MineFragment();
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.tb,accountFragment);
        transaction.commit();
        /** 添加导航按钮 */
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_person_black_24dp, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_pets_black_24dp, "领养"))
                .addItem(new BottomNavigationItem(R.drawable.ic_pets_black_24dp, "寄养"))
                .addItem(new BottomNavigationItem(R.drawable.ic_favorite_black_24dp, "相亲"))
                .setFirstSelectedPosition(0)
                .initialise();

        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.pet.broadcast.TO_ADOPT");
        myReceiver=new MyReceiver();
        localBroadcastManager.registerReceiver(myReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(myReceiver);
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (accountFragment == null) {
                    accountFragment = new MineFragment();
                }
                transaction.replace(R.id.tb, accountFragment);
                break;
            case 1:
                if (adoptFragment == null) {
                    adoptFragment = new AdoptFragment();
                }
                transaction.replace(R.id.tb, adoptFragment);
                break;
            case 2:
                if (fosterFragment==null){
                    fosterFragment=new FosterFragment();
                }
                transaction.replace(R.id.tb,fosterFragment);
            default:
                break;
        }

        transaction.commit();// 事务提交
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            onTabSelected(1);
        }
    }
}
