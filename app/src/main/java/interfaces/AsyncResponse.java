package interfaces;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by macbook on 1/19/16.
 */
public interface AsyncResponse {
    void processFinish(String output,String numberOfAdverts,String searchText);
}