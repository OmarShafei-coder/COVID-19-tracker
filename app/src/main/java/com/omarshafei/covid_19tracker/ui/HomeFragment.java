package com.omarshafei.covid_19tracker.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.omarshafei.covid_19tracker.R;

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
        final TextView hotLine15335 = root.findViewById(R.id.hot_line_15335);
        addUnderlineText(hotLine105);
        addUnderlineText(hotLine15335);

        hotLine105.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhoneNumber("105");
            }
        });

        hotLine15335.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhoneNumber("15335");
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
            String egyptData = "https://corona.lmao.ninja/v2/countries/egypt";

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

    private void dialPhoneNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
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
    public void addUnderlineText(TextView textView){
        SpannableString content = new SpannableString( textView.getText().toString() );
        content.setSpan( new UnderlineSpan() , 0 , content.length() , 0 );
        textView.setText(content);
    }
}
