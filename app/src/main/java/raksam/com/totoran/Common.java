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

    //初期化
    public void init(){
        teamNameArray = new ArrayList<>();
        singleBoolArray = new ArrayList<>();
        multiBoolArray = new ArrayList<>();

        makeTestData();

    }



    //テストデータ作成
    private void makeTestData() {
        ArrayList<String> child1 = new ArrayList<>();
        child1.add("広　島");
        child1.add("川　崎");
        teamNameArray.add(child1);
        ArrayList<String> child2 = new ArrayList<>();
        child2.add("鳥　栖");
        child2.add("福　岡");
        teamNameArray.add(child2);
        ArrayList<String> child3 = new ArrayList<>();
        child3.add("　柏　");
        child3.add("浦　和");
        teamNameArray.add(child3);
        ArrayList<String> child4 = new ArrayList<>();
        child4.add("湘　南");
        child4.add("新　潟");
        teamNameArray.add(child4);
        ArrayList<String> child5 = new ArrayList<>();
        child5.add("神　戸");
        child5.add("甲　府");
        teamNameArray.add(child5);
        ArrayList<String> child6 = new ArrayList<>();
        child6.add("磐　田");
        child6.add("名古屋");
        teamNameArray.add(child6);
        ArrayList<String> child7 = new ArrayList<>();
        child7.add("横浜Ｆ");
        child7.add("仙　台");
        teamNameArray.add(child7);
        ArrayList<String> child8 = new ArrayList<>();
        child8.add("東　京");
        child8.add("大　宮");
        teamNameArray.add(child8);
        ArrayList<String> child9 = new ArrayList<>();
        child9.add("Ｇ大阪");
        child9.add("鹿　島");
        teamNameArray.add(child9);
        ArrayList<String> child10 = new ArrayList<>();
        child10.add("千　葉");
        child10.add("徳　島");
        teamNameArray.add(child10);
        ArrayList<String> child11 = new ArrayList<>();
        child11.add("熊　本");
        child11.add("松　本");
        teamNameArray.add(child11);
        ArrayList<String> child12 = new ArrayList<>();
        child12.add("町　田");
        child12.add("Ｃ大阪");
        teamNameArray.add(child12);
        ArrayList<String> child13 = new ArrayList<>();
        child13.add("京　都");
        child13.add("水　戸");
        teamNameArray.add(child13);
    }

}
