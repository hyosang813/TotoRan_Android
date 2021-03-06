package raksam.com.totoran;

import java.util.ArrayList;

/**
 * Created by hyosang813 on 16/02/18.
 * 整合性チェックロジッククラス
 */
public class ConsistencyCheck {
    //基数
    private static final int BASE_COUNT = 13;

    //w８t５チェック
    public static boolean doubleTripleCheck(ArrayList<ArrayList<Integer>> checkArray) {

        //doubleとtripleの現在値を取得
        int doubleCount = checkArray.get(0).get(2);
        int tripleCount = checkArray.get(1).get(2);

        if (doubleCount > 8 ) return true; //wは最高8個まで
        if (tripleCount > 5 ) return true; //tは最高5個まで
        if (doubleCount == 0 && tripleCount > 5 ) return true; //wが0の時はtは5まで
        if (doubleCount == 1 && tripleCount > 5 ) return true; //wが1の時はtは5まで
        if (doubleCount == 2 && tripleCount > 4 ) return true; //wが2の時はtは4まで
        if (doubleCount == 3 && tripleCount > 3 ) return true; //wが3の時はtは3まで
        if (doubleCount == 4 && tripleCount > 3 ) return true; //wが4の時はtは3まで
        if (doubleCount == 5 && tripleCount > 2 ) return true; //wが5の時はtは2まで
        if (doubleCount == 6 && tripleCount > 1 ) return true; //wが6の時はtは1まで
        if (doubleCount == 7 && tripleCount > 1 ) return true; //wが7の時はtは1まで
        if (doubleCount == 8 && tripleCount > 0 ) return true; //wが8の時はtは0まで

        //falseは結果OK
        return false;
    }

    //シングルの個数と口数整合性チェック
    public static int singleConsistencyCheck(ArrayList<ArrayList<Integer>> checkArray) {

        //ホーム数、ドロー数、アウェイ数、口数の現在値取得
        int homeCount = checkArray.get(0).get(2);
        int drawCount = checkArray.get(1).get(2);
        int awayCount = checkArray.get(2).get(2);
        int unitCount = checkArray.get(3).get(2);

        //(１)選択合計数字が「１３以上」だったら警告
        if (homeCount + drawCount + awayCount > BASE_COUNT) return 1;

        //(２)選択合計数字と口数の整合性が合わなかったら警告　「選択合計数字が１３で口数が２以上」
        if ((homeCount + drawCount + awayCount == BASE_COUNT) && unitCount > 1) return 2;

        //(３)選択合計数字と口数の整合性が合わなかったら警告　「選択合計数字が１２で口数が４以上」
        if ((homeCount + drawCount + awayCount == BASE_COUNT - 1) && unitCount > 3) return 3;

        //(４)選択合計数字と口数の整合性が合わなかったら警告　「選択合計数字が１１で口数が１０」
        if ((homeCount + drawCount + awayCount == BASE_COUNT - 2) && unitCount > 9) return 4;

        //チェックOKは「0」
        return 0;
    }
}
