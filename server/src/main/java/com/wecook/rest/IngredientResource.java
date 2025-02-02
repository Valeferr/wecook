package com.wecook.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wecook.model.HibernateUtil;
import com.wecook.model.Ingredient;
import com.wecook.model.enums.FoodTypes;
import com.wecook.model.enums.MeasurementUnits;
import com.wecook.rest.utils.RequestParser;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Path("/ingredient")
public class IngredientResource extends GenericResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);
        if (!jsonObject.has("name") || !jsonObject.has("type") || !jsonObject.has("measurementUnit")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Ingredient ingredientRequest = new Ingredient();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            ingredientRequest.setName(jsonObject.get("name").getAsString());
            ingredientRequest.setType(
                    Enum.valueOf(
                        FoodTypes.class,
                        jsonObject.get("type").getAsString())
                    );
            Set<MeasurementUnits> measurementUnits = new HashSet<>();
            JsonArray jsonArray = jsonObject.get("measurementUnit").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                measurementUnits.add(
                    Enum.valueOf(
                        MeasurementUnits.class,
                        jsonElement.getAsString()
                    )
                );
            }
            ingredientRequest.setMeasurementUnits(measurementUnits);
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
    public Response getOne(@Context Request context, @PathParam("id") int id) {
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
                if (jsonObject.has("measurementUnit")) {
                    Set<MeasurementUnits> measurementUnits = new HashSet<>();
                    JsonArray jsonArray = jsonObject.get("measurementUnit").getAsJsonArray();
                    for (JsonElement jsonElement : jsonArray) {
                        measurementUnits.add(
                            Enum.valueOf(
                                MeasurementUnits.class,
                                jsonElement.getAsString()
                            )
                       );
                    }
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
