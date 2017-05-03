for region in 'us-east-1' 'us-east-2' 'us-west-1' 'us-west-2' 'ca-central-1' 'eu-west-1' 'eu-central-1' 'eu-west-2' 'ap-southeast-1' 'ap-southeast-2' 'ap-northeast-2' 'ap-northeast-1' 'ap-south-1' 'sa-east-1'; do
  echo -n "$region :: ";
  aws --profile $1 \
    --region $region \
    ec2 describe-instances \
    --query 'Reservations[].Instances[].[Placement.AvailabilityZone,InstanceId,InstanceType,State.Name]';
done
aws --profile $1 \
  --region 'eu-central-1' \
  s3 ls

aws --profile $1 \
  --region 'eu-central-1' \
  iam list-users
