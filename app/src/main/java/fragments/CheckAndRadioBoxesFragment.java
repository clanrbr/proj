package fragments;

/**
 * Created by macbook on 12/21/15.
 */
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import adapters.PropertyTypeAdapter;
import adapters.SimpleMultiChoiceAdapter;
import localEstatesHttpRequests.HTTPGetProperties;
import localestates.localestates.R;
import utils.HelpFunctions;
//import com.csform.android.uiapptemplate.util.ImageUtil;

public class CheckAndRadioBoxesFragment extends Fragment implements
        OnClickListener {

    public static final String ARG_SECTION_NUMBER = "section_number";

    private TextView saleCheckbox;
    private TextView rentCheckbox;
    private TextView saleCheckboxLabel;
    private TextView rentCheckboxLabel;
    private LinearLayout secondTownSpinnerLayout;
    private ListView listViewPropertyType;
    private int typeAdvert=1;

    private EditText priceFrom;
    private EditText priceTo;
    private EditText areaFrom;
    private EditText areaTo;

    private RelativeLayout moreSearchOptions;

    private ArrayList<String> stringArray;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner townSpinner;
    private Spinner secondTownSpinner;
    private TextView choosePropertyType;
    private ArrayList<String> extriArray;
    private ArrayList<String> buildTypeArray;

    // Group 1 additional fields
    private ArrayList<CharSequence> floorArray;
    private Spinner floorFromSpinner;
    private ArrayAdapter<CharSequence> floorFromAdapter;
    private Spinner floorToSpinner;
    private ArrayAdapter<CharSequence> floorToAdapter;
    private EditText yearFrom;
    private EditText yearTo;
    private Spinner tecSpinner;
    private ArrayAdapter<CharSequence> tecAdapter;
    private ArrayList<CharSequence> tecArray;
    private Spinner phoneSpinner;
    private ArrayAdapter<CharSequence> phoneAdapter;
    private ArrayList<CharSequence> phoneArray;
    private TextView extriProperty;
    private TextView buildTypeProperty;

    // Electricity
    private Spinner electricitySpinner;
    private ArrayAdapter<CharSequence> electricityAdapter;
    private ArrayList<CharSequence> electricityArray;

    // Water
    private Spinner waterSpinner;
    private ArrayAdapter<CharSequence> waterAdapter;
    private ArrayList<CharSequence> waterArray;

    // Regulation
    private Spinner regulationSpinner;
    private ArrayAdapter<CharSequence> regulationAdapter;
    private ArrayList<CharSequence> regulationArray;

    // Land Permanent Usage
    private ArrayList<String> landPermanentUsageArray;
    public ArrayList<String> landPermanentUsageSelectedValue;
    private TextView landPermanentUsageProperty;

    // Land Category
    private ArrayList<String> landCategoryArray;
    public ArrayList<String> landCategorySelectedValue;
    private TextView landCategoryProperty;

    private Spinner sortResult;
    private ArrayAdapter<CharSequence> sortAdapter;
    public static ArrayList<String> checkboxSelectedValue;
    public static ArrayList<String> currentboxSelectedValue;
    public ArrayList<String> extriSelectedValue;
    public ArrayList<String> buildTypeSelectedValue;
    private int groupNumber;
    private ArrayList<String> propertyTypes;


    private TextView searchAdvancedButton;
    private TextView clearButton;

    public static CheckAndRadioBoxesFragment newInstance() {
        return new CheckAndRadioBoxesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        checkboxSelectedValue=new ArrayList<String>();
        currentboxSelectedValue=new ArrayList<String>();

        final View rootView = inflater.inflate(R.layout.fragment_check_and_radio_boxes, null);
        townSpinner = (Spinner) rootView.findViewById(R.id.town_spinner);
        secondTownSpinnerLayout = (LinearLayout) rootView.findViewById(R.id.secondTownSpinner);
        secondTownSpinner = (Spinner) rootView.findViewById(R.id.second_town_spinner);
        sortResult = (Spinner) rootView.findViewById(R.id.sortResult);
        moreSearchOptions = (RelativeLayout) rootView.findViewById(R.id.moreSearchOptions);


        searchAdvancedButton = (TextView) rootView.findViewById(R.id.searchAdvancedButton);
        clearButton = (TextView) rootView.findViewById(R.id.clearButton);

//        listViewPropertyType = (ListView) rootView.findViewById(R.id.listViewPropertyType);

        // First Adapter for Towns
        townSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            HTTPGetProperties getProperty;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] mTestArray = getResources().getStringArray(R.array.town);
                String urlBuild="http://api.imot.bg/mobile_api/dictionary/raion?town="+ Uri.encode(mTestArray[position]);

                if ( position==0 ) {
                    secondTownSpinnerLayout.setVisibility(View.GONE);
                } else {
                     getProperty = new HTTPGetProperties() {
                        @Override
                        protected void onPostExecute(String result) {
                            if (result != null) {
                                try {
                                    JSONArray  jsonArray = new JSONArray(result);
                                    stringArray = new ArrayList<String>();
                                    stringArray.add("Всички");
                                    for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                        try {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            if ( jsonObject!=null ) {
                                                stringArray.add(jsonObject.getString("value"));
                                            }
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    secondTownSpinnerLayout.setVisibility(View.VISIBLE);
                                    spinnerAdapter.clear();
                                    secondTownSpinner.setSelection(0);
                                    spinnerAdapter.addAll(stringArray);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("HEREHERE", "EMPTY");
                            }
                        }
                     };
                    getProperty.execute(urlBuild);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("HEREHERE", "Nothing Selected");
            }

        });
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.town, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        townSpinner.setAdapter(adapter);

        // Second Adapter for Areas
        secondTownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Log.e("HEREHERE", "Nothing Selected");
            }
        });
        spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondTownSpinner.setAdapter(spinnerAdapter);

//        String[] strings = { "ВСИЧКИ","СТАЯ", "1-СТАЕН", "2-СТАЕН", "3-СТАЕН","4-СТАЕН","МНОГОСТАЕН","МЕЗОНЕТ","АТЕЛИЕ, ТАВАН","ОФИС",
//                             "МАГАЗИН","ЗАВЕДЕНИЕ","СКЛАД","ПРОМ. ПОМЕЩЕНИЕ","ХОТЕЛ","ЕТАЖ ОТ КЪЩА","КЪЩА","ВИЛА","МЯСТО","ГАРАЖ","ЗЕМЕДЕЛСКА ЗЕМЯ" };
//        ArrayList<String> propertyTypes = new ArrayList<String>();
//        propertyTypes.add("ВСИЧКИ");
//        propertyTypes.add("1-СТАЕН");
//        propertyTypes.add("2-СТАЕН");
//        propertyTypes.add("3-СТАЕН");
//        propertyTypes.add("4-СТАЕН");
//        propertyTypes.add("МНОГОСТАЕН");
//        propertyTypes.add("МЕЗОНЕТ");
//        propertyTypes.add("АТЕЛИЕ, ТАВАН");
//        propertyTypes.add("ОФИС");
//        propertyTypes.add("МАГАЗИН");
//        propertyTypes.add("ЗАВЕДЕНИЕ");
//        propertyTypes.add("СКЛАД");
//        propertyTypes.add("ПРОМ. ПОМЕЩЕНИЕ");
//        propertyTypes.add("ХОТЕЛ");
//        propertyTypes.add("ЕТАЖ ОТ КЪЩА");
//        propertyTypes.add("КЪЩА");
//        propertyTypes.add("ВИЛА");
//        propertyTypes.add("МЯСТО");
//        propertyTypes.add("ГАРАЖ");
//        propertyTypes.add("ЗЕМЕДЕЛСКА ЗЕМЯ");

        choosePropertyType= (TextView) rootView.findViewById(R.id.choosePropertyType);
        choosePropertyType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentboxSelectedValue=checkboxSelectedValue;

                propertyTypes = new ArrayList<String>();
                propertyTypes.add("ВСИЧКИ");
                propertyTypes.add("1-СТАЕН");
                propertyTypes.add("2-СТАЕН");
                propertyTypes.add("3-СТАЕН");
                propertyTypes.add("4-СТАЕН");
                propertyTypes.add("МНОГОСТАЕН");
                propertyTypes.add("МЕЗОНЕТ");
                propertyTypes.add("АТЕЛИЕ, ТАВАН");
                propertyTypes.add("ОФИС");
                propertyTypes.add("МАГАЗИН");
                propertyTypes.add("ЗАВЕДЕНИЕ");
                propertyTypes.add("СКЛАД");
                propertyTypes.add("ПРОМ. ПОМЕЩЕНИЕ");
                propertyTypes.add("ХОТЕЛ");
                propertyTypes.add("ЕТАЖ ОТ КЪЩА");
                propertyTypes.add("КЪЩА");
                propertyTypes.add("ВИЛА");
                propertyTypes.add("МЯСТО");
                propertyTypes.add("ГАРАЖ");
                propertyTypes.add("ЗЕМЕДЕЛСКА ЗЕМЯ");


                final Dialog dialog = new Dialog(getActivity());
                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                dialog.setTitle("Избери тип на имота:");
                dialog.setCancelable(false);

                View vi = li.inflate(R.layout.listview_popup, null, false);
                PropertyTypeAdapter adapterPropertiesType = new PropertyTypeAdapter(getActivity(), R.layout.propertytype_single_item, propertyTypes);
                ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                listViewPopup.setAdapter(adapterPropertiesType);
                dialog.setContentView(vi);
                dialog.show();

                Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentboxSelectedValue=new ArrayList<String>();
                        dialog.hide();
                    }
                });

                Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        groupNumber=0;
                        checkboxSelectedValue=currentboxSelectedValue;
                        String valueCheckBox="";
                        for (int i=0;i<checkboxSelectedValue.size();i++) {
                            valueCheckBox+=checkboxSelectedValue.get(i)+",";
                            if ( groupNumber==0 ) {
                                groupNumber= HelpFunctions.returnGroupNumberOfProperty(checkboxSelectedValue.get(i),checkboxSelectedValue,0);
                            }
                        }

                        if (valueCheckBox.length()>0) {
                            valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                        } else {
                            valueCheckBox="ВСИЧКИ";
                        }

                        choosePropertyType.setText(valueCheckBox);

                        if (groupNumber>0) {

                            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View optionView;
                            if ( groupNumber==1  ) {
                                moreSearchOptions.removeAllViews();
                                optionView = layoutInflater.inflate(R.layout.search_options_group_1, container, false);
                            } else if (groupNumber==2) {
                                moreSearchOptions.removeAllViews();
                                optionView = layoutInflater.inflate(R.layout.search_options_group_2, container, false);
                            } else if (groupNumber==3) {
                                moreSearchOptions.removeAllViews();
                                optionView = layoutInflater.inflate(R.layout.search_options_group_3, container, false);
                            } else if (groupNumber==4) {
                                moreSearchOptions.removeAllViews();
                                optionView = layoutInflater.inflate(R.layout.search_options_group_4, container, false);
                            } else if (groupNumber==5) {
                                moreSearchOptions.removeAllViews();
                                optionView = layoutInflater.inflate(R.layout.search_options_group_5, container, false);
                            } else if (groupNumber==6) {
                                moreSearchOptions.removeAllViews();
                                optionView = layoutInflater.inflate(R.layout.search_options_group_6, container, false);
                            } else {
                                moreSearchOptions.removeAllViews();
                                optionView = layoutInflater.inflate(R.layout.search_options_group_1, container, false);
                            }
                            moreSearchOptions.addView(optionView);

                            extriProperty = (TextView) optionView.findViewById(R.id.extriProperty);
                            // get extras
                            HTTPGetProperties getExtras = new HTTPGetProperties() {
                                @Override
                                protected void onPostExecute(String result) {
                                    if (result != null) {
                                        Log.e("HEREHERE", "THIS RESULT");
                                        Log.e("HEREHERE", result);
                                        try {
                                            JSONArray  jsonArray = new JSONArray(result);
                                            Log.e("HEREHERE",jsonArray.toString());
                                            extriArray = new ArrayList<String>();
                                            for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                extriArray.add(jsonArray.getString(i));
                                            }

                                            extriProperty.setOnClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    final Dialog dialogSimple = new Dialog(getActivity());
                                                    LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                    dialogSimple.setTitle("Избери особености:");
                                                    dialogSimple.setCancelable(false);

                                                    View vi = li.inflate(R.layout.listview_popup, null, false);
                                                    String textExtriFieldValue = (String) extriProperty.getText();
                                                    if ( (textExtriFieldValue.length()>0) && (textExtriFieldValue.contains(",")) ) {
                                                        extriSelectedValue= new ArrayList<String>();
                                                        String[] parts = textExtriFieldValue.split(",");
                                                        if ( parts.length>0 ) {
                                                            for ( int i=0;i<parts.length;i++) {
                                                                extriSelectedValue.add(parts[i]);
                                                            }
                                                        } else {
                                                            extriSelectedValue.add(textExtriFieldValue);
                                                        }
                                                    } else if ( (textExtriFieldValue.length()>0) && ( !textExtriFieldValue.equals("Всички") ) )  {
                                                        extriSelectedValue= new ArrayList<String>();
                                                        extriSelectedValue.add(textExtriFieldValue);
                                                    } else {
                                                        extriSelectedValue= new ArrayList<String>();
                                                    }

                                                    final SimpleMultiChoiceAdapter adapterExtriProperties = new SimpleMultiChoiceAdapter(getActivity(), R.layout.propertytype_single_item, extriArray,extriSelectedValue);
                                                    ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                                                    listViewPopup.setAdapter(adapterExtriProperties);
                                                    dialogSimple.setContentView(vi);
                                                    dialogSimple.show();

                                                    Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                                                    saveButton.setOnClickListener(new View.OnClickListener() {

                                                        @Override
                                                        public void onClick(View v) {
                                                            extriSelectedValue=adapterExtriProperties.returnSelectedFields();
                                                            if (extriSelectedValue!=null) {
                                                                Log.e("HEREHERE","Click Event "+extriSelectedValue.toString());
                                                                String valueCheckBox="";
                                                                for (int i=0;i<extriSelectedValue.size();i++) {
                                                                    valueCheckBox+=extriSelectedValue.get(i)+",";
                                                                }

                                                                if (valueCheckBox.length()>0) {
                                                                    valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                                                                } else {
                                                                    valueCheckBox="Всички";
                                                                }
                                                                extriProperty.setText(valueCheckBox);
                                                            }
                                                            dialogSimple.hide();
                                                        }
                                                    });

                                                    Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                                                    closeButton.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            adapterExtriProperties.clearSelectedFields();
                                                            dialogSimple.hide();
                                                        }
                                                    });
                                                }
                                            });

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Log.e("HEREHERE", "EMPTY");
                                    }
                                }
                            };

                            int extriGroup=HelpFunctions.returnExtriGroup(typeAdvert,groupNumber);
                            String urlBuildExtri="http://api.imot.bg/mobile_api/dictionary/extri?type_extri="+String.valueOf(extriGroup);
                            getExtras.execute(urlBuildExtri);

                            if ( groupNumber==1 ) {
                                // get floor
                                floorFromSpinner = (Spinner) optionView.findViewById(R.id.floor_from);
                                floorToSpinner = (Spinner) optionView.findViewById(R.id.floor_to);
                                HTTPGetProperties getFloor = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                floorArray = new ArrayList<CharSequence>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    try {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        if ( jsonObject!=null ) {
                                                            floorArray.add(jsonObject.getString("value"));
                                                        }
                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            floorFromAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, floorArray);
                                            floorFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            floorFromSpinner.setAdapter(floorFromAdapter);

                                            floorToAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, floorArray);
                                            floorToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            floorToSpinner.setAdapter(floorToAdapter);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };

                                String urlBuildFloor="http://api.imot.bg/mobile_api/dictionary/floor";
                                getFloor.execute(urlBuildFloor);

                                buildTypeProperty = (TextView) optionView.findViewById(R.id.buildTypeProperty);
                                // get build type
                                HTTPGetProperties getBuildType = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                Log.e("HEREHERE",jsonArray.toString());
                                                buildTypeArray = new ArrayList<String>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    buildTypeArray.add(jsonArray.getString(i));
                                                }

                                                buildTypeProperty.setOnClickListener(new OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        final Dialog dialogSimple = new Dialog(getActivity());
                                                        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                        dialogSimple.setTitle("Избери особености:");
                                                        dialogSimple.setCancelable(false);

                                                        View vi = li.inflate(R.layout.listview_popup, null, false);
                                                        String textbuildTypeFieldValue = (String) buildTypeProperty.getText();
                                                        if ( (textbuildTypeFieldValue.length()>0) && (textbuildTypeFieldValue.contains(",")) ) {
                                                            buildTypeSelectedValue= new ArrayList<String>();
                                                            String[] parts = textbuildTypeFieldValue.split(",");
                                                            if ( parts.length>0 ) {
                                                                for ( int i=0;i<parts.length;i++) {
                                                                    buildTypeSelectedValue.add(parts[i]);
                                                                }
                                                            } else {
                                                                buildTypeSelectedValue.add(textbuildTypeFieldValue);
                                                            }
                                                        } else if ( (textbuildTypeFieldValue.length()>0) && ( !textbuildTypeFieldValue.equals("Всички") ) )  {
                                                            buildTypeSelectedValue= new ArrayList<String>();
                                                            buildTypeSelectedValue.add(textbuildTypeFieldValue);
                                                        } else {
                                                            buildTypeSelectedValue= new ArrayList<String>();
                                                        }

                                                        final SimpleMultiChoiceAdapter adapterbuildTypeProperties = new SimpleMultiChoiceAdapter(getActivity(), R.layout.propertytype_single_item, buildTypeArray,buildTypeSelectedValue);
                                                        ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                                                        listViewPopup.setAdapter(adapterbuildTypeProperties);
                                                        dialogSimple.setContentView(vi);
                                                        dialogSimple.show();

                                                        Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                                                        saveButton.setOnClickListener(new View.OnClickListener() {

                                                            @Override
                                                            public void onClick(View v) {
                                                                buildTypeSelectedValue=adapterbuildTypeProperties.returnSelectedFields();
                                                                if (buildTypeSelectedValue!=null) {
                                                                    Log.e("HEREHERE","Click Event "+buildTypeSelectedValue.toString());
                                                                    String valueCheckBox="";
                                                                    for (int i=0;i<buildTypeSelectedValue.size();i++) {
                                                                        valueCheckBox+=buildTypeSelectedValue.get(i)+",";
                                                                    }

                                                                    if (valueCheckBox.length()>0) {
                                                                        valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                                                                    } else {
                                                                        valueCheckBox="Всички";
                                                                    }
                                                                    buildTypeProperty.setText(valueCheckBox);
                                                                }
                                                                dialogSimple.hide();
                                                            }
                                                        });

                                                        Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                adapterbuildTypeProperties.clearSelectedFields();
                                                                dialogSimple.hide();
                                                            }
                                                        });
                                                    }
                                                });

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };
                                String urlBuildType="http://api.imot.bg/mobile_api/dictionary/type_build";
                                getBuildType.execute(urlBuildType);

                                // get year of build
                                yearFrom = (EditText) optionView.findViewById(R.id.yearFrom);
                                yearTo = (EditText) optionView.findViewById(R.id.yearTo);

                                // get tec
                                tecSpinner = (Spinner) optionView.findViewById(R.id.tec);
                                HTTPGetProperties getTec = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                tecArray = new ArrayList<CharSequence>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    try {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        if ( jsonObject!=null ) {
                                                            tecArray.add(jsonObject.getString("value"));
                                                        }
                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                tecAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, tecArray);
                                                tecAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                tecSpinner.setAdapter(tecAdapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };

                                String urlBuildTec="http://api.imot.bg/mobile_api/dictionary/tec";
                                getTec.execute(urlBuildTec);

                                // get phone
                                phoneSpinner = (Spinner) optionView.findViewById(R.id.phone);
                                HTTPGetProperties getTel = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                phoneArray = new ArrayList<CharSequence>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    try {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        if ( jsonObject!=null ) {
                                                            phoneArray.add(jsonObject.getString("value"));
                                                        }
                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                phoneAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, phoneArray);
                                                phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                phoneSpinner.setAdapter(phoneAdapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };

                                String urlBuildPhone="http://api.imot.bg/mobile_api/dictionary/phone";
                                getTel.execute(urlBuildPhone);
                            } else if (groupNumber==2) {

                                buildTypeProperty = (TextView) optionView.findViewById(R.id.buildTypeProperty);
                                // get build type
                                HTTPGetProperties getBuildType = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                Log.e("HEREHERE",jsonArray.toString());
                                                buildTypeArray = new ArrayList<String>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    buildTypeArray.add(jsonArray.getString(i));
                                                }

                                                buildTypeProperty.setOnClickListener(new OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        final Dialog dialogSimple = new Dialog(getActivity());
                                                        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                        dialogSimple.setTitle("Избери особености:");
                                                        dialogSimple.setCancelable(false);

                                                        View vi = li.inflate(R.layout.listview_popup, null, false);
                                                        String textbuildTypeFieldValue = (String) buildTypeProperty.getText();
                                                        if ( (textbuildTypeFieldValue.length()>0) && (textbuildTypeFieldValue.contains(",")) ) {
                                                            buildTypeSelectedValue= new ArrayList<String>();
                                                            String[] parts = textbuildTypeFieldValue.split(",");
                                                            if ( parts.length>0 ) {
                                                                for ( int i=0;i<parts.length;i++) {
                                                                    buildTypeSelectedValue.add(parts[i]);
                                                                }
                                                            } else {
                                                                buildTypeSelectedValue.add(textbuildTypeFieldValue);
                                                            }
                                                        } else if ( (textbuildTypeFieldValue.length()>0) && ( !textbuildTypeFieldValue.equals("Всички") ) )  {
                                                            buildTypeSelectedValue= new ArrayList<String>();
                                                            buildTypeSelectedValue.add(textbuildTypeFieldValue);
                                                        } else {
                                                            buildTypeSelectedValue= new ArrayList<String>();
                                                        }

                                                        final SimpleMultiChoiceAdapter adapterbuildTypeProperties = new SimpleMultiChoiceAdapter(getActivity(), R.layout.propertytype_single_item, buildTypeArray,buildTypeSelectedValue);
                                                        ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                                                        listViewPopup.setAdapter(adapterbuildTypeProperties);
                                                        dialogSimple.setContentView(vi);
                                                        dialogSimple.show();

                                                        Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                                                        saveButton.setOnClickListener(new View.OnClickListener() {

                                                            @Override
                                                            public void onClick(View v) {
                                                                buildTypeSelectedValue=adapterbuildTypeProperties.returnSelectedFields();
                                                                if (buildTypeSelectedValue!=null) {
                                                                    Log.e("HEREHERE","Click Event "+buildTypeSelectedValue.toString());
                                                                    String valueCheckBox="";
                                                                    for (int i=0;i<buildTypeSelectedValue.size();i++) {
                                                                        valueCheckBox+=buildTypeSelectedValue.get(i)+",";
                                                                    }

                                                                    if (valueCheckBox.length()>0) {
                                                                        valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                                                                    } else {
                                                                        valueCheckBox="Всички";
                                                                    }
                                                                    buildTypeProperty.setText(valueCheckBox);
                                                                }
                                                                dialogSimple.hide();
                                                            }
                                                        });

                                                        Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                adapterbuildTypeProperties.clearSelectedFields();
                                                                dialogSimple.hide();
                                                            }
                                                        });
                                                    }
                                                });

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };
                                String urlBuildType="http://api.imot.bg/mobile_api/dictionary/type_build";
                                getBuildType.execute(urlBuildType);

                                // get year of build
                                yearFrom = (EditText) optionView.findViewById(R.id.yearFrom);
                                yearTo = (EditText) optionView.findViewById(R.id.yearTo);

                                // get tec
                                tecSpinner = (Spinner) optionView.findViewById(R.id.tec);
                                HTTPGetProperties getTec = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                tecArray = new ArrayList<CharSequence>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    try {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        if ( jsonObject!=null ) {
                                                            tecArray.add(jsonObject.getString("value"));
                                                        }
                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                tecAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, tecArray);
                                                tecAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                tecSpinner.setAdapter(tecAdapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };

                                String urlBuildTec="http://api.imot.bg/mobile_api/dictionary/tec";
                                getTec.execute(urlBuildTec);

                                // get phone
                                phoneSpinner = (Spinner) optionView.findViewById(R.id.phone);
                                HTTPGetProperties getTel = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                phoneArray = new ArrayList<CharSequence>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    try {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        if ( jsonObject!=null ) {
                                                            phoneArray.add(jsonObject.getString("value"));
                                                        }
                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                phoneAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, phoneArray);
                                                phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                phoneSpinner.setAdapter(phoneAdapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };

                                String urlBuildPhone="http://api.imot.bg/mobile_api/dictionary/phone";
                                getTel.execute(urlBuildPhone);

                            } else if (groupNumber==3) {
                            } else if (groupNumber==4) {
                                // get electricity
                                electricitySpinner = (Spinner) optionView.findViewById(R.id.electricity);
                                HTTPGetProperties getElectricity = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                electricityArray = new ArrayList<CharSequence>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    try {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        if ( jsonObject!=null ) {
                                                            electricityArray.add(jsonObject.getString("value"));
                                                        }
                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                electricityAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, electricityArray);
                                                electricityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                electricitySpinner.setAdapter(electricityAdapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };

                                String urlElectricity="http://api.imot.bg/mobile_api/dictionary/electricity";
                                getElectricity.execute(urlElectricity);

                                // get water
                                waterSpinner = (Spinner) optionView.findViewById(R.id.water);
                                HTTPGetProperties getWater = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                waterArray = new ArrayList<CharSequence>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    try {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        if ( jsonObject!=null ) {
                                                            waterArray.add(jsonObject.getString("value"));
                                                        }
                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                waterAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, waterArray);
                                                waterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                waterSpinner.setAdapter(waterAdapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };

                                String urlWater="http://api.imot.bg/mobile_api/dictionary/watter";
                                getWater.execute(urlWater);

                                // get Regulation
                                regulationSpinner = (Spinner) optionView.findViewById(R.id.regulation);
                                HTTPGetProperties getRegulation = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                regulationArray = new ArrayList<CharSequence>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    try {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        if ( jsonObject!=null ) {
                                                            regulationArray.add(jsonObject.getString("value"));
                                                        }
                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                regulationAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, regulationArray);
                                                regulationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                regulationSpinner.setAdapter(regulationAdapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };

                                String urlRegulation="http://api.imot.bg/mobile_api/dictionary/watter";
                                getRegulation.execute(urlRegulation);

                            } else if (groupNumber==5) {
                                buildTypeProperty = (TextView) optionView.findViewById(R.id.buildTypeProperty);
                                // get build type
                                HTTPGetProperties getBuildType = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                Log.e("HEREHERE",jsonArray.toString());
                                                buildTypeArray = new ArrayList<String>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    buildTypeArray.add(jsonArray.getString(i));
                                                }

                                                buildTypeProperty.setOnClickListener(new OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        final Dialog dialogSimple = new Dialog(getActivity());
                                                        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                        dialogSimple.setTitle("Избери особености:");
                                                        dialogSimple.setCancelable(false);

                                                        View vi = li.inflate(R.layout.listview_popup, null, false);
                                                        String textbuildTypeFieldValue = (String) buildTypeProperty.getText();
                                                        if ( (textbuildTypeFieldValue.length()>0) && (textbuildTypeFieldValue.contains(",")) ) {
                                                            buildTypeSelectedValue= new ArrayList<String>();
                                                            String[] parts = textbuildTypeFieldValue.split(",");
                                                            if ( parts.length>0 ) {
                                                                for ( int i=0;i<parts.length;i++) {
                                                                    buildTypeSelectedValue.add(parts[i]);
                                                                }
                                                            } else {
                                                                buildTypeSelectedValue.add(textbuildTypeFieldValue);
                                                            }
                                                        } else if ( (textbuildTypeFieldValue.length()>0) && ( !textbuildTypeFieldValue.equals("Всички") ) )  {
                                                            buildTypeSelectedValue= new ArrayList<String>();
                                                            buildTypeSelectedValue.add(textbuildTypeFieldValue);
                                                        } else {
                                                            buildTypeSelectedValue= new ArrayList<String>();
                                                        }

                                                        final SimpleMultiChoiceAdapter adapterbuildTypeProperties = new SimpleMultiChoiceAdapter(getActivity(), R.layout.propertytype_single_item, buildTypeArray,buildTypeSelectedValue);
                                                        ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                                                        listViewPopup.setAdapter(adapterbuildTypeProperties);
                                                        dialogSimple.setContentView(vi);
                                                        dialogSimple.show();

                                                        Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                                                        saveButton.setOnClickListener(new View.OnClickListener() {

                                                            @Override
                                                            public void onClick(View v) {
                                                                buildTypeSelectedValue=adapterbuildTypeProperties.returnSelectedFields();
                                                                if (buildTypeSelectedValue!=null) {
                                                                    Log.e("HEREHERE","Click Event "+buildTypeSelectedValue.toString());
                                                                    String valueCheckBox="";
                                                                    for (int i=0;i<buildTypeSelectedValue.size();i++) {
                                                                        valueCheckBox+=buildTypeSelectedValue.get(i)+",";
                                                                    }

                                                                    if (valueCheckBox.length()>0) {
                                                                        valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                                                                    } else {
                                                                        valueCheckBox="Всички";
                                                                    }
                                                                    buildTypeProperty.setText(valueCheckBox);
                                                                }
                                                                dialogSimple.hide();
                                                            }
                                                        });

                                                        Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                adapterbuildTypeProperties.clearSelectedFields();
                                                                dialogSimple.hide();
                                                            }
                                                        });
                                                    }
                                                });

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };
                                String urlBuildType="http://api.imot.bg/mobile_api/dictionary/type_build";
                                getBuildType.execute(urlBuildType);

                                // get year of build
                                yearFrom = (EditText) optionView.findViewById(R.id.yearFrom);
                                yearTo = (EditText) optionView.findViewById(R.id.yearTo);

                                // get tec
                                tecSpinner = (Spinner) optionView.findViewById(R.id.tec);
                                HTTPGetProperties getTec = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                tecArray = new ArrayList<CharSequence>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    try {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        if ( jsonObject!=null ) {
                                                            tecArray.add(jsonObject.getString("value"));
                                                        }
                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                tecAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, tecArray);
                                                tecAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                tecSpinner.setAdapter(tecAdapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };

                                String urlBuildTec="http://api.imot.bg/mobile_api/dictionary/tec";
                                getTec.execute(urlBuildTec);

                                // get phone
                                phoneSpinner = (Spinner) optionView.findViewById(R.id.phone);
                                HTTPGetProperties getTel = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                phoneArray = new ArrayList<CharSequence>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    try {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        if ( jsonObject!=null ) {
                                                            phoneArray.add(jsonObject.getString("value"));
                                                        }
                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                phoneAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, phoneArray);
                                                phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                phoneSpinner.setAdapter(phoneAdapter);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };

                                String urlBuildPhone="http://api.imot.bg/mobile_api/dictionary/phone";
                                getTel.execute(urlBuildPhone);

                            } else if (groupNumber==6) {
                                landPermanentUsageProperty = (TextView) optionView.findViewById(R.id.land_permanent_usage);
                                // get Land Permanent Usage
                                HTTPGetProperties getLandPermanentUsage = new HTTPGetProperties() {
                                        @Override
                                        protected void onPostExecute(String result) {
                                            if (result != null) {
                                                Log.e("HEREHERE", "THIS RESULT FROM HERE");
                                                Log.e("HEREHERE", result);
                                                try {
                                                    JSONArray  jsonArray = new JSONArray(result);
                                                    Log.e("HEREHERE",jsonArray.toString());
                                                    landPermanentUsageArray = new ArrayList<String>();
                                                    for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                        try {
                                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                            if ( jsonObject!=null && (jsonObject.getString("value").length()>0) ) {
                                                                landPermanentUsageArray.add(jsonObject.getString("value"));
                                                            }
                                                        }
                                                        catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    landPermanentUsageProperty.setOnClickListener(new OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            final Dialog dialogSimple = new Dialog(getActivity());
                                                            LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                            dialogSimple.setTitle("Начин на трайно ползване:");
                                                            dialogSimple.setCancelable(false);

                                                            View vi = li.inflate(R.layout.listview_popup, null, false);
                                                            String textlandPermanentUsageFieldValue = (String) landPermanentUsageProperty.getText();
                                                            if ( (textlandPermanentUsageFieldValue.length()>0) && (textlandPermanentUsageFieldValue.contains(",")) ) {
                                                                landPermanentUsageSelectedValue = new ArrayList<String>();
                                                                String[] parts = textlandPermanentUsageFieldValue.split(",");
                                                                if ( parts.length>0 ) {
                                                                    for ( int i=0;i<parts.length;i++) {
                                                                        landPermanentUsageSelectedValue.add(parts[i]);
                                                                    }
                                                                } else {
                                                                    landPermanentUsageSelectedValue.add(textlandPermanentUsageFieldValue);
                                                                }
                                                            } else if ( (textlandPermanentUsageFieldValue.length()>0) && ( !textlandPermanentUsageFieldValue.equals("Всички") ) )  {
                                                                landPermanentUsageSelectedValue= new ArrayList<String>();
                                                                landPermanentUsageSelectedValue.add(textlandPermanentUsageFieldValue);
                                                            } else {
                                                                landPermanentUsageSelectedValue= new ArrayList<String>();
                                                            }

                                                            final SimpleMultiChoiceAdapter adapterlandPermanentUsageProperties = new SimpleMultiChoiceAdapter(getActivity(), R.layout.propertytype_single_item, landPermanentUsageArray,landPermanentUsageSelectedValue);
                                                            ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                                                            listViewPopup.setAdapter(adapterlandPermanentUsageProperties);
                                                            dialogSimple.setContentView(vi);
                                                            dialogSimple.show();

                                                            Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                                                            saveButton.setOnClickListener(new View.OnClickListener() {

                                                                @Override
                                                                public void onClick(View v) {
                                                                    landPermanentUsageSelectedValue=adapterlandPermanentUsageProperties.returnSelectedFields();
                                                                    if (landPermanentUsageSelectedValue!=null) {
                                                                        Log.e("HEREHERE","Click Event "+landPermanentUsageSelectedValue.toString());
                                                                        String valueCheckBox="";
                                                                        for (int i=0;i<landPermanentUsageSelectedValue.size();i++) {
                                                                            valueCheckBox+=landPermanentUsageSelectedValue.get(i)+",";
                                                                        }

                                                                        if (valueCheckBox.length()>0) {
                                                                            valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                                                                        } else {
                                                                            valueCheckBox="Всички";
                                                                        }
                                                                        landPermanentUsageProperty.setText(valueCheckBox);
                                                                    } else {
                                                                        landPermanentUsageProperty.setText("Всички");
                                                                    }
                                                                    dialogSimple.hide();
                                                                }
                                                            });

                                                            Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                                                            closeButton.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    adapterlandPermanentUsageProperties.clearSelectedFields();
                                                                    dialogSimple.hide();
                                                                }
                                                            });
                                                        }
                                                    });

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                Log.e("HEREHERE", "EMPTY");
                                            }
                                        }
                                    };
                                    String urlLandPermanentUsage="http://api.imot.bg/mobile_api/dictionary/land_permanent_usage";
                                    getLandPermanentUsage.execute(urlLandPermanentUsage);

                                landCategoryProperty = (TextView) optionView.findViewById(R.id.land_category);
                                // get Land Category
                                HTTPGetProperties getLandCategory = new HTTPGetProperties() {
                                    @Override
                                    protected void onPostExecute(String result) {
                                        if (result != null) {
                                            Log.e("HEREHERE", "THIS RESULT FROM HERE");
                                            Log.e("HEREHERE", result);
                                            try {
                                                JSONArray  jsonArray = new JSONArray(result);
                                                Log.e("HEREHERE",jsonArray.toString());
                                                landCategoryArray = new ArrayList<String>();
                                                for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                                    try {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        if ( jsonObject!=null && (jsonObject.getString("value").length()>0) ) {
                                                            landCategoryArray.add(jsonObject.getString("value"));
                                                        }
                                                    }
                                                    catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                landCategoryProperty.setOnClickListener(new OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        final Dialog dialogSimple = new Dialog(getActivity());
                                                        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                        dialogSimple.setTitle("Начин на трайно ползване:");
                                                        dialogSimple.setCancelable(false);

                                                        View vi = li.inflate(R.layout.listview_popup, null, false);
                                                        String textlandCategoryFieldValue = (String) landCategoryProperty.getText();
                                                        if ( (textlandCategoryFieldValue.length()>0) && (textlandCategoryFieldValue.contains(",")) ) {
                                                            landCategorySelectedValue = new ArrayList<String>();
                                                            String[] parts = textlandCategoryFieldValue.split(",");
                                                            if ( parts.length>0 ) {
                                                                for ( int i=0;i<parts.length;i++) {
                                                                    landCategorySelectedValue.add(parts[i]);
                                                                }
                                                            } else {
                                                                landCategorySelectedValue.add(textlandCategoryFieldValue);
                                                            }
                                                        } else if ( (textlandCategoryFieldValue.length()>0) && ( !textlandCategoryFieldValue.equals("Всички") ) )  {
                                                            landCategorySelectedValue= new ArrayList<String>();
                                                            landCategorySelectedValue.add(textlandCategoryFieldValue);
                                                        } else {
                                                            landCategorySelectedValue= new ArrayList<String>();
                                                        }

                                                        final SimpleMultiChoiceAdapter adapterlandCategoryProperties = new SimpleMultiChoiceAdapter(getActivity(), R.layout.propertytype_single_item, landCategoryArray,landCategorySelectedValue);
                                                        ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                                                        listViewPopup.setAdapter(adapterlandCategoryProperties);
                                                        dialogSimple.setContentView(vi);
                                                        dialogSimple.show();

                                                        Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                                                        saveButton.setOnClickListener(new View.OnClickListener() {

                                                            @Override
                                                            public void onClick(View v) {
                                                                landCategorySelectedValue=adapterlandCategoryProperties.returnSelectedFields();
                                                                if (landCategorySelectedValue!=null) {
                                                                    Log.e("HEREHERE","Click Event "+landCategorySelectedValue.toString());
                                                                    String valueCheckBox="";
                                                                    for (int i=0;i<landCategorySelectedValue.size();i++) {
                                                                        valueCheckBox+=landCategorySelectedValue.get(i)+",";
                                                                    }

                                                                    if (valueCheckBox.length()>0) {
                                                                        valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                                                                    } else {
                                                                        valueCheckBox="Всички";
                                                                    }
                                                                    landCategoryProperty.setText(valueCheckBox);
                                                                } else {
                                                                    landCategoryProperty.setText("Всички");
                                                                }
                                                                dialogSimple.hide();
                                                            }
                                                        });

                                                        Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                adapterlandCategoryProperties.clearSelectedFields();
                                                                dialogSimple.hide();
                                                            }
                                                        });
                                                    }
                                                });

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Log.e("HEREHERE", "EMPTY");
                                        }
                                    }
                                };
                                String urlLandCategory="http://api.imot.bg/mobile_api/dictionary/land_category";
                                getLandCategory.execute(urlLandCategory);
                            } else {

                            }
                        } else {
                            moreSearchOptions.removeAllViews();
                        }

                        dialog.hide();
                    }
                });
            }
        });

        // Price of property
        priceFrom = (EditText) rootView.findViewById(R.id.priceFrom);
        priceFrom.setGravity(Gravity.CENTER);
        priceTo = (EditText) rootView.findViewById(R.id.priceTo);
        priceTo.setGravity(Gravity.CENTER);

        // Area of property
        areaFrom = (EditText) rootView.findViewById(R.id.areaFrom);
        areaFrom.setGravity(Gravity.CENTER);
        areaTo = (EditText) rootView.findViewById(R.id.areaTo);
        areaTo.setGravity(Gravity.CENTER);

        // sort property results
        sortAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sortValues, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortResult.setAdapter(sortAdapter);


        searchAdvancedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<HashMap<String,String>> values= new ArrayList<HashMap<String, String>>();

                values.add(HelpFunctions.generateHashForSearch("rub",typeAdvert));
                values.add(HelpFunctions.generateHashForSearch("price_min",priceFrom));
                values.add(HelpFunctions.generateHashForSearch("price_max",priceTo));
                values.add(HelpFunctions.generateHashForSearch("kv_min",areaFrom));
                values.add(HelpFunctions.generateHashForSearch("kv_max",areaTo));
                ArrayList<String> arrayValue = new ArrayList<String>();
                Collections.addAll(arrayValue, getResources().getStringArray(R.array.sortValues));
                values.add(HelpFunctions.generateHashForSearch("sort",sortResult, arrayValue));
                values.add(HelpFunctions.generateHashForSearch("type_home",choosePropertyType));
                values.add(HelpFunctions.generateHashForSearch("extri",extriProperty));

                if ( groupNumber==1 ) {

//                    values.add(HelpFunctions.generateHashForSearch("floor_from",floorFromSpinner,floorArray));
//                    values.add(HelpFunctions.generateHashForSearch("floor_to",floorToSpinner,floorArray));

                } else if ( groupNumber==2 ) {

                } else if ( groupNumber==3 ) {

                } else if ( groupNumber==4 ) {

                } else if ( groupNumber==5 ) {

                } else if ( groupNumber==6 ) {

                }
            }
        });

        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        return rootView;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.sell_radio_button:
                if (checked) {
                    typeAdvert=1;
                }
                break;
            case R.id.rent_radio_button:
                if (checked) {
                    typeAdvert=2;
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
//        Log.e("HEREHERE", String.valueOf(v.getId()));
        switch (v.getId()) {
            case R.id.sell_radio_button:
            break;
            case R.id.rent_radio_button:
            break;
//            case R.id.sell_checkbox:
//                // click on explore button
//                if (saleCheckbox.getText() == getString(R.string.material_icon_box_full)) {
//                    saleCheckbox.setText(getString(R.string.material_icon_checked_full));
//                    rentCheckbox.setText(getString(R.string.material_icon_box_full));
//                } else {
//                    saleCheckbox.setText(getString(R.string.material_icon_box_full));
//                    rentCheckbox.setText(getString(R.string.material_icon_checked_full));
//                }
//                break;
//            case R.id.sell_checkbox_label:
//                // click on explore button
//                if (saleCheckbox.getText() == getString(R.string.material_icon_box_full)) {
//                    saleCheckbox.setText(getString(R.string.material_icon_checked_full));
//                    rentCheckbox.setText(getString(R.string.material_icon_box_full));
//                } else {
//                    saleCheckbox.setText(getString(R.string.material_icon_box_full));
//                    rentCheckbox.setText(getString(R.string.material_icon_checked_full));
//                }
//                break;
//            case R.id.rent_checkbox:
//                // click on explore button
//                if (rentCheckbox.getText() == getString(R.string.material_icon_box_full)) {
//                    rentCheckbox.setText(getString(R.string.material_icon_checked_full));
//                    saleCheckbox.setText(getString(R.string.material_icon_box_full));
//
//                } else {
//                    rentCheckbox.setText(getString(R.string.material_icon_box_full));
//                    saleCheckbox.setText(getString(R.string.material_icon_checked_full));
//                }
//                break;
//            case R.id.rent_checkbox_label:
//                // click on explore button
//                if (rentCheckbox.getText() == getString(R.string.material_icon_box_full)) {
//                    rentCheckbox.setText(getString(R.string.material_icon_checked_full));
//                    saleCheckbox.setText(getString(R.string.material_icon_box_full));
//
//                } else {
//                    rentCheckbox.setText(getString(R.string.material_icon_box_full));
//                    saleCheckbox.setText(getString(R.string.material_icon_checked_full));
//                }
//                break;
        }
    }
}
