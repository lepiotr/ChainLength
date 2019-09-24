package com.example.SQLiteBike;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ChainLength.ChainLength;
import com.example.ChainLength.PDFActivity;
import com.example.myapplication1.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;


public class BikeList extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Bike> list;
    private BikeListAdapter adapter = null;

    public int id = 0;

    final Context context = this;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper sqLiteHelper;

    public ChainLength chainLength = new ChainLength();

    public ImageView imgModifyImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_list_activity);

        sqLiteHelper = new SQLiteHelper(this, "BikeDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS BIKE(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, type VARCHAR, chainset STRING, cassette STRING, bracket STRING, chainlink STRING, image BLOB)");

        listView = findViewById(R.id.list_view);
        list = new ArrayList<>();
        adapter = new BikeListAdapter(this, R.layout.bike_items, list);
        listView.setAdapter(adapter);

        // get all data from sqlite
        Cursor cursor = BikeList.sqLiteHelper.getData("SELECT * FROM BIKE");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String type = cursor.getString(2);
            String chainset = cursor.getString(3);
            String cassette = cursor.getString(4);
            String bracket = cursor.getString(5);
            String chainlink = cursor.getString(6);
            byte[] image = cursor.getBlob(7);

            list.add(new Bike(name, type, chainset, cassette, bracket, chainlink, image, id));
        }
        adapter.notifyDataSetChanged();


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(BikeList.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            Cursor c = BikeList.sqLiteHelper.getData("SELECT id FROM BIKE");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            // show dialog
                            showDialogUpdate(BikeList.this, arrID.get(position));

                        } else {
                            // delete
                            Cursor c = BikeList.sqLiteHelper.getData("SELECT id FROM BIKE");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.btnNewCalculation) {

            Intent add_mem = new Intent(this, AddBike.class);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogUpdate(Activity activity, final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_modify_record);
        dialog.setTitle("Update");

        final TextView txtBikeName;
        final EditText txtBikeType, txtChainset, txtCassette, txtSuportHub;
        final TextView txtResultName, txtResultEquals;
        final CharSequence resultEqualsString;
        final Button btnModifyCalculate, btnModifyBikeImg, btnModifyCalculation;
        final ImageButton imgInfoChainset, imgInfoCassette, imgInfoSuportHub, imgPDF;

        imgModifyImage = dialog.findViewById(R.id.imgModify);
        btnModifyBikeImg = dialog.findViewById(R.id.btnModifyImg);
        txtBikeName = dialog.findViewById(R.id.txtModifyBikeName);
        txtBikeType = dialog.findViewById(R.id.txtModifyBikeType);
        txtChainset = dialog.findViewById(R.id.txtModifyChainset);
        txtCassette = dialog.findViewById(R.id.txtModifyCassette);
        txtSuportHub = dialog.findViewById(R.id.txtModifyBracket);
        txtResultName = dialog.findViewById(R.id.txtResultName);
        txtResultEquals = dialog.findViewById(R.id.txtModifyResult);

        btnModifyCalculation = dialog.findViewById(R.id.btnModifySqlCalculation);
        btnModifyCalculate = dialog.findViewById(R.id.btnModifyCalc);

        txtChainset.setText("1");
        txtCassette.setText("1");
        txtSuportHub.setText("1");

        final TextView numberChainset = dialog.findViewById(R.id.txtModifyChainset); //get the id for TextView
        final TextView numberCassette = dialog.findViewById(R.id.txtModifyCassette); //get the id for TextView
        final TextView lengthBracketHub = dialog.findViewById(R.id.txtModifyBracket); //get the id for TextView
        final TextView resultText = dialog.findViewById(R.id.txtModifyResult); //get the id for TextView

        imgInfoChainset = dialog.findViewById(R.id.infoButtonChainset);
        imgInfoCassette = dialog.findViewById(R.id.infoButtonCassette);
        imgInfoSuportHub = dialog.findViewById(R.id.infoButtonSuportHub);

        imgPDF = dialog.findViewById(R.id.imgBtnPDF);
        imgModifyImage.setImageResource(R.mipmap.ic_launcher2);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.9);
        Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        dialog.show();

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
                Intent intent = new Intent(BikeList.this, PDFActivity.class);
                // pass PDF names and image to PDFActivity
                intent.putExtra("namePDF", txtBikeName.getText().toString());
                intent.putExtra("typePDF", txtBikeType.getText().toString());
                intent.putExtra("chainsetPDF", txtChainset.getText().toString());
                intent.putExtra("cassettePDF", txtCassette.getText().toString());
                intent.putExtra("bracketPDF", txtSuportHub.getText().toString());
                intent.putExtra("chainlinkPDF", txtResultEquals.getText().toString());
                // image sending
                intent.putExtra("byteArray", imageViewToByte(imgModifyImage));

                startActivity(intent);

            }
        });

        btnModifyBikeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        BikeList.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnModifyCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chainLength.calculateChainLength(Integer.parseInt(numberChainset.getText().toString()),
                        Integer.parseInt(numberCassette.getText().toString()),
                        Integer.parseInt(lengthBracketHub.getText().toString())); // String->Int

                // Result - Chain Link Number
                resultText.setText(String.valueOf(chainLength.functionCalculated)); // Int->String->Text
            }
        });

        btnModifyCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BikeList.sqLiteHelper.updateData(
                            txtBikeName.getText().toString().trim(),
                            txtBikeType.getText().toString().trim(),
                            txtChainset.getText().toString().trim(),
                            txtCassette.getText().toString().trim(),
                            txtSuportHub.getText().toString().trim(),
                            txtResultEquals.getText().toString().trim(),
                            imageViewToByte(imgModifyImage),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update successful",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Update error", Objects.requireNonNull(error.getMessage()));
                }
                updateBikeList();
            }
        });
    }

    private void showDialogDelete(final int idBike){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(BikeList.this);

        dialogDelete.setTitle("Warning!");
        dialogDelete.setMessage("Are you sure you want to delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    BikeList.sqLiteHelper.deleteData(idBike);
                    Toast.makeText(getApplicationContext(), "Delete successful",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("error", Objects.requireNonNull(e.getMessage()));
                }
                updateBikeList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateBikeList(){
        // get all data from sqlite
        Cursor cursor = BikeList.sqLiteHelper.getData("SELECT * FROM BIKE");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String type = cursor.getString(2);
            String chainset = cursor.getString(3);
            String cassette = cursor.getString(4);
            String bracket = cursor.getString(5);
            String chainlink = cursor.getString(6);
            byte[] image = cursor.getBlob(7);

            list.add(new Bike(name, type, chainset, cassette, bracket, chainlink, image, id));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
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

        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(Objects.requireNonNull(uri));
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgModifyImage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

}

