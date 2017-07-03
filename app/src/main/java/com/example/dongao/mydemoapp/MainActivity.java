package com.example.dongao.mydemoapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dongao.mydemoapp.widget.LoopView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private List<String> list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initViews();




    }
//    private SideBar sideBar;
//    private RecyclerView rv;

    private int minYear; // min year
    private int maxYear; // max year
    private LoopView yearLoopView;
    private LoopView monthLoopView;
    private LoopView dayLoopView;

    private int yearPos = 0;
    private int monthPos = 0;
    private int dayPos = 0;
    List<String> yearList = new ArrayList();
    List<String> monthList = new ArrayList();
    List<String> dayList = new ArrayList();

    private void initViews() {
//        rv= (RecyclerView) findViewById(R.id.rv);
//        sideBar=(SideBar)findViewById(R.id.sb);
//        sideBar.setmTextDialog((TextView) findViewById(R.id.tv));
//        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
//            @Override
//            public void onTouchingLetterChanged(String s) {
//
//
//            }
//        });
//
//        yearLoopView = (LoopView) findViewById(R.id.picker_year);
//        monthLoopView = (LoopView) findViewById(R.id.picker_month);
//        dayLoopView = (LoopView) findViewById(R.id.picker_day);
//        minYear=Calendar.getInstance().get(Calendar.YEAR);
//        maxYear=minYear+3;
//
//        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int year = minYear + yearPos;
//                int month = monthPos + 1;
//                int day = dayPos + 1;
//                StringBuffer sb = new StringBuffer();
//                sb.append(String.valueOf(year));
//                sb.append("-");
//                sb.append(format2LenStr(month));
//                sb.append("-");
//                sb.append(format2LenStr(day));
//                Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        yearLoopView.setLoopListener(new LoopScrollListener() {
//            @Override
//            public void onItemSelect(int item) {
//                yearPos = item;
//                initDayPickerView();
//            }
//        });
//
//        monthLoopView.setLoopListener(new LoopScrollListener() {
//            @Override
//            public void onItemSelect(int item) {
//                monthPos = item;
//                initDayPickerView();
//            }
//        });
//        dayLoopView.setLoopListener(new LoopScrollListener() {
//            @Override
//            public void onItemSelect(int item) {
////                int dayMaxInMonth;
////                Calendar calendar = Calendar.getInstance();
////                calendar.set(Calendar.YEAR, minYear + yearPos);
////                calendar.set(Calendar.MONTH, monthPos);
////                //get max day in month
////                dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//
//                dayPos = item;
//            }
//        });
//
//        initPickerViews(); // init year and month loop view
//        initDayPickerView(); //init day loop view

//        progressBar= (MyProgressBar) findViewById(R.id.progressBar);
    }
//    MyProgressBar progressBar;
    int count;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus)
//            progressBar.setProgress(10);
    }

    /**
     * Init year and month loop view,
     * Let the day loop view be handled separately
     */
    private void initPickerViews() {

        int yearCount = maxYear - minYear;
        for (int i = 0; i < yearCount; i++) {
            yearList.add(format2LenStr(minYear + i));
        }

        for (int j = 0; j < 12; j++) {
            monthList.add(format2LenStr(j + 1));
        }

        yearLoopView.setDataList(yearList);
        yearLoopView.setInitPosition(yearPos);

        monthLoopView.setDataList( monthList);
        monthLoopView.setInitPosition(monthPos);
    }
    /**
     * Init day item
     */
    private void initDayPickerView() {

        int dayMaxInMonth;
        Calendar calendar = Calendar.getInstance();
        dayList = new ArrayList<String>();

        calendar.set(Calendar.YEAR, minYear + yearPos);
        calendar.set(Calendar.MONTH, monthPos);

        //get max day in month
        dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < dayMaxInMonth; i++) {
            dayList.add(format2LenStr(i + 1));
        }

        dayLoopView.setDataList(dayList);

        dayLoopView.setInitPosition(0);
    }

    /**
     * Transform int to String with prefix "0" if less than 10
     * @param num
     * @return
     */
    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }










    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
