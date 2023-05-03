package cz.maku.servershandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class PubSubConfiguration {

    public static final Gson GSON = new GsonBuilder()
            .setLenient()
            .enableComplexMapKeySerialization()
            .create();
    public static final String CHANNEL = "servers";

}
