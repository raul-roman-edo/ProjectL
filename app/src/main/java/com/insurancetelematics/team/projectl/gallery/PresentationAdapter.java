package com.insurancetelematics.team.projectl.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class PresentationAdapter extends BaseAdapter {

    private List<PresentationDto> presentationDtoList;
    private Context context;

    public PresentationAdapter(Context context) {
        this.context = context;
        presentationDtoList = new ArrayList<>();
    }

    public void updatePresentationData(List<PresentationDto> listOfPresentation) {
        presentationDtoList.clear();
        presentationDtoList.addAll(listOfPresentation);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return presentationDtoList.size();
    }

    @Override
    public Object getItem(int position) {
        return presentationDtoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(context).load(presentationDtoList.get(position).urlImage).into(imageView);
        return imageView;
    }
}
