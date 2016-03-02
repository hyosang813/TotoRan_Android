package raksam.com.totoran;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by hyosang813 on 16/02/27.
 * httpから取得してパースしたデータをDBに突っ込む
 */
public class SetDatabaseData {

    // SQLiteインスタンス
    private SQLiteDatabase db;

    //コンストラクタでDBへのアクセスインスタンスを取得しとく
    SetDatabaseData(Context context) {
        DataBaseHelper mDbHelper = new DataBaseHelper(context);
        try {
            mDbHelper.createEmptyDataBase();
            db = mDbHelper.openDataBaseWithWrite(); //書き込み可能なモードでDBオープン
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        } catch(SQLException sqle){
            throw sqle;
        }
    }

    //チーム略名テーブル(abbname)にwebから取得したデータを突っ込む
    public boolean insertTeamNameList(ArrayList<ArrayList<String>> teamNameList) {
        for (ArrayList<String> insertData : teamNameList) {
            ContentValues cv = new ContentValues();
            cv.put("fullname", insertData.get(0));
            cv.put("name", insertData.get(1));

            //エラーだったら−1が返る
            if (db.insert("abbname", null, cv) == -1) return false;
        }

        //問題なければtrueを返す
        return true;
    }

    //回数テーブル(kaisu)にwebから取得したデータを突っ込む
    public boolean insertKaisu(ArrayList<String> startAndEndArray, String kaisu) {
        ContentValues cv = new ContentValues();
        cv.put("open_number", kaisu);
        cv.put("start_date", startAndEndArray.get(0));
        cv.put("end_date", startAndEndArray.get(1));

        //エラーだったら−1が返る
        if (db.insert("kaisu", null, cv) == -1) return false;

        //問題なければtrueを返す
        return true;
    }

    //組み合わせテーブル(kumiawase)にwebから取得したデータを突っ込む
    public boolean insertKumiawase(ArrayList<String> homeAwayArray, String kaisu) {

        //枠ID
        int wakuCount = 1;

        //二つ飛ばしでホームチーム名とアウェイチーム名
        for (int i = 0; i < homeAwayArray.size(); i += 2) {
            ContentValues cv = new ContentValues();
            cv.put("kaisu", kaisu);
            cv.put("id", wakuCount);
            cv.put("home_team", homeAwayArray.get(i));
            cv.put("away_team", homeAwayArray.get(i + 1));

            //エラーだったら−1が返る
            if (db.insert("kumiawase", null, cv) == -1) return false;

            wakuCount++;
        }

        //問題なければtrueを返す
        return true;
    }

    //組み合わせテーブル(kumiawase)にwebから取得した支持率データをアップデート
    public boolean updataKumiawaseRate(ArrayList <ArrayList<String>> rateArray, String kaisu) {

        //枠ID
        int wakuCount = 1;

        //三つ飛ばしでホーム支持率、ドロー支持率、アウェイ支持率
        for (int i = 0; i < 39; i += 3) {
            ContentValues cv = new ContentValues();
            cv.put("get_date", getNowDate());
            cv.put("home_rate", rateArray.get(0).get(i));
            cv.put("draw_rate", rateArray.get(0).get(i + 1));
            cv.put("away_rate", rateArray.get(0).get(i + 2));

            //bookの情報があれば一緒にアップデート
            if (rateArray.size() == 2) {
                cv.put("home_rate_b", rateArray.get(1).get(i));
                cv.put("draw_rate_b", rateArray.get(1).get(i + 1));
                cv.put("away_rate_b", rateArray.get(1).get(i + 2));
            }

            //where句の作成
            String whereStr = "kaisu = '" + kaisu + "' and id = " + wakuCount;

            //エラーだったら−1が返る
            if (db.update("kumiawase", cv, whereStr, null) == -1) return false;
            wakuCount++;
        }

        //問題なければtrueを返す
        return true;

    }

    /**
     * 現在日時をyyyy-MM-dd HH:mm形式で取得する
     */
    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPAN);
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    //明示的にDBをクローズ
    public void dbClose() {
        db.close();
    }

    //このインスタンス破棄時にdbをちゃんとクローズ　デストラクタっぽく使いたい
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        db.close();
    }
}
