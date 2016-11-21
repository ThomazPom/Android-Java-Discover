package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.unice.mbds.androiddevdiscoverlb.Person;
import fr.unice.mbds.androiddevdiscoverlb.R;
import fr.unice.mbds.androiddevdiscoverlb.ServeursActivity;

/**
 * Created by Thoma on 19/11/2016.
 */

public class PersonneAdapter extends BaseAdapter {

    private final RelativeLayout.LayoutParams lp;
    private Context context;
    public List<Person> person;
    private RelativeLayout.LayoutParams ltt;
    public List<ImageButton> buttons;
    private Boolean showButtons;


    public PersonneAdapter(Context context, List<Person> person, Boolean showButtons) {
        this.showButtons = showButtons;
        buttons = new ArrayList<>();
        this.context = context;
        this.person = person;
        ltt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ltt.setMargins(130, 25, 0, 0);


        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

    }

    @Override
    public int getCount() {
        return person.size();
    }

    @Override
    public Object getItem(int position) {
        return person.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final Person p = person.get(position);
        View v = convertView;

        v = View.inflate(context, R.layout.activity_serveurs, null);

         ImageButton iv = new ImageButton(context);
        iv.setBackgroundColor(Color.TRANSPARENT);
        if (p.getConnected()) {
            iv.setImageResource(R.mipmap.grey_ball);
            if(p.getBuzze())
            {
                iv.setImageResource(R.mipmap.green_ball);

            }
            iv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ImageButton im = (ImageButton)v;
                    im.setImageResource(R.mipmap.green_ball);
                    p.setBuzze(true);
                    return true;
                }
            });
        } else {
            iv.setImageResource(R.mipmap.red_ball);
        }

//

//  iv.setImageResource(R.mipmap.blue_ball);
        ImageButton btDel = new ImageButton(context);
        // btDel.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1000f));
        btDel.setImageResource(R.mipmap.delete);
        btDel.setBackgroundColor(Color.TRANSPARENT);
        //   btDel.setBackgroundColor(Color.GREEN);


        LinearLayout lbLL = new LinearLayout(context);
        lbLL.setLayoutParams(lp);
        lbLL.addView(btDel);
        //   lbLL.setBackgroundColor(Color.RED);
        TextView tv = new TextView(context);
        tv.setText(p.getNom() + " " + p.getPrenom());
        tv.setTextSize(25);
        tv.setLayoutParams(ltt);
        final RelativeLayout LL = new RelativeLayout(context);
        //    LL.setLayoutParams(lp);
        LL.addView(iv);
        LL.addView(tv);
        LL.addView(lbLL);
        buttons.add(btDel);



        btDel.setVisibility(showButtons ? View.VISIBLE : View.INVISIBLE);

        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(context)
                        .setMessage("Voulez vous vraiment supprimer " + p.getNom() + " " + p.getPrenom() + "(" + p.getEmail() + ")")
                        .setCancelable(false)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                p.delete(context);
                                Log.d("lparent ", LL.getParent().getClass().toString());
                                ListView lV = (ListView) LL.getParent();
                                // lV.removeViewAt(position);
                                person.remove(p);
                                buttons.remove(v);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Non, annuler", null)
                        .show();
            }
        });

        return LL;
    }

    public void hideButtons(Boolean visible) {
        this.showButtons = visible;
        for (ImageButton v : buttons) {
            v.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
