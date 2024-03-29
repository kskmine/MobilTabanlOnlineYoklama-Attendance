package com.example.attendance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.opencv.imgproc.Imgproc.adaptiveThreshold;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int REQUEST_STORAGE_PERMISSION =3 ;
    ImageView imageView;
    String imagePath;
    private static String TAG = "MainActivity";
    static {
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV initialize success");
        } else {
            Log.i(TAG, "OpenCV initialize failed");
        }
    }
    String name = null;
    String number = null;


    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    Spinner spinner;
    Spinner spinner2;
    List<String > noList;
    List<String > nameList;
    FloatingActionButton fabCancel;
    FloatingActionButton fab;
    FloatingActionButton fabNext;
    FloatingActionButton fabCarpi;
    FloatingActionButton Kyt;
    boolean fromCamera=true;
    int angle;

    private Button btnListele;
    private ListView VeriListele;
    private TextView about;
    private TextView tw;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.imageView);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fabNext = findViewById(R.id.floatingActionButton);
        fabCancel = findViewById(R.id.floatingActionButton3);
        fabCarpi = findViewById(R.id.floatingActionButton4);
        Kyt = findViewById(R.id.floatingActionButton2);
        FloatingActionButton fabSave = findViewById(R.id.fabSave);
        EditText txtName=findViewById(R.id.txtName);
        EditText txtNumber=findViewById(R.id.txtNumber);
        TextView txtView=findViewById(R.id.textView);
        TextView txtView2=findViewById(R.id.textView2);
        TextView tw=findViewById(R.id.textVieww);

        btnListele = (Button) findViewById(R.id.Listele);
        VeriListele = (ListView) findViewById(R.id.veriListele);
        about = (TextView) findViewById(R.id.about);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        fab.setVisibility(View.VISIBLE);
        Kyt.setVisibility(View.VISIBLE);
        tw.setVisibility(View.VISIBLE);
        fabCarpi.setVisibility(View.GONE);
        fabCancel.setVisibility(View.GONE);
        fabNext.setVisibility(View.GONE);
        fabSave.setVisibility(View.GONE);
        txtName.setVisibility(View.GONE);
        txtNumber.setVisibility(View.GONE);
        txtView.setVisibility(View.GONE);
        txtView2.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        spinner2.setVisibility(View.GONE);
        btnListele.setVisibility(View.GONE);
        VeriListele.setVisibility(View.GONE);
        about.setVisibility(View.GONE);

        DB dataBase = new DB(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = dataBase.getReadableDatabase();

    fab.setOnClickListener(v -> {
            // Check for the external storage permission
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // If you do not have permission, request it
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);
            } else {


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("İÇE AKTAR");
                builder.setMessage("Hangi yöntemle içe aktarmak istersiniz?");

                builder.setPositiveButton("KAMERA", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Launch the camera if the permission exists
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(getBaseContext(),
                                        "com.example.attendance.FileProvider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                angle=90;
                                fromCamera=true;
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                            }
                        }
                    }
                });

                builder.setNegativeButton("GALERİ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        fromCamera=false;
                        angle=0;
                        startActivityForResult(photoPickerIntent, 22);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();




            }

        });


        Kyt.setOnClickListener(v -> {
            Intent intent23 =new Intent(MainActivity.this,TeacherActivity.class);
        });

        fabCancel.setOnClickListener(v -> {

            imageView.setVisibility(View.VISIBLE);


            fab.setVisibility(View.VISIBLE);
            Kyt.setVisibility(View.VISIBLE);
            tw.setVisibility(View.VISIBLE);
            fabCancel.setVisibility(View.GONE);
            fabCarpi.setVisibility(View.GONE);
            fabNext.setVisibility(View.GONE);
            btnListele.setVisibility(View.GONE);
            VeriListele.setVisibility(View.GONE);
            about.setVisibility(View.GONE);
            if (fabSave.getVisibility()==View.VISIBLE){
                fabCancel.setVisibility(View.VISIBLE);
                fabCarpi.setVisibility(View.GONE);
                Kyt.setVisibility(View.GONE);
                tw.setVisibility(View.GONE);
                fabNext.setVisibility(View.VISIBLE);
            }else {
                imageView.setImageBitmap(null);
            }

            fabSave.setVisibility(View.GONE);
            txtName.setVisibility(View.GONE);
            txtNumber.setVisibility(View.GONE);
            txtName.setText("");
            txtNumber.setText("");
            txtView.setVisibility(View.GONE);
            txtView2.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            spinner2.setVisibility(View.GONE);

        });
        fabNext.setOnClickListener(v -> {
            imageView.setVisibility(View.GONE);
            //imageView.setImageBitmap(null);
            fab.setVisibility(View.GONE);
            fabCancel.setVisibility(View.VISIBLE);
            Kyt.setVisibility(View.GONE);
            tw.setVisibility(View.GONE);
            fabCarpi.setVisibility(View.GONE);
            fabNext.setVisibility(View.GONE);
            fabSave.setVisibility(View.VISIBLE);
            btnListele.setVisibility(View.GONE);
            VeriListele.setVisibility(View.GONE);
            about.setVisibility(View.GONE);

            txtName.setVisibility(View.VISIBLE);
            txtNumber.setVisibility(View.VISIBLE);
            txtView.setVisibility(View.VISIBLE);
            txtView2.setVisibility(View.VISIBLE);

            spinner.setVisibility(View.VISIBLE);
            spinner2.setVisibility(View.VISIBLE);
        });

//--------------------------SAVE-CONTROL-----------------------------------------------------------------------------------------------
        fabSave.setOnClickListener(v -> {

          /*  Intent intent = new Intent(MainActivity.this,InsertUpdate.class);
            intent.putExtra("event","insert");
            startActivity(intent);*/

            Intent inten = new Intent(getApplicationContext(),LoginActivity.class);
           // contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

            inten
                    .putExtra("gelenad", txtName.getText().toString())
                    .putExtra("gelenno",txtNumber.getText().toString());



            String control1=txtName.getText().toString();
            String control2=txtNumber.getText().toString();

            if (control1.equals("ALL")) {
                for (int i = 0; i < nameList.size() - 1; i++) {

                    if (i == 0) {
                        inten.putExtra("gelenad", nameList.get(i));
                    }
                }
            }else if (control2.equals("ALL")) {
                for (int i = 0; i < noList.size() - 1; i++) {

                    if (i == 0) {
                        inten.putExtra("gelenno", noList.get(i));
                    }
                }
            }else{
                inten.putExtra("gelenno", txtNumber.getText().toString());
            }

            dataBase.insert(sqLiteDatabase,control1,control2,"0");

            startActivityForResult(inten, 9);
        });

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                txtName.setText(spinner.getSelectedItem().toString()); //this is taking the first value of the spinner by default.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
                txtNumber.setText(spinner2.getSelectedItem().toString()); //this is taking the first value of the spinner by default.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    Mat orjn;
    String Text;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if (fabCancel.getVisibility()==View.VISIBLE) {
                fabCancel.performClick();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //builder.setTitle("HAYIR");
                builder.setMessage("Çıkmak istediğinizden emin misiniz?");

                builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            fab.setVisibility(View.VISIBLE);
            fabCarpi.setVisibility(View.GONE);
            Kyt.setVisibility(View.GONE);
            tw.setVisibility(View.GONE);
            fabCancel.setVisibility(View.VISIBLE);
            fabNext.setVisibility(View.VISIBLE);
            btnListele.setVisibility(View.GONE);
            VeriListele.setVisibility(View.GONE);
            about.setVisibility(View.GONE);
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            orjn = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);
            Utils.bitmapToMat(bitmap, orjn);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.25), (int) (bitmap.getHeight() * 0.25), true);
            Mat imgMAT = new Mat(resized.getHeight(), resized.getWidth(), CvType.CV_8UC1);
            Utils.bitmapToMat(resized, imgMAT);
            Mat deneme = findRectangle(imgMAT);
            Imgproc.cvtColor(deneme, deneme, Imgproc.COLOR_BGR2RGB);
            Bitmap yeni = convertMatToBitMap(deneme);
            bitmap = RotateBitmap(yeni, angle);
            imageView.setImageBitmap(bitmap);
            ocr(bitmap);

        }
        if (requestCode == 22 && resultCode == RESULT_OK) {

            fab.setVisibility(View.VISIBLE);
            fabCarpi.setVisibility(View.GONE);
            fabCancel.setVisibility(View.VISIBLE);
            Kyt.setVisibility(View.GONE);
            fabNext.setVisibility(View.VISIBLE);
            btnListele.setVisibility(View.GONE);
            VeriListele.setVisibility(View.GONE);
           // floatingActionButton4.setVisibility(View.GONE);

            Uri pickedImage = data.getData();
            //Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            //Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

            orjn = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);
            Utils.bitmapToMat(bitmap, orjn);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.25), (int) (bitmap.getHeight() * 0.25), true);
            Mat imgMAT = new Mat(resized.getHeight(), resized.getWidth(), CvType.CV_8UC1);
            Utils.bitmapToMat(resized, imgMAT);
            Mat deneme = findRectangle(imgMAT);
            Imgproc.cvtColor(deneme, deneme, Imgproc.COLOR_BGR2RGB);
            Bitmap yeni = convertMatToBitMap(deneme);
            bitmap = RotateBitmap(yeni, angle);
            imageView.setImageBitmap(bitmap);
            ocr(bitmap);

        }
        if (requestCode == 9) {
            if (resultCode == Activity.RESULT_OK) {
                btnListele.setVisibility(View.VISIBLE);
                VeriListele.setVisibility(View.VISIBLE);
                about.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Controlled Student", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled Controlled Student", Toast.LENGTH_SHORT).show();
            }
        }
    }

//--------------------------------------------------OCR---------------------------------------------------------------"---------------------------------------------------
public void ocr(Bitmap input){
    Bitmap bitmap=input;
    String[] noArray=new String[5];
    int sayac=0;
    boolean numberFinded=false;


    TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
    // Set the new bitmap to the ImageView
    Frame imageFrame = new Frame.Builder()
            .setBitmap(bitmap)                 // your image bitmap
            .build();
    String imageText = "";
    SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);


    for (int i = 0; i < textBlocks.size(); i++) {
        TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
        imageText = imageText +"\n"+ textBlock.getValue();
        if (i==1){
            name=textBlock.getValue();
        }
        if (textBlock.getValue().contains("+")){
            String data1 = textBlock.getValue();
            Matcher m = Pattern.compile("^?(\\+\\d{2}\\s)?\\(?\\d{3}?\\)?[\\s.-]\\d{3}[\\s.-]\\d{2}[\\s.-]\\d{2}$?").matcher(data1);
            ArrayList<String>matchesNO = new ArrayList<>();
            while (m.find()){
                String s = m.group();
                if(!matchesNO.contains(s)) {
                    matchesNO.add(s);
                    noArray[sayac] = s;
                    sayac++;
                }
            }
            number=String.valueOf(matchesNO.get(0));
            numberFinded=true;
        }
    }
    //make text line by line
    List<String> lines= Arrays.asList(imageText.split("\n"));
    String[] myArray = new String[lines.size()];
    lines.toArray(myArray);

    //removed first element of array
    int n=myArray.length-1;
    String[] newArray=new String[n];
    System.arraycopy(myArray,1,newArray,0,n);

    if (numberFinded==false) {
        for (int i = 0; i < lines.size(); i++) {
            String data1 = myArray[i];
            data1 = data1.replaceAll("\\D+", "");
            if (data1.length() > 8) {
                Matcher m = Pattern.compile("^?\\d{9}$?").matcher(data1);
                ArrayList<String> matchesNO = new ArrayList<>();
                while (m.find()) {
                    String s = m.group();
                    if (!matchesNO.contains(s)) {
                        matchesNO.add(s);
                        noArray[sayac] = s;
                        sayac++;
                    }
                }
                number = String.valueOf(matchesNO.get(0));
            }

        }
    }

    noList = new ArrayList<String >();
    //Log.e("deneme", String.valueOf(list));
    for(String s : noArray) {
        if(s != null && s.length() > 0) {
            noList.add(s);
        }
    }
    noList.add("ALL");

    nameList = new ArrayList<String >();
    for(String s : newArray) {

        if(s != null && s.length() > 0&&countWords(s)>1&&countWords(s)<4
                && !s.matches(".*\\d+.*")
                && !s.contains(",")
                && !s.contains("@")) {
            nameList.add(s.toUpperCase());
        }
    }
    nameList.add("ALL");

    Log.e("deneme", String.valueOf(noList));

    adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, nameList);

    adapter2 = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, noList);

    spinner.setAdapter(adapter);
    spinner2.setAdapter(adapter2);


}

    public static int countWords(String s){

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;

            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }
    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private Mat findRectangle(Mat src) {
        Mat blurred = src.clone();
        Imgproc.medianBlur(src, blurred, 9);

        Mat gray0 = new Mat(blurred.size(), CvType.CV_8U), gray = new Mat();

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        List<Mat> blurredChannel = new ArrayList<Mat>();
        blurredChannel.add(blurred);
        List<Mat> gray0Channel = new ArrayList<Mat>();
        gray0Channel.add(gray0);

        MatOfPoint2f approxCurve;

        double maxArea = 0;
        int maxId = -1;

        for (int c = 0; c < 3; c++) {
            int ch[] = { c, 0 };
            Core.mixChannels(blurredChannel, gray0Channel, new MatOfInt(ch));

            int thresholdLevel = 1;
            for (int t = 0; t < thresholdLevel; t++) {
                if (t == 0) {
                    Imgproc.Canny(gray0, gray, 50, 100, 3, true);
                    Imgproc.dilate(gray, gray, new Mat(), new Point(-1, -1), 1); // 1
                } else {
                    adaptiveThreshold(gray0, gray, thresholdLevel,
                            Imgproc.ADAPTIVE_THRESH_MEAN_C,
                            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                            (src.width() + src.height()) / 200, t);
                }

                Imgproc.findContours(gray, contours, new Mat(),
                        Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

                for (MatOfPoint contour : contours) {
                    MatOfPoint2f temp = new MatOfPoint2f(contour.toArray());

                    double area = Imgproc.contourArea(contour);
                    approxCurve = new MatOfPoint2f();
                    Imgproc.approxPolyDP(temp, approxCurve,
                            Imgproc.arcLength(temp, true) * 0.02, true);

                    if (approxCurve.total() == 4 && area >= maxArea) {
                        double maxCosine = 0;

                        List<Point> curves = approxCurve.toList();
                        for (int j = 2; j < 5; j++) {

                            double cosine = Math.abs(angle(curves.get(j % 4),
                                    curves.get(j - 2), curves.get(j - 1)));
                            maxCosine = Math.max(maxCosine, cosine);
                        }

                        if (maxCosine < 0.3) {
                            maxArea = area;
                            maxId = contours.indexOf(contour);
                        }
                    }
                }
            }
        }

        //if (maxId >= 0) {
        //  Imgproc.drawContours(src, contours, maxId, new Scalar(255, 0, 0, .8), 10);

        //}
        double largest_area=0;
        Rect boundrect= new Rect();
        for( int i = 0; i< contours.size(); i++ ) // iterate through each contour.
        {
            double a=Imgproc.contourArea( contours.get(i),false);  //  Find the area of contour
            if(a>largest_area){
                largest_area=a;//Store the index of largest contour
                boundrect=Imgproc.boundingRect(contours.get(i)); // Find the bounding rectangle for biggest contour
            }

        }

        boundrect.x=boundrect.x*4;
        boundrect.y=boundrect.y*4;
        boundrect.height=boundrect.height*4;
        boundrect.width=boundrect.width*4;

        return new Mat(orjn, boundrect);
    }

    private double angle(Point p1, Point p2, Point p0) {
        double dx1 = p1.x - p0.x;
        double dy1 = p1.y - p0.y;
        double dx2 = p2.x - p0.x;
        double dy2 = p2.y - p0.y;
        return (dx1 * dx2 + dy1 * dy2)
                / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2)
                + 1e-10);
    }

    private static Bitmap convertMatToBitMap(Mat input){
        Bitmap bmp = null;
        Mat rgb = new Mat();
        Imgproc.cvtColor(input, rgb, Imgproc.COLOR_BGR2RGB);

        try {
            bmp = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rgb, bmp);
        }
        catch (CvException e){
            Log.d("Exception",e.getMessage());
        }
        return bmp;
    }
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
