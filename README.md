## Palindrome game
This is a backend service for palindrome game, implemented using Java, Spring Boot, PostgreSQL, and Docker. 

### Prerequisites
You need to have Docker and Maven (also provided in the repo as Maven Wrapper).

### Installation
1. Clone the repo
   ```sh
   git clone https://github.com/github_username/repo_name.git
   ```
2. Build the project.
   ```sh
   mvn clean install
   ```
   This command compiles the project and runs tests (both unit and end-to-end) against an embedded H2 database. 
2. Run the project inside Docker.
   ```sh
   docker-compose up --build
   ```
   **Note: for next runs, remove ```--build``` argument.**
   This command starts Spring boot application on port 8080 connected to PostgreSQL (Dockerized). 

## API usage
**Swagger UI**:
Once the project is up and running, you can find Swagger UI at ```http://localhost:8080/swagger-ui/```.
**Note: request samples below are in [HTTPie](https://httpie.io/) format.**
1. Register a user, ```http POST http://localhost:8080/register username=John password=123 role=PLAYER```
2. Login with your user, ```http POST http://localhost:8080/login username=John password=123```
3. Submit a word, ```http POST http://localhost:8080/submit Authorization:"Bearer [your-token]" word=Wow```
4. Check your leader board, ```http GET http://localhost:8080/leaderboard/me Authorization:"Bearer [your-token]"```
5. Check overall leader board, ```http GET http://localhost:8080/leaderboard Authorization:"Bearer [your-token]"```

**Authorization**:
After a successful login, you will receive a JWT token with 1 hour TTL (as field ```bearerToken```). 
You need to provide this token in your consecutive requests as "Authorization" header with the 
value "Bearer [you-token]". The exact millisecond of expiration is also returned at login 
time (as field ```jwtExpirationDateInMilliseconds```). For Authorization, I have leveraged Spring security.

## Improvement ideas
**Scoring function**:
The current implementation rewards a word base on the distinct characters appearing in the word. For example word
"Rotator" gets 4, "wow" gets 2 and "xxxxxx" gets 1 and of course non-palindrome words get 0. 
It can be improved by integrating to a dictionary service, so that made-up words (such as "abcba") that currently get a 
positive score (3) will get 0 point.
 
**Logging**:
There is lots of room for improvement on logging. For example logging the request and response body, etc.

**Controllers design**:
In current implementation we have a single controller (PalindromeController). We can move endpoints to different 
controllers, for example user-related endpoints to UserController and so forth.  

**Word validation**:
A new submitted word gets validated on the controller with a regex. 
It can be better designed in a separate module/package.