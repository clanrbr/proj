package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import Adapters.PropertiesArrayAdapter;
import localEstatesHttpRequests.HTTPGetProperties;
import localestates.localestates.R;

/**
 * Created by Ado on 12/23/2015.
 */
public class MainPageFragment extends Fragment{

    private ArrayList<JSONObject> advertsJsonArray = new ArrayList<JSONObject>();
    private PropertiesArrayAdapter adapterProperties;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        Log.e("HEREHERE","GOESHERE");
        View rootView = inflater.inflate(R.layout.fragment_main_page, null);

        listView = (ListView) rootView.findViewById(R.id.listView);

        HTTPGetProperties getProperty = new HTTPGetProperties() {
            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
//                    Log.e("HEREHERE", "OT TUK LI GO PRINTI?");
//                    Log.e("HEREHERE", result);
                    try {
                        JSONObject json = new JSONObject(result);
//                        Iterator<String> itCodesets = json.keys();

                        JSONArray jsonArray = json.getJSONArray("adverts");
                        ;
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (jsonArray.getJSONObject(i) != null) {
//                                    Log.e("HEREHERE", jsonArray.getJSONObject(i).toString());
                                    advertsJsonArray.add(jsonArray.getJSONObject(i));
                                }
                            }
                        }

//                        while (itCodesets.hasNext()) {
//                            Log.e("HEREHERE", itCodesets.next());
//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (advertsJsonArray!=null) {
//                        Attempt to invoke virtual method 'java.lang.Object android.content.Context.getSystemService(java.lang.String)' on a null object reference
                        // nai-veroqtno zaradi getActivity dava ??
                        adapterProperties = new PropertiesArrayAdapter(getActivity(),R.layout.property_single_item, advertsJsonArray);
                        listView.setAdapter(adapterProperties);
                        listView.setDivider(null);
                    }
                } else {
                    Log.e("HEREHERE", "EMPTY");
                }
            }
        };

        getProperty.execute("http://api.imot.bg/mobile_api/search");


        // Inflate the layout for this fragment
        return rootView;
    }
}
