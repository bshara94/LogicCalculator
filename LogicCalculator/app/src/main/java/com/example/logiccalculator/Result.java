package com.example.logiccalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Result extends AppCompatActivity {
    static int OptionNumber;
    TextView txtResult;
    String Formula;
    RadioButton Final,Debug;
    Processor p ;
    Button CopyResult;
    FormalConverter formalConverter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        txtResult = (TextView) findViewById(R.id.txtResult);
        txtResult.setMovementMethod(new ScrollingMovementMethod());
        Final = (RadioButton)findViewById(R.id.rbFinalop);
        Debug = (RadioButton)findViewById(R.id.rbDebugop);
        CopyResult = (Button)findViewById(R.id.CRbtn);
        OptionNumber = getIntent().getIntExtra("Options",0);
         Formula = getIntent().getStringExtra("Input");

         txtResult.setText("");
        //Toast.makeText(this,"option number is :" + OptionNumber, Toast.LENGTH_SHORT).show();
        Debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Final.setChecked(false);
                txtResult.setText(Constants.TXT_DEBUG);
            }
        });

        Final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Debug.setChecked(false);
                txtResult.setText(Constants.TXT_OUTPUT);
            }
        });
        CopyResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(txtResult.getText());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", txtResult.getText());
                    Toast.makeText(Result.this, "Result was copied to clipboard.", Toast.LENGTH_SHORT).show();
                    clipboard.setPrimaryClip(clip);
                }
            }
        });
        switch(OptionNumber){

            case 0:
            case 3:
            case 4:
                try {
                    p = new Processor(Formula);
                    p.process(true);
                    txtResult.setText(Constants.TXT_OUTPUT);
                    Final.setVisibility(View.VISIBLE);
                    Debug.setVisibility(View.INVISIBLE);
                }catch (Exception e){
                    txtResult.setText("Wrong Input");
                    Final.setVisibility(View.INVISIBLE);
                    Debug.setVisibility(View.INVISIBLE);
            }
                break;
            case 1:
                try{
                p = new Processor(Formula);
                formalConverter = new FormalConverter(Formula);
                formalConverter.convert("CNF");
                txtResult.setText(Constants.TXT_OUTPUT);
                    Final.setVisibility(View.VISIBLE);
                    Debug.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    txtResult.setText("Wrong Input");
                    Final.setVisibility(View.INVISIBLE);
                    Debug.setVisibility(View.INVISIBLE);
                }

                break;
            case 2:
                try{
                p = new Processor(Formula);
                 formalConverter = new FormalConverter(Formula);
                formalConverter.convert("DNF");
                txtResult.setText(Constants.TXT_OUTPUT);
                    Final.setVisibility(View.VISIBLE);
                    Debug.setVisibility(View.VISIBLE);
                }catch (Exception e) {
                    txtResult.setText("Wrong Input");
                    Final.setVisibility(View.INVISIBLE);
                    Debug.setVisibility(View.INVISIBLE);                }
                break;

            default:

                break;
        }

    }
}
