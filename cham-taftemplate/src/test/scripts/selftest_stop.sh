#!/bin/bash
cd /usr/local/app
./stop_taf.sh

sleep 10

#AMQ
cd /data/tools/apache-activemq-5.10.0/bin
./stop.sh

sleep 10

#mysql
cd /usr/local/mysql/bin
#./stop.sh
mysqladmin -uroot shutdown
