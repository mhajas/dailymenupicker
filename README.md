# Daily menu picker

Run `mvn clean install` for install parser to repository

Run `mvn clean package`. An service jar files will be built in your target folders.

### How to run

You need to have Zomato API key for some parsers!

Projects use Lombok framework. You have to install [plugin](https://plugins.jetbrains.com/plugin/6317-lombok-plugin) for IntelliJ IDEA.

For rest testing, you can run `java -jar ./services/target/dailymenu-services-thorntail.jar`

Health check:
[http://localhost:8080/health](http://localhost:8080/health)
Zomato check:
[http://localhost:8080/restaurant/ChIJQ7t5NgeUEkcRfF9rW4VCrjA](http://localhost:8080/restaurant/ChIJQ7t5NgeUEkcRfF9rW4VCrjA)


### Docker

Make sure that you have installed and configured the Docker.

You can use predefined docker-compose file which builds an image and runs the container. First you have to add your
own Zomato key to the `dailymenu-picker-env.env` file. 

After that, run

`docker-compose up`

---

If you want more digging, you can do it manually:

1. Build image:
   
  * Manually:
    
    Run `docker build --build-arg ZOMATO_API_KEY=<YOUR-KEY> -t dailymenu-services-dockerfile:latest .`

2. Verify that image is in the repository

	`docker image ls` - you should see `dailymenu-services-dockerfile`

3. Run image
	
    `docker run -d -p 8080:8080 dailymenu-services-dockerfile`

####Docker cheat-sheet
* Show all containers - `docker ps`
* Show log - `docker logs <containerID>`
* Connect to container - `docker exec -it <container name> /bin/bash`
* Stop container - `docker kill <containerID>`
* Delete all containers - `docker rm $(docker ps -a -q)`
* Delete all images - `docker rmi $(docker images -q)`

### Minishift

Make sure that you have installed minishift and oc client (`eval $(minishift oc-env)`)

Start minishift `minishift start`

Connect to minishift via client `oc login -u developer -p developer`

Run predefined script `./minishift.sh <ZOMATO_API_KEY>`

The app will be on the address `http://dailymenu-services-dailymenupicker.<minishift ip>.nip.io/`

e.g.
[http://dailymenu-services-dailymenupicker.192.168.42.179.nip.io/health](http://dailymenu-services-dailymenupicker.192.168.42.179.nip.io/health)

Note: You can use your own route (e.g. `https://dailymenuservice.io`) however you have to edit `/etc/hosts` after that!
