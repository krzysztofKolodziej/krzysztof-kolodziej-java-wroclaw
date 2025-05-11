
**Project Overview**

Krzysztof_Kolodziej_Java_Wroclaw - a Spring Boot application in Java 21 that processes orders and optimizes payment 
methods to maximize discounts and efficiently use available limits.

**Features:**
- Card Promotions: Pays an order in full with the credit card offering the highest promotional discount.
- Full Points Payment: Uses loyalty points to cover an entire order if the point balance permits.
- Partial Points Payment: Applies at least 10% of the order value in points to unlock a 10% discount, then covers the 
remainder with cards.
- Fallback Payment: Distributes remaining charges across all methods without discounts.

**Tech/framework used** 

- Java 21
- Spring Boot
- JUnit 5
- Mockito
- Maven
- Lombok 

**Installation and Running**

1. Clone the repository:

```bash
git clone https://github.com/krzysztofKolodziej/krzysztof-kolodziej-java-wroclaw.git
cd krzysztof-kolodziej-java-wroclaw
```

2. Build the project

```bash
mvn clean install
```

3. Run the application 

```bash
java -jar target/krzysztof-kolodziej-java-wroclaw.jar <path_to_orders.json> <path_to_methods.json>
```

**Testing**

Run the tests using Maven:

```bash
mvn test
```






