# To-Do REST API

In this application I have developed the REST API for a To-Do application. The basic features are;

- Users can be registered by `/register` endpoint
- Users authenticated with Http Basic Auth.
- Users can create their own To-Do records.
- Users can add multiple tags to the To-Do records.
- Users can list their To-Do records and filter To-Do records by To-Do tags.
- Users can update To-Do records.
- Users can delete To-Do records.
- Users can update their own personal data through `/user` endpoint.

More information about endpoints can be found at this Postman documentation: https://documenter.getpostman.com/view/14145413/TzCTZ5bv

## MVC Alternative to this project (Taskcase)
I also developed an Spring MVC application which has a use case very similar. The project respository can be found at: [https://github.com/umutalacam/taskcase](URL)

## Docker Image Installation
The project repository also includesThis a directory such as docker-build. In order to build a docker image;

1. Enter the docker-build directory  
    `cd docker-build`

2. Build docker image  
    `docker build -t todobase .`

3. Run docker image.  
`    docker run -it -p 8080-8099:8080-8099 todobase`

4. Reach api endpoints through localhost:8080

**Note:** While running the docker image, configuration script will launch couchbase server and install openjdk-8 in order to launch SpringBootApplication. This takes a while.
