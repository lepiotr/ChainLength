package com.example.ChainLength;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication1.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFActivity extends AppCompatActivity {

    private ConstraintLayout pdfLayout;
    private Bitmap bitmap;
    private Button btnCreatePDF;
    private ImageView imgSqlBike;

    public String strBikeName;
    public String strBikeType;
    public String strChainsetResult;
    public String strCassetteResult;
    public String strBracketHubResult;
    public String strChainLinkResult;


    private TextView bikeName, bikeType, chainsetResult, cassetteResult, bracketHubResult, chainLinkResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_view);

        bikeName = findViewById(R.id.txtAddBikeName);
        bikeType = findViewById(R.id.txtBikeType);
        chainsetResult = findViewById(R.id.txtChainsetResult);
        cassetteResult = findViewById(R.id.txtCassetteResult);
        bracketHubResult = findViewById(R.id.txtBracketHubResult);
        chainLinkResult = findViewById(R.id.txtChainLinkResult);
        imgSqlBike = findViewById(R.id.imgSqlBike);

        btnCreatePDF = findViewById(R.id.btnCreatePDF);
        pdfLayout = findViewById(R.id.pdfLayout);

        // Receive names and image for PDF
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView bikeName = findViewById(R.id.txtAddBikeName);
            TextView bikeType = findViewById(R.id.txtBikeType);
            TextView chainsetResult = findViewById(R.id.txtChainsetResult);
            TextView cassetteResult = findViewById(R.id.txtCassetteResult);
            TextView bracketHubResult = findViewById(R.id.txtBracketHubResult);
            TextView chainLinkResult = findViewById(R.id.txtChainLinkResult);
            // names receiving
            bikeName.setText(extras.getString("namePDF"));
            bikeType.setText(extras.getString("typePDF"));
            chainsetResult.setText(extras.getString("chainsetPDF"));
            cassetteResult.setText(extras.getString("cassettePDF"));
            bracketHubResult.setText(extras.getString("bracketPDF"));
            chainLinkResult.setText(extras.getString("chainlinkPDF"));
            // image receiving
            if(getIntent().hasExtra("byteArray")) {
                ImageView imv= new ImageView(this);
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
                imgSqlBike.setImageBitmap(bitmap);
            }
        }

        // PDF file name
        strBikeName = extras.getString("namePDF");
        strBikeType = extras.getString("typePDF");

        btnCreatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("size", " " + pdfLayout.getWidth() + "  " + pdfLayout.getWidth());
                bitmap = loadBitmapFromView(pdfLayout, pdfLayout.getWidth(), pdfLayout.getHeight());
                createPdf();
            }
        });
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float height = displaymetrics.heightPixels - 700;
        float width = displaymetrics.widthPixels ;

        int convertHeight = (int) height, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHeight, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content
        String targetPdf = Environment.getExternalStorageDirectory().getPath() + "/" + strBikeName + "_" + strBikeType + ".pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {

            document.writeTo(new FileOutputStream(filePath));
            //Toast.makeText(this, "@string/pdfCreated", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Document created under location: " +
                            Environment.getExternalStorageDirectory().getPath() + "/"
                            + strBikeName + "_" + strBikeType + ".pdf",
                            Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "@string/pdfError" + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();

        //openGeneratedPDF();
    }

    private void openGeneratedPDF(){
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + strBikeName + "_" + strBikeType + ".pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(PDFActivity.this, "@string/pdfNoApp", Toast.LENGTH_LONG).show();
            }
        }
    }
}
