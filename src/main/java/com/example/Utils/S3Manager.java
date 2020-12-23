package com.example.Utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

import com.example.Model.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class S3Manager
{
    /*
    public S3Manager(){

    }
    */
    public static Boolean upload(String clientRegion, String bucketName, Product product) throws IOException {
        
        Boolean status = false;
        try {
            if(clientRegion != null ) {
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                        .withRegion(clientRegion)
                        .build();

                Gson gson = new GsonBuilder().create();
                String contents = gson.toJson(product, Product.class);

                String key = "file-" + product.getProductId().toString();

                s3Client.putObject(bucketName, key, contents);
                status = true;
            }
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        return status;
    }

    public static Product ListObjects(String clientRegion, String bucketName, String productId)  throws IOException  {
        
        Product product = null;
        String key = "";
        if(clientRegion != null) {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .build();

            ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
            List<S3ObjectSummary> objects = result.getObjectSummaries();
            String s3FileId = "file-" + productId;
            for (S3ObjectSummary os : objects) {
                key = os.getKey();
                if (s3FileId.equals(key)) {
                    product = new Product();
                    System.out.println("S3 Object Key" + key);

                    S3Object object = s3Client.getObject(bucketName, key);
                    BufferedInputStream input = new BufferedInputStream(object.getObjectContent());
                    ByteArrayOutputStream output = new ByteArrayOutputStream();

                    byte[] b = new byte[1000 * 1024];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    byte[] bytes = output.toByteArray();
                    String fileContent = new String(bytes, StandardCharsets.UTF_8);
                    Gson gson = new GsonBuilder().create();
                    product = gson.fromJson(fileContent, Product.class);
                }
            }
        }
        return product;
    }
}