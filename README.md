# HealthFirstPharmacy

A Java desktop application built using Apache NetBeans and integrated with a MySQL database.

## 🚀 Features
* Patient and medication management
* Interactive desktop user interface
* Persistent data storage using MySQL

## 🛠️ Prerequisites
Before running this project, ensure you have the following installed:
* **Java Development Kit (JDK 8 or higher)**
* **Apache NetBeans IDE**
* **MySQL Server** & **MySQL Workbench**
* **MySQL JDBC Connector** (JAR file for NetBeans library)

---

## 💾 Database Setup (MySQL)

To replicate the database required for this application:

1. Open **MySQL Workbench** and connect to your local server instance.
2. Create a new schema named `healthfirstpharmacy` (or matching your project connection configuration).
3. Go to **Server** > **Data Import**.
4. Choose **Import from Self-Contained File** and browse to select the `database.sql` file included in this repository.
5. Select your target schema and click **Start Import**.

---

## 💻 How to Run the Project in NetBeans

1. **Clone or Download** this repository to your computer.
2. Open **Apache NetBeans**.
3. Go to **File** > **Open Project** and select the downloaded project folder.
4. Expand the project on the left, go to **Libraries**, right-click, and select **Add JAR/Folder**. Add your `mysql-connector-java.jar` driver.
5. Open your database connection class file (e.g., `DBConnection.java` or `Database.java`) and update the connection credentials if necessary:
   ```java
   String url = "jdbc:mysql://localhost:3306/healthfirstpharmacy";
  ## 🔐 Default Login Credentials
Use these accounts to test the application logic right after setup:

### Admin Account
* **Username:** `admin` 
* **Password:** `admin123` 

### Cashier Account
* **Username:** `cashier1` 
* **Password:** `cashier123` 

6. Right-click the project folder and click **Run**.
