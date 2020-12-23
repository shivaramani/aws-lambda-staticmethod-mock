    SAVE_FUNCTION_NAME=staticmethodsvc-lambda
    DEPLOY_JAR=./target/staticmethodsvc-1.0-SNAPSHOT.jar
    DEPLOY_REGION=us-east-1

    # Build new Jar
    mvn clean package

    cd templates

    terraform init

    terraform apply --auto-approve

    cd ..
    
    # Update the new function and publish a new lambda version
    aws lambda --region $DEPLOY_REGION update-function-code --function-name $SAVE_FUNCTION_NAME --publish --zip-file fileb://$DEPLOY_JAR 

     