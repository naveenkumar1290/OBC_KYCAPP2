package planet_obcapp.com.obc_kyvapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

import planet_obcapp.com.obc_kyvapp.App_utils.Utility;
import planet_obcapp.com.obc_kyvapp.App_utils.util;
import planet_obcapp.com.obc_kyvapp.Login_Activity;
import planet_obcapp.com.obc_kyvapp.R;
import planet_obcapp.com.obc_kyvapp.View_Remark_CRG;
import planet_obcapp.com.obc_kyvapp.VisitRecordFirst_Activity;
import planet_obcapp.com.obc_kyvapp.Visit_CRG;

/**
 * Created by Admin on 7/21/2017.
 */

public class Visit_Histry_CRG_Adapter extends BaseAdapter {


    private final ArrayList<HashMap<String, String>> types;
    private Context mContext;
    private String ids;
    private String receivedString, responsestatus,encd_images2;
    public static final String FROM_EDIT = "EDIT";
    private  String histary_record="";
    private Activity activity;
    private String remarks,date_REVISIT;
    public Visit_Histry_CRG_Adapter(Context c, ArrayList<HashMap<String, String>> types,Activity activity) {
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

        final Visit_Histry_CRG_Adapter.Holder holder;
        if (convertView == null) {
            holder = new Visit_Histry_CRG_Adapter.Holder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.visit_history_crg_list_item, null);
            holder.visitdate = (TextView) convertView.findViewById(R.id.visitdate);
            holder.customer_name = (TextView) convertView.findViewById(R.id.customer_name);
            holder.view_remarks = (TextView) convertView.findViewById(R.id.view_remarks);
            holder.status_lead = (TextView) convertView.findViewById(R.id.status_lead);
            holder.lin_view = (LinearLayout) convertView.findViewById(R.id.lin);
         //   holder.lin_click = (LinearLayout) convertView.findViewById(R.id.lin_click);

            convertView.setTag(holder);
        } else {
            holder = (Visit_Histry_CRG_Adapter.Holder) convertView.getTag();
        }
        holder.visitdate.setText(types.get(position).get(Utility.KEY_DATE_TIME));
        holder.customer_name.setText(types.get(position).get(Utility.KEY_CUSTOMER_NAME));

    //    holder.lead_type.setText(types.get(position).get(Utility.KEY_LEAD_TYPE));
        holder.status_lead.setText(types.get(position).get(Utility.KEY_STATUS_LEAD));




//        if(position<9) {
//            holder.record_no.setText("0"+(position+1));
//
//        }else{
//            holder.record_no.setText(""+(position+1));
//        }
//
//        holder.click.setText("Click");

        holder.view_remarks.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
//                remarks = types.get(position).get(Utility.KEY_REMARK);
//                date_REVISIT  = types.get(position).get(Utility.KEY_DATE_REVISIT);
//                if(remarks!=null && remarks.length()==0 && date_REVISIT!=null){
//                    Toast.makeText(mContext,
//                            "No Remark", Toast.LENGTH_LONG).show();
              //  }else{
                    Intent mainIntent = new Intent(mContext, View_Remark_CRG.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainIntent.putExtra("UNIID", types.get(position).get( Utility.KEY_UNIIDE));
                    mContext.startActivity(mainIntent);

                   // View_remark();
             //   }



         }
        });

        holder.lin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                name =  types.get(position).get( Utility.KEY_VISITER_NAME);
//                pf_no =   types.get(position).get(Utility.KEY_PF_NO);
//                pro_name =    types.get(position).get(Utility.KEY_PROPRIETOR_NAME);
//                addh_no =   types.get(position).get( Utility.KEY_AADHAR_NO);
//                firm_name =   types.get(position).get(Utility.KEY_FIRM_NAME);
//                curr_date =   types.get(position).get(Utility.KEY_DATE_VISIT);
//                acc_no =  types.get(position).get(Utility.KEY_ACCOUNT_NO);
//                addd=  types.get(position).get(Utility.KEY_ADDRESS);
//                buss_act =    types.get(position).get(Utility.KEY_BUSI_ACTIVITY);
//                month_date = types.get(position).get(Utility.KEY_MONTH_DATE);
//                con_no =   types.get(position).get(Utility.KEY_CONTECT_NO);
//                remark=   types.get(position).get( Utility.KEY_REMARKS);
//                encd_images=  types.get(position).get( Utility.KEY_ENCODE_IMAGES);
//                encd_images2=  types.get(position).get( Utility.KEY_IMAGE_SEC);
//

            }
        });
        holder.lin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Send_saveData(position);

            }
        });
        return convertView;
    }
    class Holder {
        TextView visitdate, customer_name, view_remarks, status_lead;
        LinearLayout lin_view;
    }
    private void Send_saveData(int position) {

        histary_record="Histary_record";
        Intent mainIntent = new Intent(mContext, Visit_CRG.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.putExtra("New",histary_record);
        mainIntent.putExtra("UNIID", types.get(position).get( Utility.KEY_UNIIDE));
        mainIntent.putExtra("ID", types.get(position).get( Utility.KEY_IDE));
        mainIntent.putExtra("date_time", types.get(position).get( Utility.KEY_DATE_TIME));
        mainIntent.putExtra("customer_name", types.get(position).get(Utility.KEY_CUSTOMER_NAME));
        mainIntent.putExtra("con_person",types.get(position).get( Utility.KEY_CON_PERSON));
        mainIntent.putExtra("con_detail", types.get(position).get(Utility.KEY_CON_DETAILS));
        mainIntent.putExtra("lead_type",types.get(position).get( Utility.KEY_LEAD_TYPE));
        mainIntent.putExtra("status_lead", types.get(position).get(Utility.KEY_STATUS_LEAD));
        mainIntent.putExtra("date_revist",types.get(position).get(Utility.KEY_DATE_REVISIT));
        mainIntent.putExtra("remark", types.get(position).get(Utility.KEY_REMARK));
        mainIntent.putExtra("date_close", types.get(position).get(Utility.KEY_DATE_CLOSE));
        mainIntent.putExtra("date_conversion", types.get(position).get(Utility.KEY_DATE_CONVERSION));
        mainIntent.putExtra("sol_id", types.get(position).get(Utility.KEY_SOLL_ID));
        mainIntent.putExtra("no_of_account", types.get(position).get(Utility.KEY_NO_OF_ACCOUNT));
        mainIntent.putExtra("account",types.get(position).get( Utility.KEY_ACCOUNT));


        util.setFirstIma(mContext,types.get(position).get( Utility.KEY_IMAGE_ONE));
        util.setSECIma(mContext,types.get(position).get( Utility.KEY_IMAGE_TWO));
        //  mainIntent.putExtra("images",types.get(position).get( Utility.KEY_ENCODE_IMAGES));
        mainIntent.putExtra("latti",types.get(position).get( Utility.KEY_LATI));


        mainIntent.putExtra("longi",types.get(position).get( Utility.KEY_LONGII));
        mainIntent.putExtra("location",types.get(position).get( Utility.KEY_LOCATION));

        mainIntent.putExtra("UniqNO",types.get(position).get( Utility.KEY_UNIQENO));
        mainIntent.putExtra("Lead_type_two",types.get(position).get( Utility.KEY_LEADTYPETWO));

        mainIntent.putExtra("from", FROM_EDIT);

        //   mainIntent.putExtra("IMg_sec",types.get(position).get( Utility.KEY_IMAGE_SEC));

        mContext.startActivity(mainIntent);
        //activity.finish();

    }

    private void View_remark() {

        final Dialog dialog2 = new Dialog(mContext);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.remark_dilog);
        dialog2.setCanceledOnTouchOutside(false);
        EditText date_revisit = (EditText)dialog2.findViewById(R.id.date_revisit);
        EditText remark = (EditText)dialog2.findViewById(R.id.remark);
        TextView close = (TextView)dialog2.findViewById(R.id.close);
        date_revisit.setText(date_REVISIT);
        remark.setText(remarks);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();

    }



}
