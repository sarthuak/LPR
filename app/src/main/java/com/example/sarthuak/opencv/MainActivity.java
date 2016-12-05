package com.example.sarthuak.opencv;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.SurfaceView;

import android.view.WindowManager;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.JavaCameraView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;



import java.util.ArrayList;

import java.util.List;





public class MainActivity extends AppCompatActivity implements CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";


    //loads camera view of OpenCV for us to use this let us see using OpenCv
    private CameraBridgeViewBase mOpenCvCameraView;

    //Used in camera selection from menu (When implemented)

    Mat mRgba;
    Mat mRgbaF;
    Mat mRgbaT;
    Mat hierarchy;
    Mat drawing;
    //Scalar color;
    private static final int       VIEW_MODE_RGBA     = 0;
    private static final int       VIEW_MODE_CANNY    = 2;
    private static final int       VIEW_MODE_new    = 3;
    private static final int       VIEW_MODE_ocr   = 5;

    private int                    mViewMode;
    private MenuItem               mItemPreviewRGBA;
    private MenuItem               mItemPreviewGray;
    private MenuItem               mItemPreviewCanny;
    private MenuItem               mItemnew;
    private MenuItem               mItemocr;



    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemPreviewRGBA = menu.add("RGBA");
        mItemPreviewCanny = menu.add("Edges");
        mItemnew = menu.add("LP Detector");
        mItemocr = menu.add("OCR");
        return true;
    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.show_camera);

        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.show_camera_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();





    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
           //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {

        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
        hierarchy = new Mat();
    }

    public void onCameraViewStopped() {
        mRgba.release();
        mRgbaF.release();
        mRgbaT.release();
        hierarchy.release();

    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {


        // TODO Auto-generated method stub
        final int viewMode = mViewMode;
        switch (viewMode) {

            case VIEW_MODE_RGBA:
                // input frame has RBGA format
                mRgba = inputFrame.rgba();
                break;
            case VIEW_MODE_CANNY:
                // input frame has gray scale format
                mRgba = inputFrame.rgba();
                Imgproc.Canny(inputFrame.gray(), mRgbaF, 80, 100);
                Imgproc.cvtColor(mRgbaF, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
                break;

            case VIEW_MODE_ocr:
                startActivity(new Intent(this, ScanLicensePlateActivity.class));
                break;



            case VIEW_MODE_new:
                Mat mRgba;

                mRgba = inputFrame.rgba();
                drawing = mRgba.clone();

                mRgbaT = drawing;


                Imgproc.cvtColor(drawing, mRgbaT, Imgproc.COLOR_BGR2GRAY);

                org.opencv.core.Size s = new Size(1,1);
                Imgproc.GaussianBlur(mRgbaT, mRgbaT, s , 0 ,0);

                Imgproc.Canny(mRgbaT,mRgbaT,100,255);
                Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
                Imgproc.dilate(mRgbaT,mRgbaT,element);
                List<MatOfPoint> contours = new ArrayList<>();

                Imgproc.findContours(drawing, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
                double maxArea = -1;
                int maxAreaIdx = -1;




                for (int idx = 0; idx < contours.size(); idx++) {
                    Mat contour = contours.get(idx);

                    double contourarea = Imgproc.contourArea(contour);
                    if (contourarea > maxArea) {

                        maxArea = contourarea;
                        maxAreaIdx = idx;
                    }
                }






                Imgproc.drawContours(mRgba, contours,maxAreaIdx, new Scalar(255,0,0),5);





        }
        return mRgba; // This function must return






    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        if (item == mItemPreviewRGBA) {
            mViewMode = VIEW_MODE_RGBA;
        } else if (item == mItemPreviewCanny) {
            mViewMode = VIEW_MODE_CANNY;
        } else if (item == mItemnew) {
            mViewMode = VIEW_MODE_new;

        }
        else if (item == mItemocr){
            mViewMode = VIEW_MODE_ocr;
    }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.sarthuak.opencv/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.sarthuak.opencv/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}



