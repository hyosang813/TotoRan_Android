package raksam.com.totoran;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity {

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    //諸々の問題チェック状態確認フラグ(初期値はtrue)
    boolean issueCheckFlg = true;

    //問題発生時の対応メッセージ格納変数
    String issueMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ネットワーク接続可能判断
        if (!netWorkCheck(this)) {
            issueMessage = "当アプリはネットワーク通信による情報取得が必須です。\n端末が通信できる状態でアプリを再起動してください。";
            issueCheckFlg = false;
            showDialog();
            return;
        }

        //起動後は共通クラスを初期化
        common = (Common) getApplication();
        common.init();

        //http通信全終了を知るためのスイッチ
        ArrayList<String> finishSwitch = new ArrayList<>();

        //チーム略名リストの取得
//        new GetHttpConnectionDataTeamMapList(finishSwitch, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://www.geocities.jp/totorandata/");

        //楽天totoの回数データの取得
//        new GetHttpConnectionDataKaisu(finishSwitch, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"https://toto.rakuten.co.jp/toto/schedule/");

        /**
         * 上記の取得処理(DB格納)が完了してないと、以下の処理はやっちゃダメ
         * コールバック処理が必要かな？？
         */
        //現在が開催期間であればtotoとbookの支持率データ取得
        GetDatabaseData gdb = new GetDatabaseData(this);
        String holdNumber = gdb.holdCheck();
        if(!holdNumber.equals("Non")) {
            //支持率データ取得
            new GetHttpConnectionDataRate(finishSwitch, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,holdNumber);

        } else {
            /**
             * 開催中じゃない旨のアラート
             */
        }


        /**
         * gdbは最後にクローズしようねー
         */




//        //全ての通信からDBへの格納処理が終了するまでwhile
//        int count = 0;
//        while (true) {
//            if (finishSwitch.size() == 1) {
//                Log.v("TAG", "完了！！！！！！！！！！！！！");
//                break;
//            }
//            Log.v("TAG", String.valueOf(count++));
//            try {Thread.sleep(500);} catch (Exception e) {} //whileを回す間隔を0.5秒にしよう
//        }
//
//
        //データをDBから取得
        getDatabaseData();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("TAG", "すたああああああああああああああああああとおおおおおおおおおおおおおおお");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("TAG", "れじゅううううううううううううむうううううううううううううううううううううう");
    }

    //DBから必要情報をゲットしてcommonに格納する
    private void getDatabaseData() {
        //DB操作インスタンスの取得
        GetDatabaseData dbData = new GetDatabaseData(this);

        //現在開催中の回数を取得
        common.numberOfTimes = dbData.getOpenNumberAndEndDate().get(0);

        //現在開催中の締切日を取得
        common.deadLineTime = dbData.getOpenNumberAndEndDate().get(1);

        //データの取得年月日を取得
        common.dataGetTime = dbData.getDataGetTime(common.numberOfTimes);

        //チーム名情報を取得
        common.teamNameArray = dbData.getTeamNameArray(common.numberOfTimes);

        //toto支持率データの取得
        common.totoRateArray = dbData.getRateArray(common.numberOfTimes).get(0);

        //book支持率データの取得
        common.bookRateArray = dbData.getRateArray(common.numberOfTimes).get(1);

        //DB操作インスタンスの破棄
        dbData.dbClose();
    }

    //「シングル」ボタン押下時はシングル選択画面に画面遷移
    public void singleChoiceTransition(View v) {
        if (issueCheckFlg) {
            startActivity(new Intent(MainActivity.this, SingleChoiceActivity.class));
        } else {
            showDialog();
        }

    }

    //「マルチ」ボタン押下時はマルチ選択画面に画面遷移
    public void multiChoiceTransition(View v) {
        if (issueCheckFlg) {
            startActivity(new Intent(MainActivity.this, MultiChoiceActivity.class));
        } else {
            showDialog();
        }
    }

    // ネットワーク接続確認
    public static boolean netWorkCheck(Context context){
        ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if( info != null ){
            return info.isConnected();
        } else {
            return false;
        }
    }

    //コピーボタン押下時の挙動
    private void showDialog() {
        OkAlertDialogFragment dialog = new OkAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "注意");
        args.putString("message",issueMessage);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

}
