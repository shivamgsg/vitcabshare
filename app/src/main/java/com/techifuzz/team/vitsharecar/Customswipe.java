package com.techifuzz.team.vitsharecar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mridul on 03-10-2017.
 */

public class Customswipe extends PagerAdapter {
    private int [] image={R.drawable.mridul,R.drawable.shivam};
    private Context context;
    private LayoutInflater layoutInflater;

    public Customswipe(Context c) {
        context=c;
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemview=layoutInflater.inflate(R.layout.swipe_1,container,false);
        ImageView imageView=(ImageView) itemview.findViewById(R.id.pageviewer_image);
        TextView textView=(TextView) itemview.findViewById(R.id.start_text);
        imageView.setImageResource(image[position]);
//        textView.setText("Image Counter"+position);
        container.addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

}
