package com.example.valchapple.sqlite;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditText textView;
        final Button buttonSubmit;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Connect Text field & Submit Button
        textView = (EditText) findViewById(R.id.editText);
        buttonSubmit = (Button) findViewById(R.id.button_submit);

        buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Submit Clicked");
                // Get Text
                String txt = textView.getText().toString();

                // Check txt is not empty
                if( txt.length() > 0) {
                    Log.d(TAG, "Submit Text Not Empty.");

                }
            }
        });

        // Connect ListView and Adapter
        //    Item[] items = Item.getItems(GridViewActivity.this);
        //    GridView gridView = (GridView)findViewById(R.id.gridview);
        //    ItemsAdapter itemsAdapter = new ItemsAdapter(this, items);
        //        gridView.setAdapter(itemsAdapter);
    }
}
