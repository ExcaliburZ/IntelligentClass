package com.wings.intelligentclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wings.intelligentclass.cczu.CczuActivity;
import com.wings.intelligentclass.cczu.InfoActivity;
import com.wings.intelligentclass.cczu.LibActivity;
import com.wings.intelligentclass.search.SearchClassActivity;
import com.wings.intelligentclass.student.clazz.MyClassActivity;
import com.wings.intelligentclass.teacher.clazz.ClassManagerActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        //toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
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
            boolean wrapInScrollView = true;
            new MaterialDialog.Builder(this)
                    .title("About me")
                    .customView(R.layout.dialog_about, wrapInScrollView)
                    .positiveText("OK")
                    .show();
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


    @OnClick({R.id.rl_class, R.id.rl_my_class, R.id.rl_search_class,
            R.id.rl_cczu, R.id.rl_lib, R.id.rl_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_class:
                Intent intent = new Intent(this, ClassManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_class:
                Intent myClassintent = new Intent(this, MyClassActivity.class);
                startActivity(myClassintent);
                break;
            case R.id.rl_search_class:
                Intent serachIntent = new Intent(this, SearchClassActivity.class);
                startActivity(serachIntent);
                break;
            case R.id.rl_cczu:
                Intent cczuIntent = new Intent(this, CczuActivity.class);
                startActivity(cczuIntent);
                break;
            case R.id.rl_lib:
                Intent libIntent = new Intent(this, LibActivity.class);
                startActivity(libIntent);
                break;
            case R.id.rl_info:
                Intent infoIntent = new Intent(this, InfoActivity.class);
                startActivity(infoIntent);
                break;

        }
    }
}
