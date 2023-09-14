package com.intuit.contentAuthoringTool.Controller;

import com.intuit.contentAuthoringTool.Accessor.SchemaDBService;
import com.intuit.contentAuthoringTool.Model.Schema;
import com.intuit.contentAuthoringTool.Dto.SchemaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SchemaController {

    @Autowired
    private SchemaDBService schemaDBService;

    @GetMapping(value = "/get-schema/{schemaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSchema(@PathVariable String schemaId) {
        try {
            Schema schema = schemaDBService.getSchemaBySchemaId(schemaId);


            if (schema != null) {
                System.out.println("Schema found: " + schema.toString());
                return ResponseEntity.ok(schema);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schema not found"); //404 status code
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching schema: " + e.getMessage()); //500 status code
        }
    }

    @GetMapping(value = "/get-all-schemas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllSchemas() {
        try {
            List<Schema> schemas = schemaDBService.getAllSchemas();

            if (!schemas.isEmpty()) {
                return ResponseEntity.ok(schemas);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No schemas found"); // 404 status code
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching schemas: " + e.getMessage()); // 500 status code
        }
    }

    @PostMapping(value = "/create-schema",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSchema(@RequestBody SchemaDto schemaDto) {
        try {
            Schema newSchema = Schema.builder()
                    .schemaTitle(schemaDto.getSchemaTitle())
                    .schemaString(schemaDto.getSchemaString())
                    .build();

            schemaDBService.createSchema(newSchema);

            if (newSchema.getSchemaId() != null) {
                Schema createdSchema = schemaDBService.getSchemaBySchemaId(newSchema.getSchemaId());
                return ResponseEntity.status(HttpStatus.CREATED).body(createdSchema);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating schema: schemaId is null");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating schema: " + e.getMessage());
        }
    }
}
