package com.wecook.rest;

import com.google.gson.JsonObject;
import com.wecook.model.*;
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
import java.util.Set;

@Path("/post/{postId}/recipe/{recipeId}/step")
public class StepResource extends GenericResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context, @PathParam("postId") int postId, @PathParam("recipeId") int recipeId) {
        Step step;
        step = RequestParser.jsonRequestToClass(context, Step.class);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Recipe recipe = session.get(Recipe.class, recipeId);
            if (recipe.getPost().getId() != postId) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            recipe.getSteps().add(step);
            step.setRecipe(recipe);
            try {
                session.persist(step);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(step)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context, @PathParam("postId") int postId, @PathParam("recipeId") int recipeId) {
        Set<Step> steps;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Recipe recipe = session.get(Recipe.class, recipeId);
            if (recipe.getPost().getId() != postId) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            steps = new HashSet<>(recipe.getSteps());
        }

        return Response.ok(gson.toJson(steps)).build();
    }

    @GET
    @Path("/{stepId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("postId") int postId, @PathParam("recipeId") int recipeId, @PathParam("stepId") int stepId) {
        Step step;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Recipe recipe = session.get(Recipe.class, recipeId);
            if (recipe.getPost().getId() != postId) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            step = recipe.getSteps().stream()
                    .filter((s) -> s.getId() == stepId)
                    .findFirst()
                    .orElseThrow(NotFoundException::new);
        }

        return Response.ok(gson.toJson(step)).build();
    }

    @PATCH
    @Path("/{stepId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@Context Request context, @PathParam("postId") int postId, @PathParam("recipeId") int recipeId,@PathParam("stepId") int stepId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        Step step;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Recipe recipe = session.get(Recipe.class, recipeId);
            if (recipe.getPost().getId() != postId) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            step = recipe.getSteps().stream()
                    .filter((s) -> s.getId() == stepId)
                    .findFirst()
                    .orElseThrow(NotFoundException::new);

            try {
                if (jsonObject.has("description")) {
                    step.setDescription(jsonObject.get("description").getAsString());
                }
                if (jsonObject.has("duration")) {
                    step.setDuration(jsonObject.get("duration").getAsInt());
                }
                if (jsonObject.has("action")) {
                    step.setAction(
                        Enum.valueOf(
                            Step.Actions.class,
                            jsonObject.get("action").getAsString()
                        )
                    );
                }
                if (jsonObject.has("step_index")) {
                    step.setStepIndex(jsonObject.get("step_index").getAsInt());
                }

                session.merge(step);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(step)).build();
    }

    @DELETE
    @Path("/{stepId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context Request request, @PathParam("postId") int postId, @PathParam("recipeId") int recipeId,@PathParam("stepId") int stepId) {
        Step step;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Recipe recipe = session.get(Recipe.class, recipeId);
            if (recipe.getPost().getId() != postId) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            step = recipe.getSteps().stream()
                    .filter((s) -> s.getId() == stepId)
                    .findFirst()
                    .orElseThrow(NotFoundException::new);

            try {
                recipe.getSteps().remove(step);
                session.remove(step);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(step)).build();
    }

}
