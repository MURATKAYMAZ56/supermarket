# Supermarket API Documentation
This documentation provides information on how to use the Supermarket API endpoints.

## Getting Started
To run the project locally, follow these steps:
1. Ensure that Java 17 is installed on your machine. You can download it from [here](https://www.oracle.com/java/technologies/downloads/#java17).
2. Clone this repository:
```
    git clone https://github.com/MURATKAYMAZ56/supermarket.git
```
3. Navigate to the project directory:
```
    cd supermarket
```
4. Build the project:
```
    ./mvnw clean install
```
5. Run the application:
```
    ./mvnw spring-boot:run
```


## Making API Calls via Postman
You can use Postman (or any other client ) to make API calls to expose endpoints. 

## Base URL
`http://localhost:8080/api/products`
### Endpoints
**1. Upload a File**

**URL**: `POST /`

**Description**: Upload a CSV file containing product information. You can use supermarket.csv file for test purposes.

**Parameters**: file (multipart file): CSV file containing product information.

**Example**: Use Postman or any REST client to make a POST request to the base URL with a multipart form-data body containing a CSV file.

**2. Get All Products**

**URL**: `GET /`
**Description**: Retrieve all products sorted by expiry date.

**Response**: A JSON array of product objects.

**Example**: Make a GET request to the base URL.

**3. Get Product by Barcode**

**URL**: `GET /{barcode}`

**Description**: Retrieve product information by its barcode.

**Parameters**: barcode (path parameter): The unique identifier of the product.

**Response**: A JSON object containing product details.

**Example**: Make a GET request to the base URL with the barcode appended to it.

**4. Delete Product by Barcode**

**URL**: `DELETE /{barcode}`

**Description**: Delete a product by its barcode.

**Parameters**: barcode (path parameter): The unique identifier of the product.

**Example**: Make a DELETE request to the base URL with the barcode appended

## Accessing H2 Console
Supermarket application uses an H2 in-memory database, and you can access the H2 Console to view and query the data.
1. Open your browser and go to `http://localhost:8080/h2-console`.
2. In the login page, set the following values:
```
    JDBC URL: jdbc:h2:mem:testdb
    User Name: sa
    Password: pwd
```
3.Click the "Connect" button.
4. You will be redirected to the H2 Console. Here, you can view and execute SQL queries against the in-memory database.
5. To see data Click on `PRODUCT` table and run below script.
```
    SELECT * FROM PRODUCT 
```
## Running Tests
To run tests, execute the following command:
```
    ./mvnw test
```


---


