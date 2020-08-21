#!/bin/bash

declare -a TENANTS=(
"IBM (India)"
"Dell (Singapore)"
"Oracle Corp."
"Rehmans Restaurant"
)

for tenant in "${TENANTS[@]}"
do
  echo "creating ${tenant}"
  RESP=$(curl -s localhost:6060/newtenant -H "Content-Type: application/json" -d "{\"name\":\"${tenant}\"}")
  if [ "$RESP" == "true" ]
  then
    echo Ok
  else
    echo Failed
  fi
done
