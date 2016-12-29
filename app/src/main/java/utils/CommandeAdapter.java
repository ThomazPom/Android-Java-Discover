package utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.unice.mbds.androiddevdiscoverlb.Commande;
import fr.unice.mbds.androiddevdiscoverlb.Plats;
import fr.unice.mbds.androiddevdiscoverlb.R;

/**
 * Created by Thoma on 19/11/2016.
 */



public class CommandeAdapter extends BaseAdapter {


    private final RelativeLayout.LayoutParams lp;
    private Context context;
    public List<Commande> commandes;
    private RelativeLayout.LayoutParams imglayout;
    private RelativeLayout.LayoutParams ltt;
    public List<ImageButton> buttons;
    private Boolean addRemove;
    View.OnClickListener lineClickListener;


    public CommandeAdapter(Context context, List<Commande> comandes, Boolean addRemove, View.OnClickListener lineClickListener) {
        this.addRemove = addRemove;
        buttons = new ArrayList<>();
        this.context = context;
        this.commandes = comandes;
        ltt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ltt.setMargins(130, 25, 0, 0);


        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        this.lineClickListener=lineClickListener;

        imglayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250);
        imglayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

    }

    @Override
    public int getCount() {
        return commandes.size();
    }

    @Override
    public Object getItem(int position) {
        return commandes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final Commande c = commandes.get(position);
        View v = convertView;

        v = View.inflate(context, R.layout.activity_serveurs, null);

        ImageButton iv = new ImageButton(context);
        iv.setBackgroundColor(Color.TRANSPARENT);

        iv.setImageResource(R.mipmap.red_ball);


//

//  iv.setImageResource(R.mipmap.blue_ball);


        //    tv.setOnClickListener(buttonClickListener);

        LinearLayout vertical = new LinearLayout(context);

        LinearLayout horizontal = new LinearLayout(context);

        vertical.setLayoutParams(ltt);
        vertical.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(context);
        tv.setText(c.getServeur().getNom()+" "+c.getServeur().getPrenom()+", "+c.getPlats().size()+" plats" );
        tv.setTextSize(25);

        vertical.addView(tv);

        ImageView orderimg = new ImageView(context);
        orderimg.setImageResource(R.mipmap.order);
        orderimg.setBackgroundColor(Color.TRANSPARENT);

        RelativeLayout LL = new RelativeLayout(context);
        //    LL.setLayoutParams(lp);

        LL.addView(orderimg);
        LL.addView(vertical);
        // LL.addView(iv);

        LL.setOnClickListener(lineClickListener);
        LL.setTag(c);
        return LL;
    }

    public void hideButtons(Boolean visible) {
        this.addRemove = visible;
        for (ImageButton v : buttons) {
            v.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
