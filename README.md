

This repository contains source for a webservice built using Java/Spring-boot and hibernate framework based on REST architecture and Packer HCL

##Tools and Technologies

Infra	Tools/Technologies
Webapp	Java, Maven, Spring Boot
Github	Github Actions
Database	MySQL
PACKER
HCL
Packer
APIs
(1) Healthz API

Path: healthz
Parameters: None
Expected response: HTTP 200 OK
(2) Create User API


Auth: Basic auth (username/password)
Expected response:
HTTP 204 No Content indicating user details were updated
HTTP 401 Bad credentials if invalid username/password provided
HTTP 400 Bad Request if update user request is invalid
(4) Get User

Path: /v1/account/{userID}
HTTP Method GET
Parameters: None
Auth: Basic auth (username/password)
Expected response:
HTTP 200 OK indicating the user exists and details fetched successfully
{
   "id": "...",
   "first_name": "...",
   "last_name": "...",
   "username": "...",
   "account_created": "...",
   "account_updated": "..."
}
HTTP 401 Bad credentials if invalid username/password provided
(5) Upload Documents

Path: /v1/documents/
HTTP Method POST
Parameters: None
Auth: Basic auth (username/password)
Expected response:
HTTP 200 OK indicating the user exists and details fetched successfully
{
   "id": "...",
   "user_id": "...",
   "file_name": "...",
   "upload_date": "...",
   "url": "..."
}
(6) Get Documents

Path: /v1/documents/{doc_id}
HTTP Method GET
Parameters: None
Auth: Basic auth (username/password)
Expected response:
HTTP 200 OK indicating the user exists and details fetched successfully
{
   "id": "...",
   "user_id": "...",
   "file_name": "...",
   "upload_date": "...",
   "url": "..."
}
(7) Delete Documents

Path: /v1/document/{doc_id}
HTTP Method DELETE
Parameters: None
Auth: Basic auth (username/password)
Expected response:
HTTP 200 OK indicating the user exists and details fetched successfully
{}

(8) Verify User Email

Path: /v1/verifyUserEmail?email=user@example.com&token=sometoken
HTTP Method GET
Parameters: None
Auth: Basic auth (username/password)
Expected response:
HTTP 200 OK indicating the user email has been verified successfully and it is a part of our user gruop in DB
{}
Database
DB: MySQL
Schema:
Users Database
Password is stored using Bcrypt Hash + Salt
Web service configuration
Port Number: 8080
HTTP protocol
