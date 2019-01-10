#!/bin/bash
# 业务镜像 bsimage/rdm_common_resin_mongo_zookeeper_amq_ant__mysql_5_6_39_2:20180507202940
export SERVER_NAME="cham.TafTemplateServer"
rm  /usr/local/app/taf -r
mkdir  /usr/local/app/taf/${SERVER_NAME} -p
mkdir  /usr/local/app/taf/${SERVER_NAME}/bin
mkdir  /usr/local/app/taf/${SERVER_NAME}/conf
mkdir  /usr/local/app/taf/${SERVER_NAME}/data

mkdir /usr/local/app/taf/tmp
cd /usr/local/app/taf/tmp
unzip ${PACKAGE_NAME}
mv /usr/local/app/taf/tmp/TafTemplateServer/* /usr/local/app/taf/${SERVER_NAME}/bin


#ci依赖文件
cp /usr/local/app/taf/tmp/taskId /usr/local/app
cp /usr/local/app/taf/tmp/version.properties /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/

chmod +x  /usr/local/app/*.sh
