package planet_obcapp.com.obc_kyvapp.App_utils;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import planet_obcapp.com.obc_kyvapp.R;

/**
 * Created by planet on 3/23/17.
 */

public class util {


    Context context;

    public static void onKeyBoardDown(Context con) {
        try {
            InputMethodManager inputManager = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager.isAcceptingText()) {
                if (inputManager.isActive())
                    inputManager.hideSoftInputFromWindow(((Activity) con).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void showAlert(Context context, String msg) {
        new android.support.v7.app.AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    public static void setUserId(Context context, String NU_LOGINID) {
        try {


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("NU_LOGINID", MyUtils.encrypt(NU_LOGINID,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static String getUserId(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("NU_LOGINID", "");
            return MyUtils.decrypt(val,context);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setusername(Context context, String UserName) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("UserName", MyUtils.encrypt(UserName,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getusername(Context context) {

     String val="";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val=preferences.getString("UserName", "");
            return MyUtils.decrypt(val,context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setEmail_id(Context context, String VR_EMAIL) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("VR_EMAIL", MyUtils.encrypt(VR_EMAIL,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getEmail_id(Context context) {

        String val = "";

        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("VR_EMAIL", "");

            return MyUtils.decrypt(val,context);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setReturnUser_name(Context context, String VR_EMAIL) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("VR_USERNAME", MyUtils.encrypt(VR_EMAIL,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setP_id(String key, Context context, String P_Id) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,MyUtils.encrypt( P_Id,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getP_id(Context context) {
        String val = "";

        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            val=preferences.getString("P_Id", "");
            return MyUtils.decrypt(val,context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setKF_ID(String key, Context context, String Kf_ID) {


        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, MyUtils.encrypt(Kf_ID,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getKF_ID(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("Kf_ID", "");
            return MyUtils.decrypt(val,context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void setSOLID_value(Context context, String Kf_ID) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("sol", MyUtils.encrypt(Kf_ID,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSOLID_value(Context context) {
        String val = "";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val = preferences.getString("sol", "");
            return MyUtils.decrypt(val,context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void setPLocation(Context context, String Plocation) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Plocation", Plocation);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPLocation(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("Plocation", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setImages(Context context, String images) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Images", images);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getImages(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("Images", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setProprietor(Context context, String propriter) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("propriter", propriter);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProprietor(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("propriter", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setFirm(Context context, String Firm) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Firm", Firm);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFirm(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("Firm", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setOtherLocation(Context context, String otherlocation) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Otherlocation", otherlocation);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getOtherLocation(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("Otherlocation", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setMEdiacount(Context context, String count) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("count", count);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMEdiacount(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("count", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setFirstImage(Context context, String firstimage) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstImage", firstimage);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFirstImage(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("FirstImage", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setSECImage(Context context, String secimage) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("SECImage", secimage);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSECImage(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("SECImage", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setFirstIma(Context context, String firstimage) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstIma", firstimage);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFirstIma(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("FirstIma", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setSECIma(Context context, String secimage) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("SECIma", secimage);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSECIma(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("SECIma", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void setpfno(Context context, String PF_NO) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("PF_NO", PF_NO);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getpfno(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("PF_NO", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setPassword_CRG(Context context, String Password) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Pass", Password);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPassword_CRG(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString("Pass", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setusername_CRG(Context context, String UserName) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("User", MyUtils.encrypt(UserName,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getusername_CRG(Context context) {

      String  val="";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val= preferences.getString("User", "");
            return MyUtils.decrypt(val,context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void setunid_CRG(Context context, String UserName) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("unid", MyUtils.encrypt( UserName,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getunid_CRG(Context context) {

        String val="";
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val=preferences.getString("unid", "");

            return MyUtils.decrypt(val,context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }

    public static void setname_CRG(Context context, String UserName) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Name",MyUtils.encrypt( UserName,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getname_CRG(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return MyUtils.decrypt(preferences.getString("Name", ""),context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setCMO_CRG(Context context, String UserName) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("CMO",MyUtils.encrypt( UserName,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCMO_CRG(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return MyUtils.decrypt(preferences.getString("CMO", ""),context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setClusterNmae_CRG(Context context, String UserName) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Cluster", MyUtils.encrypt(UserName,context));
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCluster_CRG(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return MyUtils.decrypt(preferences.getString("Cluster", ""),context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}










































