package raksam.com.totoran;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hyosang813 on 16/03/02.
 * totoとbODDSの支持率取得
 * 修正版
 */
public class GetRateTotoAndBook {
    public void getRateTotoAndBook(String holdNumber, Context context) {

        //urlの作成(toto)
        String rateUlrToto = String.format("http://www.toto-dream.com/dci/I/IPC/IPC01.do?op=initVoteRate&holdCntId=%s&commodityId=01", holdNumber);

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

        //DBクラスに渡す二次元ArrayにまずはtotoRateをセット
        ArrayList<ArrayList<String>> rateArray = new ArrayList<>();
        rateArray.add(totoRateArray);

        /**
         * ここからbODDS
         */
        String holdNum = holdNumber;

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

        //42個だったらbODDSはない バッファを見て７０以上
        if (bookRateElements.size() > 70) {
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

            //DB登録用Arrayに追加
            rateArray.add(bookRateArray);
        }

        //DBにupdate
        SetDatabaseData setDb = new SetDatabaseData(context);
        if (!setDb.updataKumiawaseRate(rateArray, holdNumber)) {
            Log.v("TAG", "DB登録失敗！！");
        }

        //DBクローズ
        setDb.dbClose();
    }
}
