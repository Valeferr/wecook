package com.wecook.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wecook.model.*;
import com.wecook.model.enums.FoodCategories;
import com.wecook.rest.utils.InputValidation;
import com.wecook.rest.utils.JwtManager;
import com.wecook.rest.utils.RequestParser;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Path("/post")
public class PostResource extends GenericResource{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context) {
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        Long userId = JwtManager.getInstance().getUserId(authorizationToken);

        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        Post post = new Post();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            StandardUser standardUser = session.get(StandardUser.class, userId);

            if (!InputValidation.isImageValid(jsonObject.get("postPicture").getAsString())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            byte[] postPicture = RequestParser.base64ToByteArray(jsonObject.get("postPicture").getAsString());
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
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        Long userId = JwtManager.getInstance().getUserId(authorizationToken);

        List<Post> posts;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            posts = session.createNamedQuery(Post.GET_ALL, Post.class).getResultList();
        }

        JsonArray jsonArray = new JsonArray();
        for (Post post : posts) {
            JsonObject jsonObject = gson.toJsonTree(post).getAsJsonObject();
            jsonObject.addProperty("saved", post.getSavedPosts().stream().anyMatch(savedPost -> savedPost.getStandardUser().getId().equals(userId)));
            jsonObject.addProperty("liked", post.getLikes().stream().anyMatch(like -> like.getStandardUser().getId().equals(userId)));
            jsonArray.add(jsonObject);
        }

        return Response.ok(gson.toJson(jsonArray)).build();
    }

    @GET
    @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("postId") Long postId) {
        String authorizationToken = context.getHeader("Authorization").replaceAll("Bearer ", "");
        Long userId = JwtManager.getInstance().getUserId(authorizationToken);

        Post post;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            post = session.get(Post.class, postId);
        }

        JsonObject jsonObject = gson.toJsonTree(post).getAsJsonObject();
        jsonObject.addProperty("saved", post.getSavedPosts().stream().anyMatch(savedPost -> savedPost.getStandardUser().getId() == userId));
        jsonObject.addProperty("liked", post.getLikes().stream().anyMatch(like -> like.getStandardUser().getId() == userId));

        return Response.ok(gson.toJson(jsonObject)).build();
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
            if (recipe.getSteps().isEmpty()) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
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

    @GET
    @Path("/searchByTitle")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByTitle(@Context Request context, @QueryParam("title") String title) {
        List<Post> posts;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> criteriaQuery = builder.createQuery(Post.class);
            Root<Post> post = criteriaQuery.from(Post.class);

            Join<Post, Recipe> recipeJoin = post.join("recipe");
            criteriaQuery.select(post).where(builder.like(recipeJoin.get("title"), "%" + title + "%"));

            posts = session.createQuery(criteriaQuery).getResultList();
        }

        return Response.ok(gson.toJson(posts)).build();
    }

    @GET
    @Path("/searchByCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByCategory(@Context Request context, @QueryParam("category") String category) {
        List<Post> posts;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> criteriaQuery = builder.createQuery(Post.class);
            Root<Post> post = criteriaQuery.from(Post.class);

            Join<Post, Recipe> recipeJoin = post.join("recipe");
            criteriaQuery.select(post).where(builder.equal(recipeJoin.get("category"), Enum.valueOf(FoodCategories.class, category)));

            posts = session.createQuery(criteriaQuery).getResultList();
        }

        return Response.ok(gson.toJson(posts)).build();
    }

    @GET
    @Path("/searchByIngredient")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByIngredient(@Context Request context, @QueryParam("ingredient") String ingredient) {
        List<Post> posts;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Post> criteriaQuery = builder.createQuery(Post.class);
            Root<Post> post = criteriaQuery.from(Post.class);

            Join<Post, Recipe> recipeJoin = post.join("recipe");
            Join<Recipe, Step> stepJoin = recipeJoin.join("steps");
            Join<Step, RecipeIngredient> recipeIngredientJoin = stepJoin.join("ingredients");
            Join<RecipeIngredient, Ingredient> ingredientJoin = recipeIngredientJoin.join("ingredient");

            criteriaQuery.select(post).where(builder.equal(ingredientJoin.get("name"), ingredient));

            posts = session.createQuery(criteriaQuery).getResultList();
        }

        return Response.ok(gson.toJson(posts)).build();
    }

    @GET
    @Path("/userPosts/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPosts(@Context Request context, @PathParam("userId") Long userId) {
        Set<Post> posts;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StandardUser standardUser = session.get(StandardUser.class, userId);
            posts = standardUser.getPosts();
        }

        JsonArray jsonArray = new JsonArray();
        for (Post post : posts) {
            JsonObject jsonObject = gson.toJsonTree(post).getAsJsonObject();
            jsonObject.addProperty("saved", post.getSavedPosts().stream().anyMatch(savedPost -> savedPost.getStandardUser().getId().equals(userId)));
            jsonObject.addProperty("liked", post.getLikes().stream().anyMatch(like -> like.getStandardUser().getId().equals(userId)));
            jsonArray.add(jsonObject);
        }

        return Response.ok(gson.toJson(jsonArray)).build();
    }
}