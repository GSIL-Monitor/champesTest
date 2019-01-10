#!/bin/bash
export SERVER_NAME="cham.TafTemplateServer"
TIME=`date -d "today" +"%Y%m%d_%H%M%S"`
echo "selftest start *_* "$TIME > /dockerdata/start_$TIME.log

. /etc/profile

#AMQ
cd /data/tools/apache-activemq-5.10.0/bin
nohup  ./start.sh  &>> /dockerdata/start_$TIME.log &
sleep 10

#mysql
if [  -d "/usr/local/mysql/bin" ]
then
       cd /usr/local/mysql/bin
       nohup  ./start.sh  &>> /dockerdata/start_$TIME.log &
fi;

sleep 10

nohup mysql -uroot -proot@2018 < /usr/local/app/taf/${SERVER_NAME}/bin/apps/ROOT/WEB-INF/classes/init_mysql.sql &>> /dockerdata/start_$TIME.log &
sleep 5


cd /usr/local/app
nohup ./start_taf.sh &>> /dockerdata/start_$TIME.log &

sleep 10

#运行中型测试
nohup ./build.sh &>> /dockerdata/start_$TIME.log &

endtime=`date "+%Y-%m-%d %H:%M:%S"`
echo "selftest end *_* "$endtime >> /dockerdata/start_$TIME.log
