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

import java.time.LocalDate;
import java.util.List;

@Path("/report")
public class ReportResource extends GenericResource{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context Request context) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        if(!jsonObject.has("type") || !jsonObject.has("reason") || !jsonObject.has("standard_user")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Report report = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();

            //TODO: Controlla per aggiungere un post o un commento
            if (Enum.valueOf(Report.Types.class,jsonObject.get("type").getAsString()).equals(Report.Types.POST)) {
                report = new PostReport();
            }
            if (Enum.valueOf(Report.Types.class,jsonObject.get("type").getAsString()).equals(Report.Types.COMMENT)) {
                report = new CommentReport();
            }
            StandardUser standardUser = session.get(StandardUser.class, jsonObject.get("standard_user").getAsInt());
            //TODO: Rimuovere LocalDate.now()
            report.setDate(LocalDate.now());
            report.setUser(standardUser);
            report.setReason(
                Enum.valueOf(
                    Report.Reasons.class,
                    jsonObject.get("reason").getAsString()
                )
            );
            report.setContentType(
                Enum.valueOf(
                    Report.Types.class,
                    jsonObject.get("type").getAsString()
                )
            );

            report.setStatus(Report.States.OPEN);
            try {
                session.persist(report);
                transaction.commit();
            }catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.status(Response.Status.CREATED)
                .entity(gson.toJson(report))
                .build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context Request context) {
        List<Report> reports;

        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            reports = session.createNamedQuery(Report.GET_ALL, Report.class).getResultList();
        }

        return Response.ok(gson.toJson(reports)).build();
    }

    @GET
    @Path("/{reportId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@Context Request context, @PathParam("reportId") int reportId) {
        Report report;

        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            report = session.get(Report.class, reportId);
        }

        return Response.ok(gson.toJson(report)).build();
    }

    @PATCH
    @Path("/{reportId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@Context Request context, @PathParam("reportId") int reportId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);

        if (!jsonObject.has("states")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Report report;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            report = session.get(Report.class, reportId);

            report.setStatus(
                Enum.valueOf(
                    Report.States.class,
                    jsonObject.get("states").getAsString()
                )
            );
            try {
                session.merge(report);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(report)).build();
    }

    @PUT
    @Path("/{reportId}/post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putPost(@Context Request context, @PathParam("reportId") int reportId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);
        if (!jsonObject.has("postId")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Report report;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            report = session.get(Report.class, reportId);

            Post post = session.get(Post.class, jsonObject.get("postId").getAsInt());
            ((PostReport) report).setPost(post);

            try {
                session.merge(report);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(report)).build();
    }

    @PUT
    @Path("/{reportId}/comment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putComment(@Context Request context, @PathParam("reportId") int reportId) {
        JsonObject jsonObject = RequestParser.jsonRequestToGson(context);
        if (!jsonObject.has("commentId")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Report report;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            report = session.get(Report.class, reportId);

            Comment comment = session.get(Comment.class, jsonObject.get("commentId").getAsInt());
            ((CommentReport) report).setComment(comment);

            try {
                session.merge(report);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(report)).build();
    }

    @DELETE
    @Path("/{reportId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context Request context, @PathParam("reportId") int reportId) {
        Report report;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            report = session.get(Report.class, reportId);

            try {
                session.remove(report);
                transaction.commit();
            } catch (ConstraintViolationException e) {
                transaction.rollback();
                throw e;
            }
        }

        return Response.ok(gson.toJson(report)).build();
    }


}
