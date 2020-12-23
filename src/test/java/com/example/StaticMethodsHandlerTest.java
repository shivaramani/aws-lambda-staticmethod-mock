package com.example;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import com.example.StaticMethodsHandler;
import com.example.Model.Product;
import com.example.Model.ProductHandlerException;
import com.example.Utils.DynamoManager;
import com.example.Utils.S3Manager;
import com.google.gson.Gson;

@RunWith(PowerMockRunner.class)
@PrepareForTest({S3Manager.class, DynamoManager.class})
public class StaticMethodsHandlerTest {

    private Gson gson;

    @Mock
    S3Manager s3Manager;

    @Mock
    DynamoManager dynamoManager;

    private Product inputProduct;
    private String TEST_OUTPUT = "Success - SAVE Received Input - {\"productId\":\"1\",\"productName\":\"iphone\",\"productVersion\":\"10R\"}";
    private StaticMethodsHandler handler;

    @Before
    public void setup() throws Exception{
        gson = new Gson();

        inputProduct = new Product();
        inputProduct.setProductId("1");
        inputProduct.setProductName("iphone");
        inputProduct.setProductVersion("10R");

        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(DynamoManager.class);
        PowerMockito.mockStatic(S3Manager.class);
        PowerMockito.when(S3Manager.upload(any(), any(), any())).thenReturn(true);
        PowerMockito.when(DynamoManager.save(any(), any(), any(), any(), any())).thenReturn(true);
        handler = new StaticMethodsHandler();
    }


    @Test
    public void saveS3_validRequest_Success() throws IOException {

        String serviceOutput = handler.handleRequest(inputProduct, null);

        verify(s3Manager, times(1)).upload(any(), any(), any());
        verify(dynamoManager, times(1)).save(any(), any(), any(), any(), any());
        assertEquals(TEST_OUTPUT, serviceOutput);
    }

    @Test(expected = ProductHandlerException.class)
    public void noProduct_invalidRequest_RaiseException(){
        Product product = new Product();
        handler.handleRequest(product, null);
    }


    @Test
    public void noProduct_nullRequest_RaiseException() throws ProductHandlerException {
        try{
            handler.handleRequest(null, null);
        }
        catch(Exception ex){
            assertThat(ex, instanceOf(ProductHandlerException.class));
        }
    }


}