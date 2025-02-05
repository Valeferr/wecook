package com.wecook.rest.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wecook.model.Comment;
import com.wecook.rest.CommentResource;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
public class CommentResourceTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Gson gson;

    @Mock
    private Request context;

    @InjectMocks
    private CommentResource commentResource;

    @Mock
    private Comment mockComment;

    @BeforeEach
    public void setUp() {
        lenient().when(sessionFactory.openSession()).thenReturn(session);
    }

    @Test
    public void testTC1PostComment() {
        String jsonString = "{\"standardUserId\":1,\"text\":\"Molto Buono\"}";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStream);

        String commentText = "Molto Buono";
        mockComment = new Comment();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("standardUserId", 1);
        jsonObject.addProperty("text", commentText);

        lenient().when(context.getAttribute("entity")).thenReturn(jsonObject);

        Response response = commentResource.post(context, 1);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testTC2FailedPostCommentFormat() {
        String jsonString = "{\"standardUserId\":1,\"text\":\"Molto Buono <>#^\"}";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStream);

        String commentText = "Molto Buono <>#^";
        mockComment = new Comment();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("standardUserId", 1);
        jsonObject.addProperty("text", commentText);

        lenient().when(context.getAttribute("entity")).thenReturn(jsonObject);

        Response response = commentResource.post(context, 1);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testTC3FailedPostCommentLength() {
        String jsonString = "{\"standardUserId\":1,\"text\":\"Molto Buono <>#^\"}";
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStream);

        String commentText = "Ho provato questa ricetta e devo dire che è stata un'esperienza culinaria davvero eccezionale! Fin dal primo passo, la preparazione è risultata chiara e ben spiegata, rendendo il processo accessibile anche a chi non ha grande esperienza in cucina. Gli ingredienti erano perfettamente bilanciati, e il risultato finale è stato sorprendentemente delizioso.\n" +
                "\n" +
                "Uno degli aspetti che ho apprezzato di più è stata la combinazione dei sapori: ogni ingrediente ha trovato il suo spazio senza sovrastare gli altri, creando un’armonia di gusti che ha reso ogni boccone un piacere. La consistenza del piatto è risultata perfetta, né troppo secca né troppo umida, grazie alle giuste proporzioni indicate nella ricetta. Inoltre, il profumo che si sprigionava durante la cottura era semplicemente irresistibile, rendendo difficile aspettare prima di assaggiare!";
        mockComment = new Comment();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("standardUserId", 1);
        jsonObject.addProperty("text", commentText);

        lenient().when(context.getAttribute("entity")).thenReturn(jsonObject);

        Response response = commentResource.post(context, 1);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

}
