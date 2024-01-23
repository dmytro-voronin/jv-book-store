<h1 id="start" style="text-align: center;">Online book store</h1>

<img src="icons/bookstorelogo.jpg" alt="Bookstore Logo" width="500" height="500" style="display: block; margin: auto;">

Welcome to the online bookstore management system—your reliable partner in the literary business!
Developed with the needs of book online store owners in mind,
this application provides efficient tools for managing your literary content.

##### Features:

- Easy to manage: Easy to search, add and update books,
  categories and orders - everything you need to effectively manage your store.

- Data Security: We recognize the importance of protecting your information.
  Our system uses strong authentication and authorization mechanisms with JWT tokens for secure transactions.
  The new application was created taking into account your convenience in managing an online book store,
  providing effective tools to grow your business.

##### History of creation:

I once encountered a problem: cool books are far away, and time is limited.
And then an idea appeared: why not create an application where you can buy a book directly online?
The whole point is that everyone can easily find and order interesting books without leaving home.
I was inspired by the desire to make reading accessible to everyone, in any time and place.
Imagine, now your bookshelf is always at hand,
and you can easily find something new to read.
My application is like a store where everyone will find something to their liking.
So let's open the doors together to the fascinating world of words and adventures!



<p align="center">
  <a href="#technologies">Technologies</a> |
  <a href="#domain-models">Domain Models</a> |
  <a href="#user-roles">User Roles</a> |
  <a href="#database-structure">Database Structure</a> |
  <a href="#endpoints">Endpoints</a> |
  <a href="#getting-started">Getting Started</a> 
</p>

<h2 id="technologies"> Technologies</h2>
<ul style="list-style: none">
 <li><img src="icons/java.png" alt="" width="30" style="position: relative; top: 5px;"> Java 17 - is a version of the Java programming language that provides new features, improvements, and bug fixes.</li>
 <li><img src="icons/maven.png" alt="" width="30" style="position: relative; top: 10px;"> Maven - is a tool for managing dependencies in Java projects, as well as for automating the project build process.</li>
 <li><img src="icons/spring-logo.png" alt="" width="30" style="position: relative; top: 5px;"> Spring framework <i><small>(boot, data, security)</small></i> is a comprehensive framework for developing applications in Java. It includes various modules such as Spring Boot to simplify application creation, Spring Data to work with databases, and Spring Security to provide security. </li>
 <li><img src="icons/lombok.png" alt="" width="30" style="position: relative; top: 5px;"> Lombok - is a library for the Java language that allows you to reduce the amount of boilerplate code such as getters, setters and constructors.</li>
 <li><img src="icons/mapstruct.png" alt="" width="30" style="position: relative; top: 5px;"> MapStruct- is a Java code generation tool designed to convert objects between different types.</li>
 <li><img src="icons/mysql.png" alt="" width="30" style="position: relative; top: 5px;"> MySql 8 - is a relational database management system widely used for data storage.</li>
 <li><img src="icons/hibernate-logo.png" alt="" width="30" style="position: relative; top: 5px;"> Hibernate - is a framework for working with databases in Java, providing convenient tools for working with data and managing transactions.</li>
 <li><img src="icons/cta-icon.svg" alt="" width="30" style="position: relative; top: 5px;"> Liquibase - is a database change management tool that allows you to manage your database structure as code.</li>
 <li><img src="icons/junit5-logo.png" alt="" width="30" style="position: relative; top: 5px;"> JUnit5 <i><small>(+ Mockito)</small></i>  - these are libraries for testing in the Java language. JUnit5 provides the infrastructure for writing and running tests, and Mockito provides the infrastructure for creating mocks of objects..</li>
 <li><img src="icons/docker.png" alt="" width="30" style="position: relative; top: 5px;"> Docker - is a platform for developing, delivering and running applications in containers, making dependency management and deployment easier.</li>
 <li><img src="icons/Swagger-logo.png" alt="" width="30" style="position: relative; top: 5px;"> Swagger - is a tool for creating and documenting APIs. It allows you to automatically generate documentation based on the application source code.</li>
</ul>

<h2 id="domain-models"> Domain Models</h2>

#### User

* Attributes: Contains comprehensive information about registered users, including authentication details and personal
  information.
* Roles: Users are assigned roles, such as admin or regular user, defining their permissions and access levels.

#### Role

* Purpose: Represents the role of a user within the system, playing a crucial role in defining their access and actions.

#### Book

* Attributes: Represents detailed information about a book available in the store, including title, author, price, and
  category.

#### Category

* Purpose: Represents a category to which a book can belong, contributing to effective organization and user navigation.

#### ShoppingCart

* Purpose: Represents a user's shopping cart, capable of containing multiple items (CartItems).

#### CartItem

* Attributes: Represents an item in a user's shopping cart, linked to a specific book.

#### Order

* Purpose: Represents an order placed by a user, encapsulating OrderItems.

#### OrderItem

* Attributes: Represents an item in a user's order, associated with a specific book.


<h2 id="user-roles"> User Roles</h2>

#### Shopper (User)

* Actions: Capable of joining, signing in, exploring books, searching, managing the shopping cart, placing and reviewing
  orders, and accessing past receipts.

#### Manager (Admin)

* Actions: Empowered to arrange books, organize bookshelf sections, manage receipts, and modify their status.


<h2 id="database-structure"> Database structure</h2>

![bookstoredb.jpeg](icons%2Fbookstoredb.jpeg)

<h2 id="endpoints"> Endpoints</h2>

#### Authorization

| **HTTP method** | **Endpoint**       | **Role** | **Description**                                     |
|:----------------|:-------------------|----------|:----------------------------------------------------|
| POST            | /api/auth/register |          | Register a new user to the system                   |
| POST            | /api/auth/login    |          | Login with email and password. Response - JWT token |

#### Book management

| **HTTP method** | **Endpoint**      | **Role** | **Description**                                                   |
|:----------------|:------------------|----------|:------------------------------------------------------------------|
| GET             | /api/books        | USER     | Get all books per website pages                                   |
| GET             | /api/books/{id}   | USER     | Get the book by its id number                                     |
| GET             | /api/books/search | USER     | Search books by title and author *(titles=values&authors=values)* |
| POST            | /api/books        | ADMIN    | Create a new book                                                 |
| PUT             | /api/books/{id}   | ADMIN    | Update the book by its id number                                  |
| DELETE          | /api/books/{id}   | ADMIN    | Delete the book by its id number *(soft-delete)*                  |

#### Categories management

| **HTTP method** | **Endpoint**               | **Role** | **Description**                                      |
|:----------------|:---------------------------|----------|:-----------------------------------------------------|
| GET             | /api/categories            | USER     | Get all categories per website pages                 |
| GET             | /api/categories/{id}       | USER     | Get the category by its id number                    |
| GET             | /api/categories/{id}/books | USER     | Get list of books by the category by its id number   |
| POST            | /api/categories            | ADMIN    | Create a new category                                |
| PUT             | /api/categories/{id}       | ADMIN    | Update the category by its id number                 |
| DELETE          | /api/categories/{id}       | ADMIN    | Delete the category by its id number *(soft-delete)* |

#### Shopping cart management

| **HTTP method** | **Endpoint**              | **Role** | **Description**                                            |
|:----------------|:--------------------------|----------|:-----------------------------------------------------------|
| GET             | /api/cart                 | USER     | Get shopping cart                                          |
| POST            | /api/cart                 | USER     | Add a new book to shopping cart                            |
| PUT             | /api/cart/cart-items/{id} | USER     | Endpoint for updating quantity of an item in shopping cart |
| DELETE          | /api/cart/cart-items/{id} | USER     | Delete book from shopping cart by id                       |

#### Order management

| **HTTP method** | **Endpoint**                    | **Role** | **Description**                                                           |
|:----------------|:--------------------------------|----------|:--------------------------------------------------------------------------|
| POST            | /api/orders                     | USER     | Place an order based on your shopping cart, then shopping cart is deleted |
| GET             | /api/orders                     | USER     | Get all orders for user                                                   |
| GET             | /api/orders/{id}/items          | USER     | Get all order items by order id                                           |
| GET             | /api/orders/{id}/items/{itemId} | USER     | Get info about order item by order id and item id                         |
| PATCH           | /api/orders/{id}                | ADMIN    | Update order status for order by id                                       |


<h2 id="getting-started"> Getting Started</h2>

### Startup instructions

#### amazon cloud:

<img src="icons/aws.png" alt="Bookstore Logo" width="100" height="100" style="display: block; margin: auto;">

If you want to quickly test this application,
then you can not install it locally, but simply follow the link to the deployed application in Amazonaws.
The result will be exactly the same. 
[Swagger](http://ec2-54-91-229-61.compute-1.amazonaws.com:8081/api/swagger-ui/index.html#/) 
and 
[Postman](https://www.postman.com/downloads/) available.

#### easy way:

- your computer must have [docker](https://www.docker.com/products/docker-desktop/) installed.
- create a new directory and download [docker-compose.yml](docker-compose.yml) from [easystart](easystart/) package
- in the same directory create ".env" file. For example:
```
MYSQLDB_USER=root
MYSQLDB_ROOT_PASSWORD=4rfgt56yh
MYSQLDB_DATABASE=bookstore
MYSQLDB_LOCAL_PORT=3308
MYSQLDB_DOCKER_PORT=3306
SPRING_LOCAL_PORT=8088
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
```
- in the terminal, being in this folder, enter the command:
```bash
docker compose up
```
#### or the JAR build path:
- your computer should still have [docker](https://www.docker.com/products/docker-desktop/) installed.
- Clone this repository.
- Create a .env file in the root folder.
- if Maven is not installed on your machine, you can download and install it from the [Maven website](https://maven.apache.org/download.cgi).
- in the terminal, being in book-store folder, enter the command:
```bash
mvn package
```
- and finally:
```bash
docker compose up
```
#### after launch:
- you will be able to send http requests to the port SPRING_LOCAL_PORT specified in the .env file
- for example, you can open the page in your browser at http://localhost:8088/api/swagger-ui/index.html
  where 8088 = SPRING_LOCAL_PORT specified in the .env file by default. Or use [Postman](https://www.postman.com/downloads/).

### Postman

- Here I have prepared a collection for Postman. For convenience, the SPRING_LOCAL_PORT is set to the default value of 8088.
- Thanks to this collection, you can test the functionality of the application in a very clear and detailed manner.
- Import this file into your Postman.
  [book store requests postman collection](Book%20store%20requests.postman_collection.json) from [postman](postman/) package
### Technical details

It's worth noting that the application implements a role system with USER and ADMIN roles.
Some functionality is only accessible with administrator privileges, namely:
- create/create all/delete/update book
- create/delete/update category
- update order status

All other functionality is accessible to everyone.
There is a default user created with administrator privileges:
- name: admin.test@example.com
- password: 12345678




