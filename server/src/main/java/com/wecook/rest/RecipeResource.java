package com.wecook.rest;

import com.google.gson.JsonObject;
import com.wecook.model.HibernateUtil;
import com.wecook.model.Recipe;
import com.wecook.model.enums.FoodCategories;
import com.wecook.rest.utils.RequestParser;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Path("recipe")
public class RecipeResource extends GenericResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context) {
        Recipe recipeRequest = RequestParser.jsonRequestToClass(context, Recipe.class);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

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
    public Response getOne(@Context Request context, @PathParam("recipeId") int recipeId) {
        Recipe recipe;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            recipe = session.get(Recipe.class,recipeId);;
        }

        return Response.ok(gson.toJson(recipe)).build();
    }

    @PATCH
    @Path("/{recipeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@Context Request context, @PathParam("recipeId") int recipeId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        Recipe recipe;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            recipe = session.get(Recipe.class,recipeId);;

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
    public Response delete(@Context Request context, @PathParam("recipeId") int recipeId) {
        Recipe recipe;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            recipe = session.get(Recipe.class,recipeId);;

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
