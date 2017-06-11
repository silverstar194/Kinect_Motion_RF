## Compares costs of instances in different AWS regions

for reg in Frankfurt Ireland N.-California N.-Virginia Oregon Sao-Paulo Singapore Tokyo Seoul Sydney
do
echo -e "\n\nRegion = $reg"

case "$reg" in
Frankfurt) export AWS_DEFAULT_REGION=eu-central-1
;;
Ireland) export AWS_DEFAULT_REGION=eu-west-1
;;
N.-California) export AWS_DEFAULT_REGION=us-west-1
;;
N.-Virginia) export AWS_DEFAULT_REGION=us-east-1
;;
Oregon) export AWS_DEFAULT_REGION=us-west-2
;;
Sao-Paulo) export AWS_DEFAULT_REGION=sa-east-1
;;
Singapore) export AWS_DEFAULT_REGION=ap-southeast-1
;;
Tokyo) export AWS_DEFAULT_REGION=ap-northeast-1
;;
Seoul) export AWS_DEFAULT_REGION=ap-northeast-2
;;
Sydney) export AWS_DEFAULT_REGION=ap-southeast-2
;;
esac

aws ec2 describe-spot-price-history --product-description "Linux/UNIX" --instance-types $1 --start-time `date -u --date="7 days ago" +'%Y-%m-%dT%H:%M:00'` | jq -r -c '.SpotPriceHistory[] | "(.Timestamp),(.SpotPrice)"'
done