#!/bin/bash
export SERVER_NAME="cham.TafTemplateServer"
defaultIP=`/sbin/ifconfig eth1 | grep inet | awk -F':' '{print $2}' | awk -F' ' '{print $1}'`

main_port=$main_port
http_port=$http_port
tafPort1=$tafport1
tafPort2=$tafport2
jacocoPort=$jacocoPort
defaultAMQPort=$defaultAMQPort
AMQPort1=$AMQPort1
AMQPort2=$AMQPort2
defaultMysqlPort=$defaultMysqlPort
jmxPort=$jmxremotePort

#amq
sed -i "s/defaultAMQPort/${defaultAMQPort}/" /data/tools/apache-activemq-5.10.0/conf/jetty.xml
sed -i "s/defaultIP/${defaultIP}/" /data/tools/apache-activemq-5.10.0/conf/jetty.xml
sed -i "s/defaultIP/${defaultIP}/" /data/tools/apache-activemq-5.10.0/conf/activemq.xml
sed -i "s/defaultPort/${defaultAMQPort}/" /data/tools/apache-activemq-5.10.0/conf/activemq.xml
sed -i "s/61616/${AMQPort1}/" /data/tools/apache-activemq-5.10.0/conf/activemq.xml
sed -i "s/61615/${AMQPort2}/" /data/tools/apache-activemq-5.10.0/conf/activemq.xml

sed -i "s/defaultAmqPort/${defaultAMQPort}/" /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/application.properties
sed -i "s/defaultIP/${defaultIP}/" /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/application.properties

#mysql
sed -i "s/defaultMysqlPort/${defaultMysqlPort}/" /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/application.properties
sed -i "s/defaultMysqlPort/${defaultMysqlPort}/" /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/quartz.properties
sed -i "s/6666/${defaultMysqlPort}/" /etc/my.cnf
sed -i "s/10.242.132.27/127.0.0.1/" /etc/my.cnf

#http请求
sed -i "s/defaultIP/${defaultIP}/" /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/shiro.ini
sed -i "s/http_port/${http_port}/" /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/shiro.ini

sed -i "s/defaultIP/${defaultIP}/" /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/application.properties
sed -i "s/http_port/${http_port}/" /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/application.properties

#copy 配置文件到 bin/conf 目录
cp /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/application.properties /usr/local/app/taf/${SERVER_NAME}/bin/conf/application.properties
cp /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/shiro.ini /usr/local/app/taf/${SERVER_NAME}/bin/conf/shiro.ini
cp /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/log4j.properties /usr/local/app/taf/${SERVER_NAME}/bin/conf/log4j.properties

#taf相关
cp /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/taf.config.conf /usr/local/app/taf/${SERVER_NAME}/conf/taf.config.conf
cp /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/start_taf.sh /usr/local/app/start_taf.sh
cp /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/stop_taf.sh /usr/local/app/stop_taf.sh

sed -i "s/main_port/${main_port}/g"   /usr/local/app/taf/${SERVER_NAME}/conf/taf.config.conf
sed -i "s/http_port/${http_port}/g"   /usr/local/app/taf/${SERVER_NAME}/conf/taf.config.conf
sed -i "s/tafPort1/${tafPort1}/g"   /usr/local/app/taf/${SERVER_NAME}/conf/taf.config.conf
sed -i "s/tafPort2/${tafPort2}/g"   /usr/local/app/taf/${SERVER_NAME}/conf/taf.config.conf
sed -i "s/defaultIp/${defaultIP}/g"   /usr/local/app/taf/${SERVER_NAME}/conf/taf.config.conf
sed -i "s/jacocoPort/${jacocoPort}/" /usr/local/app/taf/${SERVER_NAME}/conf/taf.config.conf

sed -i "s/jacocoPort/${jacocoPort}/" /usr/local/app/start_taf.sh
sed -i "s/jmxPort/${jmxPort}/" /usr/local/app/start_taf.sh

#测试执行脚本
cp /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/build_test.sh /usr/local/app/build.sh

sed -i "s/self_test_port/${main_port}/" /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/build_test.xml
sed -i "s/self_test_ip/${defaultIP}/" /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/build_test.xml
sed -i "s/jacocoPort/${jacocoPort}/" /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/build_test.xml

ln -s /data/tools/apache-ant-1.9.7/bin/ant /usr/bin/ant

ln -s /data/tools/jdk1.8.0_45 /usr/local/jdk

ln -s /dockerdata /usr/local/app/taf/app_log

chmod +x  /usr/local/app/*.sh
