#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop dev-talk-server || true
docker rm dev-talk-server || true
docker pull 145023108970.dkr.ecr.ap-northeast-2.amazonaws.com/dev-talk-server/dev-talk-server:latest
docker run -d --name dev-talk-server -p 8080:8080 145023108970.dkr.ecr.ap-northeast-2.amazonaws.com/dev-talk-server/dev-talk-server:latest
echo "--------------- 서버 배포 끝 -----------------"
