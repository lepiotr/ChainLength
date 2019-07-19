package com.example.myapplication1;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final Context context = this;

    private TextInputEditText editTextInputChainset,editTextInputCassette,editTextInputBracketHub;
    TextView txtResultName, txtResultEquals, btnCalculate, btnLanguage, txtNumberChainset, txtNumberCassette, txtLengthBracketHub;
    CharSequence numberChainsetString, numberCassetteString, lengthBracketHubString, resultEqualsString;

    ImageButton imgButtonChainset, imgButtonSuportHub, imgButtonCassette;
    Button btnConfirm, btnLang;

    int initialResult=0;
    int numberChainringsLargestDiscChainset;    // Largest Chainset Disc Chainrings Number
    int numberChainringsLargestDiscCassette;    // Largest Cassette Disc Chainrings Number
    int lengthBottomBracketRearHub;             // Bottom Bracket To Rear Hub Length [cm]
    double constNumber=0.635;                   // Constant number
    int functionTempA;                          // Temporary function A: functionTempA = (numberChainringsLargestDiscChainset + numberChainringsLargestDiscCassette) / 2
    double functionTempB;                       // Temporary function B: functionTempB = lengthBottomBracketRearHub / constNumber; functionTempB = Math.round(functionTempB)
    int functionTempC;                          // Temporary function C: functionTempC = (int) functionTempB
    int functionTempD;                          // Temporary function D: functionTempD = functionTempA + functionTempC + 2
    int functionTempE;                          // Temporary function E: functionTempE = functionTempD or functionTempE = functionTempD + 1
    int functionCalculated;                     // Chain Link Number: functionCalculated = functionTempE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInputChainset = findViewById(R.id.numberChainset);
        editTextInputCassette = findViewById(R.id.numberCassette);
        editTextInputBracketHub = findViewById(R.id.lengthBracketHub);
        btnConfirm = findViewById(R.id.btnCalculate);

        editTextInputChainset.addTextChangedListener(loginTextWatcher);
        editTextInputCassette.addTextChangedListener(loginTextWatcher);
        editTextInputBracketHub.addTextChangedListener(loginTextWatcher);

        final TextView numberChainset = (TextView) findViewById(R.id.numberChainset); //get the id for TextView
        final TextView numberCassette = (TextView) findViewById(R.id.numberCassette); //get the id for TextView
        final TextView lengthBracketHub = (TextView) findViewById(R.id.lengthBracketHub); //get the id for TextView
        final TextView resultText = (TextView) findViewById(R.id.resultText); //get the id for TextView

        resultEqualsString = String.valueOf(initialResult);
        final Button changeText = (Button) findViewById(R.id.btnCalculate); //get the id for button

        txtNumberChainset = (TextView) findViewById(R.id.numberChainset);
        txtNumberCassette = (TextView) findViewById(R.id.numberCassette);
        txtLengthBracketHub = (TextView) findViewById(R.id.lengthBracketHub);
        txtResultName = (TextView) findViewById(R.id.resultName);
        txtResultEquals = (TextView) findViewById(R.id.resultText);
        btnCalculate = (TextView) findViewById(R.id.btnCalculate);
        btnLanguage = (Button) findViewById(R.id.btnLang);

        imgButtonChainset =(ImageButton)findViewById(R.id.infoButtonChainset);
        imgButtonChainset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.chainset_popup);
                ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageButton);
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
        imgButtonCassette =(ImageButton)findViewById(R.id.infoButtonCassette);
        imgButtonCassette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.cassette_popup);
                ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageButton);
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
        imgButtonSuportHub =(ImageButton)findViewById(R.id.infoButtonSuportHub);
        imgButtonSuportHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.suport_hub_popup);
                ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageButton);
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

        btnLang =(Button)findViewById(R.id.btnLang);
        btnLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.language_popup);
                RadioGroup dialogRadio = (RadioGroup) dialog.findViewById(R.id.radioPopup);
                // Close the dialog after click
                dialogRadio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialogRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // Which radio button is selected
                        Context context;
                        Resources resources;
                        if(checkedId == R.id.langEN) {
                            Toast.makeText(getApplicationContext(), "Language: English",
                                    Toast.LENGTH_SHORT).show();
                            context = LocaleHelper.setLocale(MainActivity.this, "en");
                            resources = context.getResources();

                            txtNumberChainset.setHint(resources.getString(R.string.napisKorba));
                            txtNumberCassette.setHint(resources.getString(R.string.napisKaseta));
                            txtLengthBracketHub.setHint(resources.getString(R.string.napisSuportPiasta));
                            txtResultName.setText(resources.getString(R.string.napisWynik));
                            btnCalculate.setText(resources.getString(R.string.przyciskOblicz));
                           //btnLanguageText.setText(resources.getString(R.string.napisTlumacz));

                            txtNumberChainset.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean hasFocus) {
                                    if (hasFocus) {
                                        txtNumberChainset.setHint("");
                                    } else {
                                        txtNumberChainset.setHint("Largest Chainset Disc Chainrings Number");
                                    }
                                }
                            });
                            txtNumberCassette.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean hasFocus) {
                                    if (hasFocus) {
                                        txtNumberCassette.setHint("");
                                    } else {
                                        txtNumberCassette.setHint("Largest Cassette Disc Chainrings Number");
                                    }
                                }
                            });
                            txtLengthBracketHub.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean hasFocus) {
                                    if (hasFocus) {
                                        txtLengthBracketHub.setHint("");
                                    } else {
                                        txtLengthBracketHub.setHint("Bottom Bracket To Rear Hub Length [cm]");
                                    }
                                }
                            });
                        } else if(checkedId == R.id.langPL) {
                            Toast.makeText(getApplicationContext(), "Język: Polski",
                                    Toast.LENGTH_SHORT).show();
                            context = LocaleHelper.setLocale(MainActivity.this, "pl");
                            resources = context.getResources();

                            txtNumberChainset.setHint(resources.getString(R.string.napisKorba));//, "default_value"));
                            txtNumberCassette.setHint(resources.getString(R.string.napisKaseta));
                            txtLengthBracketHub.setHint(resources.getString(R.string.napisSuportPiasta));
                            txtResultName.setText(resources.getString(R.string.napisWynik));
                            btnCalculate.setText(resources.getString(R.string.przyciskOblicz));
                           //btnLanguageText.setText(resources.getString(R.string.napisTlumacz));
                            txtNumberChainset.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean hasFocus) {
                                    if (hasFocus) {
                                        txtNumberChainset.setHint("");
                                        txtNumberChainset.setHint("Ilość zębów największej tarczy korby");
                                    } else {
                                        txtNumberChainset.setHint("");
                                        txtNumberChainset.setHint("Ilość zębów największej tarczy korby");
                                    }
                                }
                            });
                            txtNumberCassette.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean hasFocus) {
                                    if (hasFocus) {
                                        txtNumberCassette.setHint("");
                                    } else {
                                        txtNumberCassette.setHint("Ilość zębów największej tarczy kasety");
                                    }
                                }
                            });
                            txtLengthBracketHub.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean hasFocus) {
                                    if (hasFocus) {
                                        txtLengthBracketHub.setHint("");
                                    } else {
                                        txtLengthBracketHub.setHint("Odl. suport - tylna piasta [cm]");
                                    }
                                }
                            });
                        }
                        dialog.dismiss();
                        recreate();
                    }

                });
                dialog.show();
            }

        });

        changeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Largest Chainset Disc Chainrings Number
                numberChainsetString = numberChainset.getText();
                numberChainringsLargestDiscChainset = Integer.parseInt(numberChainsetString.toString()); // String->Int

                // Largest Cassette Disc Chainrings Number
                numberCassetteString = numberCassette.getText();
                numberChainringsLargestDiscCassette = Integer.parseInt(numberCassetteString.toString()); // String->Int

                // Bottom Bracket To Rear Hub Length [cm]
                lengthBracketHubString = lengthBracketHub.getText();
                lengthBottomBracketRearHub = Integer.parseInt(lengthBracketHubString.toString()); // String->Int

                // Temporary functions
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
                    // Result - Chain Link Number
                    functionCalculated = functionTempE;
                    resultEqualsString = String.valueOf(functionCalculated); // Int->String

                    // Result
                    resultText.setText(resultEqualsString);
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

            btnCalculate.setEnabled(!inputChainset.isEmpty()&!inputChainset.matches("0")&!inputCassette.isEmpty()&!inputCassette.matches("0")&!inputBracketHub.matches("0")&!inputBracketHub.matches("0"));
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
