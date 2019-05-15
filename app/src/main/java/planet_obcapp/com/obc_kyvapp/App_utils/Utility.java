package planet_obcapp.com.obc_kyvapp.App_utils;

import android.media.ExifInterface;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Admin on 4/10/2017.
 */

public class Utility {

    public static String KEY_ID="ID";
    public static String KEY_MONTH_DATE= "MONTH_DATE";
    public static String KEY_CURRENT_DATE="CURRENT_DATE";
    public static String KEY_PF_NO="PF_NO";
    public static String KEY_SALUTATION="SALUTATION";

    public static String KEY_VISITER_NAME="VISITER_NAME";
    public static String KEY_PROPRIETOR_NAME="PROPRIETOR_NAME";
    public static String KEY_AADHAR_NO="AADHAR_NO";
    public static String KEY_FIRM_NAME="FIRM_NAME";
    public static String KEY_ACCOUNT_NO="ACCOUNT_NO";
    public static String KEY_ADDRESS="ADDRESS";
    public static String KEY_BUSI_ACTIVITY="BUS_ACTIVITY";
    public static String KEY_CONTECT_NO="CONTECT_NO";
    public static String KEY_REMARKS="REMARKS";
    public static final String GALLERY_DIRECTORY_NAME = "OBC";
    public static String KEY_LATTI="LATTI";
    public static String KEY_LONGI="LONGI";
    public static String KEY_ENCODE_IMAGES="ENCODE_IMAGES";
    public static String KEY_PROPREITOR_LOCATION="PROPREITOR_LOCATION";
    public static String KEY_PROPRIETOR="PROPRIETOR";
    public static String KEY_FIRM="FIRM";
    public static String KEY_SOL_ID="SOL_ID";
    public static String KEY_USER_ID="USER_ID";
    public static String KEY_EMAIL_ID="EMAIL_ID";
    public static String KEY_VISIT_NO="VISIT_NO";
    public static String KEY_USER_NMAE="USER_NMAE";
    public static String KEY_PASSWORD="PASSWORD";
    public static String KEY_IMAGE_SEC="IMAGE_SEC";
    public static String KEY_CHQ_UNIQE="CHQ_UNIQE";
    public static String KEY_CREATE_DATE="CREATE_DATE";
    public static String KEY_DATE_VISIT="KEY_DATE_VISIT";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";
//oriental CRG constants

    public static String KEY_UNIIDE="KEY_UNIIDE";
    public static String KEY_IDE="KEY_IDE";
    public static String KEY_DATE_TIME="DATE_TIME";
    public static String KEY_CUSTOMER_NAME="CUSTOMER_NAME";
    public static String KEY_CON_PERSON="CON_PERSON";
    public static String KEY_CON_DETAILS="CON_DETAILS";
    public static String KEY_LEAD_TYPE="LEAD_TYPE";
    public static String KEY_STATUS_LEAD="STATUS_LEAD";
    public static String KEY_DATE_REVISIT="DATE_REVISIT";
    public static String KEY_REMARK="REMARK";
    public static String KEY_DATE_CLOSE="DATE_CLOSE";
    public static String KEY_DATE_CONVERSION="DATE_CONVERSION";
    public static String KEY_SOLL_ID="SOLL_ID";
    public static String KEY_NO_OF_ACCOUNT="NO_OF_ACCOUNT";
    public static String KEY_ACCOUNT="ACCOUNT";
    public static String KEY_IMAGE_ONE="IMAGE_ONE";
    public static String KEY_IMAGE_TWO="IMAGE_TWO";
    public static String KEY_LATI="LATI";
    public static String KEY_LONGII="LONGII";
    public static String KEY_LOCATION="LOCATION";

    public static String KEY_LEADTYPETWO="LEADTYPETWO";
    public static String KEY_UNIQENO="UNIQENO";
    public static String KEY_RETIME="RETIME";


    public static String KEY_DATEOPENREVISIT="LEADTYPETWO";
    public static String KEY_REMARKOPEN="UNIQENO";
    public static String KEY_STATUSLEADOPEN="RETIME";
    public static String KEY_OPENPF="RETIME";
    public static String KEY_DATE="KEY_DATE";

    public static  String base_url="https://121.241.255.225:8080/OBCInsertApp.asmx";
    public static  String base_url_crg="https://121.241.255.225:8080/crgapp.asmx";
//public static  String base_url_crg= "http://obcindia.co.in/obcnew/site/crgapp.asmx";

  /*  public static final String HEADER_API = "https://";
    public static final String HEADER_WEBVIEW_URL = "https://";
    public static final String NAMESPACE = "http://tempuri.org/";

*/


    public static String KEY_PLANET="planet";
    public static String KEY_LAUGH="laugh";
    public static String KEY_REPORT="report";
    public static String KEY_TESTING="testing";
    public final static String seed = "ipNumber";
    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognise a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }

        return degree;
    }
    public static String getUniqueId(){
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
       return datetime;
    }




}
