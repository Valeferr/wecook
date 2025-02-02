package com.wecook.rest;

import com.wecook.model.*;
import com.wecook.model.enums.FoodCategories;
import com.wecook.model.enums.FoodTypes;
import com.wecook.model.enums.MeasurementUnits;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;

import java.util.List;

@Path("/")
public class ValueSetsResource extends GenericResource {
    @GET
    @Path("/difficulties")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDifficulties(@Context Request context) {
        List<Recipe.Difficulties> difficulties = List.of(Recipe.Difficulties.class.getEnumConstants());

        return Response.ok(gson.toJson(difficulties)).build();
    }

    @GET
    @Path("/actions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllActions(@Context Request context) {
        List<Step.Actions> actions = List.of(Step.Actions.class.getEnumConstants());

        return Response.ok(gson.toJson(actions)).build();
    }

    @GET
    @Path("/measurementUnits")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMeasurementUnits(@Context Request context) {
        List<MeasurementUnits> measurementUnits = List.of(MeasurementUnits.class.getEnumConstants());

        return Response.ok(gson.toJson(measurementUnits)).build();
    }

    @GET
    @Path("/allergies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAllergy(@Context Request context) {
        List<StandardUser.Allergy> allergies = List.of(StandardUser.Allergy.class.getEnumConstants());

        return Response.ok(gson.toJson(allergies)).build();
    }

    @GET
    @Path("/foodCategories")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFoodCategories(@Context Request context) {
        List<FoodCategories> foodCategories = List.of(FoodCategories.class.getEnumConstants());

        return Response.ok(gson.toJson(foodCategories)).build();
    }

    @GET
    @Path("/foodTypes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFoodTypes(@Context Request context) {
        List<FoodTypes> foodTypes = List.of(FoodTypes.class.getEnumConstants());

        return Response.ok(gson.toJson(foodTypes)).build();
    }

    @GET
    @Path("/reasons")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReasons(@Context Request context) {
        List<Report.Reasons> reasons = List.of(Report.Reasons.class.getEnumConstants());

        return Response.ok(gson.toJson(reasons)).build();
    }
}
