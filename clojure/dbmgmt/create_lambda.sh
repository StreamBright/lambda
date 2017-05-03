aws --profile be-istvan --region eu-central-1 lambda create-function \
  --function-name dbmgmt \
  --runtime java8 \
  --role arn:aws:iam::812087793187:role/lambda-dbmgmt \
  --handler dbmgmt::handler \
  --description "Simple Lambda for RDS changes" \
  --timeout 10 \
  --memory 256 \
  --vpc-config "SubnetIds=subnet-878200fd,subnet-849ce6ec,SecurityGroupIds=sg-a80db5c3" \
  --kms-key-arn arn:aws:kms:eu-central-1:812087793187:key/05c8687a-0d2d-4e4f-8a5a-5de55ea457b8 \
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
