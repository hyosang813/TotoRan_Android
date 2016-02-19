package raksam.com.totoran;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by hyosang813 on 16/02/19.
 * 判定ロジック
 */

public class HanteiLogic {

    //基準値
    private static final float BASE_VALUE = 0.000000627f;

    //割合換算基準
    private static final float PERCENT_BASE = 0.01f;

    //判定基準
    private static final float S_HANTEI = 50.0f;
    private static final float A_HANTEI = 30.0f;
    private static final float B_HANTEI = 10.0f;
    private static final float C_HANTEI = 5.0f;
    private static final float D_HANTEI = 1.0f;
    private static final float E_HANTEI = 0.01f;

    /**
     * 判定結果を返すメソッド
     */
    public static ArrayList<ArrayList<String>> hanteiDataMake(ArrayList<ArrayList<String>> homeDrawAwayStrArray, ArrayList<ArrayList<String>> totoRateArray, ArrayList<ArrayList<String>> bookRateArray) {
        //引数Arrayに影響を及ぼさないようにコピー
        ArrayList<ArrayList<String>> localHomeDrawAwayStrArray = new ArrayList<>();
        for (ArrayList<String> childHDAstrArray: homeDrawAwayStrArray) {
            localHomeDrawAwayStrArray.add(new ArrayList<>(childHDAstrArray));
        }

        float averageToto = 1; //基準値で割る前のtoto支持率積算用
        float averageBook = 1; //基準値で割る前のbook支持率積算用

        //１数列ずつ対応していく
        for (ArrayList<String> childHDAstrArray: localHomeDrawAwayStrArray) {
            //１枠ずつ対応していく
            int wakuCount = 0;
            for (String hanteitaisyou : childHDAstrArray) {
                //totoとbookともに各枠の支持率(＊0.01)を積算
                averageToto *= Float.valueOf(totoRateArray.get(wakuCount).get(Integer.valueOf(hanteitaisyou))) * PERCENT_BASE;
                averageBook *= Float.valueOf(bookRateArray.get(wakuCount).get(Integer.valueOf(hanteitaisyou))) * PERCENT_BASE;

                //枠カウンターをインクリメント
                wakuCount++;
            }

            //totoとbookの判定用数値を取得
            float rateToto = averageToto / BASE_VALUE;
            float rateBook = averageBook / BASE_VALUE;

            //totoの判定文字列を末尾に追加
            childHDAstrArray.add(hanteiStrMake(rateToto).toUpperCase());

            //totoの判定数字を文字列として先頭(ソート用)と末尾に追加
            String hanteiNumStr = hanteiNumStrMake(rateToto);
            childHDAstrArray.add(hanteiNumStr);
            childHDAstrArray.add(0, hanteiNumStr.substring(1, hanteiNumStr.length() - 1)); //かっこ[]を外してから格納せにゃならんよね

            //bookの判定文字列と、判定数字文字列を末尾に追加
            childHDAstrArray.add(hanteiStrMake(rateBook));
            childHDAstrArray.add(hanteiNumStrMake(rateBook));

            //支持率積算用変数の初期化
            averageToto = 1;
            averageBook = 1;
        }

        //データが２個以上ある場合はソートが必要
        if (localHomeDrawAwayStrArray.size() > 1) {
            Collections.sort(localHomeDrawAwayStrArray, new HanteiListComparator());
        }

        //先頭のソート用要素を削除
        for (ArrayList<String> childHDAstrArray: localHomeDrawAwayStrArray) {
            childHDAstrArray.remove(0);
        }

        //返す
        return localHomeDrawAwayStrArray;
    }

    //判定文字列生成メソッド
    private static String hanteiStrMake(float rate) {
        if(rate >= S_HANTEI){
            return "s";
        }else if(rate >= A_HANTEI){
            return "a";
        }else if(rate >= B_HANTEI){
            return "b";
        }else if(rate >= C_HANTEI){
            return "c";
        }else if(rate >= D_HANTEI){
            return "d";
        }else if(rate >= E_HANTEI){
            return "e";
        }else{
            return "f";
        }
    }

    //判定「数値」文字列生成メソッド
    private static String hanteiNumStrMake(float rate) {
        //D判定以上はIntでいいから簡単
        if(rate >= D_HANTEI) {
            return "[" + String.valueOf((int)rate) + "]";
        } else {
            //指数表示(E-とか)を避けるためにString８桁を取得
            String numStr = String.format("%f", rate).substring(0, 8);
            //最初に0以外の数字が出てくる位置を取得する
            int index = 2; //最初の「0.」は絶対
            while (true) {
                if (!numStr.substring(index, index + 1).equals("0") && !numStr.substring(index, index + 1).equals(".") ) return "[" + numStr.substring(0, index + 1) + "]";
                if (index == 5)  return "[0]"; //文字数に達しても発見できなかったら０を返す
                index++;
            }
        }
    }
}

