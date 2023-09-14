package com.intuit.contentAuthoringTool.Middleware;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.contentAuthoringTool.Accessor.SchemaDBService;
import com.intuit.contentAuthoringTool.Model.Article;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class JsonValidatorService {

    @Autowired
    private SchemaDBService schemaDBService;

    public Boolean validateArticleAgainstSchema(Article article){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance( SpecVersion.VersionFlag.V201909 );
        String jsonString = article.getArticleJson();
        String schemaId = article.getSchemaId();
        try
        {
            String schemaString = schemaDBService.getSchemaBySchemaId(schemaId).getSchemaString();
            JsonNode json = objectMapper.readTree(jsonString);
            JsonSchema schema = schemaFactory.getSchema(schemaString);
            Set<ValidationMessage> validationResult = schema.validate(json);

            if (validationResult.isEmpty()) {
                System.out.println( "There are no validation errors." );
                return Boolean.TRUE;
            } else {
                System.out.println( "There are validation errors." );
                validationResult.forEach(vm -> System.out.println(vm.getMessage()));
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            System.out.println("Validation of the article JSON failed: with exception:"+e.getMessage());
            return Boolean.FALSE;
        }
    }

    public Boolean validateJsonSchema(String schemaString) {
        try {
            JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance( SpecVersion.VersionFlag.V201909);
            schemaFactory.getSchema(schemaString);
        } catch (Exception e) {
            System.out.println("JSON Schema validation for JSON String:"+schemaString+" failed due to exception: "
                    + e.getMessage());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}