set -m
username=admin;
password=123456;

echo "Setup couchbase server.."
/entrypoint.sh couchbase-server &

sleep 15

curl -v -X POST http://127.0.0.1:8091/pools/default -d memoryQuota=512 -d indexMemoryQuota=512

curl -v http://127.0.0.1:8091/node/controller/setupServices -d services=kv%2cn1ql%2Cindex

curl -v http://127.0.0.1:8091/settings/web -d port=8091 -d username=$username -d password=$password

curl -i -u $username:$password -X POST http://127.0.0.1:8091/settings/indexes -d 'storageMode=memory_optimized'

curl -v -u $username:$password -X POST http://127.0.0.1:8091/pools/default/buckets -d name=ToDo -d bucketType=couchbase -d ramQuotaMB=128

apt update

echo "Installing openjdk-8 package"
apt install -y openjdk-8-jre
echo "Installed java 8"

curl -v -u admin:123456 http://127.0.0.1:8093/query/service  -d 'statement=CREATE INDEX ownerId ON ToDo(ownerId)'

curl -v -u admin:123456 http://127.0.0.1:8093/query/service  -d 'statement=CREATE INDEX username ON ToDo(username)'

curl -v -u admin:123456 http://127.0.0.1:8093/query/service  -d 'statement=CREATE INDEX type ON ToDo(type)'

java -jar /opt/couchbase/todo-0.0.3.jar

