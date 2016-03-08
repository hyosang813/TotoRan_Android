/**
 * Created by hyosang813 on 16/02/07.
 * Activity間で情報共有
 */
package raksam.com.totoran;

import android.app.Application;
import java.util.ArrayList;

public class Common extends Application {

    ArrayList<ArrayList<String>> teamNameArray; //ホーム、アウェイのチーム名２次元Array
    ArrayList<ArrayList<Boolean>> singleBoolArray; //シングルのボタンBOOL２次元Array
    ArrayList<ArrayList<Boolean>> multiBoolArray; //マルチのボタンBOOL２次元Array
    ArrayList<ArrayList<String>> totoRateArray; //totoのレート情報２次元Array
    ArrayList<ArrayList<String>> bookRateArray; //bookのレート情報２次元Array
    String numberOfTimes; //回数
    String deadLineTime; //締め切り年月日
    String dataGetTime; //データ取得時間

    //初期化
    public void init(){
        teamNameArray = new ArrayList<>();
        singleBoolArray = new ArrayList<>();
        multiBoolArray = new ArrayList<>();
        totoRateArray = new ArrayList<>();
        bookRateArray = new ArrayList<>();
    }
}
