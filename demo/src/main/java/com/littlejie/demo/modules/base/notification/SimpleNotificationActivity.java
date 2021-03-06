package com.littlejie.demo.modules.base.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.littlejie.core.base.BaseActivity;
import com.littlejie.demo.R;
import com.littlejie.demo.annotation.Description;
import com.littlejie.demo.modules.MainActivity;

import butterknife.OnClick;

/**
 * 为了方便,大部分通知都没设置对应的Action,即PendingIntent
 * 除了sendFlagAutoCancelNotification()方法
 */
@Description(description = "简单的 Notification Demo")
public class SimpleNotificationActivity extends BaseActivity {

    //Notification.FLAG_FOREGROUND_SERVICE    //表示正在运行的服务
    public static final String NOTIFICATION_TAG = "littlejie";
    public static final int DEFAULT_NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;

    @Override
    protected int getPageLayoutID() {
        return R.layout.activity_simple_notification;
    }

    @Override
    protected void initData() {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initViewListener() {

    }

    @OnClick(R.id.btn_remove_all_notification)
    void removeAllNotification() {
        //移除当前 Context 下所有 Notification,包括 FLAG_NO_CLEAR 和 FLAG_ONGOING_EVENT
        mNotificationManager.cancelAll();
    }

    @OnClick(R.id.btn_remove_all_notification)
    void removeNotification() {
        //移除 ID = 1 的 Notification,注意:该方法只针对当前 Context。
        mNotificationManager.cancel(DEFAULT_NOTIFICATION_ID);
    }

    /**
     * 发送最简单的通知,该通知的ID = 1
     */
    @OnClick(R.id.btn_send_notification)
    void sendNotification() {
        //这里使用 NotificationCompat 而不是 Notification ,因为 Notification 需要 API 16 才能使用
        //NotificationCompat 存在于 V4 Support Library
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Hi,My id is 1");
        mNotificationManager.notify(DEFAULT_NOTIFICATION_ID, builder.build());
    }

    /**
     * 使用notify(String tag, int id, Notification notification)方法发送通知
     * 移除对应通知需使用 cancel(String tag, int id)
     */
    @OnClick(R.id.btn_send_notification_with_tag)
    void sendNotificationWithTag() {
        //发送一个 ID = 1 并且 LANGUAGE = littlejie 的 Notification
        //注意:此处发送的通知与 sendNotification() 发送的通知并不冲突
        //因为此处的 Notification 带有 LANGUAGE
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Send Notification With Tag")
                .setContentText("Hi,My id is 1,tag is " + NOTIFICATION_TAG);
        mNotificationManager.notify(NOTIFICATION_TAG, DEFAULT_NOTIFICATION_ID, builder.build());
    }

    @OnClick(R.id.btn_remove_notification_with_tag)
    void removeNotificationWithTag() {
        //移除一个 ID = 1 并且 LANGUAGE = littlejie 的 Notification
        //注意:此处移除的通知与 NotificationManager.cancel(int id) 移除通知并不冲突
        //因为此处的 Notification 带有 LANGUAGE
        mNotificationManager.cancel(NOTIFICATION_TAG, DEFAULT_NOTIFICATION_ID);
    }

    /**
     * 循环发送十个通知
     */
    @OnClick(R.id.btn_send_ten_notification)
    void sendTenNotifications() {
        for (int i = 0; i < 10; i++) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Send Notification Batch")
                    .setContentText("Hi,My id is " + i);
            mNotificationManager.notify(i, builder.build());
        }
    }

    /**
     * 设置FLAG_NO_CLEAR
     * 该 flag 表示该通知不能被状态栏的清除按钮给清除掉,也不能被手动清除,但能通过 cancel() 方法清除
     * Notification.flags属性可以通过 |= 运算叠加效果
     */
    @OnClick(R.id.btn_send_flag_no_clear_notification)
    void sendFlagNoClearNotification() {
        //发送 ID = 1, flag = FLAG_NO_CLEAR 的 Notification
        //下面两个 Notification 的 ID 都为 1,会发现 ID 相等的 Notification 会被最新的替换掉
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Send Notification Use FLAG_NO_CLEAR")
                .setContentText("Hi,My id is 1,i can't be clear.");
        Notification notification = builder.build();
        //设置 Notification 的 flags = FLAG_NO_CLEAR
        //FLAG_NO_CLEAR 表示该通知不能被状态栏的清除按钮给清除掉,也不能被手动清除,但能通过 cancel() 方法清除
        //flags 可以通过 |= 运算叠加效果
        notification.flags |= Notification.FLAG_NO_CLEAR;
        mNotificationManager.notify(DEFAULT_NOTIFICATION_ID, notification);
    }

    /**
     * 设置FLAG_AUTO_CANCEL
     * 该 flag 表示用户单击通知后自动消失
     */
    @OnClick(R.id.btn_send_flag_auto_cancecl_notification)
    void sendFlagAutoCancelNotification() {
        //设置一个Intent,不然点击通知不会自动消失
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Send Notification Use FLAG_AUTO_CLEAR")
                .setContentText("Hi,My id is 1,i can be clear.")
                .setContentIntent(resultPendingIntent);
        Notification notification = builder.build();
        //设置 Notification 的 flags = FLAG_NO_CLEAR
        //FLAG_AUTO_CANCEL 表示该通知能被状态栏的清除按钮给清除掉
        //等价于 builder.setAutoCancel(true);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(DEFAULT_NOTIFICATION_ID, notification);
    }

    /**
     * 设置FLAG_ONGOING_EVENT
     * 该 flag 表示发起正在运行事件（活动中）
     */
    @OnClick(R.id.btn_send_flag_ongoing_event_notification)
    void sendFlagOngoingEventNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Send Notification Use FLAG_ONGOING_EVENT")
                .setContentText("Hi,My id is 1,i can't be clear.");
        Notification notification = builder.build();
        //设置 Notification 的 flags = FLAG_NO_CLEAR
        //FLAG_ONGOING_EVENT 表示该通知通知放置在正在运行,不能被手动清除,但能通过 cancel() 方法清除
        //等价于 builder.setOngoing(true);
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(DEFAULT_NOTIFICATION_ID, notification);
    }

    @Override
    protected void process() {

    }

}
