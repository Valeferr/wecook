package com.wecook.rest;

import com.google.gson.JsonObject;
import com.wecook.model.HibernateUtil;
import com.wecook.model.Like;
import com.wecook.model.Post;
import com.wecook.model.StandardUser;
import com.wecook.rest.utils.RequestParser;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Set;

@Path("/post/{postId}/like")
public class LikeResource extends GenericResource{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context, @PathParam("postId") int postId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);
        if (!jsonObject.has("standard_user")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Like like = new Like();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            int standardUserId = jsonObject.get("standard_user").getAsInt();
            Post post = session.get(Post.class, postId);
            StandardUser standardUser = session.get(StandardUser.class, standardUserId);

            //controllo se il like è già stato inserito
            Set<Like> postLikes = post.getLikes();
            boolean likeMatch = false;
            for (Like likeSet : postLikes) {
                likeMatch = likeSet.getStandardUser().getId() == standardUserId;
            }


           if (!likeMatch) {
                like.setPost(post);
                like.setStandardUser(standardUser);

                try {
                    session.persist(like);
                    transaction.commit();
                } catch (ConstraintViolationException e) {
                    transaction.rollback();
                    throw e;
                }
           }
        }

        return Response.status(Response.Status.CREATED)
                .entity(gson.toJson(like))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context, @PathParam("postId") int postId) {
        Set<Like> likes;

        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Post post = session.get(Post.class, postId);
            likes = post.getLikes();
        }

        return Response.ok(gson.toJson(likes)).build();
    }

    @GET
    @Path("/{likeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("postId") int postId, @PathParam("likeId") int likeId) {
        Like like;

        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Post post = session.get(Post.class, postId);
            like = post.getLikes().stream()
                    .filter((l) -> l.getId() == likeId)
                    .findFirst()
                    .orElseThrow(NotFoundException::new);
        }

        return Response.ok(gson.toJson(like)).build();
    }

    @DELETE
    @Path("/{likeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context Request context, @PathParam("postId") int postId, @PathParam("likeId") int likeId) {
        Like like;

        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            like = post.getLikes().stream()
                    .filter((l) -> l.getId() == likeId)
                    .findFirst()
                    .orElseThrow(NotFoundException::new);
            StandardUser standardUser = session.get(StandardUser.class, like.getStandardUser().getId());

            Set<Like> postLikes = post.getLikes();
            postLikes.remove(like);
            post.setLikes(postLikes);

            Set<Like> standardUserLikes = standardUser.getLikes();
            standardUserLikes.remove(like);
            standardUser.setLikes(standardUserLikes);

            try {
                session.remove(like);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(like)).build();
    }
}