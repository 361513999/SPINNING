package com.hhkj.spinning.www.ui;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.BtListAdapter;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.utils.ClientManager;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * Created by cloor on 2018/1/25.
 */

public class BtListActivity extends BaseActivity {

    @Override
    public void process(Message msg) {
            switch (msg.what){
                case 1:
                    btListAdapter.updata(searchResults);
                    break;

            }
    }
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            SearchRequest request = new SearchRequest.Builder()
                    .searchBluetoothLeDevice(2000, 2).build();
            ClientManager.getClient().search(request,response);

        }
    };
    private SearchResponse response = new SearchResponse() {
        @Override
        public void onSearchStarted() {
            searchResults.clear();
        }

        @Override
        public void onDeviceFounded(SearchResult device) {

                if(!searchResults.contains(device)){
                    if(device.getName()!=null){
                        searchResults.add(device);
                    }

                }
                P.c(device.getName());
        }

        @Override
        public void onSearchStopped() {
            swipeRefreshLayout.setRefreshing(false);
            for(int i=0;i<searchResults.size();i++){
                P.c(searchResults.get(i).getName());
            }
            getHandler().sendEmptyMessage(1);
        }

        @Override
        public void onSearchCanceled() {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        ClientManager.getClient().stopSearch();
    }
    private ArrayList<SearchResult> searchResults = new ArrayList<>();
    @Override
    public void init() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        bt_lists = findViewById(R.id.bt_lists);
        btListAdapter = new BtListAdapter(BtListActivity.this,searchResults);
        bt_lists.setAdapter(btListAdapter);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        bt_lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                connnectBt(searchResults.get(i).getAddress());
            }
        });
    }
    private void connnectBt(String mac){
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(10000)
                .build();
        ClientManager.getClient().connect(mac, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if(code==REQUEST_SUCCESS){
                    P.c("连接成功");
                }
            }
        });
    }

    private ListView bt_lists;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BtListAdapter btListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_bt_layout);
        ClientManager.getClient().registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                P.c("registerBluetoothStateListener:"+openOrClosed);
            }
        });
    }
}
