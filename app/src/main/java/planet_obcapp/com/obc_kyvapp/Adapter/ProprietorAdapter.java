package planet_obcapp.com.obc_kyvapp.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import planet_obcapp.com.obc_kyvapp.App_utils.util;
import planet_obcapp.com.obc_kyvapp.R;

/**
 * Created by planet on 3/30/17.
 */

public class ProprietorAdapter extends BaseAdapter {


    private final ArrayList<HashMap<String, String>> types;
    public ArrayList<String> checkedIds = new ArrayList<>();
    private Context mContext;
    private String pro;
    private String pref_pro = "";

    public ProprietorAdapter(Context c, ArrayList<HashMap<String, String>> types, String pro, String pref_pro) {
        mContext = c;
        this.types = types;

        this.pro = pro;
        this.pref_pro = pref_pro;
    }

    @Override

    public int getCount() {
        if (types == null) {
            return 0;
        }

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

        final Holder holder;
        if (convertView == null) {
            holder = new Holder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.propriter_cheak, null);
            holder.chk = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        String pid = types.get(position).get("P_Id");
        String KYCProprietorName = types.get(position).get("KYCProprietorName");
        holder.chk.setText(KYCProprietorName);
        holder.chk.setId(Integer.parseInt(pid));

        if ((pro != null)) {
            holder.chk.setEnabled(false);
            String pros[] = pro.split(",");

            for (int i = 0; i < pros.length; i++) {
                if (pros[i].equalsIgnoreCase(pid)) {
                    holder.chk.setChecked(true);

                }
            }
        }

        if ((pref_pro != null)) {
            String str = pref_pro;
            str = str.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll(" ", "");
            String pros[] = str.split(",");

            String property_type_id = Integer.toString(holder.chk.getId());
            for (int i = 0; i < pros.length; i++) {
                if (pros[i].equals(pid)) {
                    holder.chk.setChecked(true);
                    HashSet<String> hashSet = new HashSet<String>();
                    hashSet.addAll(checkedIds);
                    checkedIds.clear();
                    checkedIds.addAll(hashSet);
                    if (!checkedIds.contains(property_type_id)) {
                        checkedIds.add(property_type_id);
                    }

                }


            }
        }
        holder.chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String property_type_id = Integer.toString(holder.chk.getId());
                    if (((CheckBox) v).isChecked()) {
                        //Toast.makeText(mContext, property_type_id, Toast.LENGTH_SHORT).show();
                        checkedIds.add(property_type_id);
                    } else {
                        checkedIds.remove(property_type_id);
                    }

                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < checkedIds.size(); i++) {
                        JSONObject contact = new JSONObject();
                        try {
                            contact.put("P_Id", checkedIds.get(i));
                            jsonArray.put(contact);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    String jsonString = jsonArray.toString();
                    util.setP_id("P_Id", mContext, jsonString);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (holder.chk.isChecked()) {

                }
            }
        });

        return convertView;
    }

    public ArrayList<String> checkedPropertyIds() {
        return checkedIds;
    }

    class Holder {
        CheckBox chk;
    }
}






