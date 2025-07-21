#!/bin/bash

echo "--------------- 클라우드 워치 에이전트 시작 명령 -----------------"
/opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -s -c file:/opt/aws/amazon-cloudwatch-agent/bin/config.json
echo "--------------- 클라우드 워치 에이전트 시작 명령 끝 -----------------"
