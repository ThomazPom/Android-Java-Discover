package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.unice.mbds.androiddevdiscoverlb.Person;
import fr.unice.mbds.androiddevdiscoverlb.Plats;
import fr.unice.mbds.androiddevdiscoverlb.R;

import static org.apache.commons.io.IOUtils.copy;

/**
 * Created by Thoma on 19/11/2016.
 */



public class PlatAdapter extends BaseAdapter {


    private Context context;
    public List<Plats> plats;
    private RelativeLayout.LayoutParams imglayout;
    private LinearLayout.LayoutParams ltt;
    public List<ImageButton> buttons;
    private Boolean addRemove;
    public View.OnClickListener buttonClickListener;


    public PlatAdapter(Context context, List<Plats> plats, Boolean addRemove, View.OnClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
        this.addRemove = addRemove;
        buttons = new ArrayList<>();
        this.context = context;
        this.plats = plats;
        ltt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ltt.setMargins(10, 10, 10, 10);



        imglayout = new RelativeLayout.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getCount() {
        return plats.size();
    }

    @Override
    public Object getItem(int position) {
        return plats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        final Plats p  = Plats.cache.get( plats.get(position).getId());

        final PlatAdapter self = this;
        p.pleaseComplete(new CallAPI.Callback() {
            @Override
            public void postCall(Object returnvalue) {
                self.notifyDataSetChanged();
            }
        });
        View v = convertView;

        v = View.inflate(context, R.layout.activity_serveurs, null);

        ImageButton iv = new ImageButton(context);
        iv.setBackgroundColor(Color.TRANSPARENT);

        iv.setImageResource(R.mipmap.red_ball);


//

//  iv.setImageResource(R.mipmap.blue_ball);
        ImageButton btAdd = new ImageButton(context);
        btAdd.setImageResource(R.mipmap.button_add);
        btAdd.setBackgroundColor(Color.TRANSPARENT);
        btAdd.setOnClickListener(buttonClickListener);

        ImageButton btDel = new ImageButton(context);
        btDel.setImageResource(R.mipmap.delete);
        btDel.setBackgroundColor(Color.TRANSPARENT);


        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(p);
                plats.remove(p);
                notifyDataSetChanged();
                buttonClickListener.onClick(v);
            }
        });
        // btDel.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1000f));
        //   btDel.setBackgroundColor(Color.GREEN);
        //  btAdd.setClickable(true);
        // btAdd.setEnabled(true);
       // lbLL.setLayoutParams(lp);
        //  lbLL.addView(btAdd);
        //   lbLL.setBackgroundColor(Color.RED);

        final ImageView photo = new ImageView(context);
        final  PlatAdapter pad =this;

        photo.setImageBitmap(p.getBitmap());

        //    tv.setOnClickListener(buttonClickListener);

        LinearLayout vertical = new LinearLayout(context);

        LinearLayout horizontal = new LinearLayout(context);


        vertical.setLayoutParams(ltt);
        vertical.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(context);
        tv.setText(p.getName());
        tv.setTextSize(25);
        tv.setTextColor(Color.BLUE);
        vertical.addView(tv);

        TextView tv2 = new TextView(context);
        tv2.setText(p.getDescription());
        tv2.setTextSize(10);
        tv2.setTextColor(Color.BLACK);
        vertical.addView(tv2);

        TextView tv3 = new TextView(context);
        tv3.setText(p.getCalories()+"kal, "+p.getPrice()+"€, -"+p.getDiscount()+"% ="+(p.getPrice()*(100-p.getDiscount())/100)+"€");
        tv3.setTextColor(Color.RED);
        tv3.setTextSize(15);
        vertical.addView(tv3);

        //vertical.addView(photo);


        final LinearLayout LL = new LinearLayout(context);
        photo.setLayoutParams(imglayout);
        LL.addView(btAdd);
        LL.addView(btDel);
        LL.addView(photo);
        LL.addView(vertical);

        buttons.add(btAdd);
        vertical.setLayoutParams(ltt);


        btAdd.setVisibility(addRemove ? View.VISIBLE : View.GONE);
        btDel.setVisibility(addRemove ? View.GONE : View.VISIBLE);
        btAdd.setTag(p);
        return LL;
    }

    public void hideButtons(Boolean visible) {
        this.addRemove = visible;
        for (ImageButton v : buttons) {
            v.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
