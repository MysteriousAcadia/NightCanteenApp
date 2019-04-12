package com.example.notanshuman.nightcanteen;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainMenuActivity extends AppCompatActivity {
    TextView amount;
    long amt=0;
    EditText ed1,ed2,ed3;

    public static String order="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        amount=(TextView)findViewById(R.id.amount);
        ed1=(EditText)findViewById(R.id.editText1);
        ed2=(EditText)findViewById(R.id.editText2);
        ed3=findViewById(R.id.editText3);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(amt!=0) {
                    if (ed1.getInputType() == 0) {
                        order += " Item 1: " + ed1.getText().toString();
                        ed1.setText("1");
                        (findViewById(R.id.checkBox1)).setSelected(false);
                    }
                    if (ed2.getInputType() == 0) {
                        order += " Item 2: " + ed2.getText().toString();
                        ed2.setText("1");
                        (findViewById(R.id.checkBox2)).setSelected(false);
                    }
                    if (ed3.getInputType() == 0) {
                        order += " Item 3: " + ed3.getText().toString();
                        ed3.setText("1");
                        (findViewById(R.id.checkBox3)).setSelected(false);
                    }
                    BackgroundTask b1= new BackgroundTask();
                    String x=order+"Total:"+Long.toString(amt);
                    amt=0;
                    b1.execute(x);

                }
                else{
                    Snackbar.make(view, "Please select your items", Snackbar.LENGTH_LONG)
                          .setAction("Action", null).show();

                }

                Intent intent= new Intent(MainMenuActivity.this,PaymentActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox1:
                if (checked){
                    amt+=60*Integer.parseInt(ed1.getText().toString());
                    ed1.setInputType(0);
                }

                else{
                    amt-=60*Integer.parseInt(ed1.getText().toString());
                    ed1.setInputType(2);

                }

                break;
            case R.id.checkBox2:
                if (checked){
                    amt+=60*Integer.parseInt(ed2.getText().toString());
                    ed2.setInputType(0);
                }
                else{
                    amt-=60*Integer.parseInt(ed2.getText().toString());
                    ed2.setInputType(2);
                }

                break;
            case R.id.checkBox3:
                if(checked){
                    amt+=60*Integer.parseInt(ed3.getText().toString());
                    ed3.setInputType(0);
                }
                else{
                    amt-=60*Integer.parseInt(ed3.getText().toString());
                    ed3.setInputType(2);
                }

        }
        amount.setText(Long.toString(amt));
    }
}
class BackgroundTask extends AsyncTask<String ,Void,Void>{
    Socket s;
    PrintWriter printWriter;
    @Override
    protected Void doInBackground(String... voids) {
        try{
            String order=voids[0];
            s=new Socket("10.20.2.48",6000);
            printWriter=new PrintWriter(s.getOutputStream());
            printWriter.write(order);
            printWriter.flush();
            printWriter.close();


        }
        catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }
}
