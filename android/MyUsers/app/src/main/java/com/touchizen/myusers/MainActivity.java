package com.touchizen.myusers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final int USER_DETAIL_REQUEST_CODE = 1000;
    RecyclerView mRecyclerView = null ;
    RecyclerUserAdapter mAdapter = null ;
    ArrayList<UserItem> mList = null;// new ArrayList<UserItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserDetail(null);
            }
        });

        getUserlist();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            initControls();
        }
    };


    public void showUserDetail(UserItem item) {

        Intent intent=new Intent(MainActivity.this,UserDetailActivity.class);
        intent.putExtra(UserDetailActivity.USERITEM, item);
        startActivityForResult(intent, USER_DETAIL_REQUEST_CODE);

    }

    private void getUserlist() {
        new Thread() {
            public void run() {
                mList = UserAPI.getUserList();
                Bundle bun = new Bundle();
                bun.putInt("list_user_finished", 0);

                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void initControls() {
        mRecyclerView = findViewById(R.id.rv_userlist) ;
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        mAdapter = new RecyclerUserAdapter(mList,this) ;
        mRecyclerView.setAdapter(mAdapter) ;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)) ;
        //mRecyclerView.setOnIte
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==USER_DETAIL_REQUEST_CODE){

            if(resultCode== Activity.RESULT_OK) {
                getUserlist();
            }
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
