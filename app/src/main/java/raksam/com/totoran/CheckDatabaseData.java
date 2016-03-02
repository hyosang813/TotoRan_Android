package raksam.com.totoran;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

/**
 * Created by hyosang813 on 16/03/02.
 * DBの状態をチェックする
 */
public class CheckDatabaseData {
    // SQLiteインスタンス
    private SQLiteDatabase db;

    //コンストラクタでDBへのアクセスインスタンスを取得しとく
    CheckDatabaseData(Context context) {
        DataBaseHelper mDbHelper = new DataBaseHelper(context);
        try {
            mDbHelper.createEmptyDataBase();
            db = mDbHelper.openDataBaseWithWrite(); //Deleteがあるので書き込み可能モードでオープン
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        } catch(SQLException sqle){
            throw sqle;
        }
    }

    //現在開催中の回数を返す（開催期間じゃなかったら"Non"を返す）
    public String holdCheck() {
        String QueryStr = "select " +
                "open_number " +
                "from " +
                "kaisu " +
                "where  " +
                "(start_date < datetime('now', 'localtime') " +
                "and " +
                "end_date > datetime('now', 'localtime')) " +
                "order by " +
                "open_number " +
                "limit 1 ";

        //カーソルにクエリ結果を格納
        Cursor cursor = db.rawQuery(QueryStr, null);

        //結果が０件だったらNonを返す
        String holdNum = "";
        if(cursor.moveToFirst()) {
            holdNum = cursor.getString(cursor.getColumnIndex("open_number"));
        } else {
            holdNum = "Non";
        }

        //カーソルを閉じる
        cursor.close();

        //返す
        return holdNum;
    }

    //３テーブル全てのデータを再取得すべきかどうかをチェックする
    public boolean kaisaiTableDataCheck() {
        String QueryStr = "select count(*) from kaisu where start_date > datetime('now', 'localtime')";

        //カーソルにクエリ結果を格納
        Cursor cursor = db.rawQuery(QueryStr, null);

        //結果が0件だったらテーブルデータを削除してtrueを返す
        if(cursor.moveToFirst()) {
            if (cursor.getInt(0) == 0) {
                tableDataDelete();
                cursor.close();
                return true;
            }
        }

        //取得しなくてもよかったらfalseを返す
        cursor.close();
        return false;
    }

    //支持率データを再取得すべきかどうかをチェックする
    public boolean rateGetCheck(String kaisu) {
        String QueryStr = "select " +
                "count(*) " +
                "from " +
                "kumiawase " +
                "where " +
                "kaisu = '" + kaisu + "' and " +
                "(get_date is null or " +
                "strftime('%Y-%m-%d %H:00:00', get_date) != " +
                "(select strftime('%Y-%m-%d %H:00:00', datetime('now', 'localtime')))) ";

        //カーソルにクエリ結果を格納
        Cursor cursor = db.rawQuery(QueryStr, null);

        //結果が0件以外だったらtrueを返す
        if(cursor.moveToFirst()) {
            if (cursor.getInt(0) != 0) {
                cursor.close();
                return true;
            }
        }

        //取得しなくてもよかったらfalseを返す
        cursor.close();
        return false;
    }

    //テーブル全削除
    public void tableDataDelete() {
        db.delete("abbname", null, null);
        db.delete("kaisu", null, null);
        db.delete("kumiawase", null, null);
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
