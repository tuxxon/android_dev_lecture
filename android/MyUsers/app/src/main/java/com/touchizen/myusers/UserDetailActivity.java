package com.touchizen.myusers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

public class UserDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String USERITEM = "_useritem";

    private static final String kUSER_FINISHED = "_USER_FINISHED";

    private static final int CREATE_USER_FINISHED = 1000;
    private static final int UPDATE_USER_FINISHED = 2000;
    private static final int DELETE_USER_FINISHED = 3000;

    UserItem mUserItem = null;

    TextView  tvUserid = null;
    EditText  etUsername = null;
    EditText  etUseremail = null;
    EditText  etUserphone = null;
    EditText  etUserdesc = null;
    TextView  tvViews = null;
    Button btAdd = null;
    Button btUpdate = null;
    Button btDelete = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        initControls();
    }

    public void initControls() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        tvUserid = (TextView) findViewById(R.id.userid);
        etUsername = (EditText) findViewById(R.id.username);
        etUseremail = (EditText) findViewById(R.id.useremail);
        etUserphone = (EditText) findViewById(R.id.userphone);
        etUserdesc = (EditText) findViewById(R.id.userdesc);
        tvViews    = (TextView) findViewById(R.id.userviews);
        btAdd      = (Button) findViewById(R.id.useradd);
        btUpdate   = (Button) findViewById(R.id.userupdate);
        btDelete   = (Button) findViewById(R.id.userdelete);

        btAdd.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        btDelete.setOnClickListener(this);

        mUserItem = (UserItem) getIntent().getSerializableExtra(USERITEM);
        btAdd.setVisibility(mUserItem!=null?View.GONE : View.VISIBLE);
        btUpdate.setVisibility(mUserItem!=null?View.VISIBLE : View.GONE);
        btDelete.setVisibility(mUserItem!=null?View.VISIBLE : View.GONE);

        if (mUserItem != null) {
            tvUserid.setText("ID:" + mUserItem.getUserId());
            etUsername.setText(mUserItem.getUserName());
            etUseremail.setText(mUserItem.getUserEmail());
            etUserphone.setText(mUserItem.getUserPhone());
            etUserdesc.setText(mUserItem.getUserDesc());
            tvViews.setText("Views:" + mUserItem.getViews());
        }
        else {
            tvUserid.setText("ID:");
            etUsername.setText("");
            etUseremail.setText("");
            etUserphone.setText("");
            etUserdesc.setText("");
            tvViews.setText("Views:");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:

                onBackPressed();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.useradd: onAddUser();    break;
            case R.id.userupdate: onUpdateUser();break;
            case R.id.userdelete: onDeleteUser();break;
            default: break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            int nFinished = bun.getInt(kUSER_FINISHED);
            switch (nFinished) {
                case CREATE_USER_FINISHED: break;
                case UPDATE_USER_FINISHED: break;
                case DELETE_USER_FINISHED: break;
            }

            onBackPressed();
        }
    };

    private void onAddUser() {
        if (mUserItem == null) {
            mUserItem = new UserItem();
        }

        mUserItem.setUserName(etUsername.getText().toString());
        mUserItem.setUserEmail(etUseremail.getText().toString());
        mUserItem.setUserPhone(etUserphone.getText().toString());
        mUserItem.setUserDesc(etUserdesc.getText().toString());

        new Thread() {
            public void run() {
                UserAPI.createUser(mUserItem);
                Bundle bun = new Bundle();
                bun.putInt(kUSER_FINISHED, CREATE_USER_FINISHED);

                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void onUpdateUser() {
        if (mUserItem == null) {
            mUserItem = new UserItem();
        }

        mUserItem.setUserName(etUsername.getText().toString());
        mUserItem.setUserEmail(etUseremail.getText().toString());
        mUserItem.setUserPhone(etUserphone.getText().toString());
        mUserItem.setUserDesc(etUserdesc.getText().toString());

        new Thread() {
            public void run() {
                UserAPI.updateUser(mUserItem);
                Bundle bun = new Bundle();
                bun.putInt(kUSER_FINISHED, UPDATE_USER_FINISHED);

                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void onDeleteUser() {
        new Thread() {
            public void run() {
                UserAPI.deleteUser(mUserItem.getUserId());
                Bundle bun = new Bundle();
                bun.putInt(kUSER_FINISHED, UPDATE_USER_FINISHED);

                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();
    }
}
