#!/bin/bash

mkdir /usr/share/elasticsearch/data/esdata
chown -R elasticsearch:elasticsearch /usr/share/elasticsearch/data/esdata
/usr/local/bin/docker-entrypoint.sh
