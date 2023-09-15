package com.intuit.contentAuthoringTool;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.intuit.contentAuthoringTool.Accessor.ArticleDBService;
import com.intuit.contentAuthoringTool.Accessor.SchemaDBService;
import com.intuit.contentAuthoringTool.Model.Article;
import com.intuit.contentAuthoringTool.Model.Schema;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccessorTests {

    @InjectMocks
    private ArticleDBService articleDBService;

    @InjectMocks
    private SchemaDBService schemaDBService;

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testSaveArticle() {
        Article article = new Article();

        articleDBService.saveArticle(article);

        verify(dynamoDBMapper, times(1)).save(article);
    }

    @Test
    public void testGetArticleById() {
        String articleId = "valid-article-id";
        Article article = new Article();
        article.setArticleId(articleId);

        when(dynamoDBMapper.load(Article.class, articleId)).thenReturn(article);

        Article result = articleDBService.getArticleById(articleId);

        assertEquals(article, result);
    }

    @Test
    public void testCreateSchema() {
        Schema schema = new Schema();

        schemaDBService.createSchema(schema);

        verify(dynamoDBMapper, times(1)).save(schema);
    }

    @Test
    public void testGetSchemaBySchemaId() {
        String schemaId = "valid-schema-id";
        Schema schema = new Schema();
        schema.setSchemaId(schemaId);

        when(dynamoDBMapper.load(Schema.class, schemaId)).thenReturn(schema);

        Schema result = schemaDBService.getSchemaBySchemaId(schemaId);

        assertEquals(schema, result);
    }
}

