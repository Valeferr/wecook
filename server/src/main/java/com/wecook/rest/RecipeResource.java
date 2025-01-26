package com.wecook.rest;

import com.google.gson.JsonObject;
import com.wecook.model.HibernateUtil;
import com.wecook.model.Post;
import com.wecook.model.Recipe;
import com.wecook.model.Step;
import com.wecook.model.enums.FoodCategories;
import com.wecook.rest.utils.RequestParser;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

@Path("/post/{postId}/recipe")
public class RecipeResource extends GenericResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context, @PathParam("postId") int postId) {
        Recipe recipeRequest = RequestParser.jsonRequestToClass(context, Recipe.class);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Post post = session.get(Post.class, postId);
            recipeRequest.setPost(post);

            try {
                session.persist(recipeRequest);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.status(Response.Status.CREATED)
                .entity(gson.toJson(recipeRequest))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context) {
        List<Recipe> recipes;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            recipes = session.createNamedQuery(Recipe.GET_ALL, Recipe.class).getResultList();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(gson.toJson(recipes)).build();
    }

    @GET
    @Path("/{recipeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("postId") int postId, @PathParam("recipeId") int recipeId) {
        Recipe recipe;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Post post = session.get(Post.class, postId);
            Query<Recipe> recipeQuery = session.createNamedQuery(Recipe.GET_RECIPE_POST, Recipe.class);
            recipeQuery.setParameter("post", post);

            recipe = recipeQuery.getSingleResult();
        }

        return Response.ok(gson.toJson(recipe)).build();
    }

    @PATCH
    @Path("/{recipeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@Context Request context, @PathParam("postId") int postId, @PathParam("recipeId") int recipeId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        Recipe recipe;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            Query<Recipe> recipeQuery = session.createNamedQuery(Recipe.GET_RECIPE_POST, Recipe.class);
            recipeQuery.setParameter("post", post);

            recipe = recipeQuery.getSingleResult();

            try {
                if (jsonObject.has("title")) {
                    recipe.setTitle(
                        jsonObject.get("title").getAsString()
                    );
                }
                if (jsonObject.has("description")) {
                    recipe.setDescription(
                        jsonObject.get("description").getAsString()
                    );
                }
                if (jsonObject.has("difficulty")) {
                    recipe.setDifficulty(
                        Enum.valueOf(
                            Recipe.Difficulties.class,
                            jsonObject.get("difficulty").getAsString()
                        )
                    );
                }
                if (jsonObject.has("category")) {
                    recipe.setCategory(
                        Enum.valueOf(
                            FoodCategories.class,
                            jsonObject.get("category").getAsString()
                        )
                    );
                }
                session.merge(recipe);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(recipe)).build();
    }

    @DELETE
    @Path("/{recipeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context Request context, @PathParam("postId") int postId, @PathParam("recipeId") int recipeId) {
        Recipe recipe;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Post post = session.get(Post.class, postId);
            Query<Recipe> recipeQuery = session.createNamedQuery(Recipe.GET_RECIPE_POST, Recipe.class);
            recipeQuery.setParameter("post", post);

            recipe = recipeQuery.getSingleResult();

            try {
                session.remove(recipe);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(recipe)).build();
    }
}
