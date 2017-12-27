package com.hhkj.spinning.www.ui;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.hhkj.spinning.www.R;
import com.hhkj.spinning.www.base.TPActivity;
import com.hhkj.spinning.www.common.Common;
import com.hhkj.spinning.www.inter.PhotoSelect;
import com.hhkj.spinning.www.inter.Tips;
import com.hhkj.spinning.www.widget.CircleImageView;
import com.hhkj.spinning.www.widget.CommonOneDate;
import com.hhkj.spinning.www.widget.CommonOneSex;
import com.hhkj.spinning.www.widget.CommonPhotoPop;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TakePhotoOptions;
import java.io.File;

/**
 * Created by Administrator on 2017/12/16/016.
 */

public class ModPersonActivity extends TPActivity {
    private TakePhoto takePhoto;
    private CircleImageView edit_person_icon;
    private final int SELECT_LIMITE = 1;
    private TextView item1,item2;
    private EditText item0,item3,item4,item5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mod_person_layout);
        takePhoto = getTakePhoto();
    }

    @Override
    public void process(Message msg) {

    }


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
                item1 = findViewById(R.id.item1);
                item2 = findViewById(R.id.item2);
                item3 = findViewById(R.id.item3);
                item4 = findViewById(R.id.item4);
                item5 = findViewById(R.id.item5);
                item0 = findViewById(R.id.item0);
                edit_person_icon = findViewById(R.id.edit_person_icon);
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
                oneDate.setI(new Tips() {
                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void sure() {

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
                oneSex.setI(new Tips() {
                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void sure() {

                    }
                });
                oneSex.showSheet();
            }
        });
    }
}
