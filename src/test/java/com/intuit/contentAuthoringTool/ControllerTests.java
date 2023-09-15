package com.intuit.contentAuthoringTool;

import com.intuit.contentAuthoringTool.Accessor.ArticleDBService;
import com.intuit.contentAuthoringTool.Accessor.SchemaDBService;
import com.intuit.contentAuthoringTool.Controller.ArticleController;
import com.intuit.contentAuthoringTool.Controller.SchemaController;
import com.intuit.contentAuthoringTool.Dto.SchemaDto;
import com.intuit.contentAuthoringTool.Middleware.JsonValidatorService;
import com.intuit.contentAuthoringTool.Model.Article;
import com.intuit.contentAuthoringTool.Model.Schema;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ControllerTests {

    @InjectMocks
    private ArticleController articleController;

    @InjectMocks
    private SchemaController schemaController;

    @Mock
    private ArticleDBService articleDBService;

    @Mock
    private JsonValidatorService jsonValidatorService;

    @Mock
    private SchemaDBService schemaDBService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetArticle_ValidArticleId() {
        String articleId = "valid-article-id";
        Article article = new Article();
        article.setArticleId(articleId);

        when(articleDBService.getArticleById(articleId)).thenReturn(article);

        ResponseEntity<Article> response = articleController.getArticle(articleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(article, response.getBody());
    }

    @Test
    public void testGetArticle_ArticleNotFound() {
        String articleId = "invalid-article-id";

        when(articleDBService.getArticleById(articleId)).thenReturn(null);

        ResponseEntity<Article> response = articleController.getArticle(articleId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testPublishArticle_ValidArticle() {
        // Arrange
        Article article = new Article();
        article.setArticleId("valid-article-id");

        when(jsonValidatorService.validateArticleAgainstSchema(article)).thenReturn(true);

        // Act
        ResponseEntity<Article> response = articleController.publishArticle(article);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(article, response.getBody());
    }

    @Test
    public void testPublishArticle_InvalidArticle() {
        Article article = new Article();
        article.setArticleId("valid-article-id");

        when(jsonValidatorService.validateArticleAgainstSchema(article)).thenReturn(false);

        // Act
        ResponseEntity<Article> response = articleController.publishArticle(article);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateArticle_ValidArticle() {
        String articleId = "valid-article-id";
        Article existingArticle = new Article();
        existingArticle.setArticleId(articleId);

        Article updatedArticle = new Article();
        updatedArticle.setArticleId(articleId);
        updatedArticle.setArticleJson("Updated JSON");

        when(articleDBService.getArticleById(articleId)).thenReturn(existingArticle);

        ResponseEntity<Article> response = articleController.updateArticle(articleId, updatedArticle);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateArticle_ArticleNotFound() {
        String articleId = "invalid-article-id";
        Article updatedArticle = new Article();

        when(articleDBService.getArticleById(articleId)).thenReturn(null);

        ResponseEntity<Article> response = articleController.updateArticle(articleId, updatedArticle);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Test cases for SchemaController

    @Test
    public void testGetSchema_ValidSchemaId() {
        // Arrange
        String schemaId = "valid-schema-id";
        Schema schema = new Schema();
        schema.setSchemaId(schemaId);

        when(schemaDBService.getSchemaBySchemaId(schemaId)).thenReturn(schema);

        // Act
        ResponseEntity<?> response = schemaController.getSchema(schemaId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schema, response.getBody());
    }

    @Test
    public void testGetSchema_SchemaNotFound() {
        String schemaId = "invalid-schema-id";

        when(schemaDBService.getSchemaBySchemaId(schemaId)).thenReturn(null);

        ResponseEntity<?> response = schemaController.getSchema(schemaId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllSchemas() {
        List<Schema> schemas = new ArrayList<>();
        Schema schema1 = new Schema();
        schema1.setSchemaId("schema-1");
        Schema schema2 = new Schema();
        schema2.setSchemaId("schema-2");
        schemas.add(schema1);
        schemas.add(schema2);

        when(schemaDBService.getAllSchemas()).thenReturn(schemas);

        ResponseEntity<?> response = schemaController.getAllSchemas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schemas, response.getBody());
    }

    @Test
    public void testGetAllSchemas_NoSchemasFound() {
        List<Schema> schemas = new ArrayList<>();

        when(schemaDBService.getAllSchemas()).thenReturn(schemas);

        ResponseEntity<?> response = schemaController.getAllSchemas();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateSchema_InvalidSchema() {
        SchemaDto schemaDto = new SchemaDto();
        schemaDto.setSchemaTitle("Invalid Schema");
        schemaDto.setSchemaString("Invalid JSON");

        when(jsonValidatorService.validateJsonSchema(schemaDto.getSchemaString())).thenReturn(false);

        ResponseEntity<?> response = schemaController.createSchema(schemaDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}