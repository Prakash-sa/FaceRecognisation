package com.example.firebasecamera;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button Camera;
    private static final int ReQUEST_IMAGE_CAMERA=124;
    private FirebaseVisionImage image;
    private ImageView image1;
    private FirebaseVisionFaceDetector detector;
    private static FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        Camera=findViewById(R.id.b1);
        image1=(ImageView)findViewById(R.id.imageview1);
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takepick=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takepick.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takepick,ReQUEST_IMAGE_CAMERA);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==ReQUEST_IMAGE_CAMERA&&resultCode==RESULT_OK){
            Bundle extras=data.getExtras();
            Bitmap bitmap=(Bitmap) extras.get("data");
            image1.setImageBitmap(bitmap);
            detectFace(bitmap);

        }
        else {
            Toast.makeText(MainActivity.this, "Error",Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void detectFace(Bitmap bitmap) {
        FirebaseVisionFaceDetectorOptions highAccuracyOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .build();
        image=FirebaseVisionImage.fromBitmap(bitmap);
        detector= FirebaseVision.getInstance().getVisionFaceDetector(highAccuracyOpts);
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                String resulttext="";
                int i=1;
                for(FirebaseVisionFace face:firebaseVisionFaces){
                    Rect rec=face.getBoundingBox();


                    resulttext=resulttext.concat("\n"+i+".").concat("\nSmile: "+face.getSmilingProbability()*100+"%")
                    .concat("\nLeftEye: "+face.getLeftEyeOpenProbability()*100+"%")
                            .concat("\nRightEye:  "+face.getRightEyeOpenProbability()*100+"%");
                    i++;
                }
                if(firebaseVisionFaces.size()==0){
                    Toast.makeText(MainActivity.this,"No Image",Toast.LENGTH_LONG).show();
                }
                else {
                    Bundle bundle=new Bundle();
                    bundle.putString(LCOapplication.RESULT_TEXT,resulttext);
                    ResultDialog resultDialog=new ResultDialog();
                    resultDialog.setArguments(bundle);
                    resultDialog.setCancelable(false);
                    resultDialog.show(getSupportFragmentManager(),LCOapplication.RESULT_DIALOG);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Listener false",Toast.LENGTH_LONG).show();
            }
        });

    }
}
