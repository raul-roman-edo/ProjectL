package com.insurancetelematics.team.projectl.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.insurancetelematics.team.projectl.R;
import com.insurancetelematics.team.projectl.gallery.PresentationAdapter;

public class GalleryActivity extends AppCompatActivity {
    private GridView gridView;
    private PresentationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        adapter = new PresentationAdapter(this);
        gridView = (GridView) findViewById(R.id.gridview_gallery);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickItem(position);
            }
        });
    }

    private void clickItem(int position) {
    }
}
