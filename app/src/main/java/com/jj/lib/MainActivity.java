package com.jj.lib;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.jj.base.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button btn1, btn2, btn3, btn4;
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        setSwipeBackEnable(false);

    }

    @Override
    protected void initData() {
        showPermissionsResult(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected boolean isToolBarBack() {
        return false;
    }

    @Override
    protected boolean isToolBarTitle() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, CActvivty.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action) {
            Intent intent = new Intent(MainActivity.this, DActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                Intent intent = new Intent(MainActivity.this, BActvivty.class);
                startActivity(intent);
                break;
            case R.id.btn2:
                Intent intent2 = new Intent(MainActivity.this, CActvivty.class);
                startActivity(intent2);
                break;
            case R.id.btn3:
                Intent intent3 = new Intent(MainActivity.this, DActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn4:
                Intent intent4 = new Intent(MainActivity.this, EActivity.class);
                startActivity(intent4);
                break;
        }
    }
}
