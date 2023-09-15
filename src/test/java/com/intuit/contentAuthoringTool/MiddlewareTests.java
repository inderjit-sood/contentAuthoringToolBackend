package com.intuit.contentAuthoringTool;

import com.intuit.contentAuthoringTool.Accessor.SchemaDBService;
import com.intuit.contentAuthoringTool.Middleware.JsonValidatorService;
import com.intuit.contentAuthoringTool.Model.Article;
import com.intuit.contentAuthoringTool.Model.Schema;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MiddlewareTests {

    @InjectMocks
    private JsonValidatorService jsonValidatorService;

    @Mock
    private SchemaDBService schemaDBService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateArticleAgainstSchema_ValidArticle() throws IOException {
        String schemaId = "valid-schema-id";
        String articleJson = "{\"title\": \"Sample Title\", \"description\": \"Sample Description\"}";
        String schemaString = "{\"type\": \"object\",\"properties\": {\"title\": {\"type\": \"string\"},\"description\": {\"type\": \"string\"}},\"required\": [\"title\",\"description\"]}";

        Schema schema = new Schema();
        schema.setSchemaId(schemaId);
        schema.setSchemaString(schemaString);

        Article article = new Article();
        article.setSchemaId(schemaId);
        article.setArticleJson(articleJson);

        when(schemaDBService.getSchemaBySchemaId(schemaId)).thenReturn(schema);

        Boolean result = jsonValidatorService.validateArticleAgainstSchema(article);

        assertTrue(result);
    }

    @Test
    public void testValidateArticleAgainstSchema_InvalidArticle() throws IOException {
        String schemaId = "valid-schema-id";
        String articleJson = "{\"title\": \"Sample Title\"}"; // Missing "description" field
        String schemaString = "{\"type\": \"object\",\"properties\": {\"title\": {\"type\": \"string\"},\"description\": {\"type\": \"string\"}},\"required\": [\"title\",\"description\"]}";

        Schema schema = new Schema();
        schema.setSchemaId(schemaId);
        schema.setSchemaString(schemaString);

        Article article = new Article();
        article.setSchemaId(schemaId);
        article.setArticleJson(articleJson);

        when(schemaDBService.getSchemaBySchemaId(schemaId)).thenReturn(schema);

        Boolean result = jsonValidatorService.validateArticleAgainstSchema(article);

        assertFalse(result);
    }

    @Test
    public void testValidateArticleAgainstSchema_SchemaNotFound() throws IOException {

        String schemaId = "invalid-schema-id"; // Schema not found
        String articleJson = "{\"title\": \"Sample Title\", \"description\": \"Sample Description\"}";

        Article article = new Article();
        article.setSchemaId(schemaId);
        article.setArticleJson(articleJson);

        when(schemaDBService.getSchemaBySchemaId(schemaId)).thenReturn(null);

        Boolean result = jsonValidatorService.validateArticleAgainstSchema(article);

        assertFalse(result);
    }
}
