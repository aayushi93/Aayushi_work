#! /bin/bash

cmd=$1
db_password=$2

case $cmd in 
	start)
		#Validating parameters to start docker
		if [ "$#" -ne 2 ]; 
		then
			echo "Invalid parameters!"
			exit 1
		fi

	#Start docker if docker service is not running
	sudo systemctl status docker || sudo systemctl start docker

	#Check if container is running
	if [ `sudo docker ps -f name=jrvs-psql | wc -l` -eq 2 ]
	then
		echo "Container - jrvs-psql is already running"
		exit 0
	fi

	#pull docker image
	sudo docker pull postgres

	#Check if volume is created. If it is not, create one.
	if [ -z `sudo docker volume ls | egrep "pgdata" | awk '{print $2}'` ]
	then
		sudo docker volume create pgdata
	fi

	#Set password using environment variable for defaut user
	export PGPASSWORD=$dbpassword

	#Check if container is created
	if [ `sudo docker container ls -a -f name=jrvs-psql | wc -l` -eq 1 ]
	then
		sudo docker run --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
		echo "jrvs-psql container has been created"
	fi

	#Run psql
	sudo docker container start jrvs-psql
	echo "jrvs-psql container is running"

	;;
stop)

	#Validating parameters to stop docker
	if [ "$#" -ne 1 ]
	then
		echo "Invalid parameters!"
		exit 1
	fi

	#Check if it is running
	if [ `sudo docker ps -f name=jrvs-psql |wc -l` -eq 1 ]
	then
		echo "The instance is not running"
		exit 1
	else
		#Stop docker container
		sudo docker container stop jrvs-psql
	fi
	;;

	#Other cases except start or stop
	*)
		echo "Invalid parameters!"
		exit 1
esac
exit 0
