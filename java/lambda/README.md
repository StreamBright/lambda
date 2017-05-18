```
mvn dependency:tree -Dverbose=true
```
```
[INFO] com.streambright:dbmgmt:jar:1.0
[INFO] +- junit:junit:jar:3.8.1:test
[INFO] +- com.amazonaws:aws-lambda-java-core:jar:1.1.0:compile
[INFO] +- com.amazonaws:aws-lambda-java-events:jar:1.3.0:compile
[INFO] |  +- com.amazonaws:aws-java-sdk-s3:jar:1.11.0:compile
[INFO] |  |  +- (com.amazonaws:aws-java-sdk-kms:jar:1.11.0:compile - omitted for conflict with 1.11.128)
[INFO] |  |  \- (com.amazonaws:aws-java-sdk-core:jar:1.11.0:compile - omitted for conflict with 1.11.128)
[INFO] |  +- com.amazonaws:aws-java-sdk-sns:jar:1.11.0:compile
[INFO] |  |  +- com.amazonaws:aws-java-sdk-sqs:jar:1.11.0:compile
[INFO] |  |  |  \- (com.amazonaws:aws-java-sdk-core:jar:1.11.0:compile - omitted for duplicate)
[INFO] |  |  \- (com.amazonaws:aws-java-sdk-core:jar:1.11.0:compile - omitted for duplicate)
[INFO] |  +- com.amazonaws:aws-java-sdk-cognitoidentity:jar:1.11.0:compile
[INFO] |  |  \- (com.amazonaws:aws-java-sdk-core:jar:1.11.0:compile - omitted for duplicate)
[INFO] |  +- com.amazonaws:aws-java-sdk-kinesis:jar:1.11.0:compile
[INFO] |  |  \- (com.amazonaws:aws-java-sdk-core:jar:1.11.0:compile - omitted for duplicate)
[INFO] |  \- com.amazonaws:aws-java-sdk-dynamodb:jar:1.11.0:compile
[INFO] |     +- (com.amazonaws:aws-java-sdk-s3:jar:1.11.0:compile - omitted for duplicate)
[INFO] |     \- (com.amazonaws:aws-java-sdk-core:jar:1.11.0:compile - omitted for duplicate)
[INFO] +- com.amazonaws:aws-lambda-java-log4j:jar:1.0.0:compile
[INFO] |  +- (com.amazonaws:aws-lambda-java-core:jar:1.1.0:compile - omitted for duplicate)
[INFO] |  \- log4j:log4j:jar:1.2.17:compile
[INFO] \- com.amazonaws:aws-java-sdk-kms:jar:1.11.128:compile
[INFO]    +- com.amazonaws:aws-java-sdk-core:jar:1.11.128:compile
[INFO]    |  +- commons-logging:commons-logging:jar:1.1.3:compile
[INFO]    |  +- org.apache.httpcomponents:httpclient:jar:4.5.2:compile
[INFO]    |  |  +- org.apache.httpcomponents:httpcore:jar:4.4.4:compile
[INFO]    |  |  +- (commons-logging:commons-logging:jar:1.2:compile - omitted for conflict with 1.1.3)
[INFO]    |  |  \- commons-codec:commons-codec:jar:1.9:compile
[INFO]    |  +- software.amazon.ion:ion-java:jar:1.0.2:compile
[INFO]    |  +- com.fasterxml.jackson.core:jackson-databind:jar:2.6.6:compile
[INFO]    |  |  +- com.fasterxml.jackson.core:jackson-annotations:jar:2.6.0:compile
[INFO]    |  |  \- com.fasterxml.jackson.core:jackson-core:jar:2.6.6:compile
[INFO]    |  +- com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:jar:2.6.6:compile
[INFO]    |  |  \- (com.fasterxml.jackson.core:jackson-core:jar:2.6.6:compile - omitted for duplicate)
[INFO]    |  \- joda-time:joda-time:jar:2.8.1:compile
[INFO]    \- com.amazonaws:jmespath-java:jar:1.11.128:compile
[INFO]       \- (com.fasterxml.jackson.core:jackson-databind:jar:2.6.6:compile - omitted for duplicate)
```
