package raksam.com.totoran;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends FragmentActivity {

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    //ネットワーク通信の問題チェック状態確認フラグ(初期値はtrue)
    boolean networkIssueCheckFlg = true;

    //開催期間じゃない場合チェック状態確認フラグ(初期値はtrue)
    boolean notOpenCheckFlg = true;

    //問題発生時の対応メッセージ格納変数
    String issueMessage = "";

    //自分自身のコンテキスト
    Context me;

    //インジケータ
    ProgressDialog indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ネットワーク接続可能判断
        if (!netWorkCheck(this)) {
            issueMessage = "当アプリはネットワーク通信による情報取得が必須です。\n通信できる状態でアプリを再起動してください。";
            networkIssueCheckFlg = false;
            showDialog();
            return;
        }

        //起動後は共通クラスを初期化
        common = (Common) getApplication();
        common.init();

        //自分自身のコンテキスト
        me = this;

        //非同期処理の開始
        getHttpData();
    }

    //非同期処理の切り出し
    private void getHttpData() {

        //インジケータ開始
        indicator = new ProgressDialog(this);
        indicator.setMessage("データ取得中...");
        indicator.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        indicator.setCanceledOnTouchOutside(false); //インジケータ回転中はタップを無効にする
        indicator.show();

        //データベースインスタンスを取得
        final CheckDatabaseData cdb = new CheckDatabaseData(me);

        /**
         * jdeferred ってのを使うとよりシンプルに非同期の連携が書けそう
         */
        //非同期処理の開始
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... params) {
                //開催(回数テーブル)のデータが古いかどうかチェックして古ければデータ再取得
                if(cdb.kaisaiTableDataCheck()) {
                    //チーム名マップリストと回数テーブルとkumiawaseテーブルのデータをインサート
                    new GetTeamMapList().getTeamMapList("http://www.geocities.jp/totorandata/", me);
                    new GetKaisuTableData().getKaisuTableData("https://toto.rakuten.co.jp/toto/schedule/", me);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //各テーブルのinsertが終わったら別の非同期スレッドで対象回数totoと(あれば)bookの支持率取得の非同期処理を開始
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(final Void... params) {
                        String holdNumber = cdb.holdCheck();
                        if(!holdNumber.equals("Non")) { //現在が開催期間であればtotoとbookの支持率データ取得(kaisuテーブル参照)
                           if (cdb.rateGetCheck(holdNumber)) { //データ取得時間がnullだったり、１時間以上前だったらデータ取得(kumiawaseテーブル参照)
                               new GetRateTotoAndBook().getRateTotoAndBook(holdNumber, me);
                               cdb.dbClose(); //DBクローズ
                           }
                        } else {
                            issueMessage = "現在開催中のtotoはありません。";
                            notOpenCheckFlg = false;
                            showDialog();
                            cdb.dbClose(); //DBクローズ
                            indicator.dismiss(); //インジケータの終了
                            indicator = null; //インジケータの終了
                            cancel(true); //cancelすることで次の処理はスルー
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        //支持率の取得が終わったら各種データをcommonに格納
                        getDatabaseData();

                        /**
                         * 連打時のリークやぬるぽを防ぐために0.5secのタイムラグを設けようかね
                         * リークが発生したりインジケータ終了のタイミングでnullチェックしないと落ちたりする
                         * 連打が鍵？？？
                         */
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {

                        }

                        //インジケータの終了
                        if (indicator != null) indicator.dismiss();
                        indicator = null;

                        //念のため開催フラグをtrueにしとく
                        notOpenCheckFlg = true;
                    }
                }.execute();
            }
        }.execute();
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

        //締切日時を表示
        showDeadLineDate(common.numberOfTimes, common.deadLineTime);

        //DB操作インスタンスの破棄
        dbData.dbClose();
    }

    //「シングル」ボタン押下時はシングル選択画面に画面遷移
    public void singleChoiceTransition(View v) {
        if (networkIssueCheckFlg && notOpenCheckFlg) {
            startActivity(new Intent(MainActivity.this, SingleChoiceActivity.class));
        } else {
            showDialog();
        }
    }

    //「マルチ」ボタン押下時はマルチ選択画面に画面遷移
    public void multiChoiceTransition(View v) {
        if (networkIssueCheckFlg && notOpenCheckFlg) {
            startActivity(new Intent(MainActivity.this, MultiChoiceActivity.class));
        } else {
            showDialog();
        }
    }

    //「データ再取得」ボタン押下時は強制再取得
    public void dataReGetAll(View v) {
        if (networkIssueCheckFlg) {
            //テーブルデータを削除して再取得
            CheckDatabaseData cdb = new CheckDatabaseData(me);
            cdb.tableDataDelete();
            getHttpData();
        } else {
            showDialog();
        }
    }

    //「支持率確認」ボタン押下時は支持率確認画面に遷移
    public void oddsConfirm(View v) {
        if (networkIssueCheckFlg && notOpenCheckFlg)  {
            startActivity(new Intent(MainActivity.this, RateDonfirmActivity.class));
        } else {
            showDialog();
        }
    }

    //回数と締切日時を表示
    private void showDeadLineDate(String numberOfTimes, String deadLine) {
        //回数の先頭0取っぱらい処理と最終表示text生成
        String showNumber = numberOfTimes.substring(0, 1).equals("0") ? numberOfTimes.substring(1, numberOfTimes.length()) : numberOfTimes;
        String finalShowText =  "対象回:第" + showNumber + "回 toto\n" + deadLine;

        //対象TextViewを取得して表示
        TextView deadView = (TextView)findViewById(R.id.deadline);
        deadView.setText(finalShowText);
    }

    // ネットワーク接続確認
    private static boolean netWorkCheck(Context context){
        ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    //メッセージダイアログ
    private void showDialog() {
        OkAlertDialogFragment dialog = new OkAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "注意");
        args.putString("message",issueMessage);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

}
