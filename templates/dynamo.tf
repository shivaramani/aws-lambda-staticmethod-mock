resource "aws_dynamodb_table" "staticmethodsvc_table" {
  name           = "${var.app_prefix}-${var.stage_name}-table-${data.aws_caller_identity.current.account_id}"
  hash_key       = "productId"
  billing_mode   = "PROVISIONED"
  read_capacity  = 5
  write_capacity = 5
  
  attribute {
    name = "productId"
    type = "S"
  }

  attribute {
    name = "productName"
    type = "S"
  }

  attribute {
    name = "productVersion"
    type = "S"
  }

  global_secondary_index {
    name               = "NameVersionIndex"
    hash_key           = "productName"
    range_key          = "productVersion"
    projection_type    = "INCLUDE"
    write_capacity     = 5
    read_capacity      = 5
    non_key_attributes = ["productId", "productName", "productVersion"]
 }

  tags = {
    Name        =  "${var.app_prefix}-dynamo=-table"
    Environment = "Dev"
  }
}