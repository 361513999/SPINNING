package com.spinning.www.ui;

import android.os.Bundle;
import android.widget.ListView;

import com.hhkj.spinning.www.R;
import com.spinning.www.adapter.PersonLeftAdapter;
import com.spinning.www.base.BaseActivity;

/**
 * Created by Administrator on 2017/12/18/018.
 */

public class PersonCenterActivity extends BaseActivity {
    private PersonLeftAdapter personLeftAdapter;
    private ListView person_left;
    @Override
    public void process(int what) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_center_layout);
        personLeftAdapter = new PersonLeftAdapter(PersonCenterActivity.this);
        person_left = findViewById(R.id.person_left);
        person_left.setAdapter(personLeftAdapter);
    }

    @Override
    public void init() {

    }
}
