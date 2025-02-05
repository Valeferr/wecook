package com.wecook.rest;

import com.google.gson.JsonObject;
import com.wecook.model.Comment;
import com.wecook.model.HibernateUtil;
import com.wecook.model.Post;
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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Path("/post/{postId}/comment")
public class CommentResource extends GenericResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context, @PathParam("postId") int postId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);
        if(!jsonObject.has("standardUserId") || !jsonObject.has("text")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String text = jsonObject.get("text").getAsString();
        if(!InputValidation.isCommentValid(text) || text.length() > 400) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Comment comment = new Comment();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            StandardUser standardUser = session.get(StandardUser.class, jsonObject.get("standardUserId").getAsInt());
            comment.setPost(post);
            comment.setStandardUser(standardUser);

            comment.setPublicationDate(LocalDate.now());
            comment.setStatus(Comment.States.ACTIVE);

            comment.setText(text);

            try {
                session.persist(comment);
                transaction.commit();
            }catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.status(Response.Status.CREATED)
                .entity(gson.toJson(comment))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context, @PathParam("postId") int postId) {
        Set<Comment> comments;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Post post = session.get(Post.class, postId);
            comments = new HashSet<>(post.getComments());
        }
        return Response.ok(gson.toJson(comments)).build();
    }

    @GET
    @Path("/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("postId") int postId, @PathParam("commentId") int commentId) {
        Comment comment;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Post post = session.get(Post.class, postId);
            comment = post.getComments().stream()
                        .filter((c) -> c.getId() == commentId)
                        .findFirst()
                        .orElseThrow(NotFoundException::new);
        }

        return Response.ok(gson.toJson(comment)).build();
    }

    @PATCH
    @Path("/{commentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@Context Request context, @PathParam("postId") int postId, @PathParam("commentId") int commentId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);
        if (!jsonObject.has("state")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Comment comment;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Post post = session.get(Post.class, postId);
            comment = post.getComments().stream()
                    .filter((c) -> c.getId() == commentId)
                    .findFirst()
                    .orElseThrow(NotFoundException::new);
            comment.setStatus(
                Enum.valueOf(
                    Comment.States.class,
                    jsonObject.get("state").getAsString()
                )
            );

            try {
                session.merge(comment);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }
        return Response.ok(gson.toJson(comment)).build();
    }

    @DELETE
    @Path("/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context Request context, @PathParam("postId") int postId, @PathParam("commentId") int commentId) {

        Comment comment;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            comment = post.getComments().stream()
                    .filter((c) -> c.getId() == commentId)
                    .findFirst()
                    .orElseThrow(NotFoundException::new);
            try {
                post.getComments().remove(comment);
                session.remove(comment);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }

        }
        return Response.ok(gson.toJson(comment)).build();
    }
}