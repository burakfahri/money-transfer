# money-transfer 

[![CircleCI](https://circleci.com/gh/burakfahri/money-transfer.svg?style=svg)](https://circleci.com/gh/burakfahri/money-transfer) [![Build Status](https://travis-ci.org//burakfahri/money-transfer.svg?branch=master)](https://travis-ci.org/burakfahri/money-transfer) [![Coverage Status](https://coveralls.io/repos/github/burakfahri/money-transfer/badge.svg?branch=master)](https://coveralls.io/github/burakfahri/money-transfer?branch=master)

---
Money transfer project allows people the transfer money between accounts

## run
```
mvn spring-boot run
```
## logs file
```
./logs/money-transfer
```
## swagger docs
```
http://localhost:8080/swagger-ui.html#/
```
## architecture
![model architecture](/docs/model.png)
### Available Customer Services

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /customers/ | get all customers | 
| GET | /customers/{customerId} | get customer by id | 
| POST | /customers | create a new customer | 
| PUT | /customers/{customerId} | update a customer | 
| DELETE | /customers/{customerId} | remove customer | 

### Available Account Services

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /accounts/ | get all accounts | 
| GET | /accounts/{accountId} | get account by id | 
| POST | /accounts | create a new account | 
| PUT | /accounts/{accountId} | update an account | 
| DELETE | /accounts/{accountId} | remove an account | 

### Available Account Services

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /transactions/ | get all transactions | 
| GET | /transactions/{transactionId} | get transaction by id | 
| GET | /transactions/account/{accountId} | get transaction by account id |
| GET | /transactions/customer/{customerId} | get transaction by customer id | 
| POST | /transactions/account/{accountId}/withdraw/{amount} | withdraw money from account | 
| POST | /transactions/account/{accountId}/deposit/{amount} | deposit money from account | 
| POST | /transactions/transfer/from/{senderAccountId}/to/{receiverAccountId}/amount/{amount}/comment/{comment} | transfer money from one acount to another | 

### Http Status
- 200 OK: The request has succeeded
- 201 Created: The request has created
- 400 Bad Request: The request could not be understood by the server 
- 404 Not Found: The requested resource cannot be found
- 500 Internal Server Error: The server encountered an unexpected condition 

## Sample Jsons
___

### customer
```
{
  "customerName": "name",
  "customerSurname": "surname",
  "customerPhone": {
    "area": 530,
    "exch": 250,
    "ext": 400
  }
}
```
### account
```
{
  "openDate": "Sep 10, 2018 6:24:55 PM",
  "currentBalance": 100,
  "customerId": {
    "id": "CUS-4"
  }
}
```
### transaction
```
{
  "senderCustomerId": {
    "id": "CUS-4"
  },
  "receiverCustomerId": {
    "id": "CUS-4"
  },
  "date": "Sep 10, 2018 6:24:55 PM",  
  "currentBalance": 100,
  "balance": 100,
  "explanation": "EXPL",
  "TransactionType": "TRANSFER"
}
```

### example posts via curl

```
GET CUSTOMERS
curl -H "Content-Type:application/json" -X GET http://localhost:8080/customers | python -m json.tool

POST CUSTOMERS
curl -H "Content-Type:application/json" -X POST -d @customerEvent.json http://localhost:8080/customers

QUERY CUSTOMERS
curl -H "Content-Type:application/json" -X GET http://localhost:8080/customers/{CUS-1}

POST ACCOUNTS
curl -H "Content-Type:application/json" -X POST -d @accountEvent.json http://localhost:8080/accounts

GET ACCOUNTS
curl -H "Content-Type:application/json" -X GET http://localhost:8080/accounts | python -m json.tool

QUERY ACCOUNTS
curl -H "Content-Type:application/json" -X GET http://localhost:8080/accounts/{ACC-1}

WITHDRAW ACCOUNTS
curl -H "Content-Type:application/json" -X POST http://localhost:8080/transactions/account/{ACC-1}/withdraw/{amount}

DEPOSIT ACCOUNTS
curl -H "Content-Type:application/json" -X POST http://localhost:8080/transactions/account/{ACC-1}/deposit/{amount}

TRANSFER ACCOUNTS
curl -H "Content-Type:application/json" -X POST http://localhost:8080/transactions/transfer/from/{ACC-1}/to/{ACC-2}/amount/{amount}/comment/exmp

```