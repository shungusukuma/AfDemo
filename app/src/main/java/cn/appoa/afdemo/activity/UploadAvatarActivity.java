package cn.appoa.afdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.utils.SnackbarUtils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Calendar;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseImageActivity;
import cn.appoa.aframework.dialog.DatePickerDialog;
import cn.appoa.aframework.listener.OnCallbackListener;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;


public class UploadAvatarActivity extends BaseImageActivity
        implements View.OnClickListener, OnCallbackListener {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("头像上传")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_upload_avatar);
    }

    private ImageView iv_avatar;
    private TextView tv_birthday;
    private TextView tv_constellation;

    @Override
    public void initView() {
        super.initView();
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_constellation = (TextView) findViewById(R.id.tv_constellation);
        //点击事件
        iv_avatar.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        tv_constellation.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    /**
     * 时间选择器
     */
    private DatePickerDialog dialogDate;

    /**
     * 生日
     */
    private String birthday;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar://头像
                dialogUpload.showDialog();
                break;
            case R.id.tv_birthday://生日
                if (dialogDate == null) {
                    dialogDate = new DatePickerDialog(mActivity, this, 1);
                    //生日从1900年1月1日到今天
                    dialogDate.initData(DatePickerDialog.str2Long("1900-01-01 00:00", false)
                            , System.currentTimeMillis());
                    //相关设置
                    //dialogDate.setCanShowPreciseTime(false);
                    //dialogDate.setCanShowMinuteTime(true);
                }
                if (TextUtils.isEmpty(birthday)) {
                    dialogDate.showDatePickerDialog("请选择日期", System.currentTimeMillis());
                } else {
                    dialogDate.showDatePickerDialog("请选择日期", birthday);
                }
                break;
            case R.id.tv_constellation://星座
                SnackbarUtils.Long(v, "选择生日后将自动设置星座")
                        .setAction("选择生日", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UploadAvatarActivity.this.onClick(tv_birthday);
                            }
                        }).show();
                break;
        }
    }

    @Override
    public void onCallback(int type, Object... obj) {
        if (type == 1) {
            birthday = (String) obj[0];
            tv_birthday.setText(birthday);
            Calendar calendar = (Calendar) obj[1];
            tv_constellation.setText(DatePickerDialog.getConstellation(calendar));
        }
    }

    @Override
    public void getImageBitmap(Uri imageUri, String imagePath, Bitmap imageBitmap) {
        //裁剪
        CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result != null && result.getUri() != null) {
                getImageBitmap(uriToBitmap(this, result.getUri()));
            }
        }
    }

    /**
     * 图片
     */
    private String base64Avatar = "";

    @Override
    public void getImageBitmap(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            iv_avatar.setImageBitmap(imageBitmap);
            base64Avatar = bitmapToBase64(imageBitmap);
        }
    }

}
