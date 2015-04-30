package com.baidu.securerandom;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQuery;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;

import dalvik.system.DexClassLoader;


public class MainActivity extends Activity {
    String Activity_A = "Activity_A";
    String Activity_B = "Activity_B";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.print(this.Activity_A);
        System.out.print(this.Activity_B);

        testSecureRandom1();
        testSecureRandom2();
        testFileMode();
        testDBMode();
        testSharedPreferences();
        testFlagNewTask();
        testDynamicReceiver();
        testExec();
        testLogCat();
        testClipboardManager();
        testDexClassLoader(this.getPackageName());
    }

    Intent makeIntent () {
        return new Intent();
    }

    void testBranch(int a) {
        // int a = 10;
        if(a==1) {
            System.out.println("a=1");
        } else if(a==2) {
            System.out.println("a=2");
        } else if(a==3) {
            System.out.println("a=3");
        } else {
            System.out.println("a>=4");
        }
    }
    void testPendingIntent(Intent i, int a) {

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        // PendingIntent pi1 = PendingIntent.getActivity(this, 0, new Intent("com.icbc.ACTION.save"), 0);
        Intent t = new Intent();

        Intent m = makeIntent();
        PendingIntent pi22aaaa = PendingIntent.getActivity(this, 0, m, 0);

        if(a > 10) {
            // PendingIntent pi2 = PendingIntent.getActivity(this, 0, new Intent(this, PublicReceiver.class), 0);
            t.setAction("set_action_xxx");
            PendingIntent pi22 = PendingIntent.getActivity(this, 0, t, 0);
            return;
        }

        t.setClassName(this, "com.baidu.securerandom.share");

        PendingIntent pi3 = PendingIntent.getActivity(this, 0, t, 0);
        Notification notification = new Notification();
        notification.setLatestEventInfo(this, "aaa", "bb", pi3);
        nm.notify(0, notification);

    }
    void testDexClassLoader(String pkg) {
        DexClassLoader cl1 = new DexClassLoader("/sdcard/evil.apk", "/data/data/com.example.cl/.cache", "/data/data/com.example.cl/lib/", getClassLoader());
        // cl1.loadClass("evil");
        String path = "/data/data/" + pkg;
        DexClassLoader cl2 = new DexClassLoader(path, "/data/data/com.example.cl/.cache", "/data/data/com.example.cl/lib/", getClassLoader());

    }

    void testClipboardManager() {

        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);


        ClipData clip1 = ClipData.newPlainText("simple text","Hello, World!");

        Uri copyUri = Uri.parse("content://com.example.contacts/copy/lastname");
        ClipData clip2 = ClipData.newUri(getContentResolver(),"URI",copyUri);

        Intent appIntent = new Intent(this, MainActivity.class);
        ClipData clip3 = ClipData.newIntent("Intent",appIntent);

        clipboard.setPrimaryClip(clip1);
        clipboard.setPrimaryClip(clip2);
        clipboard.setPrimaryClip(clip3);

        // clipboard.setText(clip1); deprecated in API 11
    }


    void testSecureRandom1() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] b = new byte[] { 1, 2, 3, 4 };
        secureRandom.setSeed(b);
        // Prior to Android 4.2, the next line would always return the same number!
        System.out.println("r1: " + secureRandom.nextInt());
    }

    void testSecureRandom2() {
        SecureRandom secureRandom = new SecureRandom(new byte[] { 1, 2, 3, 4 });
        // Prior to Android 4.2, the next line would always return the same number!
        System.out.println("r1: " + secureRandom.nextInt());
    }

    void testFileMode() {
        try {
            FileOutputStream f = openFileOutput("fileoutput", Context.MODE_WORLD_READABLE);
            f.write(22);
            f.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    void testDBMode() {
        try {
            SQLiteDatabase s1 = openOrCreateDatabase("testdb1", Context.MODE_WORLD_READABLE, null);
            SQLiteDatabase s4 = openOrCreateDatabase("testdb4", Context.MODE_WORLD_WRITEABLE, null, null);
            s4.close();
        }catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    void testSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("preferences", Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", "James");
        editor.putString("age", "Gosling");
        editor.commit();
    }

    void testSharedPreferences2() {
        String name = getSharedPreferences("preferences", Context.MODE_PRIVATE).getString("name", "");
        System.out.println(name);
    }

    void testFlagNewTask() {
        Intent intent=new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        Intent i = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
        startActivity(i);
    }

    void testDynamicReceiver() {
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        }, new IntentFilter("xxxxxxxxxxxxxxxxxx"));


        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        }, new IntentFilter("yyyyyyyyyyyyyyyyyyy"), null, null);
    }

    void testLogCat(){
        Log.d("TIM", "aaaaaa");
        Log.d("TIM", "aaaaaa111111", new Throwable());
        Log.v("TIM", "bbbbbb");
    }

    void testExec() {
        try {
            Process p1 = Runtime.getRuntime().exec(new String[]{"/system/bin/ls","-l"}, new String[]{"a=1", "b=2"});
            Process p2 = Runtime.getRuntime().exec("/system/bin/ls", new String[]{"a=1", "b=2"}, new File("/system/bin/"));
            Process p3 = Runtime.getRuntime().exec(new String[]{"/system/bin/ls","-l"}, new String[]{"a=1", "b=2"}, new File("/system/bin/"));
            Process p4 = Runtime.getRuntime().exec("/system/bin/ls", new String[]{"a=1", "b=2"});
            Process p5 = Runtime.getRuntime().exec("/system/bin/ls");
            Process p6 = Runtime.getRuntime().exec(new String[]{"/system/bin/ls","-l"});

            Process p7 = new ProcessBuilder()
                    .command("/system/bin/ping", "android.com")
                    .redirectErrorStream(true)
                    .start();

            Runtime.getRuntime().load("/data/data/com.baidu.seclab/lib/libtest.so");
            Runtime.getRuntime().loadLibrary("test");

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    void r88() {
        Intent lv0_Intent = new Intent("com.baidu.android.pushservice.action.METHOD");
        lv0_Intent.addFlags(32);
        lv0_Intent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
        lv0_Intent.putExtra("method_version", "V2");
        sendBroadcast(lv0_Intent);
    }

    void r9() {
        Intent i1 = new Intent("1111111111");
        sendBroadcast(i1);

        // null permission
        Intent i2 = new Intent("2222222222");
        sendBroadcast(i2, "com.cc.com");

        Intent i3 = i();
        sendBroadcast(i3, "com.bb.com");
    }

    Intent i() {
        return new Intent(this, PublicReceiver.class);
    }
}
