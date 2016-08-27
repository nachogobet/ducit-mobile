package ducit.unounocuatro.com.ducit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.Utils;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    static{
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV not loaded");
        } else{
            Log.d(TAG, "OpenCV loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        setContentView(R.layout.activity_main);
        //System.loadLibrary("opencv_java310");
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init("/storage/emulated/0/", "spa");
        //Bitmap bMap = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/celu2.jpg");

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

        Bitmap scale = Bitmap.createBitmap(adapt.width(), adapt.height(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(adapt, scale);

        baseApi.setImage(cleanLines(scale));

        String recognizedText = baseApi.getUTF8Text();
        LinearLayout lView = new LinearLayout(this);

        TextView myText = new TextView(this);
        myText.setText(recognizedText);

        lView.addView(myText);

        setContentView(lView);
        baseApi.end();
    }

    private static Bitmap cleanLines(Bitmap image) {
        boolean line=true;
        for(int i=1; i<image.getWidth()-50; i++){
            for(int j=1; j<image.getHeight(); j++){
                for(int z=i; z<i+50; z++){
                    if(image.getPixel(z,j) != Color.BLACK)
                        line=false;
                }
                if(line){
                    doCleanLines(image, i, j);
                }
                line=true;
            }
        }
        return image;
    }

    private static void doCleanLines(Bitmap image, int i, int j) {
        image.setPixel(i, j, Color.WHITE);
        if(i>0 && j>0 && image.getPixel(i-1, j-1)==Color.BLACK)
            doCleanLines(image, i-1, j-1);
        if(i>0 && image.getPixel(i-1, j)==Color.BLACK)
            doCleanLines(image, i-1, j);
        if(i>0 && image.getPixel(i-1, j+1)==Color.BLACK)
            doCleanLines(image, i-1, j+1);
        if(j>0 && image.getPixel(i, j-1)==Color.BLACK)
            doCleanLines(image, i, j-1);
        if(image.getPixel(i, j+1)==Color.BLACK)
            doCleanLines(image, i, j+1);
        if(j>0 && image.getPixel(i+1, j-1)==Color.BLACK)
            doCleanLines(image, i+1, j-1);
        if(image.getPixel(i+1, j)==Color.BLACK)
            doCleanLines(image, i+1, j);
        if(image.getPixel(i+1, j+1)==Color.BLACK)
            doCleanLines(image, i+1, j+1);
    }
}
