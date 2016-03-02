package raksam.com.totoran;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hyosang813 on 16/02/26.
 * DBからデータをゲットする
 */

public class GetDatabaseData {
    // SQLiteインスタンス
    private SQLiteDatabase db;

    //コンストラクタでDBへのアクセスインスタンスを取得しとく
    GetDatabaseData(Context context) {
        DataBaseHelper mDbHelper = new DataBaseHelper(context);
        try {
            mDbHelper.createEmptyDataBase();
            db = mDbHelper.openDataBase(); //読み取り専用モードでDBオープン
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        } catch(SQLException sqle){
            throw sqle;
        }
    }

    //現在開催中の回数と締切日を取得
    public ArrayList<String> getOpenNumberAndEndDate() {
        String QueryStr = "select " +
                "open_number, " +
                "strftime('%Y年%m月%d日 %H時%M分締切', end_date) as end_date " +
                "from " +
                "kaisu " +
                "where " +
                "start_date < datetime('now', 'localtime') and " +
                "end_date > datetime('now', 'localtime') " +
                "order by " +
                "open_number " +
                "limit 1 ";

        //カーソルにクエリ結果を格納
        Cursor cursor = db.rawQuery(QueryStr, null);
        ArrayList<String> returnStr = new ArrayList<>();

        //カーソルから回数と締め切り日を取得
        if(cursor.moveToFirst()) {
            returnStr.add(cursor.getString(cursor.getColumnIndex("open_number")));
            returnStr.add(cursor.getString(cursor.getColumnIndex("end_date")));
        }

        //カーソルを閉じる
        cursor.close();

        //返す
        return returnStr;
    }

    //データ取得年月日を取得
    public String getDataGetTime(String kaisu) {
        String QueryStr = "select " +
                "strftime('%Y年%m月%d日 %H時%M分時点', get_date) as get_date " +
                "from " +
                "kumiawase " +
                "where " +
                "kaisu = '" + kaisu + "' " +
                "order by get_date " +
                "limit 1";

        //カーソルにクエリ結果を格納
        Cursor cursor = db.rawQuery(QueryStr, null);
        String returnStr = "";

        //カーソルから回数と締め切り日を取得
        if(cursor.moveToFirst()) returnStr = cursor.getString(cursor.getColumnIndex("get_date"));

        //カーソルを閉じる
        cursor.close();

        //返す
        return returnStr;
    }


    //ホームチーム名とアウェイチーム名の二次元Arrayを取得
    public ArrayList<ArrayList<String>> getTeamNameArray(String kaisu) {
        String QueryStr = "select " +
                "a.name as home_team, " +
                "b.name as away_team " +
                "from " +
                "(select " +
                "id, " +
                "name " +
                "from " +
                "kumiawase " +
                "left join abbname on (kumiawase.home_team like '%%'||abbname.fullname||'%%') " +
                "where " +
                "kaisu = '" + kaisu + "' " +
                ") a " +
                "inner join " +
                "(select " +
                "id, " +
                "name " +
                "from " +
                "kumiawase " +
                "left join abbname on (kumiawase.away_team like '%%'||abbname.fullname||'%%') " +
                "where " +
                "kaisu = '" + kaisu + "'" +
                ") b " +
                "on a.id = b.id " +
                "order by a.id";

        //カーソルにクエリ結果を格納
        Cursor cursor = db.rawQuery(QueryStr, null);
        ArrayList<ArrayList<String>> teamNameArray = new ArrayList<>();

        //カーソルを１行づつ回してホームチームとアウェイチームを取得
        if(cursor.moveToFirst()){
            do{
                ArrayList<String> teamNameArrayChild = new ArrayList<>();
                teamNameArrayChild.add(cursor.getString(cursor.getColumnIndex("home_team")));
                teamNameArrayChild.add(cursor.getString(cursor.getColumnIndex("away_team")));
                teamNameArray.add(teamNameArrayChild);
            }while(cursor.moveToNext());
        }

        //カーソルを閉じる
        cursor.close();

        //返す
        return teamNameArray;
    }

    //toto支持率とbook支持率の３次元Arrayを取得
    public ArrayList<ArrayList<ArrayList<String>>> getRateArray(String kaisu) {
        String QueryStr = "select " +
                "home_rate, " +
                "draw_rate, " +
                "away_rate, " +
                "home_rate_b, " +
                "draw_rate_b, " +
                "away_rate_b " +
                "from " +
                "kumiawase " +
                "where " +
                "kaisu = '" + kaisu + "'";

        //カーソルにクエリ結果を格納
        Cursor cursor = db.rawQuery(QueryStr, null);
        ArrayList<ArrayList<ArrayList<String>>> rateStrArray = new ArrayList<>();
        ArrayList<ArrayList<String>> tempTotoRateStrArray = new ArrayList<>();
        ArrayList<ArrayList<String>> tempBookRateStrArray = new ArrayList<>();

        //カーソルを１行づつ回して支持率を取得
        if(cursor.moveToFirst()){
            do{
                ArrayList<String> totoRateStr = new ArrayList<>();
                ArrayList<String> bookRateStr = new ArrayList<>();
                //判定時の添字と合わせるためにホームとドローの位置を変えてる
                totoRateStr.add(cursor.getString(cursor.getColumnIndex("draw_rate")));
                totoRateStr.add(cursor.getString(cursor.getColumnIndex("home_rate")));
                totoRateStr.add(cursor.getString(cursor.getColumnIndex("away_rate")));
                bookRateStr.add(cursor.getString(cursor.getColumnIndex("draw_rate_b")));
                bookRateStr.add(cursor.getString(cursor.getColumnIndex("home_rate_b")));
                bookRateStr.add(cursor.getString(cursor.getColumnIndex("away_rate_b")));
                tempTotoRateStrArray.add(totoRateStr);
                tempBookRateStrArray.add(bookRateStr);
            }while(cursor.moveToNext());
            rateStrArray.add(tempTotoRateStrArray);
            rateStrArray.add(tempBookRateStrArray);
        }

        //カーソルを閉じる
        cursor.close();

        //返す
        return rateStrArray;
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
