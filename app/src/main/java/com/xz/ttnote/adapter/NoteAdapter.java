package com.xz.ttnote.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.Logger;
import com.xz.ttnote.R;
import com.xz.ttnote.callback.OnItemClickListener;
import com.xz.ttnote.callback.OnItemLongClickListener;
import com.xz.ttnote.constant.Local;
import com.xz.ttnote.entity.Note;
import com.xz.ttnote.utils.FileIOUtil;
import com.xz.ttnote.utils.TimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Note> mList;
    private OnItemClickListener mlistener;
    private OnItemLongClickListener mLongListener;

    public NoteAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<>();
    }

    public void refresh(List<Note> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;

    }

    public void setOnItemlongClickListener(OnItemLongClickListener listener) {
        mLongListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvContent.setText(Html.fromHtml(mList.get(position).getContent()));
        holder.tvDate.setText(TimeUtil.getSimDate("MM月dd日 HH:mm", Long.valueOf(mList.get(position).getDate())));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView tvDate;
        TextView delete;
        TextView share;
        LinearLayout layout;
        FrameLayout layout2;
        private TranslateAnimation mShowAction;
        private TranslateAnimation mHideAction;
        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (layout2.getVisibility() == View.VISIBLE) {
                    layout2.startAnimation(mHideAction);
                    layout2.setVisibility(View.GONE);
                }
            }
        };

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //初始化动画
            initAnim();
            //自动隐藏菜单
            Runnable autoHide = new Runnable() {
                @Override
                public void run() {
                    handler.sendMessage(handler.obtainMessage());
                }
            };

            tvContent = itemView.findViewById(R.id.tv_content);
            tvDate = itemView.findViewById(R.id.tv_date);
            layout = itemView.findViewById(R.id.layout);
            layout2 = itemView.findViewById(R.id.layout_2);
            delete = itemView.findViewById(R.id.delete);
            share = itemView.findViewById(R.id.share);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onClick(mList.get(getLayoutPosition()));
                }
            });
            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onClick(getLayoutPosition());
//                   mList.remove(getLayoutPosition());
//                   notifyItemRemoved(getLayoutPosition());
                    if (layout2.getVisibility() == View.GONE) {
                        layout2.setVisibility(View.VISIBLE);
                        layout2.startAnimation(mShowAction);
                        //两秒后自动隐藏菜单
                        handler.postDelayed(autoHide, 2000);
                    }

                    return true;
                }
            });
            layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layout2.getVisibility() == View.VISIBLE) {
                        layout2.startAnimation(mHideAction);
                        layout2.setVisibility(View.GONE);

                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show();
                    FileIOUtil.deleteDir(new File(Local.storage+mList.get(getLayoutPosition()).getDate()));
                    mList.remove(getLayoutPosition());
                    notifyItemRemoved(getLayoutPosition());
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
                }
            });



        }

        private void initAnim() {
            //进入动画
            mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    1f, Animation.RELATIVE_TO_SELF, 0f);
            mShowAction.setInterpolator(new OvershootInterpolator());//插值器，上下弹动效果
            mShowAction.setDuration(300);
            //退出动画
            mHideAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0f, Animation.RELATIVE_TO_SELF, 1f);
            mHideAction.setDuration(300);
        }

    }
}
