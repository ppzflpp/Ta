package com.dragon.ta.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dragon.ta.MainApplication;
import com.dragon.ta.R;
import com.dragon.ta.fragment.CartFragment;
import com.dragon.ta.fragment.HomeFragment;
import com.dragon.ta.manager.DataManager;
import com.dragon.ta.model.CartGood;
import com.dragon.ta.model.Good;
import com.dragon.ta.model.User;
import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnListFragmentInteractionListener, CartFragment.OnFragmentInteractionListener {

    private TextView mUserNameView;
    private SimpleDraweeView mUserIconView;

    private TextView mHomePageView;
    private TextView mCartView;

    private Fragment mHomeFragment;
    private Fragment mCartFragment;

    private FragmentManager mFragmentManager;

    private MainApplication.DataChangeListener mDataChangeListener;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DataManager.MSG_LOAD_DATA_SUCCESS:
                    if(mHomeFragment != null){
                        ((HomeFragment)mHomeFragment).updateData();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getSupportFragmentManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        initViews();
        onTabSelected(R.id.home_page);

        mDataChangeListener = new MainApplication.DataChangeListener() {
            @Override
            public void onDataChange() {
                String path = ((MainApplication)getApplication()).getUser().getIconPath();
                Log.d("TAG","onDataChange..." + path);
                if(path != null) {
                    mUserIconView.setImageURI(Uri.parse(path));
                }
                mUserNameView.setText(((MainApplication)getApplication()).getUser().getNick());
            }
        };
        ((MainApplication)getApplication()).registerDataListener(mDataChangeListener);

        DataManager.getInstance(getApplicationContext()).loadData(true,mHandler);
    }

    private void initViews() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mUserNameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
        mUserIconView = (SimpleDraweeView) navigationView.getHeaderView(0).findViewById(R.id.user_icon);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = ((MainApplication) getApplication()).getUser();
                if (!user.isLogin()) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    loadData();
                }
            }
        };


        mUserNameView.setOnClickListener(clickListener);
        mUserIconView.setOnClickListener(clickListener);

        mHomePageView = (TextView) findViewById(R.id.home_page);
        mHomePageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTabSelected(R.id.home_page);
            }
        });
        mCartView = (TextView) findViewById(R.id.cart);
        mCartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTabSelected(R.id.cart);
            }
        });
    }

    private void onTabSelected(int id) {
        switch (id) {
            case R.id.home_page:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                }

                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, mHomeFragment);
                transaction.commit();
                //update data
                mHandler.sendEmptyMessage(DataManager.MSG_LOAD_DATA_SUCCESS);
                break;
            case R.id.cart:
                if (mCartFragment == null) {
                    mCartFragment = new CartFragment();
                }

                FragmentTransaction transaction1 = mFragmentManager.beginTransaction();
                transaction1.replace(R.id.fragment_container, mCartFragment);
                transaction1.commit();
                break;
        }
    }

    private void loadData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        if (((MainApplication) getApplication()).getUser().isLogin()) {
            mUserNameView.setText(((MainApplication) getApplication()).getUser().getNick());
        } else {
            mUserNameView.setText(R.string.user_name);
        }
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
        //getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        ((MainApplication)getApplication()).unregisterDataListener(mDataChangeListener);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = new Intent();

        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            if(!((MainApplication)getApplication()).getUser().isLogin()){
                intent.setClass(this,LoginActivity.class);
                startActivity(intent);
                return true;
            }
            intent.setClass(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_my_order) {
            if(!((MainApplication)getApplication()).getUser().isLogin()){
                intent.setClass(this,LoginActivity.class);
                startActivity(intent);
                return true;
            }
            intent.setClass(this, OrderActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {
            intent.setClass(this, FeedbackActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            intent.setClass(this, AboutActivity.class);
            startActivity(intent);
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(Good good) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("good", good);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(CartGood cartGood) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("good", cartGood.getGood());
        startActivity(intent);
    }
}
