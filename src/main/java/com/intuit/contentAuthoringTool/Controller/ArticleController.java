package com.intuit.contentAuthoringTool.Controller;


import com.intuit.contentAuthoringTool.Accessor.ArticleDBService;
import com.intuit.contentAuthoringTool.Model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController {

    @Autowired
    private ArticleDBService articleDBService;

    @GetMapping(value= "/get-article/{articleId}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> getArticle(@PathVariable String articleId) {
        try {
            Article article = articleDBService.getArticleById(articleId);
            if (article != null) {
                return ResponseEntity.ok(article);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/publish-article",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> publishArticle(@RequestBody Article article) {
        try {
            articleDBService.saveArticle(article);
            return ResponseEntity.status(HttpStatus.CREATED).body(article);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/update-article/{articleId}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> updateArticle(@PathVariable String articleId, @RequestBody Article article) {
        try {
            Article existingArticle = articleDBService.getArticleById(articleId);
            if (existingArticle != null) {
                existingArticle.setArticleJson(article.getArticleJson());
                articleDBService.saveArticle(existingArticle);
                return ResponseEntity.ok(existingArticle);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

