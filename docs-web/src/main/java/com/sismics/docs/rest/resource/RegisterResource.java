package com.sismics.docs.rest.resource;


import com.sismics.docs.core.constant.Constants;
import com.sismics.docs.core.dao.RegisterDao;
import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.dao.dto.RegisterDto;
import com.sismics.docs.core.model.jpa.Register;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.rest.constant.BaseFunction;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.exception.ServerException;
import com.sismics.rest.util.ValidationUtil;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/register")
public class RegisterResource extends BaseResource {
    /**
     * Creates a new register request.
     *
     * @api {put} /register Register a new user
     * @apiName PostRegister
     * @apiGroup Register
     * @apiParam {String{3..50}} username Username
     * @apiParam {String{8..50}} password Password
     * @apiParam {String{1..100}} email E-mail
     * @apiParam {Number} storage_quota Storage quota (in bytes)
     * @apiSuccess {String} status Status OK
     * @apiError (client) ForbiddenError Access denied
     * @apiError (client) ValidationError Validation error
     * @apiError (server) PrivateKeyError Error while generating a private key
     * @apiError (client) AlreadyExistingUsername Login already used
     * @apiError (server) UnknownError Unknown server error
     * @apiPermission admin
     * @apiVersion 1.5.0
     *
     * @param username User's username
     * @param password Password
     * @param email E-Mail
     * @return Response
     */
    @POST
    public Response createRegister(
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("email") String email){

        username = ValidationUtil.validateLength(username, "username", 3, 50);
        ValidationUtil.validateUsername(username, "username");
        password = ValidationUtil.validateLength(password, "password", 8, 50);
        email = ValidationUtil.validateLength(email, "email", 1, 100);
        ValidationUtil.validateEmail(email, "email");

        Register register = new Register();
        register.setUsername(username);
        register.setPassword(password);
        register.setEmail(email);

        RegisterDao dao = new RegisterDao();
        try {
            dao.create(register);
        } catch (Exception e) {
            if ("AlreadyExistingUsername".equals(e.getMessage())) {
                throw new ClientException("AlreadyExistingUsername", "Login already used", e);
            } else {
                throw new ServerException("UnknownError", "Unknown server error", e);
            }
        }

        // Always return OK
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    @GET
    @Path("list")
    public Response getALlRegisters(){
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        if (!hasBaseFunction(BaseFunction.ADMIN)) {
            throw new ClientException("ForbiddenError", "Not admin");
        }

        JsonArrayBuilder registers = Json.createArrayBuilder();

       RegisterDao dao = new RegisterDao();
        List<RegisterDto> registerList = dao.getAll();
        for (RegisterDto registerDto: registerList) {
            registers.add(Json.createObjectBuilder()
                    .add("id", registerDto.getId())
                    .add("username", registerDto.getUsername())
                    .add("email", registerDto.getEmail())
                    .add("create_date", registerDto.getCreateTimestamp())
                    .add("state", registerDto.getState()));
        }

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("register", registers);
        return Response.ok().entity(response.build()).build();
    }

    @GET
    @Path("list/active")
    public Response getActiveRegister(){
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        if (!hasBaseFunction(BaseFunction.ADMIN)) {
            throw new ClientException("ForbiddenError", "Not admin");
        }

        JsonArrayBuilder registers = Json.createArrayBuilder();

        RegisterDao dao = new RegisterDao();
        List<RegisterDto> registerList = dao.getActive();
        for (RegisterDto registerDto: registerList) {
            registers.add(Json.createObjectBuilder()
                    .add("id", registerDto.getId())
                    .add("username", registerDto.getUsername())
                    .add("email", registerDto.getEmail())
                    .add("create_date", registerDto.getCreateTimestamp()));

        }
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("register", registers);
        return Response.ok().entity(response.build()).build();
    }

    @POST
    @Path("accept")
    public Response acceptRegister(
            @FormParam("id") String id
    ) throws Exception {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        if (!hasBaseFunction(BaseFunction.ADMIN)) {
            throw new ClientException("ForbiddenError", "Not admin");
        }

        RegisterDao dao = new RegisterDao();
        Register register =dao.getById(id);
        if (register == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        dao.updateState(id, "ACCEPTED");

        // Create the user
        User user = new User();
        user.setRoleId(Constants.DEFAULT_USER_ROLE);
        user.setUsername(register.getUsername());
        user.setPassword(register.getPassword());
        user.setEmail(register.getEmail());
        user.setStorageQuota(10000000L);
        user.setOnboarding(true);

        // Create the user
        UserDao userDao = new UserDao();
        try {
            userDao.createWithoutHash(user, principal.getId());
        } catch (Exception e) {
            if ("AlreadyExistingUsername".equals(e.getMessage())) {
                throw new ClientException("AlreadyExistingUsername", "Login already used", e);
            } else {
                throw new ServerException("UnknownError", "Unknown server error", e);
            }
        }

        // Always return OK
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();

    }

    @POST
    @Path("reject")
    public Response rejectRegister(
            @FormParam("id") String id
    ) throws Exception{
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        if (!hasBaseFunction(BaseFunction.ADMIN)) {
            throw new ClientException("ForbiddenError", "Not admin");
        }

        RegisterDao dao = new RegisterDao();
        Register register =dao.getById(id);
        if (register == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        dao.updateState(id, "REJECTED");
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }
}
