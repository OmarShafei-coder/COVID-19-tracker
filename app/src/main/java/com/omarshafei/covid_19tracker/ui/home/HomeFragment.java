package com.omarshafei.covid_19tracker.ui.home;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



import com.omarshafei.covid_19tracker.R;

public class HomeFragment extends Fragment {

    private String strJson;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView totalInfected = root.findViewById(R.id.total_infected_people);
        final TextView todayInfected = root.findViewById(R.id.today_infected_people);
        final TextView totalDead = root.findViewById(R.id.total_dead_people);
        final TextView todayDead = root.findViewById(R.id.today_dead_people);
        final TextView totalRecovered = root.findViewById(R.id.total_recovered_people);

        final TextView hotLine105 = root.findViewById(R.id.hot_line_105);
        addUnderlineText(hotLine105);

        hotLine105.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhoneNumber();
            }
        });
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) Objects.requireNonNull(getActivity())
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            String egyptData = "https://corona.lmao.ninja/countries/egypt";

            //HTTP request
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(egyptData)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        strJson = Objects.requireNonNull(response.body()).string();

                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                // Convert sampleJsonResponse String into a JSONObject
                                try {
                                    JSONObject jsonRootObject = new JSONObject(strJson);

                                    String cases = Integer.toString(jsonRootObject.getInt("cases"));
                                    totalInfected.setText(cases);

                                    String todayCases = Integer.toString(jsonRootObject.getInt("todayCases"));
                                    todayInfected.setText(todayCases);

                                    String deaths = Integer.toString(jsonRootObject.getInt("deaths"));
                                    totalDead.setText(deaths);

                                    String todayDeaths = Integer.toString(jsonRootObject.getInt("todayDeaths"));
                                    todayDead.setText(todayDeaths);

                                    String recovered = Integer.toString(jsonRootObject.getInt("recovered"));
                                    totalRecovered.setText(recovered);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            LinearLayout linearLayout = root.findViewById(R.id.parent);
            displayToast(linearLayout);
            closeApp();
        }

        return root;
    }

    private void dialPhoneNumber() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "105"));
        if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * @param viewGroup the parent of the layout
     */
    public void displayToast(ViewGroup viewGroup){
        viewGroup.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Check your internet and try again", Toast.LENGTH_LONG).show();
    }

    public void closeApp(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after one second = 1000ms
                //kill the app
                Objects.requireNonNull(getActivity()).finish();
            }
        }, 1000);
    }

    /**
     * @param textView the underlined text
     */
    private void addUnderlineText(TextView textView){
        SpannableString content = new SpannableString( "105" );
        content.setSpan( new UnderlineSpan() , 0 , content.length() , 0 );
        textView.setText(content);
    }
}
