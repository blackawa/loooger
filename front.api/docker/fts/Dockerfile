FROM docker.elastic.co/elasticsearch/elasticsearch-oss:6.1.2
ADD elasticsearch.yml /usr/share/elasticsearch/config/
ADD entrypoint.sh /usr/local/bin/fts-entrypoint.sh
RUN chmod 755 /usr/local/bin/fts-entrypoint.sh
ENTRYPOINT ["/usr/local/bin/fts-entrypoint.sh"]
