package {{=it.packageName}};

import android.util.Log;

import com.amazon.pwain.sdk.PWAINMerchantBackendResponseProcessor;
import com.amazon.pwain.sdk.PWAINProcessPaymentRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ResponseProcessor implements PWAINMerchantBackendResponseProcessor{

    @Override
    public PWAINProcessPaymentRequest processSignAndEncryptResponse(final String response) {
        try {

            Map<String, String> parameters = getDecodedQueryParameters(response.trim());
            return new PWAINProcessPaymentRequest(parameters.get("payload"), parameters.get("iv"), parameters.get("key"));
        } catch (Exception e) {
            Log.d("merchant", "exception while processing sign and encrypt response", e);
        }
        return null;
    }

    @Override
    public boolean processVerifySignatureResponse(final String response) {
        try {
            if (response.trim().equalsIgnoreCase("true")) {
                return true;
            }
        } catch (Exception e) {
            Log.d("merchant", "excpetion while processing verify signature response", e);
        }
        return false;
    }

    private Map<String, String> getDecodedQueryParameters(String query) throws UnsupportedEncodingException {
        if (query == null || query.trim().length() < 1) {
            return null;
        }
        HashMap<String, String> parameters = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int index = pair.indexOf("=");
            parameters.put(URLDecoder.decode(pair.substring(0, index), "UTF-8"), URLDecoder.decode(pair.substring(index + 1), "UTF-8"));
        }
        return parameters;
    }

}
