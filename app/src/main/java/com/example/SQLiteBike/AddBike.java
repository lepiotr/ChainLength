package com.example.SQLiteBike;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ChainLength.ChainLength;
import com.example.ChainLength.PDFActivity;
import com.example.myapplication1.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class AddBike extends AppCompatActivity {

    private Button btnCalculate, btnAddBikeImg, btnAddCalculate;
    private TextView txtBikeName;
    private EditText txtBikeType, txtChainset, txtCassette, txtSuportHub;
    private TextView txtResultName, txtResultEquals;
    private CharSequence resultEqualsString;
    public ImageButton imgInfoChainset, imgInfoCassette, imgInfoSuportHub, imgPDF;
    private ImageView imgAddImage;

    final int REQUEST_CODE_GALLERY = 999;

    final Context context = this;

    public static SQLiteHelper sqLiteHelper;

    public ChainLength chainLength = new ChainLength();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //init();

        btnCalculate = findViewById(R.id.btnAddCalculate);
        btnAddBikeImg = findViewById(R.id.btnAddBikeImg);
        btnAddCalculate = findViewById(R.id.btnAddSqlCalculation);

        txtBikeName = findViewById(R.id.txtAddBikeName);
        txtBikeType = findViewById(R.id.txtAddBikeType);
        txtChainset = findViewById(R.id.txtAddChainset);
        txtCassette = findViewById(R.id.txtAddCassette);
        txtSuportHub = findViewById(R.id.txtAddBracket);

        txtResultName = findViewById(R.id.txtResultName);
        txtResultEquals = findViewById(R.id.txtResult);

        imgInfoChainset = findViewById(R.id.infoButtonChainset);
        imgInfoCassette = findViewById(R.id.infoButtonCassette);
        imgInfoSuportHub = findViewById(R.id.infoButtonSuportHub);

        txtBikeName.addTextChangedListener(loginTextWatcher);
        txtBikeType.addTextChangedListener(loginTextWatcher);
        txtChainset.addTextChangedListener(loginTextWatcher);
        txtCassette.addTextChangedListener(loginTextWatcher);
        txtSuportHub.addTextChangedListener(loginTextWatcher);

        imgPDF = findViewById(R.id.imgBtnPDF);

        imgAddImage = findViewById(R.id.imgAdd);
        imgAddImage.setImageResource(R.mipmap.ic_launcher2);

        final TextView numberChainset = findViewById(R.id.txtAddChainset); //get the id for TextView
        final TextView numberCassette = findViewById(R.id.txtAddCassette); //get the id for TextView
        final TextView lengthBracketHub = findViewById(R.id.txtAddBracket); //get the id for TextView
        final TextView resultText = findViewById(R.id.txtResult); //get the id for TextView

        // SQL initialize
        sqLiteHelper = new SQLiteHelper(this, "BikeDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS BIKE(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, type VARCHAR, chainset STRING, cassette STRING, bracket STRING, chainlink STRING, image BLOB)");

        // Buttons pressed
        imgInfoChainset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.chainset_popup);
                ImageButton dialogButton = dialog.findViewById(R.id.imageButton);
                // Close the dialog after click
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        imgInfoCassette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.cassette_popup);
                ImageButton dialogButton = dialog.findViewById(R.id.imageButton);
                // Close the dialog after click
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        imgInfoSuportHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.suport_hub_popup);
                ImageButton dialogButton = dialog.findViewById(R.id.imageButton);
                // Close the dialog after click
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        // PDF
        imgPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBike.this, PDFActivity.class);
                // pass PDF names and image to PDFActivity
                intent.putExtra("namePDF", txtBikeName.getText().toString());
                intent.putExtra("typePDF", txtBikeType.getText().toString());
                intent.putExtra("chainsetPDF", txtChainset.getText().toString());
                intent.putExtra("cassettePDF", txtCassette.getText().toString());
                intent.putExtra("bracketPDF", txtSuportHub.getText().toString());
                intent.putExtra("chainlinkPDF", txtResultEquals.getText().toString());
                // image sending
                intent.putExtra("byteArray", imageViewToByte(imgAddImage));

                startActivity(intent);
            }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chainLength.calculateChainLength(Integer.parseInt(numberChainset.getText().toString()),
                        Integer.parseInt(numberCassette.getText().toString()),
                        Integer.parseInt(lengthBracketHub.getText().toString())); // String->Int

                // Result - Chain Link Number
                resultText.setText(String.valueOf(chainLength.functionCalculated)); // Int->String->Text
            }
        });

        btnAddBikeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        AddBike.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnAddCalculate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try{
                    sqLiteHelper.insertData(
                            txtBikeName.getText().toString().trim(),
                            txtBikeType.getText().toString().trim(),
                            txtChainset.getText().toString().trim(),
                            txtCassette.getText().toString().trim(),
                            txtSuportHub.getText().toString().trim(),
                            txtResultEquals.getText().toString().trim(),
                            imageViewToByte(imgAddImage)
                    );
                    Toast.makeText(getApplicationContext(), "Add successful", Toast.LENGTH_SHORT).show();
                    txtBikeName.setText("");
                    txtBikeType.setText("");
                    txtChainset.setText("");
                    txtCassette.setText("");
                    txtSuportHub.setText("");
                    txtResultEquals.setText("");
                    imgAddImage.setImageResource(R.mipmap.ic_launcher2);

                    Intent intent = new Intent(AddBike.this, BikeList.class);
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputBikeName = txtBikeName.getText().toString().trim();
            String inputBikeType = txtBikeType.getText().toString().trim();
            String inputChainset = txtChainset.getText().toString().trim();
            String inputCassette = txtCassette.getText().toString().trim();
            String inputBracketHub = txtSuportHub.getText().toString().trim();

            // Calculate only if the parameters are set: inputChainset, inputCassette, inputBracketHub
            btnCalculate.setEnabled(!inputBikeName.isEmpty()&!inputBikeName.matches("0")&!inputBikeType.isEmpty()&!inputBikeType.matches("0")&!inputChainset.isEmpty()&!inputChainset.matches("0")&!inputCassette.isEmpty()&!inputCassette.matches("0")&!inputBracketHub.matches("0")&!inputBracketHub.matches("0"));
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(Objects.requireNonNull(uri));

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgAddImage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){
        //TODO
    }


}
