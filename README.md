[![Java CI with Maven](https://github.com/Brest-Java-Course-2021-2/Dzmitry-Kalustau-EH/actions/workflows/maven.yml/badge.svg)](https://github.com/Brest-Java-Course-2021-2/Dzmitry-Kalustau-EH/actions/workflows/maven.yml)

# Expenses Helper
Simple home assistant for keeping and accounting of expenses

## Idea
The main idea of this application is to keep track of expenses and display detailed statistics for specific categories in order to optimize costs in the future.
For more detailed information: [Software requirements specification](documentation/srs)

## Requirements
* JDK 11+
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

## Available REST endpoints
You can only use the rest application. Use the following curl commands:
#### Categories
Get all categories:
```
curl --request GET 'http://localhost:8088/categories'
```
Get Category by Id:
```
curl --request GET 'http://localhost:8088/categories/1' | json_pp
```
Get Id of last Category:
```
curl --request GET 'http://localhost:8088/categories/last_id' | json_pp
```
Create new Category
```
curl --request POST 'http://localhost:8088/categories' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
"categoryId": "7",
"categoryName": "Fuel"
}'
```
Update Category
```
curl --request PUT 'http://localhost:8088/categories' \
--header 'Content-Type: application/json' \
--data-raw '{
"categoryId": "2",
"categoryName": "Gifts"
}'
```

#### Expenses
Get all expenses:
```
curl --request GET 'http://localhost:8088/expenses'
```
Get Expense by Id:
```
curl --request GET 'http://localhost:8088/expenses/1' | json_pp
```
Get Id of last Expense:
```
curl --request GET 'http://localhost:8088/expenses/last_id' | json_pp
```
Create new Expense
```
curl --request POST 'http://localhost:8088/expenses' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
"expenseId": "9",
"dateOfExpense": "2022-01-01",
"categoryId": "1",
"sumOfExpense": "50"
}'
```
Update Expense
```
curl --request PUT 'http://localhost:8088/expenses' \
--header 'Content-Type: application/json' \
--data-raw '{
"expenseId": "2",
"dateOfExpense": "2021-10-01",
"categoryId": "1",
"sumOfExpense": "1"
}'
```

#### Calculate Sum
Get all Categories with expenses:
```
curl --request GET 'http://localhost:8088/calculate-sum'
```
Get first and last dates from expenses:
```
curl --request GET 'http://localhost:8088/calculate-sum/dates'
```
Get total sum:
```
curl --request GET 'http://localhost:8088/calculate-sum/totalsum'
```
Add dates
```
curl --request POST 'http://localhost:8088/calculate-sum' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json' \
--data-raw '{
"dateFrom": "2021-10-01",
"dateTo": "2021-10-02"
}'
```