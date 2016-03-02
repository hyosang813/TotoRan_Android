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

import java.security.cert.CertificateParsingException;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity {

    //共通クラスの取得
    protected Common common; // グローバル変数を扱うクラス

    //諸々の問題チェック状態確認フラグ(初期値はtrue)
    boolean issueCheckFlg = true;

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
            issueMessage = "当アプリはネットワーク通信による情報取得が必須です。\n端末が通信できる状態でアプリを再起動してください。";
            issueCheckFlg = false;
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
        indicator.show();

        //データベースインスタンスを取得
        final CheckDatabaseData cdb = new CheckDatabaseData(me);

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
                            issueCheckFlg = false;
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

                        //インジケータの終了
                        indicator.dismiss();
                        indicator = null;
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

        return info != null && info.isConnected();
//
//        if( info != null ){
//            return info.isConnected();
//        } else {
//            return false;
//        }
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
