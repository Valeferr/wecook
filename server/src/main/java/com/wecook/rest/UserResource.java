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
import com.wecook.rest.utils.JwtManager;
import com.wecook.rest.utils.RequestParser;
import com.wecook.rest.utils.SecurityUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
import java.util.List;

@Path("/users")
public class UserResource extends GenericResource {
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

    @POST
    @Path("/searchByUsername")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByName(@Context Request context) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);
        String username = jsonObject.get("username").getAsString();

        List<User> users;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> user = criteriaQuery.from(User.class);
            criteriaQuery.select(user).where(builder.like(user.get("username"), username + "%"));
            users = session.createQuery(criteriaQuery).getResultList();
        }

        return Response.ok(gson.toJson(users)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context) {
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        Long userId = JwtManager.getInstance().getUserId(authorizationToken);

        List<User> users;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            users = session.createNamedQuery(User.GET_ALL, User.class).getResultList();
        }

        JsonArray jsonArray = new JsonArray();
        for (User user : users) {
            if (user instanceof StandardUser) {
                JsonObject jsonUser = gson.toJsonTree(user, StandardUser.class).getAsJsonObject();
                jsonUser.addProperty("isFollowing", ((StandardUser) user).getFollowers().stream().anyMatch(follower -> follower.getId().equals(userId)));
                jsonArray.add(jsonUser);
            } else {
                jsonArray.add(gson.toJsonTree(user));
            }
        }

        return Response.ok(gson.toJson(jsonArray)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("id") int id) {
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        Long userId = JwtManager.getInstance().getUserId(authorizationToken);
        
        User user;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.get(User.class, id);
            if (user instanceof StandardUser) {
                JsonObject jsonUser = gson.toJsonTree(user, StandardUser.class).getAsJsonObject();
                jsonUser.addProperty("isFollowing", ((StandardUser) user).getFollowers().stream().anyMatch(follower -> follower.getId().equals(userId)));
                return Response.ok(gson.toJson(jsonUser)).build();
            }
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
                    if (!InputValidation.isImageValid(jsonUser.get("profilePicture").getAsString())) {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    }
                    byte[] profilePicture = RequestParser.base64ToByteArray(jsonUser.get("profilePicture").getAsString());
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

        User user = null;
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

    @POST
    @Path("/follower")
    @Produces(MediaType.APPLICATION_JSON)
    public Response followUser(@Context Request context) {
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        Long userId = JwtManager.getInstance().getUserId(authorizationToken);

        JsonObject jsonUser = RequestParser.jsonRequestToGson(context);
        if (!jsonUser.has("followedId")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        StandardUser standardUser;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, userId);
            User followed = session.get(User.class, jsonUser.get("followedId").getAsLong());

            if (followed == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            if (!user.getRole().equals(User.Roles.STANDARD) || !followed.getRole().equals(User.Roles.STANDARD)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            standardUser = (StandardUser) user;
            StandardUser standardFollowed = (StandardUser) followed;

            standardUser.getFollowing().add(standardFollowed);

            session.merge(standardUser);
            transaction.commit();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(gson.toJson(standardUser)).build();
    }

    @DELETE
    @Path("/follower/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollowUser(@Context Request context, @PathParam("userId") Long unfollowedUserId) {
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        Long userId = JwtManager.getInstance().getUserId(authorizationToken);

        StandardUser standardUser = new StandardUser();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, userId);
            User followed = session.get(User.class, unfollowedUserId);

            if (user == null || followed == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            if (!user.getRole().equals(User.Roles.STANDARD) || !followed.getRole().equals(User.Roles.STANDARD)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            standardUser = (StandardUser) user;
            StandardUser standardFollowed = (StandardUser) followed;

            standardUser.getFollowing().remove(standardFollowed);

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
