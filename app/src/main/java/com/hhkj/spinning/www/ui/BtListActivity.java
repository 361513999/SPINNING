package com.hhkj.spinning.www.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.adapter.BtListAdapter;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.BaseActivity;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.common.SharedUtils;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.inter.Tips;
import com.hhkj.spinning.www.utils.ClientManager;
import com.hhkj.spinning.www.widget.CommonTips;
import com.hhkj.spinning.www.widget.NewToast;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                case 0:
                    //断开并上传数据
                   // NewToast.makeText(BtListActivity.this,"断开并上传",Common.TTIME).show();
                    CommonTips commonTips = new CommonTips(BtListActivity.this,null);
                    commonTips.init("忽略","上传","断开并上传数据");
                    commonTips.setI(new Tips() {
                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void sure() {
                            ClientManager.getClient().disconnect(sharedUtils.getStringValue("bt_mac"));
                            sharedUtils.clear("bt_mac");
                              Intent intent = getIntent();
                              if(intent.hasExtra("xl")){
                                  updata(intent);
                              }else{
                                  AppManager.getAppManager().finishActivity(BtListActivity.this);
                              }
                              if(btListAdapter!=null){
                                  btListAdapter.notifyDataSetChanged();
                              }
                        }
                    });
                    commonTips.showSheet();

                    break;
                case 2:
                    cancleLoadView();
                    NewToast.makeText(BtListActivity.this,"数据已上传",Common.TTIME).show();
                    AppManager.getAppManager().finishActivity(BtListActivity.this);
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

    private void updata(final Intent intent){
        showLoadView();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cls","Sys.Data");
            jsonObject.put("method","UpLoadData");
            jsonObject.put("toKen",sharedUtils.getStringValue("token"));
            JSONObject object = new JSONObject();
            //{\"XL\":1,\"SD\":1,\"KAL\":1,\"KM\":1,\"TotalKM\":1,\"ZS\":1,\"Time\":\"12:25\"}
            object.put("XL",intent.getStringExtra("xl"));
            object.put("SD",intent.getStringExtra("sd"));
            object.put("KAL",intent.getStringExtra("cal"));
            object.put("KM",intent.getStringExtra("lc"));
            object.put("TotalKM",intent.getStringExtra("zlc"));
            object.put("ZS","0");
            object.put("Time",intent.getStringExtra("sj"));

            jsonObject.put("param",object.toString());
            httpPost("Post", jsonObject.toString(), new Result() {
                @Override
                public void success(JSONObject data) {
                    sharedUtils.setStringValue("zlc",intent.getStringExtra("zlc"));
                    getHandler().sendEmptyMessage(2);
                }

                @Override
                public void error(String data) {

                }

                @Override
                public void unLogin() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private SearchResponse response = new SearchResponse() {
        @Override
        public void onSearchStarted() {
            searchResults.clear();
        }

        @Override
        public void onDeviceFounded(SearchResult device) {

            if(!searchResults.contains(device)){
                 //   if(device.getName()!=null){
                        searchResults.add(device);
                 //   }

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
    private   boolean contain2(String input, String regex) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(input);
        boolean result = m.find();
        return result;
    }
    private ArrayList<SearchResult> searchResults = new ArrayList<>();
    private TextView title;
    private void checkConnect(int i){
        SharedUtils sharedUtils = new SharedUtils(Common.initMap);
        ArrayList<String> keys =  sharedUtils.getKeys();
       if(!isCom(keys,i)){
           NewToast.makeText(BtListActivity.this,"禁止连接",Common.TTIME).show();
       }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String bt_mac = sharedUtils.getStringValue("bt_mac");
            ClientManager.getClient().disconnect(bt_mac);
            AppManager.getAppManager().finishActivity(this);
        }
        return true;

    }

    private boolean isCom(ArrayList<String> keys,int i){
        for(int k=0;k<keys.size();k++){

            if(contain2(searchResults.get(i).getName(),keys.get(k))){
                connnectBt(searchResults.get(i));
                return  true;
            }
        }
        return  false;
    }

    @Override
    public void init() {
        title = findViewById(R.id.title);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        bt_lists = findViewById(R.id.bt_lists);
        btListAdapter = new BtListAdapter(BtListActivity.this,searchResults,getHandler());
        bt_lists.setAdapter(btListAdapter);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        bt_lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String bt_mac = sharedUtils.getStringValue("bt_mac");
                if(searchResults.get(i).getAddress().equals(bt_mac)&&ClientManager.getClient().getConnectStatus(bt_mac)==2){
                    getHandler().sendEmptyMessage(0);
                }else{
                    checkConnect(i);



                }


            }
        });
    }
    private void connnectBt(final SearchResult result){

        sharedUtils.setStringValue("bt_name",result.getName());
        sharedUtils.setStringValue("bt_mac",result.getAddress());
        setResult(1000);
        AppManager.getAppManager().finishActivity(BtListActivity.this);

    /*    BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(10000)
                .build();
         ClientManager.getClient().connect(result.getAddress(), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                if(code==REQUEST_SUCCESS){
                    P.c("连接成功");
                    sharedUtils.setStringValue("bt_name",result.getName());
                    sharedUtils.setStringValue("bt_mac",result.getAddress());
                    NewToast.makeText(BtListActivity.this,"连接成功", Common.TTIME).show();
                    setResult(1000);
                    AppManager.getAppManager().finishActivity(BtListActivity.this);
                }
            }
        });*/
    }



    private ListView bt_lists;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BtListAdapter btListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_bt_layout);
        ClientManager.getClient().registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                P.c("registerBluetoothStateListener:"+openOrClosed);
            }
        });
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(2000, 2).build();
        ClientManager.getClient().search(request,response);
    }
}
