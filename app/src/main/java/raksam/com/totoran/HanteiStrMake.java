package raksam.com.totoran;

import java.util.ArrayList;

/**
 * Created by hyosang813 on 16/02/19.
 * 判定数列データを最終表示形態に整形するクラス
 */

public class HanteiStrMake {
    //判定文字列を整形して最後に支持率取得時点を付与して返す
    public static String resultHanteiStr(ArrayList<ArrayList<String>> hanteiStrArray, String jitenStr) {
        //返す文字列変数
        String returnStr = "";

        //１数列ずつ処理
        for (ArrayList<String> strArray : hanteiStrArray) {
            for (int i = 0; i < strArray.size(); i++) {
                returnStr += strArray.get(i);

                //必要な場所にスペース追加
                switch (i) {
                    case 4:
                    case 9:
                    case 12:
                    case 14:
                        returnStr += " ";
                        break;
                    default:break;
                }
            }
            //１数列終了したら改行追加
            returnStr += "\n";
        }

        //最後に改行を入れて支持率取得時点を追加
        returnStr += "\n" + jitenStr;

        //返す
        return returnStr;
    }
}
