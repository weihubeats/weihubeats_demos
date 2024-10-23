package com.json.schema.networknt.demo;

import com.fasterxml.jackson.core.JsonLocation;
import com.networknt.schema.InputFormat;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaId;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.networknt.schema.serialization.JsonNodeReader;
import com.networknt.schema.utils.JsonNodes;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author : wh
 * @date : 2024/10/22 15:56
 * @description:
 */
class JsonSchemaDemoTest {


    @Test
    void testBasicTypeValidation() {
       /* String schema = "{"
            + "\"type\": \"object\","
            + "\"properties\": {"
            + "\"name\": {\"type\": \"string\"},"
            + "\"age\": {\"type\": \"integer\"},"
            + "\"isStudent\": {\"type\": \"boolean\"}"
            + "},"
            + "\"required\": [\"name\", \"age\"]"
            + "}";

        String validJson = "{"
            + "\"name\": \"John Doe\","
            + "\"age\": 30,"
            + "\"isStudent\": false"
            + "}";

        String invalidJson = "{"
            + "\"name\": \"John Doe\","
            + "\"age\": \"30\","  // Should be integer
            + "\"isStudent\": \"yes\""  // Should be boolean
            + "}";*/


        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012, builder ->
            // This creates a mapping from $id which starts with https://www.example.org/ to the retrieval URI classpath:schema/
            builder.schemaMappers(schemaMappers -> schemaMappers.mapPrefix("https://www.example.org/", "classpath:schema/"))
        );

        SchemaValidatorsConfig.Builder builder = SchemaValidatorsConfig.builder();

        SchemaValidatorsConfig config = builder.build();

        JsonSchema schema = jsonSchemaFactory.getSchema(SchemaLocation.of("https://www.example.org/example-main.json"), config);

        String input = "{\r\n"
            + "  \"main\": {\r\n"
            + "    \"common\": {\r\n"
            + "      \"field\": \"invalidfield\"\r\n"
            + "    }\r\n"
            + "  }\r\n"
            + "}";

        Set<ValidationMessage> assertions = schema.validate(input, InputFormat.JSON, executionContext -> {
            // By default since Draft 2019-09 the format keyword only generates annotations and not assertions
            executionContext.getExecutionConfig().setFormatAssertionsEnabled(true);
        });
        
        


    }
    
    @Test
    public void test2() {
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);

        SchemaValidatorsConfig.Builder builder = SchemaValidatorsConfig.builder();
        SchemaValidatorsConfig config = builder.build();
// Due to the mapping the meta-schema will be retrieved from the classpath at classpath:draft/2020-12/schema.
        JsonSchema schema = jsonSchemaFactory.getSchema(SchemaLocation.of(SchemaId.V202012), config);
        String input = "{\r\n"
            + "  \"type\": \"object\",\r\n"
            + "  \"properties\": {\r\n"
            + "    \"key\": {\r\n"
            + "      \"title\" : \"My key\",\r\n"
            + "      \"type\": \"invalidtype\"\r\n"
            + "    }\r\n"
            + "  }\r\n"
            + "}";
        Set<ValidationMessage> assertions = schema.validate(input, InputFormat.JSON, executionContext -> {
            // By default since Draft 2019-09 the format keyword only generates annotations and not assertions
            executionContext.getExecutionConfig().setFormatAssertionsEnabled(true);
        });
        
        System.out.println(assertions);
    }
    
    @Test
    public void test3() {
        String schemaData = """
            {
            	"$schema": "https://json-schema.org/draft/2020-12/schema",
            	"type": "object",
            	"properties": {
            		"age": {
            			"type": "number",
            			"minimum": 0
            		},
            		"name": {
            			"type": "string"
            		},
            		"email": {
            			"type": "string",
            			"format": "email"
            		}
            	}
            }
            """;
        
        
        String inputData = """
            {
            	"age": -5,
            	"name": null,
            	"email": "invalid"
            }
            """;
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012,
            builder -> builder.jsonNodeReader(JsonNodeReader.builder().locationAware().build()));
        
        SchemaValidatorsConfig config = SchemaValidatorsConfig.builder().build();
        
        JsonSchema schema = factory.getSchema(schemaData, InputFormat.JSON, config);
        
        Set<ValidationMessage> messages = schema.validate(inputData, InputFormat.JSON, executionContext -> {
            executionContext.getExecutionConfig().setFormatAssertionsEnabled(true);
        });

        String inputData1 = """
            {
            	"age": 3,
            	"name": "xiaozou",
            	"email": "111423@qq.com"
            }
            """;

        Set<ValidationMessage> messages1 = schema.validate(inputData1, InputFormat.JSON, executionContext -> {
            executionContext.getExecutionConfig().setFormatAssertionsEnabled(true);
        });

        System.out.println(messages1);
        
        List<ValidationMessage> list = messages.stream().toList();
        ValidationMessage format = list.get(0);
        JsonLocation formatInstanceNodeTokenLocation = JsonNodes.tokenLocationOf(format.getInstanceNode());
        JsonLocation formatSchemaNodeTokenLocation = JsonNodes.tokenLocationOf(format.getSchemaNode());
        ValidationMessage minLength = list.get(1);
        JsonLocation minLengthInstanceNodeTokenLocation = JsonNodes.tokenLocationOf(minLength.getInstanceNode());
        JsonLocation minLengthSchemaNodeTokenLocation = JsonNodes.tokenLocationOf(minLength.getSchemaNode());

        assertEquals("format", format.getType());
        assertEquals("date", format.getSchemaNode().asText());
        assertEquals(5, formatSchemaNodeTokenLocation.getLineNr());
        assertEquals(17, formatSchemaNodeTokenLocation.getColumnNr());
        assertEquals("1", format.getInstanceNode().asText());
        assertEquals(2, formatInstanceNodeTokenLocation.getLineNr());
        assertEquals(16, formatInstanceNodeTokenLocation.getColumnNr());
        assertEquals("minLength", minLength.getType());
        assertEquals("6", minLength.getSchemaNode().asText());
        assertEquals(6, minLengthSchemaNodeTokenLocation.getLineNr());
        assertEquals(20, minLengthSchemaNodeTokenLocation.getColumnNr());
        assertEquals("1", minLength.getInstanceNode().asText());
        assertEquals(2, minLengthInstanceNodeTokenLocation.getLineNr());
        assertEquals(16, minLengthInstanceNodeTokenLocation.getColumnNr());
        assertEquals(16, minLengthInstanceNodeTokenLocation.getColumnNr());
    }

}