package com.intuit.contentAuthoringTool.Accessor;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.intuit.contentAuthoringTool.Model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleDBService {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public void saveArticle(Article article) {
        dynamoDBMapper.save(article);
    }

    public Article getArticleById(String articleId) {
        return dynamoDBMapper.load(Article.class, articleId);
    }
}