package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.mvc.Controller;

/**
 * Created by chewb on 4/7/2016.
 */
public class ControllerBase extends Controller {

    public User getAuthenicatedUser(){
        return Ebean.find(User.class, Long.parseLong(session().get("User")));
    }

}
