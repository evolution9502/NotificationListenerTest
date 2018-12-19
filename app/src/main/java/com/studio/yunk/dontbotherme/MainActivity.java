package com.studio.yunk.dontbotherme;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.studio.yunk.dontbotherme.Fragment.LineItemFragment;
import com.studio.yunk.dontbotherme.Fragment.MessengerFragment;
import com.studio.yunk.dontbotherme.Fragment.WeChatFragment;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "DBM_MainActivity";
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    private AlertDialog enableNotificationListenerAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.container);

        mTabHost.addTab(mTabHost.newTabSpec("one")
                        .setIndicator("LINE",getResources().getDrawable(R.drawable.line_logo_320))
                ,LineItemFragment.class,null);

        //同上方Tab設定，不同處為帶入參數的差異
        mTabHost.addTab(mTabHost.newTabSpec("two")
                        .setIndicator("Messenger",getResources().getDrawable(R.drawable.messenger_logo_320))
                ,MessengerFragment.class,null);

        //同上方Tab設定，不同處為帶入參數的差異
        mTabHost.addTab(mTabHost.newTabSpec("three")
                        .setIndicator("WeChat",getResources().getDrawable(R.drawable.wechat_logo_320))
                ,WeChatFragment.class, null);

        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }
    }

    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }
}
