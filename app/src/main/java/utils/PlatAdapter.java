package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.unice.mbds.androiddevdiscoverlb.Person;
import fr.unice.mbds.androiddevdiscoverlb.Plats;
import fr.unice.mbds.androiddevdiscoverlb.R;

/**
 * Created by Thoma on 19/11/2016.
 */

public class PlatAdapter extends BaseAdapter {

    private final RelativeLayout.LayoutParams lp;
    private Context context;
    public List<Plats> plats;
    private RelativeLayout.LayoutParams ltt;
    public List<ImageButton> buttons;
    private Boolean addRemove;
    View.OnClickListener buttonClickListener;


    public PlatAdapter(Context context, List<Plats> plats, Boolean addRemove, View.OnClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
        this.addRemove = addRemove;
        buttons = new ArrayList<>();
        this.context = context;
        this.plats = plats;
        ltt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ltt.setMargins(130, 25, 0, 0);


        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

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

        final Plats p = plats.get(position);
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
                plats.remove(p);
                notifyDataSetChanged();
            }
        });

        // btDel.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1000f));
        //   btDel.setBackgroundColor(Color.GREEN);
        //  btAdd.setClickable(true);
        // btAdd.setEnabled(true);
        LinearLayout lbLL = new LinearLayout(context);
        lbLL.setLayoutParams(lp);
        //  lbLL.addView(btAdd);
        //   lbLL.setBackgroundColor(Color.RED);
        TextView tv = new TextView(context);

        //    tv.setOnClickListener(buttonClickListener);

        tv.setText(p.getName());
        tv.setTextSize(25);
        tv.setLayoutParams(ltt);
        final RelativeLayout LL = new RelativeLayout(context);
        //    LL.setLayoutParams(lp);
        LL.addView(btAdd);
        LL.addView(btDel);
        // LL.addView(iv);
        LL.addView(tv);
        LL.addView(lbLL);

        buttons.add(btAdd);


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
