package planet_obcapp.com.obc_kyvapp.Adapter;

import android.app.AlertDialog;
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
import android.widget.BaseAdapter;
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
import planet_obcapp.com.obc_kyvapp.R;
import planet_obcapp.com.obc_kyvapp.VisitRecordFirst_Activity;


/**
 * Created by Admin on 4/10/2017.
 */

public class VisitHistoryAdapter extends BaseAdapter {


    private final ArrayList<HashMap<String, String>> types;
    private Context mContext;
    private String ids;
    private String receivedString, responsestatus,encd_images2;
    int catchhandl;

     private String name,pf_no,pro_name,addh_no,firm_name,curr_date,acc_no,addd,buss_act,month_date,con_no,remark,encd_images,latti,longi,pro_location,proprietor,firm,solutation,email,user_id,sol_id,uniq_visit_no,subMitdataLimit;


    public VisitHistoryAdapter(Context c, ArrayList<HashMap<String, String>> types) {
        mContext = c;
        this.types = types;

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

        final VisitHistoryAdapter.Holder holder;
        if (convertView == null) {
            holder = new VisitHistoryAdapter.Holder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.visit_history_list_item, null);
            holder.date = (TextView) convertView.findViewById(R.id.visitdate);
            holder.firm_name = (TextView) convertView.findViewById(R.id.firmname);
            holder.record_no = (TextView) convertView.findViewById(R.id.record_no);
            holder.click = (TextView) convertView.findViewById(R.id.click_txt);
            holder.lin_view = (LinearLayout) convertView.findViewById(R.id.lin_view);
            holder.lin_click = (LinearLayout) convertView.findViewById(R.id.lin_click);
            holder.view = (TextView) convertView.findViewById(R.id.view);
            convertView.setTag(holder);
        } else {
            holder = (VisitHistoryAdapter.Holder) convertView.getTag();
        }
        holder.date.setText(types.get(position).get(Utility.KEY_DATE_VISIT));
        holder.firm_name.setText(types.get(position).get(Utility.KEY_FIRM_NAME));

       if(position<9) {
            holder.record_no.setText("0"+(position+1));

       }else{
           holder.record_no.setText(""+(position+1));
       }

        holder.click.setText("Click");



        holder.lin_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name =  types.get(position).get( Utility.KEY_VISITER_NAME);
                pf_no =   types.get(position).get(Utility.KEY_PF_NO);
                pro_name =    types.get(position).get(Utility.KEY_PROPRIETOR_NAME);
                addh_no =   types.get(position).get( Utility.KEY_AADHAR_NO);
                firm_name =   types.get(position).get(Utility.KEY_FIRM_NAME);
                curr_date =   types.get(position).get(Utility.KEY_DATE_VISIT);
                acc_no =  types.get(position).get(Utility.KEY_ACCOUNT_NO);
                addd=  types.get(position).get(Utility.KEY_ADDRESS);
                buss_act =    types.get(position).get(Utility.KEY_BUSI_ACTIVITY);
                month_date = types.get(position).get(Utility.KEY_MONTH_DATE);
                con_no =   types.get(position).get(Utility.KEY_CONTECT_NO);
                remark=   types.get(position).get( Utility.KEY_REMARKS);
                encd_images=  types.get(position).get( Utility.KEY_ENCODE_IMAGES);
                encd_images2=  types.get(position).get( Utility.KEY_IMAGE_SEC);
                latti= types.get(position).get( Utility.KEY_LATTI);
                longi= types.get(position).get( Utility.KEY_LONGI);
                pro_location =  types.get(position).get( Utility.KEY_PROPREITOR_LOCATION);
                proprietor =   types.get(position).get( Utility.KEY_PROPRIETOR);
                firm =  types.get(position).get( Utility.KEY_FIRM);

                solutation= types.get(position).get( Utility.KEY_SALUTATION);
                email =  types.get(position).get( Utility.KEY_EMAIL_ID);
                user_id =   types.get(position).get( Utility.KEY_USER_ID);
                sol_id =  types.get(position).get( Utility.KEY_SOL_ID);
                uniq_visit_no =  types.get(position).get( Utility.KEY_VISIT_NO);






                AlertDialog alertbox = new AlertDialog.Builder(mContext)
                        .setMessage("Are you sure you want to resend this  Visit Record?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {


                                if (isConnectingToInternet() == true) {
                                    new performBackgroundTask().execute();
                                } else {
                                    Toast.makeText(mContext,
                                            "No Internet Connection....", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        })
                        .show();

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
        TextView date, firm_name, record_no, click,view;
        LinearLayout lin_view,lin_click;
    }
    private void Send_saveData(int position) {


            Intent mainIntent = new Intent(mContext, VisitRecordFirst_Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.putExtra("name", types.get(position).get( Utility.KEY_VISITER_NAME));
            mainIntent.putExtra("Pf_No", types.get(position).get(Utility.KEY_PF_NO));
            mainIntent.putExtra("sap_spn",types.get(position).get( Utility.KEY_SALUTATION));
            mainIntent.putExtra("pro_name", types.get(position).get(Utility.KEY_PROPRIETOR_NAME));
            mainIntent.putExtra("aadh_no",types.get(position).get( Utility.KEY_AADHAR_NO));
            mainIntent.putExtra("firm_name", types.get(position).get(Utility.KEY_FIRM_NAME));
            mainIntent.putExtra("curr_date",types.get(position).get(Utility.KEY_DATE_VISIT));
            mainIntent.putExtra("ac_no", types.get(position).get(Utility.KEY_ACCOUNT_NO));
            mainIntent.putExtra("address", types.get(position).get(Utility.KEY_ADDRESS));
            mainIntent.putExtra("buss_act", types.get(position).get(Utility.KEY_BUSI_ACTIVITY));
            mainIntent.putExtra("month_date", types.get(position).get(Utility.KEY_MONTH_DATE));
            mainIntent.putExtra("con_no", types.get(position).get(Utility.KEY_CONTECT_NO));
            mainIntent.putExtra("remarks",types.get(position).get( Utility.KEY_REMARKS));
            util.setFirstImage(mContext,types.get(position).get( Utility.KEY_ENCODE_IMAGES));

          //  mainIntent.putExtra("images",types.get(position).get( Utility.KEY_ENCODE_IMAGES));
        mainIntent.putExtra("latti",types.get(position).get( Utility.KEY_LATTI));
        mainIntent.putExtra("longi",types.get(position).get( Utility.KEY_LONGI));
        mainIntent.putExtra("pro_location",types.get(position).get( Utility.KEY_PROPREITOR_LOCATION));
        mainIntent.putExtra("propreitor",types.get(position).get( Utility.KEY_PROPRIETOR));
        mainIntent.putExtra("firm",types.get(position).get( Utility.KEY_FIRM));
        mainIntent.putExtra("Visit_no",types.get(position).get( Utility.KEY_VISIT_NO));
     //   mainIntent.putExtra("IMg_sec",types.get(position).get( Utility.KEY_IMAGE_SEC));
        util.setSECImage(mContext,types.get(position).get( Utility.KEY_IMAGE_SEC));
            mContext.startActivity(mainIntent);

    }


    private boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    class performBackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(mContext);

        @Override
        protected void onPreExecute() {
            Dialog.setMessage("Please wait...");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        public void internetAlertDialog()
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle("No Internet Connection");
            alertDialogBuilder
                    .setMessage("Check Internet Connection!")
                    .setCancelable(true)

                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            //   Complain.this.finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            try {
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                }

                if(catchhandl==0){
                if(subMitdataLimit.equals("99")){

                    util.showAlert(mContext,"Data can not submit more than 99 records per day");

                }else{

                    if(responsestatus.equalsIgnoreCase("Data Submitted Successfully")){
                        Toast.makeText(mContext,
                            "Data Resent Successfully",Toast.LENGTH_LONG).show();
                }else{
                        Toast.makeText(mContext,
                                "Null",Toast.LENGTH_LONG).show();

                    }
                }
                }else {
                    Toast.makeText(mContext,
                            "Network Error...Please try again", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(mContext,
                        "Network Error...", Toast.LENGTH_LONG).show();
                Dialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            sand_visitData();
            return null;
        }
    }

   private void sand_visitData()
    {
     catchhandl=0;
        final String NAMESPACE = "http://tempuri.org/";
        final String URL =Utility.base_url;
        final String SOAP_ACTION ="http://tempuri.org/InsertOBC";
        final String METHOD_NAME = "InsertOBC";



        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("NameVisitingOfficial",  name);
        request.addProperty("PFvisitingofficial",pf_no);
        request.addProperty("Salutation", solutation);
        request.addProperty("NameProprietor",pro_name);
        request.addProperty("AadhaarNumber", addh_no);
        request.addProperty("NameFirm",firm_name);
        request.addProperty("DateofVisit", curr_date);
        request.addProperty("AccountNumber",acc_no);
        request.addProperty("Address",addd);
        request.addProperty("NatureofBusinessActivity", buss_act);
        request.addProperty("RunningSince",month_date);
        request.addProperty("ContactNo", con_no);
        request.addProperty("Remarks", remark);
        request.addProperty("KYCProprietor_ID", proprietor);
        request.addProperty("KYCFirm_ID", firm);
        request.addProperty("UserId",user_id);
        request.addProperty("Email", util.getEmail_id(mContext));
        request.addProperty("Images", encd_images);
        request.addProperty("Images1", encd_images2);

        request.addProperty("Longitude", longi);
        request.addProperty("Latitude", latti);
        request.addProperty("PLocation", pro_location);
        request.addProperty("SolidID",sol_id);
        request.addProperty("UniqueNo", uniq_visit_no);
        request.addProperty("ChkPDF", "2");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,60000);

        try
        {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            KvmSerializable ks = (KvmSerializable)envelope.bodyIn;
            for(int j=0;j<ks.getPropertyCount();j++)
            {
                ks.getProperty(j);
            }
            receivedString = ks.toString();

        }
        catch (Exception e)
        {
            catchhandl++;
            e.printStackTrace();
        }

        try
        {
            String Jsonstring=receivedString;

            if(!Jsonstring.contains("ID")) {
                subMitdataLimit = "";
                String news = Jsonstring.substring(Jsonstring.indexOf("["));
                String n1 = news;
                JSONArray jArray = new JSONArray(n1);

                for (int k = 0; k < (jArray.length()); k++) {
                    JSONObject json_obj = jArray.getJSONObject(k);
                    responsestatus = json_obj.getString("Status");

                }
            }else{
                subMitdataLimit = "99";
            }
        }
        catch(Exception e)
        {
            catchhandl++;
            e.printStackTrace();
        }

    }
}
