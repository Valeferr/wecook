package com.wecook.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wecook.model.HibernateUtil;
import com.wecook.model.ModeratorUser;
import com.wecook.model.StandardUser;
import com.wecook.model.User;
import com.wecook.model.enums.FoodCategories;
import com.wecook.rest.utils.InputValidation;
import com.wecook.rest.utils.RequestParser;
import com.wecook.rest.utils.SecurityUtils;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Path("/users")
public class UserResource extends GenericResource{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context) throws ConstraintViolationException {
        User requestUser;

        requestUser = RequestParser.jsonRequestToClass(context, User.class);
        String passwordHash = SecurityUtils.sha256(requestUser.getPassword());
        requestUser.setPassword(passwordHash);

        User user = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            if (requestUser.getRole().equals(User.Roles.STANDARD)) {
                user = new StandardUser();
            } else if (requestUser.getRole().equals(User.Roles.MODERATOR)) {
                user = new ModeratorUser();
            }
            if (!InputValidation.isEmailValid(requestUser.getEmail())){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            user.setEmail(requestUser.getEmail());
            user.setUsername(requestUser.getUsername());
            user.setPassword(requestUser.getPassword());
            user.setRole(requestUser.getRole());

            try {
                session.persist(user);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.status(Response.Status.CREATED)
                .entity(gson.toJson(user))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context) {
        List<User> users;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            users = session.createNamedQuery(User.GET_ALL, User.class).getResultList();
        }

        return Response.ok(gson.toJson(users)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("id") int id) {
        User user;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.get(User.class, id);
        }

        return Response.ok(gson.toJson(user)).build();

    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@Context Request context, @PathParam("id") int id) throws ConstraintViolationException {
        JsonObject jsonUser = RequestParser.jsonRequestToGson(context);

        User user = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            user = session.get(User.class, id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            if (!user.getRole().equals(User.Roles.STANDARD)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            try {
                if (jsonUser.has("username")) {
                    ((StandardUser) user).setUsername(jsonUser.get("username").getAsString());
                }
                if (jsonUser.has("favoriteDish")) {
                    String favoriteDish = jsonUser.get("favoriteDish").getAsString();
                    if(!InputValidation.isFavoriteDishValid(favoriteDish)) {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    }
                    ((StandardUser) user).setFavoriteDish(favoriteDish);
                }

                if (jsonUser.has("foodPreference")) {
                    ((StandardUser) user).setFoodPreference(
                        Enum.valueOf(
                            FoodCategories.class,
                            jsonUser.get("foodPreference").getAsString()
                        )
                    );
                }
                if (jsonUser.has("profilePicture")) {
                    System.out.println(jsonUser.get("profilePicture").getAsString());
                    byte[] profilePicture = Base64.getDecoder().decode(jsonUser.get("profilePicture").getAsString());
                    if (!InputValidation.isImageValid(profilePicture)) {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    }
                    ((StandardUser) user).setProfilePicture(profilePicture);
                }
                session.merge(user);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return Response.ok(gson.toJson(user)).build();
    }

    @PATCH
    @Path("/{id}/allergies")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putAllergies(@Context Request context, @PathParam("id") int id) {
        User user = null;

        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);
        JsonArray jsonAllergies = jsonObject.get("allergies").getAsJsonArray();

        List<StandardUser.Allergy> allergies = new ArrayList<>();
        for (JsonElement jsonElement : jsonAllergies) {
            allergies.add(
                Enum.valueOf(
                    StandardUser.Allergy.class,
                    jsonElement.getAsString()
                )
            );
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            user = session.get(User.class, id);

            if (!user.getRole().equals(User.Roles.STANDARD)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            if(jsonObject.has("operation")) {
                if (jsonObject.get("operation").getAsString().equals("add")) {
                    for (StandardUser.Allergy allergy : allergies) {
                        ((StandardUser) user).getAllergies().add(allergy);
                    }
                } else if (jsonObject.get("operation").getAsString().equals("delete")) {
                    for (StandardUser.Allergy allergy : allergies) {
                        ((StandardUser) user).getAllergies().remove(allergy);
                    }
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            try {
                session.merge(user);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(user)).build();
    }

    @PUT
    @Path("/{id}/follow")
    @Produces(MediaType.APPLICATION_JSON)
    public Response followUser(@Context Request context, @PathParam("id") int id) {
        StandardUser standardUser = new StandardUser();
        JsonObject jsonUser = RequestParser.jsonRequestToGson(context);
        int followedId = jsonUser.get("followedId").getAsInt();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            User followed = session.get(User.class, followedId);

            if (user == null || followed == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            if (!user.getRole().equals(User.Roles.STANDARD) || !followed.getRole().equals(User.Roles.STANDARD)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            standardUser = (StandardUser) user;
            StandardUser standardFollowed = (StandardUser) followed;

            if (!standardUser.getFollowing().contains(standardFollowed)) {
                standardUser.getFollowing().add(standardFollowed);
            }

            session.merge(standardUser);
            transaction.commit();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(gson.toJson(standardUser)).build();
    }

    @DELETE
    @Path("/{id}/unfollow/{followedId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unFollowUser(@PathParam("id") int id, @PathParam("followedId") int followedId) {
        StandardUser standardUser = new StandardUser();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            User followed = session.get(User.class, followedId);

            if (user == null || followed == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            if (!user.getRole().equals(User.Roles.STANDARD) || !followed.getRole().equals(User.Roles.STANDARD)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            standardUser = (StandardUser) user;
            StandardUser standardFollowed = (StandardUser) followed;


            if (standardUser.getFollowing().contains(standardFollowed)) {
                standardUser.getFollowing().remove(standardFollowed);
            }

            session.merge(standardUser);
            transaction.commit();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(gson.toJson(standardUser)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context Request context, @PathParam("id") int id) {
        User user;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.get(User.class, id);
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(user)).build();
    }
}
