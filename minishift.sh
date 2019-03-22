#!/usr/bin/env bash

checkOcDoesNotExist () {
    if command_exists oc;
        then
            return 1
        else
            return 0
    fi
}

command_exists () {
    type "$1" &> /dev/null ;
}

ZOMATO_KEY=$1
if [[ $# -eq 0 ]]
  then
    echo "WARNING! You didn't set Zomato API key"
fi

echo "[DEBUG] ZOMATO KEY > " ${ZOMATO_KEY}

if checkOcDoesNotExist
    then
        echo "oc is not correctly set up, trying to fix it ..."
        eval $(minishift oc-env)
        if checkOcDoesNotExist ; then
            echo "The fix was not successful. Please set up oc client correctly"
            exit
        fi
        echo "Successfully fixed"
fi

read -p "Do you want to start with a clean MiniShift? It takes some time and deletes whole MiniShift instance! (y/n)" CREATE_OC
if [[ "$CREATE_OC" = "y" ]]; then
  minishift delete
  minishift start
else
  read -p "Are want to delete old project with daily menu services?(y/n)" DELETE_PROJECT
  if [[ "$DELETE_PROJECT" = "y" ]]; then
    oc delete project dailymenupicker
  else
    echo "See you later!"
    exit
  fi
fi


echo "Creating new project"
oc new-project dailymenupicker

echo "Building and deploying daily menu services"
# Deploy the app
cd services
oc new-build --binary --name=dailymenu-services -l app=dailymenu-services
oc set env bc dailymenu-services ZOMATO_API_KEY=${ZOMATO_KEY}
oc start-build dailymenu-services --from-dir=. --follow
oc new-app dailymenu-services -l app=dailymenu-services
oc expose service dailymenu-services

echo "You can check health on: http://dailymenu-services-dailymenupicker.`minishift ip`.nip.io/health"

echo "Opening minishift console"
minishift console &
exit
