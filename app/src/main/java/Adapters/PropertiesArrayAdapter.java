package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import localestates.localestates.R;

/**
 * Created by Ado on 11/30/2015.
 */
public class PropertiesArrayAdapter extends ArrayAdapter<JSONObject> {

    private int textView;
    private ArrayList<JSONObject> data;
    Context context;

    public PropertiesArrayAdapter(Context context, int resource,
                           ArrayList<JSONObject> data) {
        super(context, resource, data);

        this.context=context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        JSONObject property = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.property_single_item, parent, false);
        }

//        Log.e("HEREHERE", property.toString());
        TextView propertyTitle = (TextView) convertView.findViewById(R.id.propertyTitle);
        TextView propertyPrice = (TextView) convertView.findViewById(R.id.propertyPrice);
//        TextView propertyPrice = (TextView) convertView.findViewById(R.id.propertyPrice);
        ImageView bigImage = (ImageView) convertView.findViewById(R.id.bigImage);

        try {
            JSONArray pictureArray=property.getJSONArray("pictures");
            if ( pictureArray!=null  ) {
                Picasso.with(context).load(pictureArray.get(0).toString()).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(bigImage);
//                new ImageLoad(pictureArray.get(0).toString(), bigImage).execute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            String type_home=property.getString("type_home");
            if ( type_home!=null ) {
                propertyTitle.setText("Продава "+type_home);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String town=property.getString("town");
            if ( town!=null ) {
                propertyTitle.setText(propertyTitle.getText()+" "+town);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String raion=property.getString("raion");
            if ( raion!=null ) {
                propertyTitle.setText(propertyTitle.getText()+" "+raion);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String street=property.getString("street");
            if ( street!=null ) {
                propertyTitle.setText(propertyTitle.getText()+" "+street);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String price=property.getString("price");
            if ( price!=null ) {
                propertyPrice.setText(price);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String currency=property.getString("currency");
            if ( currency!=null ) {
                propertyPrice.setText(propertyPrice.getText()+" "+currency);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;

    }
}

