package com.wecook.rest;

import com.google.gson.JsonObject;
import com.wecook.model.HibernateUtil;
import com.wecook.model.Post;
import com.wecook.model.Recipe;
import com.wecook.rest.utils.RequestParser;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

@Path("/post")
public class PostResource extends GenericResource{


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        Post post = new Post();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            byte[] postPicture = Base64.getDecoder().decode(jsonObject.get("post_picture").getAsString());

            post.setPostPicture(postPicture);
            post.setStatus(
                Enum.valueOf(
                    Post.States.class,
                    jsonObject.get("post_state").getAsString()
                )
            );

            post.setPublicationDate(LocalDate.now());
            try {
                session.persist(post);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
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

            if (jsonObject.has("post_state")) {
                post.setStatus(
                    Enum.valueOf(
                        Post.States.class,
                        jsonObject.get("post_state").getAsString()
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