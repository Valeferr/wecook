package com.wecook.rest;

import com.google.gson.JsonObject;
import com.wecook.model.HibernateUtil;
import com.wecook.model.Post;
import com.wecook.model.SavedPost;
import com.wecook.model.StandardUser;
import com.wecook.rest.utils.JwtManager;
import com.wecook.rest.utils.RequestParser;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.time.LocalDate;
import java.util.Set;

@Path("/savedPost")
public class SavedPostResource extends GenericResource{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context) {
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        Long userId = JwtManager.getInstance().getUserId(authorizationToken);

        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);
        if (!jsonObject.has("postId")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        SavedPost savedPost = new SavedPost();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Post post = session.get(Post.class, jsonObject.get("postId").getAsInt());
            StandardUser standardUser = session.get(StandardUser.class, userId);

            savedPost.setSaveDate(LocalDate.now());
            savedPost.setPost(post);
            savedPost.setStandardUser(standardUser);

            try {
                session.persist(savedPost);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.status(Response.Status.CREATED)
                .entity(gson.toJson(savedPost))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context) {
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        Long userId = JwtManager.getInstance().getUserId(authorizationToken);

        Set<SavedPost> savedPosts;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StandardUser standardUser = session.get(StandardUser.class, userId);
            savedPosts = standardUser.getSavedPosts();
        }
        return Response.ok(gson.toJson(savedPosts)).build();
    }

    @GET
    @Path("/{savedPostId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("savedPostId") int savedPostId) {
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        Long userId = JwtManager.getInstance().getUserId(authorizationToken);

        SavedPost savedPost;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StandardUser standardUser = session.get(StandardUser.class, userId);
            savedPost = standardUser.getSavedPosts().stream()
                    .filter((s) -> s.getId() == savedPostId)
                    .findFirst()
                    .orElseThrow(NotFoundException::new);
        }

        return Response.ok(gson.toJson(savedPost)).build();
    }

    @DELETE
    @Path("/{savedPostId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context Request context, @PathParam("savedPostId") int savedPostId) {
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        Long userId = JwtManager.getInstance().getUserId(authorizationToken);

        SavedPost savedPost;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            StandardUser standardUser = session.get(StandardUser.class, userId);
            savedPost = standardUser.getSavedPosts().stream()
                    .filter((s) -> s.getId() == savedPostId)
                    .findFirst()
                    .orElseThrow(NotFoundException::new);

            Post post = session.get(Post.class, savedPost.getPost().getId());

            Set<SavedPost> postSavedPosts = post.getSavedPosts();
            postSavedPosts.remove(savedPost);
            post.setSavedPosts(postSavedPosts);

            Set<SavedPost> standardUserSavedPost = standardUser.getSavedPosts();
            standardUserSavedPost.remove(savedPost);
            standardUser.setSavedPosts(standardUserSavedPost);
            try {
                session.remove(savedPost);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }

        }

        return Response.ok(gson.toJson(savedPost)).build();
    }
}
