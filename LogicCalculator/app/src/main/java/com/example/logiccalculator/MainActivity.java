package com.example.logiccalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    int OptionNumber;
    TextView formulatxt ;
    Button abtn,bbtn,cbtn,dbtn,ebtn,fbtn,gbtn,hbtn,ibtn,jbtn,kbtn,lbtn,mbtn;
    Button nbtn,obtn,pbtn,qbtn,rbtn,sbtn,tbtn,ubtn,vbtn,wbtn,xbtn,ybtn,zbtn;
    Button Backspacebtn,Proccessbtn,Examplebtn,Clearbtn,bkbutton2;
    Button Sepbtn,Notbtn,Orbtn,Andbtn,Leftparbtn,Rightparbtn,Impbtn,Deductionbtn,multiDeductionbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        formulatxt = (TextView)findViewById(R.id.Formulatxt);
        Backspacebtn =(Button)findViewById(R.id.Backspacebtn);
        Proccessbtn =(Button)findViewById(R.id.Proccessbtn);
        Examplebtn =(Button)findViewById(R.id.Examplebtn);
        Examplebtn.setVisibility(View.INVISIBLE);
        Clearbtn =(Button)findViewById(R.id.Clearbtn);
        bkbutton2 =(Button)findViewById(R.id.bkbutton2);
        abtn = (Button)findViewById(R.id.abtn);
        bbtn = (Button)findViewById(R.id.bbtn);
        cbtn = (Button)findViewById(R.id.cbtn);
        dbtn = (Button)findViewById(R.id.dbtn);
        ebtn = (Button)findViewById(R.id.ebtn);
        fbtn = (Button)findViewById(R.id.fbtn);
        gbtn = (Button)findViewById(R.id.gbtn);
        hbtn = (Button)findViewById(R.id.hbtn);
        ibtn = (Button)findViewById(R.id.ibtn);
        jbtn = (Button)findViewById(R.id.jbtn);
        kbtn = (Button)findViewById(R.id.kbtn);
        lbtn = (Button)findViewById(R.id.lbtn);
        mbtn = (Button)findViewById(R.id.mbtn);
        nbtn = (Button)findViewById(R.id.nbtn);
        obtn = (Button)findViewById(R.id.obtn);
        pbtn = (Button)findViewById(R.id.pbtn);
        qbtn = (Button)findViewById(R.id.qbtn);
        rbtn = (Button)findViewById(R.id.rbtn);
        sbtn = (Button)findViewById(R.id.sbtn);
        tbtn = (Button)findViewById(R.id.tbtn);
        ubtn = (Button)findViewById(R.id.ubtn);
        vbtn = (Button)findViewById(R.id.vbtn);
        wbtn = (Button)findViewById(R.id.wbtn);
        xbtn = (Button)findViewById(R.id.xbtn);
        ybtn = (Button)findViewById(R.id.ybtn);
        zbtn = (Button)findViewById(R.id.zbtn);
        Sepbtn=(Button)findViewById(R.id.Sepbtn);
        Andbtn =findViewById(R.id.Andbtn);
        Orbtn =findViewById(R.id.Orbtn);
        Deductionbtn =findViewById(R.id.Deductionbtn);
        multiDeductionbtn=findViewById(R.id.mutliDeductionbtn);
        Impbtn=findViewById(R.id.Impbtn);
        Leftparbtn=findViewById(R.id.LeftParbtn);
        Rightparbtn=findViewById(R.id.Rightparbtn);
        Notbtn=findViewById(R.id.Notbtn);
        Spinner spinner = findViewById(R.id.Actionspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        bkbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HomePage.class));

            }
        });

        Sepbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString() + getString(R.string.SeparatorSign));
            }
        });
        Leftparbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+ getString(R.string.LeftParSign));
            }
        });
        Rightparbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+ getString(R.string.RightParSign));
            }
        });
        Notbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+ getString(R.string.NotSign));
            }
        });
        Andbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString() + getString(R.string.AndSign));
            }
        });
        Orbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+getString(R.string.OrSign));
            }
        });
        Deductionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+getString(R.string.DEDUCTIONSign));
            }
        });
        multiDeductionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+getString(R.string.multidedsign2));
            }
        });
        Impbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+getString(R.string.IMPLIESSign));
            }
        });
        abtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"a");

            }
        });
        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"b");
            }
        });
        cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"c");
            }
        });
        dbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"d");
            }
        });
        ebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"e");
            }
        });
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"f");
            }
        });
        gbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"g");

            }
        });
        hbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"h");
            }
        });
        ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"i");
            }
        });
        jbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"j");
            }
        });
        kbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"k");
            }
        });
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"l");
            }
        });
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"m");
            }
        });
        nbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"n");
            }
        });
        obtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"o");
            }
        });
        pbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"p");
            }
        });
        qbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"q");
            }
        });
        rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"r");
            }
        });
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"s");
            }
        });
        tbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"t");
            }
        });
        ubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"u");
            }
        });
        vbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"v");
            }
        });
        wbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"w");
            }
        });
        xbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"x");
            }
        });
        ybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"y");
            }
        });
        zbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText(formulatxt.getText().toString()+"z");
            }
        });
        Backspacebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lenght =formulatxt.getText().toString().length();
                if(lenght>0)
                    formulatxt.setText(formulatxt.getText().toString().substring(0,lenght-1));
            }
        });
        Clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulatxt.setText("");
            }
        });


        Proccessbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Input = formulatxt.getText().toString();
                if(!(Input.trim().length()>0))
                    Toast.makeText(MainActivity.this, "Write a forumla before you process please.", Toast.LENGTH_SHORT).show();
                else {
                    Constants.TXT_DEBUG ="";
                    Constants.TXT_OUTPUT="";
                    Intent intent = new Intent(getBaseContext(), Result.class);
                    intent.putExtra("Options", OptionNumber);
                    intent.putExtra("Input", Input);
                    startActivity(intent);
                }
            }
        });
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        formulatxt.setText("");
        OptionNumber =position;
        //String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        switch(position){
            case 0:
                multiDeductionbtn.setEnabled(false);
                Deductionbtn.setEnabled(false);
                Sepbtn.setEnabled(false);
                break;
            case 1:
                multiDeductionbtn.setEnabled(false);
                Deductionbtn.setEnabled(false);
                Sepbtn.setEnabled(false);
                break;
            case 2:
                multiDeductionbtn.setEnabled(false);
                Deductionbtn.setEnabled(false);
                Sepbtn.setEnabled(false);
                break;
            case 3:
                multiDeductionbtn.setEnabled(true);
                Deductionbtn.setEnabled(true);
                Sepbtn.setEnabled(true);
                break;
            case 4:
                multiDeductionbtn.setEnabled(true);
                Deductionbtn.setEnabled(true);
                Sepbtn.setEnabled(true);
                break;
        }
    }


    public void onNothingSelected(AdapterView<?> parent) {

    }

}
