# AWS Lambda with Junit PowerMockITO Services to upload/get files/rows into AWS S3 and DynamoDB

An example implementation of uploading and downloading/get files from AWS S3. Save and read rows from DynamoDB. The example provides JUnit testing using Mockito & PowerMockITO 


    1. Two sample lambdas (save and get) - to S3 and DynamodB
    2. Terraform implementation for deploying the lambda
    3. Powermock for S3 and DynamoDB testing static methods.
    4. Sample CLI commands to test


### Deployment
    #### Manual Steps
        1. $ mvn clean package
        2. cd templates
        3. terraform init
        4. terraform plan
        4. terraform apply --auto-approve
        5. Create a new AWS Lambda (in console or CLI),
            - Name: staticmethodsvc-lambda
            - Runtime: java8
            - Role: Default Lambda Role
        6. Upload the the jar.
        7. for "SAVE" requests, provide "com.example.StaticMethodsHandler::handleRequest" in "Handler" textbox (on the AWS Console)
        9. "Save" and "Test"

    
    #### Above steps are automated with - "Exec.sh"
        1. Provided "exec.sh" does the above   
        2. chmod +x exec.sh
        3. ./exec.sh 

    ##### Cleanup: 
        1. cd templates
        2. terraform destroy
        3. Make sure to cleanup S3(& optionally DynamoDB table) data before destroying     


### Testing

#### Using AWS Console
   1. Open AWS Console > Lambda > productsmocksavesvc-lambda
   2. Configure test event. ex: {"productid":"1","productname":"iPhone","productversion":"10R"}
   3. Click Test
   4. Open AWS Console > Lambda > productsmockgetsvc-lambda
   2. Configure test event. ex: {"productid":"1","productname":"iPhone","productversion":"10R"}
   3. Click Test

#### Using CLI

$ aws lambda invoke --function-name "staticmethodsvc-lambda" --cli-binary-format raw-in-base64-out  --payload '{"productId":"1","productName":"iPhone","productVersion":"10R"}'  "output.txt"

$ cat output.txt

