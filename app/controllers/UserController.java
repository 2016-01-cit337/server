package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;

public class UserController extends ControllerBase {

    @javax.inject.Inject
    FormFactory formFactory;

    public Result register(){
        User user = play.libs.Json.fromJson(request().body().asJson(), User.class);
        if(user == null)
            return badRequest();
        Form<User> form = formFactory.form(User.class).bindFromRequest();

        // Check form errors
        JsonNode node = validateUser(form);
        if(node != null)
            return badRequest(play.libs.Json.toJson(node));

        // Save the entry
        return createUser();
    }

    public JsonNode validateUser(Form<User> form) {
        if(form.hasErrors()) {
            JsonNode node = form.errorsAsJson();    // Get errors
            User error = new User();

            if(form.error("firstName") != null)
                error.setFirstName(node.get("firstName").get(0).asText() );
            if(form.error("lastName") != null)
                error.setLastName( node.get("lastName").get(0).asText() );
            if(form.error("email") != null)
                error.setEmail( node.get("email").get(0).asText() );
            if(form.error("password") != null)
                error.setPassword(node.get("password").get(0).asText());
            return play.libs.Json.toJson(error);
        }
        User user = form.get();
        if(!user.getPassword().equals(user.getConfirmPassword()))
        {
            User error = new User();
            error.setPassword("Passwords do not match");
            error.setConfirmPassword("Passwords do not match");
            return play.libs.Json.toJson(error);
        }
        // Check for unique email
        User existingUser = User.findByEmail(user.getEmail());
        if(existingUser != null)
        {
            User error = new User();
            error.setEmail("This email is already in use");
            return play.libs.Json.toJson(error);
        }

        return null;
    }

    // Crud Methods
    private  Result getUser() {
        List<User> users = Ebean.find(User.class).findList();
        return ok(play.libs.Json.toJson(users));
    }

    private Result getUser(Long id) {
        User user = Ebean.find(User.class,id);
        return user == null ? notFound() : ok(play.libs.Json.toJson(user));
    }

    private Result createUser() {
        User user = play.libs.Json.fromJson(request().body().asJson(), User.class);
        user.setPassword(User.getSha256(user.getPassword()));
        user.setCreatedDate(Instant.now().getEpochSecond());
        Ebean.save(user);
        return created(play.libs.Json.toJson(user));
    }

    private Result updateUser(Long id) {
        User user = play.libs.Json.fromJson(request().body().asJson(), User.class);
        Ebean.update(user);
        return ok(play.libs.Json.toJson(user));
    }

    private Result deleteUser(Long id) {
        User user = Ebean.find(User.class, id);
        Ebean.delete(user);
        return noContent(); // http://stackoverflow.com/a/2342589/1415732
    }

    // Endpoint for user login
    public Result authenticate() {
        // 1. Define class to send JSON response back
        class Login {
            public Long     id;
            public String email;
            public String token;

            public Login() {

            }
        }

        // 2. Read email and password from request()
        JsonNode request = request().body().asJson();
        String email = request.get("email").asText();
        String password = request.get("password").asText();

        // 3. Find user with given email
        Login ret = new Login();
        User user = User.findByEmail(email);
        if (user == null) {
            return unauthorized(play.libs.Json.toJson(ret));
        }
        // 4. Compare password.
        String sha256 = User.getSha256(request.get("password").asText());
        if (sha256.equals(user.getPassword())) {
            // Success
            String authToken = generateAuthToken();
            user.setToken(authToken);
            Ebean.update(user);
            ret.token = authToken;
            ret.email = user.getEmail();
            ret.id = user.getId();
            return ok(play.libs.Json.toJson(ret));

        }
        // 5. Unauthorized access
        return unauthorized();
    }

    //@Security.Authenticated(ActionAuthenticator.class)
    private String generateAuthToken() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return bytes.toString();
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result logout() {
        User user = getAuthenicatedUser();
        if (user != null) {
            user.setToken(null);
            Ebean.save(user);
            return ok();
        }
        return unauthorized();
    }
}
