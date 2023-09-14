package com.intuit.contentAuthoringTool.Middleware;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.stereotype.Service;

@Service
public class JsonValidatorService {

    private static String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    public static Boolean validateJsonObject() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance( SpecVersion.VersionFlag.V201909 );

        String jsonStream = readFileAsString( "/Users/inderjit/Documents/IntuitContentAuthoringTool/backend/contentAuthoringTool/src/main/resources/test-objects/faq-1.json" );
        String schemaStream = readFileAsString( "/Users/inderjit/Documents/IntuitContentAuthoringTool/backend/contentAuthoringTool/src/main/resources/schemas/faq-schema.json" );

        try
        {

            JsonNode json = objectMapper.readTree(jsonStream);
            JsonSchema schema = schemaFactory.getSchema(schemaStream);

            Set<ValidationMessage> validationResult = schema.validate( json );

            if (validationResult.isEmpty()) {
                System.out.println( "There is no validation errors" );
                return Boolean.TRUE;
            } else {
                System.out.println( "There are validation errors" );
                validationResult.forEach(vm -> System.out.println(vm.getMessage()));
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {

        }

        return Boolean.FALSE;
    }
}