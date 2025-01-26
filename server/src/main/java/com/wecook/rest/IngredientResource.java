package com.wecook.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.wecook.model.HibernateUtil;
import com.wecook.model.Ingredient;
import com.wecook.rest.utils.RequestParser;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;


@Path("/ingredient")
public class IngredientResource extends GenericResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context) {
        Ingredient ingredientRequest;

        try {
            ingredientRequest = RequestParser.jsonRequestToClass(context, Ingredient.class);
        } catch (JsonSyntaxException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                session.persist(ingredientRequest);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.status(Response.Status.CREATED)
                .entity(gson.toJson(ingredientRequest))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context) {
        List<Ingredient> ingredients;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ingredients = session.createNamedQuery(Ingredient.GET_ALL,Ingredient.class).getResultList();
        }

        return Response.ok(gson.toJson(ingredients)).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByType(@Context Request context, @PathParam("id") int id) {
        Ingredient ingredient;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ingredient = session.get(Ingredient.class, id);
        }

        return Response.ok(gson.toJson(ingredient)).build();
    }

    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@Context Request context, @PathParam("id") int id) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        Ingredient ingredient;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            ingredient = session.get(Ingredient.class, id);
            if (ingredient == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            try {
                if (jsonObject.has("name")) {
                    ingredient.setName(
                      jsonObject.get("name").getAsString()
                    );
                }
                if (jsonObject.has("type")) {
                    ingredient.setName(
                        jsonObject.get("recipe").getAsString()
                    );
                }
                session.merge(ingredient);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(ingredient)).build();
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context Request context, @PathParam("id") int id) {
        Ingredient ingredient;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            ingredient = session.get(Ingredient.class, id);
            if (ingredient == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            try {
                session.remove(ingredient);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(ingredient)).build();
    }
}
