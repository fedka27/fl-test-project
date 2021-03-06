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

import wash.rocket.xor.rocketwash.model.ProfileResult;
import wash.rocket.xor.rocketwash.util.Constants;

//import com.google.api.client.json.jackson2.JacksonFactory;

public class AddToFavoriteRequest extends GoogleHttpClientSpiceRequest<ProfileResult> {

    private String baseUrl;
    private String session_id;
    private int id;
    private int organization_id;

    public AddToFavoriteRequest(String session_id, int id, int organization_id) {
        super(ProfileResult.class);
        this.baseUrl = Constants.URL + "favourites";
        this.session_id = session_id;
        this.id = id;
        this.organization_id = organization_id;
    }

    @Override
    public ProfileResult loadDataFromNetwork() throws IOException {
        String uri = Uri.parse(baseUrl)
                .buildUpon()
                .appendQueryParameter("id", "" + id)
                .appendQueryParameter("organization_id", "" + organization_id)
                .build().toString();
        Log.d("loadDataFromNetwork", "uri = " + uri);

        HttpHeaders header = new HttpHeaders();
        header.set("X-Rocketwash-Session-Id", session_id);

        HttpRequest request = getHttpRequestFactory().buildPostRequest(new GenericUrl(uri), null).setHeaders(header);
        //request.setParser(new JacksonFactory().createJsonObjectParser());
        // return request.execute().parseAs(getResultType());

        InputStream content = request.execute().getContent();
        String result = "";

        if (content != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(content, out);
            result = out.toString("UTF-8");
        }

        Log.d("AddToFavoriteRequest", "result = " + result);

        //ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        // ProfileResult res = mapper.readValue(result, getResultType());

        ProfileResult res = LoganSquare.parse(result, ProfileResult.class);

        res.getData().setString(result);

        return res;
    }

}
