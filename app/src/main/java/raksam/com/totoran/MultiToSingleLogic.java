package raksam.com.totoran;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by hyosang813 on 16/02/21.
 * マルチの数列をシングルの数列に変換するクラス
 * ついでにその数列にドロー数の目印と、文字列グループ、ドロー数グループのArrayを作成
 */
public class MultiToSingleLogic {

    public static ArrayList<ArrayList<String>> multiToSingleDataMake(ArrayList<ArrayList<String>> multiDataArray,
                                                                     ArrayList<ArrayList<Object>> hanteiStrUpperArray,
                                                                     ArrayList<ArrayList<Object>> hanteiStrLowerArray,
                                                                     ArrayList<ArrayList<Object>> drawGroupArray,
                                                                     ArrayList<ArrayList<String>> totoRateArray,
                                                                     ArrayList<ArrayList<String>> bookRateArray) {


        //組み合わせを作成するためにノンタップ部「-」を削除しながら有効組み合わせ総数を算出
        ArrayList<ArrayList<String>> localMultiDataArray = new ArrayList<>();
        int combiCount = 1;
        for (ArrayList<String> multiWakuArray : multiDataArray) {
            ArrayList<String> childWakuStrArray = new ArrayList<>();
            for (String homeDrawAway : multiWakuArray) {
                if (!homeDrawAway.equals("-")) childWakuStrArray.add(homeDrawAway);
            }
            combiCount *= childWakuStrArray.size();
            localMultiDataArray.add(childWakuStrArray);
        }

        //マルチの数列から全組み合わせのシングル数列を生成
        ArrayList<ArrayList<String>> mTosStrArray = new ArrayList<>();
        int p = 0, s = 0; // p:商, s:余り

        // (3) 組み合わせをcombiCount回求める（i：組み合わせ番号）
        for (int i = 0; i < combiCount; i++) {
            ArrayList<String> childMTosStrArray = new ArrayList<>();

            //p(商)の初期値はiの値を与える
            p = i;
            for (int j = 0; j < localMultiDataArray.size(); j++) {
                s = p % localMultiDataArray.get(j).size();
                p = (p - s) / localMultiDataArray.get(j).size();;

                childMTosStrArray.add(localMultiDataArray.get(j).get(s));
            }
            mTosStrArray.add(childMTosStrArray);
        }

        //返すArrayを作成して判定ロジックをかます
        ArrayList<ArrayList<String>> singleHanteiStrArray = HanteiLogic.hanteiDataMake(mTosStrArray, totoRateArray, bookRateArray);

        //数列単位で0の個数を追加しつつ、ドローと大文字と小文字の全種類をひろう
        ArrayList<String> upperStrArray = new ArrayList<>(); //大文字
        ArrayList<String> lowerStrArray = new ArrayList<>(); //小文字
        ArrayList<Integer> drawStrArray = new ArrayList<>(); //ドロー
        for (ArrayList<String> singleWakuArray : singleHanteiStrArray) {
            //まずはドローの数を調査
            int drawCount = 0;
            for (int i = 0; i < 13; i++) {
                if (singleWakuArray.get(i).equals("0")) drawCount++;
            }
            //ドローの数を最後尾に追加
            singleWakuArray.add(String.valueOf(drawCount));

            //ドローと大文字と小文字が存在しなかったら追加
            if (!drawStrArray.contains(drawCount)) drawStrArray.add(drawCount);
            if (!upperStrArray.contains(singleWakuArray.get(13))) upperStrArray.add(singleWakuArray.get(13));

            //bookRateは存在しない可能性あり
            if (bookRateArray.get(0).get(0) != null) {
                if (!lowerStrArray.contains(singleWakuArray.get(15))) lowerStrArray.add(singleWakuArray.get(15));
            }
        }

        //それぞれの種類をソート
        Collections.sort(drawStrArray);
        Collections.sort(upperStrArray);
        Collections.sort(lowerStrArray); //bookRateは存在しない可能性ありだけど悪さはしないでしょ

        
        //スイッチ用にbooleanとセットにする(初期値はtrue)
        for (Integer drawCount : drawStrArray) {
            ArrayList<Object> mixBoolData = new ArrayList<>();
            mixBoolData.add(String.valueOf(drawCount));
            mixBoolData.add(true);
            drawGroupArray.add(mixBoolData);
        }

        for (String upperString : upperStrArray) {
            ArrayList<Object> mixBoolData = new ArrayList<>();
            mixBoolData.add(upperString);
            mixBoolData.add(true);
            hanteiStrUpperArray.add(mixBoolData);
        }

        for (String lowerString : lowerStrArray) { //bookRateは存在しない可能性ありだけど悪さはしないでしょ
            ArrayList<Object> mixBoolData = new ArrayList<>();
            mixBoolData.add(lowerString);
            mixBoolData.add(true);
            hanteiStrLowerArray.add(mixBoolData);
        }

        //返す
        return singleHanteiStrArray;
    }
}
