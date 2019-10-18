package com.xz.ttnote.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.orhanobut.logger.Logger;
import com.xz.ttnote.R;
import com.xz.ttnote.base.BaseActivity;
import com.xz.ttnote.constant.Local;
import com.xz.ttnote.custom.EditTextPro;
import com.xz.ttnote.custom.TTPopupWindow;
import com.xz.ttnote.entity.Note;
import com.xz.ttnote.utils.IOStreamUtil;
import com.xz.ttnote.utils.ImageUtil;
import com.xz.ttnote.utils.ScreenUtil;
import com.xz.ttnote.utils.TimeUtil;
import com.xz.ttnote.utils.Uri2PathUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditActivity extends BaseActivity implements View.OnClickListener {

    private ConstraintLayout topBar;
    private ImageView tvBack;
    private ImageView tvMenu;
    private EditTextPro ed;
    private ImageView addPhoto;
    private TextView tvDate;

    private String noteCache;//本次便签缓存的目录
    private long timestap;

    private TTPopupWindow ttpop;
    private Note note;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit;
    }

    @Override
    protected boolean homeAsUpEnabled() {
        return false;
    }

    @Override
    protected void initData() {
        initView();
        timestap = System.currentTimeMillis();

        note = (Note) getIntent().getSerializableExtra("note");
        if (note!=null){
            //继续编辑模式
            timestap = Long.valueOf(note.getDate());
            tvDate.setText(TimeUtil.getSimDate("MM月dd日 HH:mm", timestap));
            ed.setText(note.getContent());
        }

        noteCache = Local.storage + timestap + "/";
        tvDate.setText(TimeUtil.getSimDate("MM月dd日 HH:mm", timestap));
        ttpop = new TTPopupWindow(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ed.getText().toString().equals("")) {
            //空不操作
            return;
        }
        FileOutputStream fos = null;
        try {
            //首先判断缓存目录是否存在
            File createdir = new File(noteCache);
            if (!createdir.isDirectory()) {
                createdir.mkdirs();
            }
            fos = new FileOutputStream(noteCache + "note.tt");
            fos.write(ed.getText().toString().getBytes());
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                    sToast("已保存!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        topBar = findViewById(R.id.top_bar);
        tvBack = findViewById(R.id.tv_back);
        tvMenu = findViewById(R.id.tv_menu);
        tvDate = findViewById(R.id.tv_date);
        ed = findViewById(R.id.ed);
        addPhoto = findViewById(R.id.add_photo);


        tvBack.setOnClickListener(this);
        tvMenu.setOnClickListener(this);
        addPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_menu:
                showPopupMenu();
                break;
            case R.id.add_photo:
                selectPhoto();
                break;
        }
    }

    private void showPopupMenu() {
//        View view = LayoutInflater.from(this).inflate(R.layout.custom_popup_menu, null);
//        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//        popupWindow.showAsDropDown(tvMenu );

        ttpop.showAsDropDown(tvMenu, -170, -50);
    }

    private void selectPhoto() {
        try {

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 666);
            }
        } catch (Exception e) {
            sDialog("错误", "照片选择器启动失败：\nerror:0x0001", 0);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 666) {
            if (resultCode == RESULT_OK) {


                String cachePath = copy2Cache(Uri2PathUtil.uri2Path(this, data.getData()));
                if (cachePath != null) {
                    //复制完成

                    //插入图片
                    insertImg(cachePath);

                }

            } else {
                sDialog("异常", "照片获取失败\nerror:0x0002", 0);
            }
        }

    }

    /**
     * 拷贝文件到缓存目录
     *
     * @param oldPath
     * @return
     */
    private String copy2Cache(String oldPath) {
        String[] split = oldPath.split(File.separator);
        String filePath = noteCache + split[split.length - 1];
        //        Logger.w(filePath);
        //首先判断缓存目录是否存在
        File createdir = new File(noteCache);
        if (!createdir.exists()) {
            createdir.mkdirs();
        }
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        //复制图片到缓存目录
        try {
            inputStream = new FileInputStream(oldPath);
            outputStream = new FileOutputStream(filePath);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, byteRead);
            }
            outputStream.flush();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOStreamUtil.close(inputStream, outputStream);

        }

        return null;
    }

    private void insertImg(String path) {
        String tagPath = "<img src=\"" + path + "\"/>";//为图片路径加上<img>标签
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap != null) {
            SpannableString ss = getBitmapMime(path, tagPath);
            insertPhotoToEditText(ss);
            ed.append("\n");
            Log.d("xz", ed.getText().toString());
        }
    }
    //endregion

    //region 将图片插入到EditText中
    private void insertPhotoToEditText(SpannableString ss) {
        Editable et = ed.getText();
        int start = ed.getSelectionStart();
        et.insert(start, ss);
        ed.setText(et);
        ed.setSelection(start + ss.length());
        ed.setFocusableInTouchMode(true);
        ed.setFocusable(true);
    }
    //endregion

    private SpannableString getBitmapMime(String path, String tagPath) {
        SpannableString ss = new SpannableString(tagPath);//这里使用加了<img>标签的图片路径

        int width = ScreenUtil.getScreenWidth(EditActivity.this);
        int height = ScreenUtil.getScreenHeight(EditActivity.this);


        Bitmap bitmap = ImageUtil.getSmallBitmap(path, width, 480);
        ImageSpan imageSpan = new ImageSpan(this, bitmap);
        ss.setSpan(imageSpan, 0, tagPath.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}