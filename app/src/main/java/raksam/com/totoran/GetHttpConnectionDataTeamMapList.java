package raksam.com.totoran;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by hyosang813 on 16/02/26.
 * http通信でジオシティーズのチーム略名データを取得するクラス
 * okhttpを採用
 */
public class GetHttpConnectionDataTeamMapList extends AsyncTask<String, String, String> {
    //終了通知スイッチ
    public ArrayList<String> finishSwitch;

    //ヘルパークラスに渡すコンテキスト
    private Context context;

    //コンストラクタで終了通知スイッチをメインActivityからもらっておく
    public GetHttpConnectionDataTeamMapList(ArrayList<String> finishSwitch, Context context) {
        this.finishSwitch = finishSwitch;
        this.context = context;
    }

    /**
     *バックグラウンド(メイン以外のスレッド)での処理
     */
    @Override
    protected String doInBackground(String... params) {

        //htmlデータの取得
        Document document = new Document(params[0]);
        try {
            document = Jsoup.connect(params[0]).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //htmlパース(tdタグのデータに絞る)
        Elements tdTagDataList = document.body().children().select("td");

        //DBに登録する二次元Arrayにデータを整理
        ArrayList<ArrayList<String>> teamNameList = new ArrayList<>();
        for (int i = 0; i < tdTagDataList.size(); i += 2) {
            ArrayList<String> fullAndAbridList = new ArrayList<>();
            fullAndAbridList.add(tdTagDataList.get(i).text());
            fullAndAbridList.add(tdTagDataList.get(i + 1).text());
            teamNameList.add(fullAndAbridList);
        }

        //DBに登録
        SetDatabaseData setDb = new SetDatabaseData(context);
        if (!setDb.insertTeamNameList(teamNameList)) {
            Log.v("TAG", "DB登録失敗！！");
        }

        //一応1秒くらい待って
        try {Thread.sleep(1000);} catch (Exception e) {}

        //終了通知スイッチに値を追加
        this.finishSwitch.add("FINISH");

        //onPostExecuteの引数になるみたいだけど処理させる内容今の所なし
        return "";
    }

    /**
     *バックグラウンドでの処理終了後にメインスレッドで行う処理
     */
//    @Override
//    protected void onPostExecute(String s) {
//        super.onPostExecute(s);
//
//    }
}


/**
 * okhttpでのhtml取得方法は以下
 * jsoup使ったら不要かも
 */
////リクエストオブジェクトを生成　params配列に引数で渡されたurlが入ってる
//Request request = new Request.Builder()
//        .url(params[0])
//        .get()
//        .build();
//
////htmlデータをStringで取得
//OkHttpClient client = new OkHttpClient();
//String htmlStr = "";
//try {htmlStr = client.newCall(request).execute().body().string();} catch (IOException e) {}

