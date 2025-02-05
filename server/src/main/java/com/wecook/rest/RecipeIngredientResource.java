package com.wecook.rest;

import com.google.gson.JsonObject;
import com.wecook.model.*;
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

import java.util.List;

@Path("/recipe/{recipeId}/step/{stepId}/recipeIngredient")
public class RecipeIngredientResource extends GenericResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context, @PathParam("recipeId") int recipeId, @PathParam("stepId") int stepId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        if (!jsonObject.has("quantity") || !jsonObject.has("measurementUnit") || !jsonObject.has("ingredientId")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Step step = session.get(Step.class, stepId);

            boolean recipeMatch = step.getRecipe().getId() == recipeId;
            if (!recipeMatch) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            step.getIngredients().add(recipeIngredient);

            Ingredient ingredient = session.get(
                Ingredient.class,
                jsonObject.get("ingredientId").getAsInt()
            );
            recipeIngredient.setIngredient(ingredient);
            recipeIngredient.setQuantity(jsonObject.get("quantity").getAsDouble());

            MeasurementUnits measurementUnits = Enum.valueOf(MeasurementUnits.class, jsonObject.get("measurementUnit").getAsString());
            if (!ingredient.getMeasurementUnits().contains(measurementUnits)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            recipeIngredient.setMeasurementUnit(measurementUnits);

            try {
                recipeIngredient.setStep(step);
                session.persist(recipeIngredient);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.status(Response.Status.CREATED)
                .entity(gson.toJson(recipeIngredient))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context, @PathParam("recipeId") int recipeId, @PathParam("stepId") int stepId) {
        List<RecipeIngredient> recipeIngredients;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Step step = session.get(Step.class, stepId);

            boolean recipeMatch = step.getRecipe().getId() == recipeId;
            if (!recipeMatch) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            recipeIngredients = step.getIngredients();
        }

        return Response.ok(gson.toJson(recipeIngredients)).build();
    }

    @GET
    @Path("/{recipeIngredientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("recipeId") int recipeId, @PathParam("stepId") int stepId, @PathParam("recipeIngredientId") int recipeIngredientId) {
        RecipeIngredient recipeIngredient;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            recipeIngredient = session.get(RecipeIngredient.class, recipeIngredientId);

            boolean recipeMatch = recipeIngredient.getStep().getRecipe().getId() == recipeId;
            boolean stepMatch = recipeIngredient.getStep().getId() == stepId;
            if (!recipeMatch || !stepMatch) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }

        return Response.ok(gson.toJson(recipeIngredient)).build();
    }

    @PATCH
    @Path("/{recipeIngredientId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@Context Request context, @PathParam("recipeId") int recipeId, @PathParam("stepId") int stepId, @PathParam("recipeIngredientId") int recipeIngredientId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        RecipeIngredient recipeIngredient;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            recipeIngredient = session.get(RecipeIngredient.class, recipeIngredientId);

            boolean recipeMatch = recipeIngredient.getStep().getRecipe().getId() == recipeId;
            boolean stepMatch = recipeIngredient.getStep().getId() == stepId;
            if (!recipeMatch || !stepMatch) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            Ingredient ingredient = session.get(
                    Ingredient.class,
                    recipeIngredient.getIngredient().getId()
            );

            try {
                if (jsonObject.has("quantity")) {
                    recipeIngredient.setQuantity(jsonObject.get("quantity").getAsDouble());
                }

                if (jsonObject.has("measurementUnit")) {
                    MeasurementUnits measurementUnits = Enum.valueOf(MeasurementUnits.class, jsonObject.get("measurementUnit").getAsString());
                    if (!ingredient.getMeasurementUnits().contains(measurementUnits)) {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    }
                    recipeIngredient.setMeasurementUnit(measurementUnits);
                }

                if (jsonObject.has("ingredient")) {
                    recipeIngredient.setIngredient(gson.fromJson(jsonObject.get("ingredient"), Ingredient.class));
                }

                session.merge(recipeIngredient);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(recipeIngredient)).build();
    }

    @DELETE
    @Path("/{recipeIngredientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context Request context, @PathParam("recipeId") int recipeId, @PathParam("stepId") int stepId, @PathParam("recipeIngredientId") int recipeIngredientId) {
        RecipeIngredient recipeIngredient;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            recipeIngredient = session.get(RecipeIngredient.class, recipeIngredientId);

            boolean recipeMatch = recipeIngredient.getStep().getRecipe().getId() == recipeId;
            boolean stepMatch = recipeIngredient.getStep().getId() == stepId;
            if (!recipeMatch || !stepMatch) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            Step step = recipeIngredient.getStep();
            step.getIngredients().remove(recipeIngredient);
            Ingredient ingredient = recipeIngredient.getIngredient();
            ingredient.getRecipeIngredient().remove(recipeIngredient);

            try {
                session.remove(recipeIngredient);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(recipeIngredient)).build();
    }
}