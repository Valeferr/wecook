package com.wecook.rest;

import com.google.gson.JsonObject;
import com.wecook.model.HibernateUtil;
import com.wecook.model.Post;
import com.wecook.model.Recipe;
import com.wecook.model.StandardUser;
import com.wecook.rest.utils.InputValidation;
import com.wecook.rest.utils.RequestParser;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Path("/post")
public class PostResource extends GenericResource{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);
        if (!jsonObject.has("standardUserId")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Post post = new Post();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            StandardUser standardUser = session.get(StandardUser.class, jsonObject.get("standardUserId").getAsInt());

            byte[] postPicture = RequestParser.base64ToByteArray(jsonObject.get("postPicture").getAsString());
            if (!InputValidation.isImageValid(postPicture)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            post.setPostPicture(postPicture);
            post.setStandardUser(standardUser);
            post.setStatus(Post.States.ACTIVE);
            post.setPublicationDate(LocalDate.now());

            Set<Post> posts = standardUser.getPosts();
            posts.add(post);
            standardUser.setPosts(posts);
            try {
                session.persist(post);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Response.status(Response.Status.CREATED)
                .entity(gson.toJson(post))
                .build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context) {
        List<Post> posts;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            posts = session.createNamedQuery(Post.GET_ALL, Post.class).getResultList();
        }

        return Response.ok(gson.toJson(posts)).build();
    }

    @GET
    @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("postId") int postId) {
        Post post;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            post = session.get(Post.class, postId);
        }

        return Response.ok(gson.toJson(post)).build();
    }

    @PATCH
    @Path("/{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@Context Request context, @PathParam("postId") int postId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        Post post;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            post = session.get(Post.class, postId);

            if (jsonObject.has("postState")) {
                post.setStatus(
                    Enum.valueOf(
                        Post.States.class,
                        jsonObject.get("postState").getAsString()
                    )
                );
            }
            try {
                session.merge(post);
                transaction.commit();
            }catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(post)).build();
    }

    @PUT
    @Path("/{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context Request context, @PathParam("postId") int postId){
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        Post post;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            post = session.get(Post.class, postId);
            Recipe recipe = session.get(Recipe.class, jsonObject.get("recipeId").getAsInt());

            post.setRecipe(recipe);
            try {
                session.merge(recipe);
                transaction.commit();
            }catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(post)).build();
    }

    @DELETE
    @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context Request context, @PathParam("postId") int postId) {
        Post post;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            post = session.get(Post.class, postId);
            StandardUser standardUser = post.getStandardUser();

            Set<Post> posts = standardUser.getPosts();
            posts.remove(post);
            standardUser.setPosts(posts);

            try {
                session.remove(post);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(post)).build();
    }
}