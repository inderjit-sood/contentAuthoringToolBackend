package com.intuit.contentAuthoringTool.Accessor;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.intuit.contentAuthoringTool.Model.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class SchemaDBService {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public void createSchema(Schema schema) {
        dynamoDBMapper.save(schema);
    }

    public Schema getSchemaBySchemaId(String schemaId) {
        return dynamoDBMapper.load(Schema.class, schemaId);
    }

    public List<Schema> getAllSchemas() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return dynamoDBMapper.scan(Schema.class, scanExpression);
    }
}