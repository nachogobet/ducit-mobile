package ducit.unounocuatro.com.ducit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init("/storage/emulated/0/", "spa");
        Bitmap bMap = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/celu2.jpg");
        baseApi.setImage(bMap);
        String recognizedText = baseApi.getUTF8Text();
        LinearLayout lView = new LinearLayout(this);

        TextView myText = new TextView(this);
        myText.setText(recognizedText);

        lView.addView(myText);

        setContentView(lView);
        baseApi.end();
    }
}
