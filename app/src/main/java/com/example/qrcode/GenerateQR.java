package com.example.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenerateQR extends AppCompatActivity {

    private TextView qrcodetv;
    private ImageView qrcode;
    private TextInputEditText dataEdit;
    private Button buttonqr;
    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        qrcodetv=findViewById(R.id.idqrcodetext);
        qrcode=findViewById(R.id.idQrcode);
        dataEdit=findViewById(R.id.idEditData);
        buttonqr=findViewById(R.id.buttonGqr);

        buttonqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data=dataEdit.getText().toString();
                if (data.isEmpty()){
                    Toast.makeText(GenerateQR.this, "Please enter some data to generate QR Code", Toast.LENGTH_SHORT).show();
                }else{
                    WindowManager manager=(WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display=manager.getDefaultDisplay();
                    Point point=new Point();
                    display.getSize(point);
                    int width=point.x;
                    int height=point.y;
                    int dimen=width<height ? width:height;
                    dimen=dimen*3/4;


                    qrgEncoder=new QRGEncoder(dataEdit.getText().toString(),null, QRGContents.Type.TEXT,dimen);
                    try{
                        bitmap=qrgEncoder.encodeAsBitmap();
                        qrcodetv.setVisibility(View.GONE);
                        qrcode.setImageBitmap(bitmap);

                    }catch (WriterException e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}