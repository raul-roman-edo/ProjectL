package com.insurancetelematics.team.projectl.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.insurancetelematics.team.projectl.R;
import com.insurancetelematics.team.projectl.images.gallery.GalleryFragment;

public class MainActivity extends AppCompatActivity implements MainView {
    private MainPresenter presenter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        Fragment fragment = GalleryFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.screenContainer, fragment).commit();
    }
}
