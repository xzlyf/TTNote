package com.xz.ttnote.activity;

import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.Logger;
import com.xz.ttnote.R;
import com.xz.ttnote.adapter.NoteAdapter;
import com.xz.ttnote.base.BaseActivity;
import com.xz.ttnote.constant.Local;
import com.xz.ttnote.callback.OnItemClickListener;
import com.xz.ttnote.callback.OnItemLongClickListener;
import com.xz.ttnote.entity.Note;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;
    @BindView(R.id.tv_menu)
    ImageView tvMenu;
    @BindView(R.id.note_recycler)
    RecyclerView noteRecycler;
    private NoteAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean homeAsUpEnabled() {
        return false;
    }

    @Override
    protected void initData() {

        //初始化存储位置
        Local.storage = getFilesDir().getAbsolutePath() + "/note/";
        Local.cache = getCacheDir().getAbsolutePath() + "/note/";

        initRecycler();
        try {
            getLocalNote();
        } catch (IOException e) {
            e.printStackTrace();
            sToast("资源文件读取失败");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            getLocalNote();
        } catch (IOException e) {
            e.printStackTrace();
            sToast("资源文件读取失败");
        }
    }

    /**
     * 获取本地便签资源
     */
    private void getLocalNote() throws IOException {
        File file = new File(Local.storage);
        if (!file.exists()) {
            return;
        }
        List<Note> mlist = new ArrayList<>();
        Note note ;
        File[] files = file.listFiles();
        InputStream instream;
        InputStreamReader inputreader;
        BufferedReader buffreader;
        String line = "";
        File isexis;
        for (File f : files) {
            isexis = new File(f+"/note.tt");
            if (!isexis.exists()){
                break;
            }
            note = new Note();
            instream = new FileInputStream(isexis);
            if (instream != null) {
                String content = "";
                inputreader = new InputStreamReader(instream,   "UTF-8");
                buffreader = new BufferedReader(inputreader);

                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    content += line + "\n";
                }
                instream.close();//关闭输入流
                note.setContent(content);
                note.setDate(f.getName());
                mlist.add(note);
            }
        }
        //反转list ，时间前在前
//        Collections.reverse(mlist);
        adapter.refresh(mlist);

    }

    private void initRecycler() {
        adapter = new NoteAdapter(this);
        noteRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(Note note) {

                startActivity(new Intent(MainActivity.this, EditActivity.class).putExtra("note",note));

            }
        });

        adapter.setOnItemlongClickListener(new OnItemLongClickListener() {
            @Override
            public void onClick(int posi) {
                //长按事件
//                sToast("测试");

            }
        });

    }

    @OnClick(R.id.btn_add)
    public void add() {
        startActivity(new Intent(this, EditActivity.class));
    }


}
