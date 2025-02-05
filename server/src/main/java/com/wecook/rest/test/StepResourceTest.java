package com.wecook.rest.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wecook.model.Ingredient;
import com.wecook.model.RecipeIngredient;
import com.wecook.model.Step;
import com.wecook.rest.IngredientResource;
import com.wecook.rest.RecipeIngredientResource;
import com.wecook.rest.StepResource;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.Request;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class StepResourceTest {
    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Gson gson;

    @Mock
    private Request context;

    @InjectMocks
    private StepResource stepResource;

    @InjectMocks
    private RecipeIngredientResource recipeIngredientResource;

    @InjectMocks
    private IngredientResource ingredientResource;

    private Step step;

    private RecipeIngredient recipeIngredient;

    private Ingredient ingredient;

    @BeforeEach
    public void setUp() {
        lenient().when(sessionFactory.openSession()).thenReturn(session);
    }

    @Test
    public void testTC1StepValid() {
        JsonObject jsonRequestStep = new JsonObject();
        jsonRequestStep.addProperty("action", "CUT");
        jsonRequestStep.addProperty("description", "la pancetta");
        jsonRequestStep.addProperty("duration", 2);
        jsonRequestStep.addProperty("stepIndex", 1);

        InputStream inputStreamStep = new ByteArrayInputStream(jsonRequestStep.toString().getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStreamStep);

        Response responseStep = stepResource.post(context, 1);
        assertEquals(Response.Status.CREATED.getStatusCode(), responseStep.getStatus());

        JsonObject jsonRequestRecipeIngredient = new JsonObject();
        jsonRequestRecipeIngredient.addProperty("quantity", 200.0);
        jsonRequestRecipeIngredient.addProperty("measurementUnit", "CENTILITER");
        jsonRequestRecipeIngredient.addProperty("ingredientId", 1);

        InputStream inputStreamRecipeIngredient = new ByteArrayInputStream(jsonRequestRecipeIngredient.toString().getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStreamRecipeIngredient);

        Response responseRecipeIngredient = recipeIngredientResource.post(context,1,1);

        assertEquals(Response.Status.CREATED.getStatusCode(), responseRecipeIngredient.getStatus());
    }

    @Test
    public void testTC2StepNoIngredients() {
        JsonObject jsonRequestStep = new JsonObject();
        jsonRequestStep.addProperty("action", "COOK");
        jsonRequestStep.addProperty("description", "su una padella antiaderente");
        jsonRequestStep.addProperty("duration", 14);
        jsonRequestStep.addProperty("stepIndex", 7);

        InputStream inputStreamStep = new ByteArrayInputStream(jsonRequestStep.toString().getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStreamStep);

        Response responseStep = stepResource.post(context, 1);
        assertEquals(Response.Status.CREATED.getStatusCode(), responseStep.getStatus());
    }

    @Test
    public void testTC3StepDescriptionLength() {
        JsonObject jsonRequestStep = new JsonObject();
        jsonRequestStep.addProperty("action", "COOK");
        jsonRequestStep.addProperty("description", "Versa lentamente la farina setacciata nel composto di uova e zucchero, mescolando delicatamente con una spatola dal basso verso l’alto per incorporare aria. Questo passaggio è essenziale per ottenere un impasto soffice e omogeneo, pronto per la cottura in forno già caldo.");
        jsonRequestStep.addProperty("duration", 14);
        jsonRequestStep.addProperty("stepIndex", 7);

        InputStream inputStreamStep = new ByteArrayInputStream(jsonRequestStep.toString().getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStreamStep);

        Response responseStep = stepResource.post(context, 1);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), responseStep.getStatus());
    }

    @Test
    public void testTC4StepDescriptionFormat() {
        JsonObject jsonRequestStep = new JsonObject();
        jsonRequestStep.addProperty("action", "COOK");
        jsonRequestStep.addProperty("description", "su  una >< #/ padella antiaderente");
        jsonRequestStep.addProperty("duration", 14);
        jsonRequestStep.addProperty("stepIndex", 7);

        InputStream inputStreamStep = new ByteArrayInputStream(jsonRequestStep.toString().getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStreamStep);

        Response responseStep = stepResource.post(context, 1);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), responseStep.getStatus());
    }

    @Test
    public void testTC5StepActionNotInsert() {
        JsonObject jsonRequestStep = new JsonObject();
        jsonRequestStep.addProperty("description", "su una padella antiaderente");
        jsonRequestStep.addProperty("duration", 14);
        jsonRequestStep.addProperty("stepIndex", 7);

        InputStream inputStreamStep = new ByteArrayInputStream(jsonRequestStep.toString().getBytes(StandardCharsets.UTF_8));
        lenient().when(context.getInputStream()).thenReturn(inputStreamStep);

        Response responseStep = stepResource.post(context, 1);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), responseStep.getStatus());
    }
}
