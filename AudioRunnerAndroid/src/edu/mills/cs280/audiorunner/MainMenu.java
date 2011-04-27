package edu.mills.cs280.audiorunner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        Button start = (Button) findViewById(R.id.Start_Button);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), BrowseMusic.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }
}
