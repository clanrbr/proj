package utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import localestates.localestates.R;
import localestates.localestates.SearchActivity;

/**
 * Created by Ado on 11/30/2015.
 */
public class HelpFunctions {

    public static boolean checkGroupOfPropertyType(String type, ArrayList<String> selectedFields) {
        int match=0;
        if ( selectedFields.size()>0 ) {
            switch (selectedFields.get(0)) {
                case "1-СТАЕН":
                    match=1;
                    break;
                case "2-СТАЕН":
                    match=1;
                    break;
                case "3-СТАЕН":
                    match=1;
                    break;
                case "4-СТАЕН":
                    match=1;
                    break;
                case "МНОГОСТАЕН":
                    match=1;
                    break;
                case "МЕЗОНЕТ":
                    match=1;
                    break;
                case "АТЕЛИЕ, ТАВАН":
                    match=1;
                    break;

                case "ОФИС":
                    match=2;
                    break;
                case "МАГАЗИН":
                    match=2;
                    break;
                case "ЗАВЕДЕНИЕ":
                    match=2;
                    break;
                case "СКЛАД":
                    match=2;
                    break;
                case "ПРОМ. ПОМЕЩЕНИЕ":
                    match=2;
                    break;
                case "ХОТЕЛ":
                    match=2;
                    break;

                case "ЕТАЖ ОТ КЪЩА":
                    match=3;
                    break;
                case "КЪЩА":
                    match=3;
                    break;
                case "ВИЛА":
                    match=3;
                    break;

                case "МЯСТО":
                    match=4;
                    break;

                case "ГАРАЖ":
                    match=5;
                    break;

                case "ЗЕМЕДЕЛСКА ЗЕМЯ":
                    match=6;
                    break;
            }

            if ( (match==1) && ( (type=="1-СТАЕН") || (type=="2-СТАЕН") || (type=="3-СТАЕН") || (type=="4-СТАЕН") || (type=="МНОГОСТАЕН") || (type=="МЕЗОНЕТ") || (type=="АТЕЛИЕ, ТАВАН") ) ) {
                return true;
            } else if ( (match==2 ) && ( (type=="ХОТЕЛ") || (type=="ПРОМ. ПОМЕЩЕНИЕ") || (type=="СКЛАД") || (type=="ЗАВЕДЕНИЕ") || (type=="МАГАЗИН") || (type=="ОФИС") ) ) {
                return true;
            } else if ( (match==3) && ( (type=="ВИЛА") || (type=="КЪЩА") || (type=="ЕТАЖ ОТ КЪЩА") ) ) {
                return true;
            } else if ( (match==4) && (type=="МЯСТО") ) {
                return true;
            } else if ( (match==5) && (type=="ГАРАЖ")  ) {
                return true;
            } else if ( (match==6) && (type=="ЗЕМЕДЕЛСКА ЗЕМЯ") ) {
                return true;
            }

            return false;
        }

        return true;
    }

    public static int returnGroupNumberOfProperty(String typeProperty, ArrayList<String> selectedFields,int typeAdvert) {
        int match=0;
        String type="";
        if (typeProperty!=null) {
            type=typeProperty;
        } else {
            if (selectedFields.size()>0) {
                type=selectedFields.get(0);
            }
        }

        if (  (type=="1-СТАЕН") || (type=="2-СТАЕН") || (type=="3-СТАЕН") || (type=="4-СТАЕН") || (type=="МНОГОСТАЕН") || (type=="МЕЗОНЕТ") || (type=="АТЕЛИЕ, ТАВАН") )  {
            match=1;
        } else if (  (type=="ХОТЕЛ") || (type=="ПРОМ. ПОМЕЩЕНИЕ") || (type=="СКЛАД") || (type=="ЗАВЕДЕНИЕ") || (type=="МАГАЗИН") || (type=="ОФИС") )  {
            match=2;
        } else if ( (type=="ВИЛА") || (type=="КЪЩА") || (type=="ЕТАЖ ОТ КЪЩА") ) {
            match=3;
        } else if ( type=="МЯСТО") {
            match=4;
        } else if (type=="ГАРАЖ") {
            match=5;
        } else if ( type=="ЗЕМЕДЕЛСКА ЗЕМЯ") {
            match=6;
        }

        return match;
    }

    public static int returnExtriGroup(int typeAdvert,int groupNumber) {
        if ( typeAdvert==1 ) {
            if ( (groupNumber==1) || (groupNumber==2) || (groupNumber==3) || (groupNumber==5) ) {
                return 1;
            } else if ( groupNumber==4 ) {
                return 3;
            } else {
                return 5;
            }
        } else {
            if ( (groupNumber==1) || (groupNumber==2) || (groupNumber==3) || (groupNumber==5) ) {
                return 2;
            } else if ( groupNumber==4 ) {
                return 4;
            } else {
                return 6;
            }
        }
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, int value) {
        HashMap<String, String> result;
        if ( value>0 ) {
            result = new HashMap<String,String>();
            result.put(searchName,String.valueOf(value));
        } else {
            result=null;
        }

        return result;
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, TextView field) {
        HashMap<String, String> result;
        if ( field.getText().toString()!="" ) {
            result = new HashMap<String,String>();
            result.put(searchName,field.getText().toString());
        } else {
            result=null;
        }

        return result;
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, EditText field) {
        HashMap<String, String> result;
        if ( field.getText().toString()!="" ) {
            result = new HashMap<String,String>();
            result.put(searchName,field.getText().toString());
        } else {
            result=null;
        }

        return result;
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, Spinner field) {
        HashMap<String, String> result;
        int position = field.getSelectedItemPosition();
        result = new HashMap<String,String>();
        result.put(searchName,String.valueOf(position));
        return result;
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, Spinner field,ArrayList<CharSequence> values) {
        HashMap<String, String> result;
        int position = field.getSelectedItemPosition();
        if ( values.get(position).toString()!="" ) {
            result = new HashMap<String,String>();
            result.put(searchName,values.get(position).toString());
        } else {
            result=null;
        }

        return result;
    }

    public static String convertToUrl(ArrayList<HashMap<String,String>> values) {
        String buildUrl="";
        for (int y=0;y<values.size();y++) {
            HashMap<String,String> hashMapValues=values.get(y);
            if (hashMapValues!=null) {
                for (Map.Entry me : hashMapValues.entrySet()) {
                    if ( (me.getValue()!=null) && (me.getValue()!="") ) {
                        String encodedValue=me.getValue().toString();
                        if (me.getValue().toString().contains(",")) {
                            String[] parts = encodedValue.split(",");
                            for ( int i=0;i<parts.length;i++) {
                                try {
                                    buildUrl+=me.getKey().toString()+"="+ URLEncoder.encode(parts[i], "UTF-8")+"&";
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                encodedValue=URLEncoder.encode(encodedValue, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            buildUrl+=me.getKey().toString()+"="+encodedValue+"&";
                        }
                    }
                }
            }

        }
        if ( buildUrl!="" ) {
            buildUrl="?"+buildUrl;
            buildUrl = buildUrl.substring(0, buildUrl.length()-1);
        }

        return buildUrl;
    }

//    public static HashMap<String, String> generateHashForSearch(String searchName, TextView field, ArrayList values) {
//        HashMap<String, String> result;
//        if ( field.getText().toString()!="Всички" ) {
//            result = new HashMap<String,String>();
//            if ( field.getText().toString().contains(",") ) {
//                result.put(searchName,field.getText().toString());
//            } else {
//                result.put(searchName,field.getText().toString());
//            }
//        } else {
//            result=null;
//        }
//
//        return result;
//    }


//    public static String getString(InputStream stream) throws IOException {
//        if (stream == null) {
//            return "";
//        }
//        BufferedReader reader =new BufferedReader(new InputStreamReader(stream));
//        StringBuilder content = new StringBuilder();
//        String newLine;
//        do {
//            newLine = reader.readLine();
//            if (newLine != null) {
//                content.append(newLine).append('\n');
//            }
//        } while (newLine != null);
//
//        reader.close();
//        if (content.length() > 0) {
//            // strip last newline
//            content.setLength(content.length() - 1);
//        }
//
//        Log.e("HEREHERE", content.toString());
//
//        return content.toString();
//    }
}
