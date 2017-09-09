package helpers;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.carlos.fokus.R;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import constants.ApiUrls;

/**
 * Created by eceybrito on 08/09/2017.
 */

public class ApiHelper {

    private Context context;

    public ApiHelper(Context context) {
        this.context = context;
    }

    private RequestQueue requestQueue;

    public void getMarkerDetail (int id) {

        JsonArrayRequest arrReq = new JsonArrayRequest(
                Request.Method.GET,
                ApiUrls.apiUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        );

        requestQueue.add(arrReq);
    }

    public void showDialogFokus(Marker marker){

        final Dialog dialog = new Dialog(context.getApplicationContext());
        dialog.setTitle("Detalhes do foku");

        dialog.setContentView(R.layout.custom_info_contents);

        ImageView imageView = (ImageView) dialog.findViewById(R.id.imageCamera);

        // set the custom dialog components - text, image and button
        final TextView description = (TextView) dialog.findViewById(R.id.description);

        Button dialogButton = (Button) dialog.findViewById(R.id.btSend);
        RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);

        dialog.show();
    }
}
