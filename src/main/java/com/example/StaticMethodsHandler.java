package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Model.Product;
import com.example.Model.ProductHandlerException;
import com.example.Utils.S3Manager;
import com.example.Utils.DynamoManager;
import java.io.IOException;

// TEST DUMMY JAVA LAMBDA
public class StaticMethodsHandler implements RequestHandler<Product, String> {

  Gson gson = new Gson();
  static Logger logger = LoggerFactory.getLogger(StaticMethodsHandler.class);

  @Override
  public String handleRequest(final Product request, final Context context) {
    
    logger.info("Received request in the SAVE Service");
    String requestJson = gson.toJson(request, Product.class);
    String output = String.format("Success - SAVE Received Input - %s", requestJson);

    String region = System.getenv("REGION");
    String bucketName = System.getenv("S3_BUCKET");
    String dynamoTable = System.getenv("DATABASE_TABLE");
    
    if(request != null && request.getProductId() != null &&
                          request.getProductName() != null &&
                          request.getProductVersion() != null)
    {
      String productId = request.getProductId();
      String productName = request.getProductName();
      String productVersion = request.getProductVersion();

      try {
        Boolean s3UploadStatus = S3Manager.upload(region, bucketName, request);
        if(s3UploadStatus){
          DynamoManager.save(region, dynamoTable, productId, productName, productVersion);
        }
        else{
          throw new ProductHandlerException("Error saving products");
        }
      }
      catch (IOException e) {
        throw new ProductHandlerException("Error saving products");
      } 
      catch (Exception ex) {
        throw new ProductHandlerException("Error saving products");
      }
     
    }
    else{
      throw new ProductHandlerException("Invalid product request.");
    }

    return  output;
    
  }

  
}