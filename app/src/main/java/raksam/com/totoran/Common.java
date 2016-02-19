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

    //初期化
    public void init(){
        teamNameArray = new ArrayList<>();
        singleBoolArray = new ArrayList<>();
        multiBoolArray = new ArrayList<>();
        totoRateArray = new ArrayList<>(); //このタイミング(MainActivityのonCreate)じゃもしかしたら遅いかも？？？
        bookRateArray = new ArrayList<>(); //このタイミング(MainActivityのonCreate)じゃもしかしたら遅いかも？？？

        //テストデータ
        makeTestDataTeam();
        makeTestDataToto();
        makeTestDataBook();

    }



    //テストデータ作成
    private void makeTestDataTeam() {
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

    //テストデータ作成
    private void makeTestDataToto() {
        ArrayList<String> child1 = new ArrayList<>();
        child1.add("78.55");
        child1.add("11.56");
        child1.add("9.89");
        totoRateArray.add(child1);

        ArrayList<String> child2 = new ArrayList<>();
        child2.add("47.61");
        child2.add("25.67");
        child2.add("26.72");
        totoRateArray.add(child2);

        ArrayList<String> child3 = new ArrayList<>();
        child3.add("44.43");
        child3.add("26.76");
        child3.add("28.81");
        totoRateArray.add(child3);

        ArrayList<String> child4 = new ArrayList<>();
        child4.add("58.71");
        child4.add("26.39");
        child4.add("14.90");
        totoRateArray.add(child4);

        ArrayList<String> child5 = new ArrayList<>();
        child5.add("56.54");
        child5.add("21.55");
        child5.add("21.91");
        totoRateArray.add(child5);

        ArrayList<String> child6 = new ArrayList<>();
        child6.add("15.87");
        child6.add("17.14");
        child6.add("66.99");
        totoRateArray.add(child6);

        ArrayList<String> child7 = new ArrayList<>();
        child7.add("45.22");
        child7.add("27.94");
        child7.add("26.84");
        totoRateArray.add(child7);

        ArrayList<String> child8 = new ArrayList<>();
        child8.add("38.71");
        child8.add("31.10");
        child8.add("30.19");
        totoRateArray.add(child8);

        ArrayList<String> child9 = new ArrayList<>();
        child9.add("75.90");
        child9.add("15.41");
        child9.add("8.69");
        totoRateArray.add(child9);

        ArrayList<String> child10 = new ArrayList<>();
        child10.add("17.38");
        child10.add("22.97");
        child10.add("59.65");
        totoRateArray.add(child10);

        ArrayList<String> child11 = new ArrayList<>();
        child11.add("69.63");
        child11.add("20.16");
        child11.add("10.21");
        totoRateArray.add(child11);

        ArrayList<String> child12 = new ArrayList<>();
        child12.add("12.58");
        child12.add("19.48");
        child12.add("67.94");
        totoRateArray.add(child12);

        ArrayList<String> child13 = new ArrayList<>();
        child13.add("18.23");
        child13.add("20.52");
        child13.add("61.25");
        totoRateArray.add(child13);
    }

    //テストデータ作成
    private void makeTestDataBook() {
        ArrayList<String> child1 = new ArrayList<>();
        child1.add("19.77");
        child1.add("25.77");
        child1.add("54.47");
        bookRateArray.add(child1);

        ArrayList<String> child2 = new ArrayList<>();
        child2.add("47.61");
        child2.add("25.67");
        child2.add("26.72");
        bookRateArray.add(child2);

        ArrayList<String> child3 = new ArrayList<>();
        child3.add("80.00");
        child3.add("5.00");
        child3.add("15.00");
        bookRateArray.add(child3);

        ArrayList<String> child4 = new ArrayList<>();
        child4.add("58.71");
        child4.add("26.39");
        child4.add("14.90");
        bookRateArray.add(child4);

        ArrayList<String> child5 = new ArrayList<>();
        child5.add("56.54");
        child5.add("21.55");
        child5.add("21.91");
        bookRateArray.add(child5);

        ArrayList<String> child6 = new ArrayList<>();
        child6.add("15.87");
        child6.add("17.14");
        child6.add("66.99");
        bookRateArray.add(child6);

        ArrayList<String> child7 = new ArrayList<>();
        child7.add("45.22");
        child7.add("27.94");
        child7.add("26.84");
        bookRateArray.add(child7);

        ArrayList<String> child8 = new ArrayList<>();
        child8.add("38.71");
        child8.add("31.10");
        child8.add("30.19");
        bookRateArray.add(child8);

        ArrayList<String> child9 = new ArrayList<>();
        child9.add("75.90");
        child9.add("15.41");
        child9.add("8.69");
        bookRateArray.add(child9);

        ArrayList<String> child10 = new ArrayList<>();
        child10.add("17.38");
        child10.add("22.97");
        child10.add("59.65");
        bookRateArray.add(child10);

        ArrayList<String> child11 = new ArrayList<>();
        child11.add("19.77");
        child11.add("25.77");
        child11.add("54.47");
        bookRateArray.add(child11);

        ArrayList<String> child12 = new ArrayList<>();
        child12.add("63.71");
        child12.add("22.85");
        child12.add("13.44");
        bookRateArray.add(child12);

        ArrayList<String> child13 = new ArrayList<>();
        child13.add("39.65");
        child13.add("28.76");
        child13.add("31.59");
        bookRateArray.add(child13);
    }

}
