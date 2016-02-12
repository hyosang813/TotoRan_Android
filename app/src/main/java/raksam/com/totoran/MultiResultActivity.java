package raksam.com.totoran;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MultiResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_result);

        //等幅フォント(MONOSPACE)の指定
        TextView tv = (TextView)findViewById(R.id.result_text_view_multi);
        tv.setTypeface(Typeface.MONOSPACE);
    }

    //マルチ条件指定画面に戻る
    public void goBack(View v) {
        finish();
    }
}
