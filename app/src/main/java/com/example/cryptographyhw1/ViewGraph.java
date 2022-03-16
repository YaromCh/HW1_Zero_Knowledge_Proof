package com.example.cryptographyhw1;

import androidx.appcompat.app.AppCompatActivity;



import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ViewGraph extends AppCompatActivity {

    final int CENTER_X=-500;
    final int CENTER_Y=700;
    final int RADIUS=400;
    final int DELAY_TAIME=1500;
    final int SHORT_DELAY_TAIME=400;

    private Boolean[][] archeryArry;
    private TextView TV;
    private ImageView turn_state;
    private String nodes;
    private boolean IS_SHOW;
    private boolean is_auto_run;
    private double probability;

    private int nodesArry_X[];
    private int nodesArry_Y[];
    private RelativeLayout draw_graphLL;
    private ImageView circleD[];
    private int nodesArry[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_graph);
        turn_state=findViewById(R.id.turn_state);

        TV = findViewById(R.id.test_VG);
        nodes = getIntent().getStringExtra("nodes");
        String archery = getIntent().getStringExtra("archery");
        archeryArry=new Boolean[Integer.valueOf(nodes)][Integer.valueOf(nodes)];
        IS_SHOW=false;
        TV.setText("nodes: "+nodes+", archery: "+archery);
        stringTo_archeryArry(getIntent().getStringExtra("archeryArry"));

        draw_graphLL = findViewById(R.id.draw_graphLL_VG);

        nodesArry = new int[Integer.valueOf(nodes)];
        for(int i=0; i<Integer.valueOf(nodes); i++){
            nodesArry[i]=0;
        }

        nodesArry_X = new int[Integer.valueOf(nodes)];
        nodesArry_Y = new int[Integer.valueOf(nodes)];

        for(int i=0; i<Integer.valueOf(nodes); i++){
            nodesArry_X[i]=(int)(CENTER_X+RADIUS*(Math.sin(Math.toRadians(360/Integer.valueOf(nodes)*i))));
            nodesArry_Y[i]=(int)(CENTER_Y+RADIUS*(Math.cos(Math.toRadians(360/Integer.valueOf(nodes)*i))));
        }


        circleD=new ImageView[Integer.parseInt(nodes)];
        createNodes();

        //hideColor(circleD,nodesArry);
        drawAllLine();

        Button statusBT = findViewById(R.id.statusBT);
        statusBT.setText("Cheat!!!");
        statusBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusBT.getText()=="Cheat!!!") {
                    statusBT.setText("Hide");
                    viewColor(circleD,nodesArry);
                }
                else {
                    statusBT.setText("Cheat!!!");
                    hideColor(circleD,nodesArry);
                }
            }
        });

        is_auto_run=false;
        Button AutoRunBT = findViewById(R.id.auto_run);
        AutoRunBT.setText("Run");
        AutoRunBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AutoRunBT.getText()=="Run") {
                    AutoRunBT.setText("Stop");
                    is_auto_run=true;
                    autoRun();
                } else{
                    AutoRunBT.setText("Run");
                    is_auto_run=false;

                }
            }
        });


        probability=0;
        TextView probabilityTV = findViewById(R.id.probabilityTV);
        probabilityTV.setText("probability");
        if(!fillAllTheNodesInColors()){
            Toast.makeText(ViewGraph.this, "Is not possible to fill with 3 colors!",
                    Toast.LENGTH_LONG).show();
        }
    }

    //-------------------START PRIVATE FUNC------------------------------------

    private void createNodes (){
        for(int i=0; i<Integer.valueOf(nodes); i++) {
            circleD[i] = new ImageView(this);
            circleD[i].setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circleD[i].setImageResource(R.drawable.circle_object);
            circleD[i].setScaleType(ImageView.ScaleType.FIT_XY);
            circleD[i].setX(nodesArry_X[i]);
            circleD[i].setY(nodesArry_Y[i]);
            circleD[i].setId(i);
            circleD[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!IS_SHOW) {
                        showNeighbors(v.getId());
                        hideDelay();
                        fillAllTheNodesInColors();
                        IS_SHOW=true;
                    }
                }
            });
            draw_graphLL.addView(circleD[i]);
        }

    }

    private void hideColor(ImageView image[], int nodesArry[]){
        turn_state.setImageResource(R.drawable.user_rutn);
        for(int i=0; i<Integer.valueOf(nodesArry.length); i++)
            image[i].setColorFilter(getResources().getColor(R.color.white));
    }

    private void viewColor(ImageView image[], int nodesArry[]){
        for(int i=0; i<Integer.valueOf(nodesArry.length); i++) {
            switch (nodesArry[i]) {
                case 1:
                    image[i].setColorFilter(getResources().getColor(R.color.option1));
                    break;
                case 2:
                    image[i].setColorFilter(getResources().getColor(R.color.option2));
                    break;
                case 3:
                    image[i].setColorFilter(getResources().getColor(R.color.option3));
                    break;
            }
        }
    }

    private void stringTo_archeryArry(String s){
        int index=0;
        for(int i=0; i<Integer.valueOf(nodes); i++){
            for(int j=0; j<Integer.valueOf(nodes); j++){
                if (s.charAt(index)=='0')
                    archeryArry[i][j]=false;
                else
                    archeryArry[i][j]=true;
                index++;
            }
        }
    }

    private void drawAllLine(){
        for(int i=0; i<circleD.length;i++){
            for(int j=0; j<circleD.length;j++){
                if(i<=j){
                    if(archeryArry[i][j]) {
                        drawLine(circleD[i].getId(), circleD[j].getId());
                    }
                }
            }
        }
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
    private void showNeighbors(int index){
        //--------------delete current views and create again:
        if(!is_auto_run) {
            draw_graphLL.removeAllViews();
            changeToIzumprfyGraph();
            createNodes();
            drawAllLine();
        }
        //--------------finish to create again and show the neighbors
        showNode(index);
        for (int i=0; i<circleD.length; i++){
            if(archeryArry[index][i])
                showNode(i);
        }
    }
    private void showNode(int index){
        turn_state.setImageResource(R.drawable.android_turn);
        addProbability();
        switch (nodesArry[index]) {
            case 1:
                circleD[index].setColorFilter(getResources().getColor(R.color.option1));
                break;
            case 2:
                circleD[index].setColorFilter(getResources().getColor(R.color.option2));
                break;
            case 3:
                circleD[index].setColorFilter(getResources().getColor(R.color.option3));
                break;
        }
    }

    private void hideDelay(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(DELAY_TAIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hideColor(circleD,nodesArry);
                IS_SHOW=false;
            }
        }.start();
    }

    private void autoRun(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (is_auto_run) {
                    int rand = (int) (Math.random() * nodesArry.length);
                    showNeighbors(rand);
                    try {
                        Thread.sleep(SHORT_DELAY_TAIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    hideColor(circleD, nodesArry);
                    fillAllTheNodesInColors();
                }
            }
        }.start();
    }

    private void addProbability(){
        TextView probabilityTV = findViewById(R.id.probabilityTV);
        probability+=(1-probability)*((double)1)/((double)(Integer.valueOf(nodes)*Integer.valueOf(nodes)));
        probabilityTV.setText("probability: "+(int)(probability*100)+"%");
    }
    //----------------------------CREATE IZUMORFY GRAPH----------------------------

    private void changeToIzumprfyGraph(){
        Boolean[][] temp_archeryArry=new Boolean[Integer.valueOf(nodes)][Integer.valueOf(nodes)];
        int[] hashIndex = new int[Integer.valueOf(nodes)];
        for(int i=0; i<Integer.valueOf(nodes); i++)
            hashIndex[i]=i;
        int replace1=-1;
        int temp=0;
        for(int i=0; i<Integer.valueOf(nodes); i++){
            replace1 = (int)(Math.random()*Integer.valueOf(nodes));
            temp=hashIndex[0];
            hashIndex[0]=hashIndex[replace1];
            hashIndex[replace1]=temp;
        }

//        Toast.makeText(ViewGraph.this, hashIndex[0]+','+hashIndex[1]+','+hashIndex[2]+','+hashIndex[3]+',',
//                Toast.LENGTH_LONG).show();

        for(int i=0; i<Integer.valueOf(nodes); i++){
            for(int j=0; j<Integer.valueOf(nodes); j++) {
                temp_archeryArry[hashIndex[i]][hashIndex[j]]=archeryArry[i][j];
            }
        }
        for(int i=0; i<Integer.valueOf(nodes); i++) {
            for (int j = 0; j < Integer.valueOf(nodes); j++) {
                archeryArry[i][j] = temp_archeryArry[i][j];
            }
        }
    }

    //----------------------------CHECK ALGORITHEM--------------------------------
    private boolean ifNeighborsInColor(int indexNode, int color){
        for(int i=0; i<archeryArry.length; i++) {
            if (archeryArry[indexNode][i]){
                if(nodesArry[i]==color)
                    return true;
            }
        }
        return false;
    }
    private boolean ifCanFillInColor(int indexNode, int color){
        if (!ifNeighborsInColor(indexNode, color)) {
            nodesArry[indexNode]=color;
            return true;
        }
        return false;
    }
    private boolean checkAllOptionsColorToNode(int indexNode, int currentColor){
        for(int i=0; i<3; i++) {
            currentColor++;
            if (currentColor==4)
                currentColor=1;
            if((!ifCanFillInColor(indexNode, currentColor))&&(nodesArry[indexNode]==0))
                return false;
        }
        return true;
    }
    private boolean moveNexNode(int indexNode, int currentColor){
        ifNodeChecked[indexNode]=true;
        for(int i=0; i<nodesArry.length; i++) {
            if(archeryArry[indexNode][i]) {
                if(!checkAllOptionsColorToNode(i, currentColor))
                    return false;
            }
        }
        for(int i=0; i<nodesArry.length; i++) {
            if(archeryArry[indexNode][i])
                if(!ifNodeChecked[i])
                    moveNexNode(i,currentColor);
        }
        return true;
    }
    private Boolean fillRandomColor(int startNode){
        ifNodeChecked=new boolean[nodesArry.length];
        for(int i=0; i<nodesArry.length;i++)
            ifNodeChecked[i]=false;
        int currentColor=(int)(Math.random()*3)+1;
        nodesArry[startNode]=currentColor;
        ifNodeChecked[startNode]=true;
        return moveNexNode(startNode,currentColor);
    }
    private boolean[] ifNodeChecked;
    private boolean fillAllTheNodesInColors(){
        int startNode=(int)(Math.random()*nodesArry.length);
        if(!fillRandomColor(startNode))
            return false;
        for(int i=0; i<nodesArry.length; i++)
            if(nodesArry[i]==0)
                if(!fillRandomColor(i))
                    return false;
        return true;
    }
}