package wash.rocket.xor.rocketwash.requests;

import android.net.Uri;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.IOUtils;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import wash.rocket.xor.rocketwash.model.AvailableTimesResult;
import wash.rocket.xor.rocketwash.util.Constants;

public class AvailableTimesRequest extends GoogleHttpClientSpiceRequest<AvailableTimesResult> {

    private String baseUrl;
    private String session_id;
    private int id;
    private int organization_id;
    private int services_duration;
    private String time_range_start;
    private String time_range_end;


    public AvailableTimesRequest(String session_id,
                                 int id,
                                 int organization_id,
                                 String time_range_start,
                                 String time_range_end,
                                 int services_duration) {
        super(AvailableTimesResult.class);
        this.baseUrl = Constants.URL + "service_locations/%d/available_times";
        this.session_id = session_id;
        this.id = id;
        this.organization_id = organization_id;
        this.time_range_start = time_range_start;
        this.time_range_end = time_range_end;
        this.services_duration = services_duration;
    }

    @Override
    public AvailableTimesResult loadDataFromNetwork() throws IOException {

        String uri = Uri.parse(String.format(baseUrl, id))
                .buildUpon()
                .appendQueryParameter("organization_id", "" + organization_id)
                .appendQueryParameter("time_range_start", time_range_start)
                .appendQueryParameter("time_range_end", time_range_end)
                .appendQueryParameter("services_duration", "" + services_duration)
                .build().toString();

        Log.d("loadDataFromNetwork", "uri = " + uri);

        HttpHeaders header = new HttpHeaders();
        header.set("X-Rocketwash-Session-Id", session_id);
        //header.set("Accept", "application/json");
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(uri)).setHeaders(header);

        String result = ""; //request.execute().parseAsString();

        InputStream content = request.execute().getContent();
        if (content != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(content, out);
            result = out.toString("UTF-8");
        }

        Log.d("AvailableTimesResult", " res = " + result);
        Log.w("AvailableTimesResult", " start parse json ");

        //ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //WashServiceResult res = mapper.readValue(result, getResultType());

        AvailableTimesResult res = LoganSquare.parse(result, AvailableTimesResult.class);

        Log.w("AvailableTimesResult", " end parse json ");

        return res;

        //ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        //return mapper.readValue(result, getResultType());
    }

}
