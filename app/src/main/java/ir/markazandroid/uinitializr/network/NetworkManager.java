package ir.markazandroid.uinitializr.network;

import android.content.Context;

import ir.markazandroid.uinitializr.object.User;

/**
 * Coded by Ali on 29/11/2017.
 */

public interface NetworkManager {


    void login(User user, OnResultLoaded<User> listener);

    void getUser(OnResultLoaded<User> listener, boolean withLogin);


    class Builder {
        private String tag;
        private Context context;

        public Builder() {
        }

        public Builder from(Context context) {
            this.context = context;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public NetworkManager build() {
            return new NetworkManagerImp(context, tag);
        }
    }

}
