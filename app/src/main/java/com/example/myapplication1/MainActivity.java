package com.example.myapplication1;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    final Context context = this;
    private Button button;

    private TextInputEditText editTextInputChainset,editTextInputCassette,editTextInputBracketHub;
    private Button buttonConfirm;

    Spinner spinnerLanguage;
    TextView resultNameText,resultTextText,btnChangeTextText,spinnerNameText, numberChainsetText, numberCassetteText,lengthBracketHubText, alertDialogText;
    ArrayAdapter<String> mAdapter;
    ImageButton infoButtonChainset, infoButtonSuportHub, infoButtonCassette;

    CharSequence numberChainsetString, numberCassetteString, lengthBracketHubString, resultTextString;
    int initialResult=0;
    int numberChainringsLargestDiscChainset;    // Liczba zebow na najwiekszej tarczy korby
    int numberChainringsLargestDiscCassette;     // Liczba zebow na najwiekszej tarczy kasety
    int lengthBottomBracketRearHub;             // Odleglosc od srodka suportu do srodka tylnej piasty
    double constNumber=0.635;                   // Stala
    int functionTempA;                          // Funkcja przeliczajaca posrednia
    double functionTempB;                       // Funkcja przeliczajaca posrednia
    int functionTempC;                          // Funkcja przeliczajaca posrednia
    int functionTempD;                          // Funkcja przeliczajaca posrednia
    int functionTempE;                          // Funkcja przeliczajaca posrednia
    int functionCalculated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInputChainset = findViewById(R.id.numberChainset);
        editTextInputCassette = findViewById(R.id.numberCassette);
        editTextInputBracketHub = findViewById(R.id.lengthBracketHub);
        buttonConfirm = findViewById(R.id.btnChangeText);

        editTextInputChainset.addTextChangedListener(loginTextWatcher);
        editTextInputCassette.addTextChangedListener(loginTextWatcher);
        editTextInputBracketHub.addTextChangedListener(loginTextWatcher);


        final TextView numberChainset = (TextView) findViewById(R.id.numberChainset); //get the id for TextView
        final TextView numberCassette = (TextView) findViewById(R.id.numberCassette); //get the id for TextView
        final TextView lengthBracketHub = (TextView) findViewById(R.id.lengthBracketHub); //get the id for TextView
        final TextView resultText = (TextView) findViewById(R.id.resultText); //get the id for TextView

        resultTextString = String.valueOf(initialResult);
        final Button changeText = (Button) findViewById(R.id.btnChangeText); //get the id for button

        spinnerLanguage = (Spinner) findViewById(R.id.spinner);
        numberChainsetText = (TextView) findViewById(R.id.numberChainset);
        numberCassetteText = (TextView) findViewById(R.id.numberCassette);
        lengthBracketHubText = (TextView) findViewById(R.id.lengthBracketHub);
        resultNameText = (TextView) findViewById(R.id.resultName);
        resultTextText = (TextView) findViewById(R.id.resultText);
        btnChangeTextText = (TextView) findViewById(R.id.btnChangeText);
        spinnerNameText = (TextView) findViewById(R.id.spinnerName);
        mAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.language_option));
        spinnerLanguage.setAdapter(mAdapter);

        if (LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("en")) {
            spinnerLanguage.setSelection(mAdapter.getPosition("English"));
        } else if (LocaleHelper.getLanguage(MainActivity.this).equalsIgnoreCase("pl")) {
            spinnerLanguage.setSelection(mAdapter.getPosition("Polski"));
        }

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Context context;
                Resources resources;
                switch (i) {
                    case 0:
                        //numberChainsetText.setText.("");

                        context = LocaleHelper.setLocale(MainActivity.this, "en");
                        resources = context.getResources();
                        numberChainsetText.setHint(resources.getString(R.string.napisKorba));//,"default_value"));
                        //numberChainsetText.setHint(null);
                        numberCassetteText.setHint(resources.getString(R.string.napisKaseta));
                        lengthBracketHubText.setHint(resources.getString(R.string.napisSuportPiasta));
                        resultNameText.setText(resources.getString(R.string.napisWynik));
                        //resultTextText.setText(resources.getString(R.string.napisWynikRownania));
                        btnChangeTextText.setText(resources.getString(R.string.przyciskOblicz));
                        spinnerNameText.setText(resources.getString(R.string.napisTlumacz));

                        numberChainsetText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (hasFocus) {
                                    //numberChainsetText.setHint("");
                                    numberChainsetText.setHint("Largest Chainset Disc Chainrings Number");
                                } else {
                                    //numberChainsetText.setHint(null);
                                    numberChainsetText.setHint("Largest Chainset Disc Chainrings Number");//Largest Chainset Disc Chainrings Number");
                                }
                            }
                        });
                        numberCassetteText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (hasFocus) {
                                    numberCassetteText.setHint("");
                                } else {
                                    numberCassetteText.setHint("Largest Cassette Disc Chainrings Number");
                                }
                            }
                        });
                        lengthBracketHubText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (hasFocus) {
                                    lengthBracketHubText.setHint(" ");
                                } else {
                                    lengthBracketHubText.setHint("Bottom Bracket To Rear Hub Length [cm]");
                                }
                            }
                        });
                        break;
                    case 1:
                        context = LocaleHelper.setLocale(MainActivity.this, "pl");
                        resources = context.getResources();
                        //numberChainsetText.setHint(null);
                        numberChainsetText.setHint(resources.getString(R.string.napisKorba));//, "default_value"));
                        numberCassetteText.setHint(resources.getString(R.string.napisKaseta));
                        lengthBracketHubText.setHint(resources.getString(R.string.napisSuportPiasta));
                        resultNameText.setText(resources.getString(R.string.napisWynik));
                        //resultTextText.setText(resources.getString(R.string.napisWynikRownania));
                        btnChangeTextText.setText(resources.getString(R.string.przyciskOblicz));
                        spinnerNameText.setText(resources.getString(R.string.napisTlumacz));
                          numberChainsetText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                          @Override
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (hasFocus) {
                                    //numberChainsetText.setHint("");
                                    numberChainsetText.setHint("Ilość zębów największej tarczy korby");
                                } else {
                                    //numberChainsetText.setHint(null);
                                    numberChainsetText.setHint("Ilość zębów największej tarczy korby");//Ilość zębów największej tarczy korby");
                                }
                            }
                        });
                        numberCassetteText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (hasFocus) {
                                    numberCassetteText.setHint("");
                                } else {
                                    numberCassetteText.setHint("Ilość zębów największej tarczy kasety");
                                }
                            }
                        });
                        lengthBracketHubText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (hasFocus) {
                                    lengthBracketHubText.setHint("");
                                } else {
                                    lengthBracketHubText.setHint("Odl. suport - tylna piasta [cm]");
                                }
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        infoButtonChainset =(ImageButton)findViewById(R.id.infoButtonChainset);
        infoButtonChainset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.chainset_popup);
                ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageButton);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        infoButtonCassette =(ImageButton)findViewById(R.id.infoButtonCassette);
        infoButtonCassette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.cassette_popup);
                ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageButton);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        infoButtonSuportHub =(ImageButton)findViewById(R.id.infoButtonSuportHub);
        infoButtonSuportHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.suport_hub_popup);
                ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageButton);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        changeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Liczba zebow na najwiekszej tarczy korby
                numberChainsetString = numberChainset.getText();
                int numberChainringsLargestDiscChainset = Integer.parseInt(numberChainsetString.toString()); // String->Int

                // Liczba zebow na najwiekszej tarczy kasety
                numberCassetteString = numberCassette.getText();
                int numberChainringsLargestDiscCassette = Integer.parseInt(numberCassetteString.toString()); // String->Int

                // Odleglosc od srodka suportu do srodka tylnej piasty
                lengthBracketHubString = lengthBracketHub.getText();
                int lengthBottomBracketRearHub = Integer.parseInt(lengthBracketHubString.toString()); // String->Int

                if(numberChainringsLargestDiscChainset>0&&numberChainringsLargestDiscCassette>0&&lengthBottomBracketRearHub>0) {
                    // Funkcje przeliczajace posrednie
                    functionTempA = (numberChainringsLargestDiscChainset + numberChainringsLargestDiscCassette) / 2;
                    functionTempB = lengthBottomBracketRearHub / constNumber;
                    functionTempB = Math.round(functionTempB);
                    functionTempC = (int) functionTempB;
                    functionTempD = functionTempA + functionTempC + 2;
                        if (functionTempD % 2 == 0) {
                            functionTempE = functionTempD;
                        } else {
                            functionTempE = functionTempD + 1;
                        }
                        // Wynik
                        functionCalculated = functionTempE;
                        resultTextString = String.valueOf(functionCalculated); // Int->String

                        // Wyswietlenie wyniku
                        resultText.setText(resultTextString);
                }
                else{
                    alertDialogText = (TextView) findViewById(R.id.textAlert);
                    // add  listener
                    alertDialogText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // custom dialog
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.alert_inputs);
                            TextView textAlertDialog = (TextView) dialog.findViewById(R.id.textAlert);
                            // if button is clicked, close the custom dialog
                            textAlertDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    });
                }
            }


        });


    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputChainset = editTextInputChainset.getText().toString().trim();
            String inputCassette = editTextInputCassette.getText().toString().trim();
            String inputBracketHub = editTextInputBracketHub.getText().toString().trim();

            btnChangeTextText.setEnabled(!inputChainset.isEmpty()&!inputChainset.matches("0")&!inputCassette.isEmpty()&!inputCassette.matches("0")&!inputBracketHub.matches("0")&!inputBracketHub.matches("0"));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }*/
}
