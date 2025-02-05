package com.wecook.rest.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wecook.model.User;
import com.wecook.rest.LoginResource;
import com.wecook.rest.utils.RequestParser;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginResourceTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private org.glassfish.grizzly.http.server.Session httpSession;

    @Mock
    private Query<User> userQuery;

    @Mock
    private Gson gson;

    @Mock
    private Request context;

    @InjectMocks
    private LoginResource loginResource;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        lenient().when(sessionFactory.openSession()).thenReturn(session);
    }

    @Test
    public void testTC1LoginValid() {
        mockUser = new User();
        mockUser.setEmail("valentferrent@gmail.com");
        mockUser.setPassword("plumcakeCaduto@22");

        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("email", "valentferrent@gmail.com");
        jsonRequest.addProperty("password", "plumcakeCaduto@22");
        String jsonString = jsonRequest.toString();
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStream);
        lenient().when(session.createNamedQuery(User.GET_BY_EMAIL, User.class)).thenReturn(userQuery);
        lenient().when(userQuery.setParameter("email", "valentferrent@gmail.com")).thenReturn(userQuery);
        lenient().when(userQuery.getSingleResult()).thenReturn(mockUser);
        lenient().when(context.getSession(true)).thenReturn(httpSession);

        try (var mockedParser = mockStatic(RequestParser.class)) {
            mockedParser.when(() -> RequestParser.jsonRequestToClass(context, User.class)).thenReturn(mockUser);
            Response response = loginResource.login(context);

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        }
    }

    @Test
    public void testTC2LoginInvalidPasswrd() {
        mockUser = new User();
        mockUser.setEmail("valentferrent@gmail.com");
        mockUser.setPassword("plumcake@22");

        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("email", "valentferrent@gmail.com");
        jsonRequest.addProperty("password", "plumcake@22");
        String jsonString = jsonRequest.toString();
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStream);
        lenient().when(session.createNamedQuery(User.GET_BY_EMAIL, User.class)).thenReturn(userQuery);
        lenient().when(userQuery.setParameter("email", "valentferrent@gmail.com")).thenReturn(userQuery);
        lenient().when(userQuery.getSingleResult()).thenReturn(mockUser);
        lenient().when(context.getSession(true)).thenReturn(httpSession);

        try (var mockedParser = mockStatic(RequestParser.class)) {
            mockedParser.when(() -> RequestParser.jsonRequestToClass(context, User.class)).thenReturn(mockUser);
            Response response = loginResource.login(context);

            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        }
    }

    @Test
    public void testTC3LoginInvalidEmailAndPassword() {
        mockUser = new User();
        mockUser.setEmail("valentt@gmail.com");
        mockUser.setPassword("plumcake@22");

        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("email", "valentt@gmail.com");
        jsonRequest.addProperty("password", "plumcake@22");
        String jsonString = jsonRequest.toString();
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStream);

        Response response = loginResource.login(context);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());

    }
}
