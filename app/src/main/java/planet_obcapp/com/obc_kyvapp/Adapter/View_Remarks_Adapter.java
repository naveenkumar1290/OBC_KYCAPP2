package planet_obcapp.com.obc_kyvapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import planet_obcapp.com.obc_kyvapp.App_utils.Utility;
import planet_obcapp.com.obc_kyvapp.App_utils.util;
import planet_obcapp.com.obc_kyvapp.R;
import planet_obcapp.com.obc_kyvapp.Visit_CRG;

/**
 * Created by Admin on 7/31/2017.
 */

public class View_Remarks_Adapter extends BaseAdapter {


    private final ArrayList<HashMap<String, String>> types;
    private Context mContext;
    private Activity activity;

    public View_Remarks_Adapter(Context c, ArrayList<HashMap<String, String>> types,Activity activity) {
        mContext = c;
        this.types = types;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final View_Remarks_Adapter.Holder holder;
        if (convertView == null) {
            holder = new View_Remarks_Adapter.Holder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.visit_remarks_item, null);
            holder.visitdate = (TextView) convertView.findViewById(R.id.visitdate);
            holder.remarks = (TextView) convertView.findViewById(R.id.remarks);
            holder.first_visitdate = (TextView) convertView.findViewById(R.id.first_visitdate);

         //   holder.lin_view = (LinearLayout) convertView.findViewById(R.id.lin_view);
            //   holder.lin_click = (LinearLayout) convertView.findViewById(R.id.lin_click);

            convertView.setTag(holder);
        } else {
            holder = (View_Remarks_Adapter.Holder) convertView.getTag();
        }
        holder.visitdate.setText(types.get(position).get(Utility.KEY_DATEOPENREVISIT));
        holder.remarks.setText(types.get(position).get(Utility.KEY_REMARKOPEN));
        holder.first_visitdate.setText(types.get(position).get(Utility.KEY_DATE));



        return convertView;
    }
    class Holder {
        TextView visitdate, remarks,first_visitdate;
       // LinearLayout lin_view;
    }
}
