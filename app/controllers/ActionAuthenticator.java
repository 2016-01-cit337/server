package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class ActionAuthenticator extends Security.Authenticator {

    private static User getUser(Http.Context ctx){
        String token = getTokenFromHeader(ctx);
        if (token != null) {
            User user = Ebean.find(User.class).where().eq("token", token).findUnique();
            if (user != null) {
                ctx.session().put("User", user.getId().toString());
                return user;
            }
        }
        return null;
    }

    @Override
    public String getUsername(Http.Context ctx) {
        User user = getUser(ctx);
        return user != null ? user.getEmail() : null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return super.onUnauthorized(context);
    }

    private static String getTokenFromHeader(Http.Context ctx) {
        String[] authTokenHeaderValues = ctx.request().headers().get("X-AUTH-TOKEN");
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            return authTokenHeaderValues[0];
        }
        return null;
    }
}
