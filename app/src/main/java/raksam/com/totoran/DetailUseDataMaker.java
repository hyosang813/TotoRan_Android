package raksam.com.totoran;

import java.util.ArrayList;

/**
 * Created by hyosang813 on 16/02/14.
 * シングル／マルチ詳細画面で必要なデータをboolArrayから生成する
 */
public class DetailUseDataMaker {
    //基数の１３
    private static final int BASE_COUNT = 13;

    //対象のboolArray
    ArrayList<ArrayList<Boolean>>  rawArray;

    //初期化(コンストラクタ)と同時にシングル or マルチのboolArrayを保持
    DetailUseDataMaker(ArrayList<ArrayList<Boolean>>  boolArray) {
        rawArray = boolArray;
    }

    //シングルに必要なピッカー選択値(連番)を返す
    //ホームの選択値、ドローの選択値、アウェイの選択値、口数の選択値を格納した２次元配列(minValueとmaxValueとcurrentValueの3値)
    protected ArrayList<ArrayList<Integer>> singlePickDataMake() {
        //返すArrayListを生成
        ArrayList<ArrayList<Integer>> returnSingleArray = new ArrayList<>();

        //それぞれのminValue一時格納変数
        int homeCheckCount = 0;
        int drawCheckCount = 0;
        int awayCheckCount = 0;
        int nontapCount = 0;

        for (ArrayList<Boolean> wakuBoolArray : rawArray) {
            switch (wakuBoolArray.indexOf(true)) {
                case 0:
                    homeCheckCount++;
                    break;
                case 1:
                    drawCheckCount++;
                    break;
                case 2:
                    awayCheckCount++;
                    break;
                default://タップなし枠は「−1」が返る
                    nontapCount++;
                    break;
            }
        }

        //最大選択可能口数
        int unitMax = 10;
        switch (nontapCount) {
            case 0:
                unitMax = 1;
                break;
            case 1:
                unitMax = 3;
                break;
            case 2:
                unitMax = 9;
                break;
            default:break;
        }

        //minとmaxのArray生成
        returnSingleArray.add(minMaxArrayMake(homeCheckCount, (BASE_COUNT - (drawCheckCount + awayCheckCount))));
        returnSingleArray.add(minMaxArrayMake(drawCheckCount, (BASE_COUNT - (homeCheckCount + awayCheckCount))));
        returnSingleArray.add(minMaxArrayMake(awayCheckCount, (BASE_COUNT - (homeCheckCount + drawCheckCount))));
        returnSingleArray.add(minMaxArrayMake(1, unitMax));

        //返す
        return returnSingleArray;
    }

    //マルチに必要なピッカー選択値(連番)を返す
    //ダブルの選択数、トリプルの選択数を格納した２次元配列(minValueとmaxValueとcurrentValueの3値)
    protected ArrayList<ArrayList<Integer>> multiPickDataMake() {
        //返すArrayListを生成
        ArrayList<ArrayList<Integer>> returnMultiArray = new ArrayList<>();

        //それぞれのminValue一時格納変数
        int doubleCheckCount = 0;
        int tripleCheckCount = 0;


        for (ArrayList<Boolean> wakuBoolArray : rawArray) {
            int trueCount = 0;
            for (boolean boolValue : wakuBoolArray) {
                if (boolValue) trueCount++;
            }
            switch (trueCount) {
                case 2:
                    doubleCheckCount++;
                    break;
                case 3:
                    tripleCheckCount++;
                    break;
                default:break;
            }
        }

        //minとmaxのArray生成
        returnMultiArray.add(minMaxArrayMake(doubleCheckCount, 8));
        returnMultiArray.add(minMaxArrayMake(tripleCheckCount, 5));

        //返す
        return returnMultiArray;
    }


    //最小値と最大値と現在値(初期値はminと同様)を格納するArrayList生成メソッド
    private ArrayList<Integer> minMaxArrayMake(int min, int max) {
        ArrayList<Integer> minMaxValueArray = new ArrayList<>();
        minMaxValueArray.add(min);
        minMaxValueArray.add(max);
        minMaxValueArray.add(min);
        return minMaxValueArray;
    }
}
