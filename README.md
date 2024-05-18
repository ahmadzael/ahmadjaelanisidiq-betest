# User API Spec

## Register User

- Endpoint : POST /api/users

Request Body:

```json
{
  "username": "",
  "password": "",
  "name": ""
}
```

Response Body (Success):

```json
{
  "data": "ok"
}
```

Response Body (Failed,401):

```json
{
  "errors": "username or password wrong"
}
```

## Login User

- Endpoint : POST /api/auth/login

Request Body:

```json
{
  "username": "ahmad",
  "password": "rahasia"
}
```

Response Body (Success):

```json
{
  "data": {
    "token": "jwttoken",
    "expiredat": 2312121
  }
}
```

Response Body (Failed,401):

```json
{
  "errors": "username or password wrong"
}
```

## Get User

- Endpoint : GET /api/users/current

Request Header :

- X-API-TOKEN: Token {Mandatory}

Response Body (Success):

```json
{
  "data": {
    "username": "ahmadjaelani",
    "name": "Ahmad Jaelani Sidiq"
  }
}
```

Response Body (Failed,401):

```json
{
  "errors": "Unauthorized"
}
```

## Update User

Request Header :

- X-API-TOKEN: Token {Mandatory}

- Endpoint : PATCH /api/users/current

Request Body:

```json
{
  "name": "Ahmad jaelani sidiq",
  "password": "new password"
}
```

Response Body (Success):

```json
{
  "data": {
    "username": "ahmadjaelani",
    "name": "Ahmad Jaelani Sidiq"
  }
}
```

Response Body (Failed,401):

```json
{
  "errors": "Unauthorized"
}
```


## Logout User

Endpoint : DELETE /api/auth/logout

Request Header :

- X-API-TOKEN: Token {Mandatory}
  Response Body (Success):

```json
{
  "data": "sucess"
}
```




# Account API Spec

## Create Account

Endpoint: POST /api/contact/{idContact}/addresses

Request Header :

- X-API-TOKEN: Token {Mandatory}
  Response Body (Success):

Request Body:
```json
{
 "street": "jalan bintaro",
  "city": "Tangerang Selatan",
  "prvince": "Banten",
  "country": "Indonesia",
  "postalCode": "123456"
}
```

Response Body (Success):
```json
{
  "id": "stringID",
 "street": "jalan bintaro",
  "city": "Tangerang Selatan",
  "prvince": "Banten",
  "country": "Indonesia",
  "postalCode": "123456"
}
```

Response Body (Failed):
```json
{
  "errors": "Contact is not found" 
}
```

## Update Account

Endpoint: PUT /api/contact/{idContact}/addresses/{idAddress}

Request Header :

- X-API-TOKEN: Token {Mandatory}
  Response Body (Success):

Request Body:
```json
{
  "id": "stringID",
  "street": "jalan bintaro",
  "city": "Tangerang Selatan",
  "prvince": "Banten",
  "country": "Indonesia",
  "postalCode": "123456"
}
```

Response Body (Success):
```json
{
  "id": "stringID",
  "street": "jalan bintaro",
  "city": "Tangerang Selatan",
  "prvince": "Banten",
  "country": "Indonesia",
  "postalCode": "123456"
}
```
Response Body (Failed):
```json
{
  "errors": "error on update data" 
}
```

## Get Account

Endpoint: GET /api/contact/{idContact}/addresses/{idAddresses}

Request Header :

- X-API-TOKEN: Token {Mandatory}
  Response Body (Success):

Request Body:

Response Body (Success):
```json
{
  "id": "stringID",
  "street": "jalan bintaro",
  "city": "Tangerang Selatan",
  "prvince": "Banten",
  "country": "Indonesia",
  "postalCode": "123456"
}
```
Response Body (Failed):
```json
{
  "errors": "addresses not found" 
}
```

## List Account

Endpoint: DELETE /api/contact/{idContact}addresses/{idAddresses}

Request Header :

- X-API-TOKEN: Token {Mandatory}
  Response Body (Success):

Request Body:

Response Body (Success):
```json
{
  "data": "success" 
}
```

Response Body (Failed):
```json
{
  "errors": "error on delete addresses" 
}
```

Response Body (Success):
```json
{
  "data": [
    {
      "id": "stringID",
      "street": "jalan bintaro",
      "city": "Tangerang Selatan",
      "prvince": "Banten",
      "country": "Indonesia",
      "postalCode": "123456"
  }
  ]
}
```
Response Body (Failed):
```json
{
  "errors": "addresses not found" 
}
```
