#!/bin/sh

# GCEの中に入って叩いた。
# 本当はコメントアウトされた方のコマンドを叩けた方がよかった。
# kubelet --experimental-allowed-unsafe-sysctls 'vm.max_map_count=262144'
sudo /usr/sbin/sysctl -w vm.max_map_count=262144
