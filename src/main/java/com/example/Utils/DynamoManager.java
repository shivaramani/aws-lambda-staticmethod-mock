package com.example.Utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;

import com.amazonaws.util.StringUtils;
import com.example.Model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public  class DynamoManager
{
    /*
    public DynamoManager(){

    }
    */

    public static Boolean save(String clientRegion, String tableName, String productId, String productName, String productVersion ){

        Boolean status = false;
        if(!StringUtils.isNullOrEmpty(clientRegion)) {
            HashMap<String, AttributeValue> item_values = new HashMap<String, AttributeValue>();

            item_values.put("productId", new AttributeValue(productId));
            item_values.put("productName", new AttributeValue(productName));
            item_values.put("productVersion", new AttributeValue(productVersion));

            AmazonDynamoDB dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(clientRegion)
                    .build();
            try {
                dynamoDbClient.putItem(tableName, item_values);
                status = true;
            } catch (ResourceNotFoundException e) {
                System.err.format("Error: The table \"%s\" can't be found.\n", tableName);
                System.err.println("Be sure that it exists and that you've typed its name correctly!");
            } catch (AmazonServiceException e) {
                System.err.println(e.getMessage());
            }
        }
        return status;
    }

    public static Product get(String clientRegion, String tableName, String productId){
        Product product = null;

        if(!StringUtils.isNullOrEmpty(clientRegion)) {
            AmazonDynamoDB dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(clientRegion)
                    .build();

            DynamoDB dynamoDB = new DynamoDB(dynamoDbClient);
            try {
                Table table = dynamoDB.getTable(tableName);
                GetItemSpec spec = new GetItemSpec()
                        .withPrimaryKey("productId", productId);
                String productJson = table.getItem(spec).toJSON();
                Gson gson = new GsonBuilder().create();
                product = gson.fromJson(productJson, Product.class);
            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }
        }
        return product;
    }

}