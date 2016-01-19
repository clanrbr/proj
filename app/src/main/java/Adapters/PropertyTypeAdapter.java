package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fragments.CheckAndRadioBoxesFragment;
import localestates.localestates.AdvanceSearchActivity;
import localestates.localestates.R;
import utils.HelpFunctions;

/**
 * Created by Ado on 12/26/2015.
 */
public class PropertyTypeAdapter extends ArrayAdapter<String> {

    private int textView;
    private ArrayList<String> data;
    private TextView propertyTypeCheckbox;
    private TextView propertyTitle;
    private LinearLayout propertyTypeWholeLane;
    Context context;
    int position=0;
//    ArrayList<String> checkboxSelected;

    public PropertyTypeAdapter(Context context, int resource,
                                  ArrayList<String> data) {
        super(context, resource, data);
        this.context=context;
//        this.textView = textViewResourceId;
        this.data = data;
//        checkboxSelected=new ArrayList<String>();
    }

    public void updateResults() {
        // assign the new result list to your existing list it will work
        Log.e("HEREHERE","GOES HERE");
        notifyDataSetChanged();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String type = getItem(position);
//        Log.e("HEREHERE",String.valueOf(position));
//        Log.e("HEREHERE",type);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.propertytype_single_item, parent, false);
        }


        propertyTitle = (TextView) convertView.findViewById(R.id.type_of_property_single_line);
        propertyTitle.setText(type);

        propertyTypeCheckbox = (TextView) convertView.findViewById(R.id.fragment_property_type_checkbox);

        if ( AdvanceSearchActivity.currentboxSelectedValue.indexOf(type)>-1 ) {
            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_checked_full));
        } else {
            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_check_empty));
            convertView.setEnabled(HelpFunctions.checkGroupOfPropertyType(type,AdvanceSearchActivity.currentboxSelectedValue));
        }

        propertyTypeWholeLane = (LinearLayout) convertView.findViewById(R.id.whole_lane);
        propertyTypeWholeLane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View)v.getParent();
                if ( parent!=null ) {
                    TextView propertyTypeCheckbox = (TextView) parent.findViewById(R.id.fragment_property_type_checkbox);
                    TextView  propertyTitle = (TextView) parent.findViewById(R.id.type_of_property_single_line);

                    if ( position==0) {
                        AdvanceSearchActivity.currentboxSelectedValue=new ArrayList<String>();
                    }

                    if ( parent.isEnabled() ) {
                        if ( AdvanceSearchActivity.currentboxSelectedValue.indexOf(propertyTitle.getText())>-1 ) {
                            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_check_empty));
                            AdvanceSearchActivity.currentboxSelectedValue.remove(propertyTitle.getText());
                        } else {
                            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_checked_full));
                            AdvanceSearchActivity.currentboxSelectedValue.add(propertyTitle.getText().toString());
                        }
                    } else {
                        if ( propertyTypeCheckbox.getText()==context.getString(R.string.material_icon_checked_full) ) {
                            propertyTypeCheckbox.setText(context.getString(R.string.material_icon_check_empty));
                            AdvanceSearchActivity.currentboxSelectedValue.remove(propertyTitle.getText());
                        }
                    }

                    updateResults();
                }
            }
        });
//        TextView propertyPrice = (TextView) convertView.findViewById(R.id.propertyPrice);

        return convertView;

    }
}