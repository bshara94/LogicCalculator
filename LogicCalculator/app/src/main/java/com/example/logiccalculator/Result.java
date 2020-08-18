package com.example.logiccalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
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
    FormalConverter formalConverter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        txtResult = (TextView) findViewById(R.id.txtResult);
        txtResult.setMovementMethod(new ScrollingMovementMethod());
        Final = (RadioButton)findViewById(R.id.rbFinalop);
        Debug = (RadioButton)findViewById(R.id.rbDebugop);
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
