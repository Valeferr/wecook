package com.wecook.rest.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wecook.model.User;
import com.wecook.rest.LoginResource;
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
        mockUser.setEmail("s.albino2@studenti.unisa.it");
        mockUser.setPassword("s.albino");

        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("email", "s.albino2@studenti.unisa.it");
        jsonRequest.addProperty("password", "s.albino");
        String jsonString = jsonRequest.toString();
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStream);

        Response response = loginResource.login(context);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }

    @Test
    public void testTC2LoginInvalidPasswrd() {
        mockUser = new User();
        mockUser.setEmail("s.albino2@studenti.unisa.it");
        mockUser.setPassword("aprmeg");

        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("email", "s.albino2@studenti.unisa.it");
        jsonRequest.addProperty("password", "aprmeg");
        String jsonString = jsonRequest.toString();
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStream);

        Response response = loginResource.login(context);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
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
