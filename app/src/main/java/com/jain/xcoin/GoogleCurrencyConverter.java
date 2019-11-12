package com.jain.xcoin;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class GoogleCurrencyConverter implements XCoin {

    public static final String DEFAULT_URL_FORMAT = "https://free.currconv.com/api/v7/convert?apiKey=0baad58eff115c77fdc2&q=%s&compact=y";
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private final String urlFormat;

    public GoogleCurrencyConverter() {
        this(DEFAULT_URL_FORMAT);
    }

    public GoogleCurrencyConverter(String urlFormat) {
        this.urlFormat = urlFormat;
    }

    @Override
    public double getConversionRate(String fromCurrencyCode, String toCurrencyCode) throws XCoinException {
        String result = null;
        try {
            String conversionText = String.format("%s_%s", fromCurrencyCode, toCurrencyCode);
            String urlString = String.format(this.urlFormat, conversionText);
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //Connect to our url
            urlConnection.connect();


            InputStream responseBody = urlConnection.getInputStream();
            //Create a new InputStreamReader
            InputStreamReader responseBodyReader =
                    new InputStreamReader(responseBody, "UTF-8");
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (name.equals(conversionText)) {
                    return readResult(jsonReader);
                }
                else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            return 0;
        }
        catch(Exception e)
        {
            throw new XCoinException("Failed to get conversion rate from " + fromCurrencyCode + " to " + toCurrencyCode, e);
        }
    }

    private Double readResult(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        while(jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals("val")) {
                return jsonReader.nextDouble();
            }
            else {
                jsonReader.skipValue();
            }
        }
        return null;
    }
}
