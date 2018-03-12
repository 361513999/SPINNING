package com.hhkj.spinning.www.ui;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.AppManager;
import com.hhkj.spinning.www.base.TPActivity;
import com.hhkj.spinning.www.bean.PersonCenter0;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.common.FileUtils;
import com.hhkj.spinning.www.common.P;
import com.hhkj.spinning.www.inter.PhotoSelect;
import com.hhkj.spinning.www.inter.Result;
import com.hhkj.spinning.www.inter.Tips;
import com.hhkj.spinning.www.inter.TipsText;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.hhkj.spinning.www.widget.CommonOneDate;
import com.hhkj.spinning.www.widget.CommonOneInfo;
import com.hhkj.spinning.www.widget.CommonOneSex;
import com.hhkj.spinning.www.widget.CommonPhotoPop;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/16/016.
 */

public class ModPersonActivity extends TPActivity {
    private TakePhoto takePhoto;
    private CircleImageView edit_person_icon;
    private final int SELECT_LIMITE = 1;
    private TextView item1,item2;
    private EditText item0,item6;
    private TextView item3,item4,item5;
    private TextView send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mod_person_layout);
        takePhoto = getTakePhoto();

    }

    @Override
    public void process(Message msg) {
        switch (msg.what){
            case 2:

                ImageLoader.getInstance().displayImage("file://"+ImagePath,edit_person_icon);
                break;
            case 1:
                ImageLoader.getInstance().clearDiskCache();
                ImageLoader.getInstance().clearMemoryCache();
                setResult(1000);
                AppManager.getAppManager().finishActivity(ModPersonActivity.this);
                break;
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        ArrayList<TImage> imsge = result.getImages();
        if(imsge!=null&&imsge.size()!=0){
            ImagePath  = imsge.get(0).getCompressPath();
            P.c("file://"+ImagePath);
            P.c(Thread.currentThread().getName());

          getHandler().sendEmptyMessage(2);
           // edit_person_icon.setTag(path);
        }
    }
    private String ImagePath = null;
    private PhotoSelect photoSelect = new PhotoSelect() {
        private void init(){
            int width = 1280;
            int height = 800;
            LubanOptions option=new LubanOptions.Builder()
                    .setMaxHeight(height)
                    .setMaxWidth(width)
                    .setMaxSize(200*1024)
                    .create();
            CompressConfig config = CompressConfig.ofLuban(option);
            config.enableReserveRaw(true);
            takePhoto.onEnableCompress(config,true);

            TakePhotoOptions.Builder builder=new TakePhotoOptions.Builder();
            builder.setWithOwnGallery(true);//使用自带相册
            builder.setCorrectImage(true);//纠正拍照角度
            takePhoto.setTakePhotoOptions(builder.create());
        }
        @Override
        public void camcer( ) {
            File file=new File(Common.CACHE_STAFF_IMAGES+"/"+System.currentTimeMillis()+".jpg");
            if (!file.getParentFile().exists())file.getParentFile().mkdirs();
            Uri imageUri = Uri.fromFile(file);
            init();
            takePhoto.onPickFromCapture(imageUri);
        }
        @Override
        public void photos( ) {
            init();
            takePhoto.onPickMultiple(SELECT_LIMITE);
//            takePhoto.onPickFromGallery();
        }
    };
    @Override
    public void init() {
                send = findViewById(R.id.send);
                item1 = findViewById(R.id.item1);
                item2 = findViewById(R.id.item2);
                item3 = findViewById(R.id.item3);
                item4 = findViewById(R.id.item4);
                item5 = findViewById(R.id.item5);
                 item6 = findViewById(R.id.item6);
                item0 = findViewById(R.id.item0);
                edit_person_icon = findViewById(R.id.edit_person_icon);

            Intent intent = getIntent();
            if(intent.hasExtra("obj")){
                PersonCenter0 center0 = (PersonCenter0) intent.getSerializableExtra("obj");
                item0.setText(center0.getUserName());
                item1.setText(center0.getBirthday());
                item2.setText(center0.isSex()?"男":"女");
                item3.setText(center0.getHeight());
                item4.setText(center0.getWeight());
                item5.setText(center0.getIdealWeight());
                item6.setText(center0.getInfo());
                ImageLoader.getInstance().displayImage(FileUtils.addImage(center0.getUrl()),edit_person_icon);


            }

                edit_person_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonPhotoPop.showSheet(ModPersonActivity.this,inflater,photoSelect,0);
                    }
                });
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonOneDate oneDate = new CommonOneDate(ModPersonActivity.this,null);
                oneDate.init(null,null,"请选择出生日期");
                oneDate.setI(new TipsText() {
                    @Override
                    public void cancel(String txt) {

                    }

                    @Override
                    public void sure(String txt) {
                        item1.setText(txt);
                    }
                });
                oneDate.showSheet();
            }
        });
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonOneSex oneSex = new CommonOneSex(ModPersonActivity.this,null);
                oneSex.init(null,null,"请选择性别");
                oneSex.setI(new TipsText() {
                    @Override
                    public void cancel(String txt) {

                    }

                    @Override
                    public void sure(String txt) {
                        item2.setText(txt.equals("1")?"男":"女");
                    }
                });
                oneSex.showSheet();
            }
        });
        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonOneInfo oneInfo = new CommonOneInfo(ModPersonActivity.this,null,140,229);
                oneInfo.init("取消","确定","请选择身高","cm",getArr(140,229),getNow(item3)==0?140:getNow(item3));
                oneInfo.setI(new TipsText() {
                    @Override
                    public void cancel(String txt) {

                    }

                    @Override
                    public void sure(String txt) {
                        item3.setText(txt);
                    }
                });
                oneInfo.showSheet();
            }
        });
        item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonOneInfo oneInfo = new CommonOneInfo(ModPersonActivity.this,null,35,134);

                oneInfo.init("取消","确定","请选择体重","kg",getArr(35,134),getNow(item4)==0?35:getNow(item4));
                oneInfo.setI(new TipsText() {
                    @Override
                    public void cancel(String txt) {

                    }

                    @Override
                    public void sure(String txt) {
                        item4.setText(txt);
                    }
                });
                oneInfo.showSheet();
            }
        });
        item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonOneInfo oneInfo = new CommonOneInfo(ModPersonActivity.this,null,35,134);

                oneInfo.init("取消","确定","请选择目标体重","kg",getArr(35,134),getNow(item5)==0?35:getNow(item5));
                oneInfo.setI(new TipsText() {
                    @Override
                    public void cancel(String txt) {

                    }

                    @Override
                    public void sure(String txt) {
                        item5.setText(txt);
                    }
                });
                oneInfo.showSheet();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("method","EditUserInfo");
                    jsonObject.put("cls","Sys.User");
                    jsonObject.put("toKen",sharedUtils.getStringValue("token"));
                    JSONObject object = new JSONObject();
                    if(ImagePath!=null){
                        object.put("Url", Bitmap2StrByBase64(BitmapFactory.decodeFile( ImagePath)));
                    }
                    object.put("Id",sharedUtils.getStringValue("id"));
                    if(item0.getText().toString().length()!=0){
                        object.put("UserName",item0.getText().toString());
                    }
                    if(item1.getText().toString().length()!=0){
                        object.put("Birthday",item1.getText().toString());
                    }
                    if(item2.getText().toString().length()!=0){
                        object.put("Sex",item2.getText().toString().equals("男")?true:false);
                    }
                    if(item3.getText().toString().length()!=0){
                        object.put("Height",item3.getText().toString());
                    }
                    if(item4.getText().toString().length()!=0){
                        object.put("Weight",item4.getText().toString());
                    }
                    if(item5.getText().toString().length()!=0){
                        object.put("IdealWeight",item5.getText().toString());
                    }
                    if(item6.getText().toString().length()!=0){
                        object.put("UserExplain",item6.getText().toString());
                    }


//                    object.put("Height",item5.getText().toString());
                    jsonObject.put("param",object.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                httpPost("Post", jsonObject.toString(), new Result() {
                        @Override
                        public void success(JSONObject data) {

                            getHandler().sendEmptyMessage(1);

                        }

                        @Override
                        public void error(String data) {

                        }

                        @Override
                        public void unLogin() {

                        }
                    });
            }
        });
    }
    private int getNow(TextView view){
        String temp = view.getText().toString();
        int result = 0;
        try {
            result = Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return  result;
    }

    private String []getArr(int min,int max){
        String[] tems = new String[max-min+1];
        int k = 0;
        for(int i=min;i<=max;i++){
            tems[k] = String.valueOf(i);
            k = k+1;
        }
        return  tems;
    };


    private String Bitmap2StrByBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
