package com.example.valchapple.sqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Connect Text field & Submit Button
//    Button linearV = (Button) findViewById(R.id.btn_lin_v);
//        linearV.setOnClickListener(new View.OnClickListener(){
//
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(MainActivity.this, LinearVerticalViewActivity.class);
//            startActivity(intent);
//        }
//    });

    // Connect ListView and Adapter
//    Item[] items = Item.getItems(GridViewActivity.this);
//    GridView gridView = (GridView)findViewById(R.id.gridview);
//    ItemsAdapter itemsAdapter = new ItemsAdapter(this, items);
//        gridView.setAdapter(itemsAdapter);
}
