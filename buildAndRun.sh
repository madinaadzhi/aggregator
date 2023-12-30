apt install git -y
apt install mysql-server -y
mysql -e "create database aggDB"
mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'rootroot';"
apt install openjdk-17-jre --fix-missing -y
git clone https://github.com/madinaadzhi/aggregator.git
cd aggregator
./mvnw package
java -jar target/productAggregator-0.0.1-SNAPSHOT.jar
java -cp target/productAggregator-0.0.1-SNAPSHOT.jar -Dloader.main=org.madi.productaggregator.ImportMarket org.springframework.boot.loader.PropertiesLauncher shop.fora.ua
