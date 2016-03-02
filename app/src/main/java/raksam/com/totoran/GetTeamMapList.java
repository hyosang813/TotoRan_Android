package raksam.com.totoran;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hyosang813 on 16/03/02.
 * http通信でジオシティーズのチーム略名データを取得するクラス
 * の修正版
 * ※こっちのクラス側じゃなくて呼び出し側で非同期開始
 */
public class GetTeamMapList {

    public void getTeamMapList(String url, Context context) {

        //htmlデータの取得
        Document document = new Document(url);
        try {
            document = Jsoup.connect(url).get();
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

        //DBクローズ
        setDb.dbClose();
    }
}
