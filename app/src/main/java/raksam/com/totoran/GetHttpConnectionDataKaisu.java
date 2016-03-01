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
 * Created by hyosang813 on 16/02/26.
 * http通信で楽天totoの開催回データを取得するクラス
 * okhttpを採用
 */
public class GetHttpConnectionDataKaisu extends AsyncTask<String, String, String> {
    //終了通知スイッチ
    public ArrayList<String> finishSwitch;

    //ヘルパークラスに渡すコンテキスト
    private Context context;

    //コンストラクタ
    public GetHttpConnectionDataKaisu(ArrayList<String> finishSwitch, Context context) {
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

        //htmlパース(href属性のデータに絞る)
        Elements hrefTagDataList = document.getElementsByAttribute("href");

        //特定の文字列をキーに回数(4桁)を抽出
        ArrayList<String> kaisuArray = new ArrayList<>();
        for (Element hrefData : hrefTagDataList) {
            String getStr = hrefData.text();
            if (getStr.contains("販売くじ種: toto") && !getStr.contains("販売くじ種: toto Goal")) {
                //４桁対応しとこうかね
                String firstStr = getStr.substring(getStr.indexOf("第") + 1);  //「第」の後の文字列
                String secondStr = firstStr.substring(0,firstStr.indexOf("回")); //「回」の後の文字列
                String addStr = secondStr.length() == 3 ? "0" + secondStr : secondStr; //３文字なら前に「0」を足す
                kaisuArray.add(addStr);
            }
        }

        //DBインスタンスを取得しておく
        SetDatabaseData setDb = new SetDatabaseData(context);

        //回数ごとの詳細データを取得
        for (String kaisu : kaisuArray) {
            //URLの生成
            String urlStr = "http://www.toto-dream.com/dci/I/IPA/IPA01.do?op=disptotoLotInfo&holdCntId=" + kaisu;

            //htmlデータの取得
            Document documentDetail = new Document(urlStr);
            try {
                documentDetail = Jsoup.connect(urlStr).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //htmlパース(開始日と終了日のデータに絞る)
            Elements startAndEndElements = documentDetail.getElementsByTag("td").attr("getKey", "class");

            //開始日と終了日を整形
            int startAndEndCount = 0;
            ArrayList<String> startAndEndArray = new ArrayList<>();
            for (Element startAndEndElement : startAndEndElements) {
                if (startAndEndElement.toString().contains("type5")) {
                    //まずは前後のトリム
                    String trimStr = startAndEndElement.text().trim();

                    //年月日は共通
                    String yearStr = trimStr.substring(0, 4) + "-" + trimStr.substring(5, 7) + "-" + trimStr.substring(8, 10) + " " ;

                    //時分秒は処理ディスパッチ
                    String hourStr = "";
                    if (startAndEndCount == 0) {
                        hourStr = trimStr.substring(14, 16) + ":" + trimStr.substring(17, 19);
                    } else {
                        hourStr = trimStr.substring(19, 21) + ":" + trimStr.substring(22, 24);
                    }

                    //整形した年月日時分を格納
                    startAndEndArray.add(yearStr + hourStr);
                    startAndEndCount++;

                    //開始日と終了日だけ取得したらループ終了
                    if (startAndEndCount == 2) break;
                }
            }

            //回数テーブルでデータをinsert
            if (!setDb.insertKaisu(startAndEndArray, kaisu)) {
                Log.v("TAG", "DB登録失敗！！");
            }

            //htmlパース(組み合わせテーブルデータ)
            Elements wakuDetailElements = documentDetail.getElementsByTag("td");

            //組み合わせデータを取得
            ArrayList<String> homeAwayArray = new ArrayList<>();
            int wakuCount = 0;
            for (Element wakuDetailElement : wakuDetailElements) {
                if (wakuDetailElement.toString().contains("185")) {
                    homeAwayArray.add(wakuDetailElement.text());
                    wakuCount++;
                }
                //13枠分取得したらループ終了
                if (wakuCount == 26) break;
            }

            //回数テーブルでデータをinsert
            if (!setDb.insertKumiawase(homeAwayArray, kaisu)) {
                Log.v("TAG", "DB登録失敗！！");
            }
        }

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

