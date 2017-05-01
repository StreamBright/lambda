aws --profile be-istvan --region eu-central-1 lambda create-function \
  --function-name dbmgmt \
  --role arn:aws:iam::812087793187:role/service-role/lambda-rds-update \
  --handler dbmgmt \
  --description "Simple Lambda for RDS changes" \
  --timeout 10 \
  --memory 256 \
  --runtime java8 \
  --zip-file fileb://./target/dbmgmt-0.1.0-standalone.jar

#   create-function
# --function-name <value>
# --runtime <value>
# --role <value>
# --handler <value>
# [--code <value>]
# [--description <value>]
# [--timeout <value>]
# [--memory-size <value>]
# [--publish | --no-publish]
# [--vpc-config <value>]
# [--dead-letter-config <value>]
# [--environment <value>]
# [--kms-key-arn <value>]
# [--zip-file <value>]
# [--cli-input-json <value>]
# [--generate-cli-skeleton <value>]

# {
#     "CodeSha256": "ZY9El4PVNoh4/TR7WUasNGpLowHigmZzzS85mFKzSnM=",
#     "FunctionName": "dbmgmt",
#     "CodeSize": 4373647,
#     "MemorySize": 128,
#     "FunctionArn": "arn:aws:lambda:eu-central-1:812087793187:function:dbmgmt",
#     "Version": "$LATEST",
#     "Role": "arn:aws:iam::812087793187:role/service-role/lambda-rds-update",
#     "Timeout": 100,
#     "LastModified": "2017-04-27T11:02:43.210+0000",
#     "Handler": "dbmgmt::handler",
#     "Runtime": "java8",
#     "Description": ""
# }
