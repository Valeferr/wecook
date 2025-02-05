package com.wecook;

import com.wecook.rest.exceptions.*;
import com.wecook.rest.filters.CORSFilter;
import com.wecook.rest.filters.JwtFilter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/wecook/";

    public static void main(String[] args) {
        ResourceConfig config = new ResourceConfig()
                .packages("com.wecook.rest")
                .register(CORSFilter.class)
                .register(JwtFilter.class)
                .register(ConstraintViolationExceptionMapper.class)
                .register(JsonSyntaxExceptionMapper.class)
                .register(NoResultExceptionMapper.class)
                .register(NotFoundExceptionMapper.class)
                .register(GenericExceptionMapper.class);

        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

        Runtime.getRuntime().addShutdownHook(new Thread(httpServer::shutdownNow));
    }
}