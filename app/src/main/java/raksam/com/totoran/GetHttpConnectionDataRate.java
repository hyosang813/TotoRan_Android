package raksam.com.totoran;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hyosang813 on 16/02/29.
 * totoの支持率取得
 */
public class GetHttpConnectionDataRate extends AsyncTask<String, String, String> {
    //終了通知スイッチ
    public ArrayList<String> finishSwitch;

    //ヘルパークラスに渡すコンテキスト
    private Context context;

    //コンストラクタ
    public GetHttpConnectionDataRate(ArrayList<String> finishSwitch, Context context) {
        this.finishSwitch = finishSwitch;
        this.context = context;
    }

    /**
     *バックグラウンド(メイン以外のスレッド)での処理
     */
    @Override
    protected String doInBackground(String... params) {

        //urlの作成(toto)
        String rateUlrToto = String.format("http://www.toto-dream.com/dci/I/IPC/IPC01.do?op=initVoteRate&holdCntId=%s&commodityId=01", params[0]);

        //htmlデータの取得
        Document documentRateToto = new Document(rateUlrToto);
        try {
            documentRateToto = Jsoup.connect(rateUlrToto).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //htmlパース(支持率のデータに絞る)
        Elements totoRateElements = documentRateToto.getElementsByClass("pernum");

        ArrayList<String> totoRateArray = new ArrayList<>();
        for (Element totoRateElement : totoRateElements) {
            //カッコ内の％の数字のみを抽出
            String targetStr = totoRateElement.text().trim();
            String firstStr = targetStr.substring(targetStr.indexOf("（") + 1);  //「（」の後の文字列
            totoRateArray.add(firstStr.substring(0,firstStr.indexOf("%"))); //「%」の前の文字列
        }

        //DBにupdate
        SetDatabaseData setDb = new SetDatabaseData(context);
        if (!setDb.updataKumiawaseTotoRate(totoRateArray, params[0])) {
            Log.v("TAG", "DB登録失敗！！");
        }

        /**
         * ここからbODDS
         */
        //urlの作成(book)
//        String holdNum = "827";
        String holdNum = params[0];

        if (holdNum.substring(0, 1).equals("0")) holdNum = holdNum.substring(1, holdNum.length()); //先頭が「0」だったら取っ払う
        String rateUlrBook = String.format("http://tobakushi.net/toto/tototimes/bg_%s.html", holdNum);

        //htmlデータの取得
        Document documentRateBook = new Document(rateUlrBook);
        try {
            documentRateBook = Jsoup.connect(rateUlrBook).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //htmlパース(支持率のデータに絞る)
        Elements bookRateElements = documentRateBook.getElementsByClass("ave");

        //42個だったらbODDSがないから終了
        if (bookRateElements.size() <= 42) {
            this.finishSwitch.add("OK");
            return "";
            /**
             * iOS版はbODDSがなければ「0.0」をdbに入れて使う時に「--.--」に変換してる
             * 意味わからんからnullにしといてselectする時に「--.--」なりなんなりにすりゃいいんじゃね？
             */
        }

        ArrayList<String> bookRateArray = new ArrayList<>();
        int wakuCount = 0;
        for (Element bookRateElement : bookRateElements) {
            //modが0,1,2はスルー、3,4,5は格納
            if (wakuCount % 6 > 2) {
                String targetStr = bookRateElement.text().trim();
                bookRateArray.add(targetStr.substring(0,targetStr.indexOf("%"))); //「%」の前の文字列
            }

            wakuCount++;

            //最後の１行は14枠だから不要
            if (wakuCount >= 6 * 13) break;
        }

        //DBにupdate
        if (!setDb.updataKumiawaseBookRate(bookRateArray, params[0])) {
            Log.v("TAG", "DB登録失敗！！");
        }

        //DBクローズ
        setDb.dbClose();

        this.finishSwitch.add("OK");
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
