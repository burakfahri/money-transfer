# money-transfer 

[![CircleCI](https://circleci.com/gh/burakfahri/money-transfer.svg?style=svg)](https://circleci.com/gh/burakfahri/money-transfer) [![Build Status](https://travis-ci.org//burakfahri/money-transfer.svg?branch=master)](https://travis-ci.org/burakfahri/money-transfer) [![Coverage Status](https://coveralls.io/repos/github/burakfahri/money-transfer/badge.svg?branch=master)](https://coveralls.io/github/burakfahri/money-transfer?branch=master)

---
Money transfer project allows people the transfer money between accounts

## build

```
clean install compile assembly:single
```
## run

```
java -jar ~/.m2/repository/pl/com/revolut/money-transfer-main/1.0/money-transfer-main-1.0-jar-with-dependencies.jar
```
## logs file
```
./logs/money-transfer
```
### Links

What things you need to install the software and how to install them

```
Give examples
```

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
- 201 OK: The request has created
- 202 OK: The request has accepted
- 400 Bad Request: The request could not be understood by the server 
- 404 Not Found: The requested resource cannot be found
- 500 Internal Server Error: The server encountered an unexpected condition 

### Sample Jsons

####customer
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
####account
```
{
  "openDate": "Sep 10, 2018 6:24:55 PM",
  "currentBalance": 100,
  "customerId": {
    "id": "CUS-4"
  }
}
```
####transaction
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