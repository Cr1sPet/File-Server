
sed -i 's/server_port/${server_port}/g' ./server/src/main/resources/server-app.properties

sed -i 's/server_data_dir/${server_data_dir}/g' ./server/src/main/resources/server-app.properties

sed -i 's/server_serialization_filepath/${server_serialization_filepath}/g' ./server/src/main/resources/server-app.properties

mkdir -p ${server_data_dir}/files

cd ./server &&  mvn clean package &&  java -jar ./target/server-1.0-SNAPSHOT.jar