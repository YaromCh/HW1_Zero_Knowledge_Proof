package com.example.cryptographyhw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PointF;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectArchery extends AppCompatActivity {


    final int CENTER_X=-500;
    final int CENTER_Y=700;
    final int RADIUS=400;
    final String MES="number: ";

    private int nodesArry_X[];
    private int nodesArry_Y[];
    private Boolean[][] archeryArry;
    private RelativeLayout draw_graphLL;
    private int number_of_archeries;
    private TextView mes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_archery);
        mes=findViewById(R.id.num_archery);
        mes.setText(MES+number_of_archeries);

        number_of_archeries=0;
        TextView TV = findViewById(R.id.test);
        String nodes = getIntent().getStringExtra("nodes");
        draw_graphLL = findViewById(R.id.draw_graphLL);

        int nodesArry[] = new int[Integer.valueOf(nodes)];
        for(int i=0; i<Integer.valueOf(nodes); i++){
            nodesArry[i]=0;
        }

        nodesArry_X = new int[Integer.valueOf(nodes)];
        nodesArry_Y = new int[Integer.valueOf(nodes)];

        for(int i=0; i<Integer.valueOf(nodes); i++){
            nodesArry_X[i]=(int)(CENTER_X+RADIUS*(Math.sin(Math.toRadians(360/Integer.valueOf(nodes)*i))));
            nodesArry_Y[i]=(int)(CENTER_Y+RADIUS*(Math.cos(Math.toRadians(360/Integer.valueOf(nodes)*i))));
        }

        TV.setText("Select the archeries:");





        ImageView circleD;

        archeryArry=new Boolean[Integer.valueOf(nodes)][Integer.valueOf(nodes)];
        for(int i=0; i<Integer.valueOf(nodes); i++){
            for(int j=0; j<Integer.valueOf(nodes); j++){
                archeryArry[i][j]=false;
            }
        }



        for(int i=0; i<Integer.valueOf(nodes); i++) {
            circleD = new ImageView(this);
            circleD.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circleD.setImageResource(R.drawable.circle_object);
            circleD.setScaleType(ImageView.ScaleType.FIT_XY);
            circleD.setX(nodesArry_X[i]);
            circleD.setY(nodesArry_Y[i]);
            circleD.setId(i);
            circleD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickOnArchery(v);
                }
            });
            draw_graphLL.addView(circleD);

        }

        Button finish= findViewById(R.id.finish_select_aBT);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectArchery.this, ViewGraph.class);
                intent.putExtra("nodes", nodes);
                intent.putExtra("archery", number_of_archeries+"");
                intent.putExtra("archeryArry",archeryArry_toString(archeryArry));
                startActivity(intent);
            }
        });


    }
    private boolean isFirstBt=true;
    private int idFirstBt;
    private void clickOnArchery(View v){
        if(isFirstBt){
            idFirstBt=v.getId();
        }
        else {
            drawLine(idFirstBt, v.getId());
            number_of_archeries++;
            mes=findViewById(R.id.num_archery);
            mes.setText(MES+number_of_archeries);
            addArcheryToArry(idFirstBt, v.getId());
        }
        isFirstBt=!isFirstBt;
    }

    private void drawLine(int idF, int idL){
        PointF root = new PointF(nodesArry_X[idF]+1050,nodesArry_Y[idF]+36);
        PointF end = new PointF(nodesArry_X[idL]+1050,nodesArry_Y[idL]+36);

        LineView lineV = new LineView(this);
        lineV.setPointA(root);
        lineV.setPointB(end);
        lineV.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        draw_graphLL.addView(lineV);

        lineV.draw();
    }

    private void addArcheryToArry (int idF, int idL){
        archeryArry[idF][idL]=true;
        archeryArry[idL][idF]=true;
    }

    private String archeryArry_toString(Boolean[][] archeryArry){
        String ret="";
        for(int i=0; i<archeryArry.length; i++) {
            for (int j = 0; j < archeryArry.length; j++) {
                if (archeryArry[i][j]) {
                    ret += "1";
                } else {
                    ret += "0";
                }
            }
        }
        return ret;
    }
}