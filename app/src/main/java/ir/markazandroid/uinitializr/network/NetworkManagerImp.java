package ir.markazandroid.uinitializr.network;

import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;

import ir.markazandroid.uinitializr.UInitializrApplication;
import ir.markazandroid.uinitializr.network.JSONParser.Parser;
import ir.markazandroid.uinitializr.network.formdata.FormDataParser;
import ir.markazandroid.uinitializr.object.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Coded by Ali on 29/11/2017.
 */

class NetworkManagerImp implements NetworkManager {

    private Context context;
    private Parser parser;
    private OkHttpClient client;
    private String tag;

    NetworkManagerImp(Context context, String tag) {
        this.tag = tag;
        this.context = context;
        try {
            parser = ((UInitializrApplication) context.getApplicationContext()).getParser();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        client = ((UInitializrApplication) context.getApplicationContext()).getNetworkClient().getClient();
    }

    @Override
    public void login(User user, final OnResultLoaded<User> listener) {
        Request request = new Request.Builder()
                .post(FormDataParser.objectToFormBody(user))
                .url(NetStatics.AUTHENTICATION_LOGIN)
                .build();
        client.newCall(request).enqueue(new CBack(context, tag) {
            @Override
            public void fail(IOException e) {
                listener.failed(e);
            }

            @Override
            public boolean isSuccessfull(int code) {
                return true;
            }

            @Override
            public boolean isSuccessfull(int code, JSONObject response) {
                if (code == 200) listener.loaded(parser.get(User.class, response));
                else listener.loaded(null);
                return false;
            }
        });
    }

    @Override
    public void getUser(final OnResultLoaded<User> listener, final boolean withLogin) {
        Request request = new Request.Builder()
                .url(NetStatics.API_GETME)
                .get()
                .build();
        client.newCall(request).enqueue(new CBack(context, tag) {
            @Override
            public boolean isSuccessfull(int code) {
                if (withLogin) {
                    boolean done = super.isSuccessfull(code);
                    if (!done)
                        listener.loaded(null);
                    return done;
                } else {
                    if (code != 200) {
                        listener.loaded(null);
                    }
                    return code == 200;
                }
            }

            @Override
            public boolean isSuccessfull(int code, JSONObject response) {
                listener.loaded(parser.get(User.class, response));
                return false;
            }


            @Override
            public void fail(IOException e) {
                listener.failed(e);
            }
        });
    }

}
