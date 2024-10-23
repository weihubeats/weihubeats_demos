package com.json.schema.demo;

import com.github.erosb.jsonsKema.FormatValidationPolicy;
import com.github.erosb.jsonsKema.JsonParser;
import com.github.erosb.jsonsKema.JsonValue;
import com.github.erosb.jsonsKema.Schema;
import com.github.erosb.jsonsKema.SchemaLoader;
import com.github.erosb.jsonsKema.ValidationFailure;
import com.github.erosb.jsonsKema.Validator;
import com.github.erosb.jsonsKema.ValidatorConfig;
import java.util.Objects;

/**
 * @author : wh
 * @date : 2024/10/21 16:27
 * @description:
 */
public class JsonSkemaDemo {

    public static void main(String[] args) {

        JsonValue schemaJson = new JsonParser("""
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
            """).parse();

        Schema schema = new SchemaLoader(schemaJson).load();

        Validator validator = Validator.create(schema, new ValidatorConfig(FormatValidationPolicy.ALWAYS));

        JsonValue instance = new JsonParser("""
            {
            	"age": -5,
            	"name": null,
            	"email": "invalid"
            }
            """).parse();

        ValidationFailure failure = validator.validate(instance);

        JsonValue success = new JsonParser("""
            {
            	"age": 3,
            	"name": "xiaozou",
            	"email": "111423@qq.com"
            }
            """).parse();
        
        ValidationFailure validateSuccess = validator.validate(success);

    
        
//        System.out.println(failure);

        if (Objects.nonNull(failure)) {
            failure.getCauses().forEach(System.out::println);
            
        }
        
        if (Objects.isNull(validateSuccess)) {
            System.out.println(" validate json success");
        }
        
//        System.out.println(validateSuccess);

    }
}
