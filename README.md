[![Java CI with Maven](https://github.com/Brest-Java-Course-2021-2/Dzmitry-Kalustau-EH/actions/workflows/maven.yml/badge.svg)](https://github.com/Brest-Java-Course-2021-2/Dzmitry-Kalustau-EH/actions/workflows/maven.yml)

# Expenses Helper
Simple home assistant for keeping and accounting of expenses

## Idea
The main idea of this application is to keep track of expenses and display detailed statistics for specific categories in order to optimize costs in the future.
For more detailed information: [Software requirements specification](documentation/srs)

## Requirements
* JVM 11+
* Apache Maven
* Git

## How to start
#### Download project to your pc
git clone https://github.com/Brest-Java-Course-2021-2/Dzmitry-Kalustau-EH.git
#### Install the project by running the following command:
mvn clean install
#### Run the rest application on port 8088
java -jar rest-app/target/rest-app-1.0-SNAPSHOT.jar
#### Run the web application on port 8080
java -jar web-app/target/web-app-1.0-SNAPSHOT.jar 

In your browser, go to http://localhost:8080.