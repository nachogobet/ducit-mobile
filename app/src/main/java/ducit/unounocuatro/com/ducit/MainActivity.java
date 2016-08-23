package ducit.unounocuatro.com.ducit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.Utils;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init("/storage/emulated/0/", "spa");
        //Bitmap bMap = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/celu2.jpg");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat source = Imgcodecs.imread("/storage/emulated/0/Pictures/celu2.jpg");
        Mat marcado = new Mat();
        Mat hsv = new Mat();
        Mat gauss = new Mat();
        Mat adapt = new Mat();
        Scalar narBajo = new Scalar(0,85,75);
        Scalar narAlto = new Scalar(50,255,255);


        Imgproc.cvtColor(source, hsv, Imgproc.COLOR_BGR2HSV);
        //Highgui.imwrite("./src/main/resources/images/hsv.jpg", hsv);


        Core.inRange(hsv, narBajo, narAlto, marcado);
        //Highgui.imwrite("./src/main/resources/images/hsv.jpg", marcado);


        //aplico gauss

        Imgproc.GaussianBlur(marcado, gauss, new Size(9,9), 0);
        //Imgcodecs.imwrite("./src/main/resources/images/hsv.jpg", gauss);

        //Aplico adaptative threshold

        Imgproc.adaptiveThreshold(gauss, adapt, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 41,11);

        //Imgcodecs.imwrite("./src/main/resources/images/hsv.jpg",adapt);

        Bitmap scale = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(source, scale);

        baseApi.setImage(scale);

        String recognizedText = baseApi.getUTF8Text();
        LinearLayout lView = new LinearLayout(this);

        TextView myText = new TextView(this);
        myText.setText(recognizedText);

        lView.addView(myText);

        setContentView(lView);
        baseApi.end();
    }
}
