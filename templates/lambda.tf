resource "aws_lambda_function" "staticmethodsvc" {
  filename      = "${var.lambda_source_zip_path}"
  function_name = "${var.app_prefix}svc-lambda"
  role          = "${aws_iam_role.staticmethodsvc_lambda_role.arn}"
  handler       = "com.example.StaticMethodsHandler::handleRequest"
  runtime       = "java8"
  memory_size   = 2048
  timeout       = 300
  
  source_code_hash = "${filebase64sha256(var.lambda_source_zip_path)}"
  depends_on = ["aws_iam_role.staticmethodsvc_lambda_role"]

  environment {
    variables = {
      Env = "Dev"
      S3_BUCKET = "${aws_s3_bucket.s3_staticmethodsvc_bucket.bucket}"
      DATABASE_TABLE = "${aws_dynamodb_table.staticmethodsvc_table.name}"
      REGION = "${data.aws_region.current.name}"
    }
  }
}


output "staticmethodsvc" {
  value = "${aws_lambda_function.staticmethodsvc}"
}
