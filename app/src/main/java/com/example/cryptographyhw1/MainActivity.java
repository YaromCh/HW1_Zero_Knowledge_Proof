// HW1 - Cryptography
// Students:
// 1. Elad Zafrani, I.D: 313128621
// 2. Yarom Chernia, I.D: 311374730

package com.example.cryptographyhw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button create_graphBT = findViewById(R.id.create_graphBT);
        EditText nodesET = findViewById(R.id.nodesET);

        create_graphBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nodes = nodesET.getText().toString();

                Intent intent = new Intent(MainActivity.this, SelectArchery.class);
                intent.putExtra("nodes", nodes);
                startActivity(intent);
            }
        });

    }
}