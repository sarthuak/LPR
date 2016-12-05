package io.anyline.examples.ocr.result;

/**
 * Created by Sarthuak on 02-12-2016.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sarthuak.opencv.R;

import java.io.File;
import java.io.FileOutputStream;

import at.nineyards.anyline.util.DimensUtil;
//import io.anyline.examples.R;


public class LicensePlateResultView extends RelativeLayout {

    private static final String TAG = "OCR";

    private TextView licensePlateText;
    private TextView countryText;

    public LicensePlateResultView(Context context) {
        super(context);
        init();
    }

    public LicensePlateResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LicensePlateResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {

        setPadding(DimensUtil.getPixFromDp(getContext(), 4), DimensUtil.getPixFromDp(getContext(), 16),
                DimensUtil.getPixFromDp(getContext(), 4), DimensUtil.getPixFromDp(getContext(), 16));



        setBackgroundResource(R.drawable.license_plate_background);

        inflate(getContext(), R.layout.license_plate_result, this);

        licensePlateText = (TextView) findViewById(R.id.text_license_plate);
        String number =  licensePlateText.getText().toString();

        File root = new File(android.os.Environment.getExternalStorageDirectory()+"//lpr.txt");
        Log.d("adasd",android.os.Environment.getExternalStorageDirectory().getAbsolutePath());
        try {
            FileOutputStream fos=new FileOutputStream(root,true);
            fos.write(number.getBytes());
            Log.d("number",number);
        } catch (Exception e) {
            e.printStackTrace();
        }


        countryText = (TextView) findViewById(R.id.text_country);
    }

    public void setLicensePlate(String licensePlate) {
        licensePlateText.setText(licensePlate.trim());
    }
    public void setCountry(String country) {
        countryText.setText(country.trim());
    }
}
