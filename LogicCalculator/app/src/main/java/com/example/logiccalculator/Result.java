package com.example.logiccalculator;

import androidx.appcompat.app.AppCompatActivity;

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
                p= new Processor(Formula);
                p.process(true);
                txtResult.setText(Constants.TXT_OUTPUT);
                break;
            case 1:
                p = new Processor(Formula);
                formalConverter = new FormalConverter(Formula);
                formalConverter.convert("CNF");
                txtResult.setText(Constants.TXT_OUTPUT);

                break;
            case 2:
                p = new Processor(Formula);
                 formalConverter = new FormalConverter(Formula);
                formalConverter.convert("DNF");
                txtResult.setText(Constants.TXT_OUTPUT);
                break;
            case 3:
                p= new Processor(Formula);
                p.process(true);
                txtResult.setText(Constants.TXT_OUTPUT);
                break;
            case 4:
                p= new Processor(Formula);
                p.process(true);
                txtResult.setText(Constants.TXT_OUTPUT);
                break;
            default:

                break;
        }

    }
}
