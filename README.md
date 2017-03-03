# jdbc-servlet-api
Java library that provides an extended Servlet API customized for processing JDBC Query or Update Statements

## Getting Started

### How to use this library?

Currently, you have to retrieve source code from the GitHub repository and install the Java library locally thanks to Maven.

```
git clone https://github.com/JeromeRocheteau/jdbc-servlet-api.git
cd jdbc-servlet-api
mvn install
```

Once done, you need to insert the following dependency into your file `pom.xml`:

```xml
    <dependency>
      <groupId>com.github.jeromerocheteau</groupId>
      <artifactId>jdbc-servlet-api</artifactId>
      <version>1.0</version>
    </dependency>
```

### How to connect to a database?

Create a XML file `context.xml` in the folder `src/main/webapp/META-INF` 
and fulfill the snippet below with the appropriate settings:

```xml
<Context>
  <Resource name="jdbc/database"
            auth="Container"
            type="javax.sql.DataSource"
            username="..."
            password="..."
            driverClassName="..."
            url="jdbc:mysql://localhost:3306/..." />
</Context>
```

In your `web.xml` file in the folder `src/main/webapp/WEB-INF`, 

1. specify a reference to the JDBC resource defined in the `context.xml` file
2. specify a context parameter called `jdbc-resouce` with the JDBC resource reference attribute `res-ref-name` value
3. specify the context servlet listener `com.github.servlet.jdbc.JdbcListener` that will manage the JDBC resource

```xml
<web-app>

  <resource-ref>
    <description>JDBC Resource</description>
    <res-ref-name>jdbc/database</res-ref-name>
    <res-type>javax.sql.DataSource</res-type>
    <res-auth>Container</res-auth>
  </resource-ref>

  <context-param>
    <param-name>jdbc-resource</param-name>
    <param-value>jdbc/database</param-value>
  </context-param>
  
  <listener>
    <listener-class>com.github.servlet.jdbc.JdbcListener</listener-class>
  </listener>
  
</web-app>
```

### How to use JDBC Servlets?

In order to register a JDBC servlet, 
within your `web.xml` file in the folder `src/main/webapp/WEB-INF`, 
you just have to specify a servlet and to provide, 
firstly, its Java class qualified name and, 
secondly, the relative path of the SQL query 
that this JDBC servlet has to process.

```xml
  <servlet>
    <servlet-name>my-jdbc-servlet</servlet-name>
    <servlet-class>com.github.servlets.MyJdbcServlet</servlet-class>
    <init-param>
      <param-name>sql-query</param-name>
      <param-value>/com/github/queries/my-sql-query.sql</param-value>
    </init-param>
  </servlet>
```

The JDBC servlet corresponds either to a JdbcQueryServlet or to a JdbcUpdateServlet.

#### How to define a JDBC Query Servlet?

A JDBC Query Servlet consists of a servlet that executes a SQL query statement `select ... from ...`
i.e. queries that returns a JDBC `ResultSet`. Thus, such JDBC servlets have to override 
at least three methods:

1. the first one `doFill` makes possible to grab parameter values of the HTTP request `HttpServletRequest` and to inject them into the SQL query `PreparaedStatement`;
2. the second one `doMap` consists in transforming the content of the `ResultSet` into a Java object that stands for the result of SQL query; 
3. the third one overrides Java servlets `doGet`, `doPost`, etc methods and could use the method `doProcess` and `doPrint` in order to exeutes the SQL query and to write the transformed result on the response output.

```java
public class MyJdbcServlet extends JdbcQueryServlet<List<String>> {

	@Override
	protected void doFill(PreparedStatement statement, HttpServletRequest request) 
	throws Exception { }

	@Override
	protected List<String> doMap(HttpServletRequest request, ResultSet resultSet) 
	throws Exception {
		List<String> names = new LinkedList<String>();
		while (resultSet.next()) {
			String name = resultSet.getString("name");
			names.add(name);
		}
		return names;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException {
		List<String> names = this.doProcess(request);
		this.doPrint(names, response);
	}
	
}
```
The file `my-sql-query.sql` should be located in the folders `com/github/queries`
as a Java project resource i.e. within the folder `src/main/resources`.
It merely consists of a SQL query that retrieves values of the attribute `name` 
from a table called `names` in this example.

```sql
select name from names;
```

### How to use JDBC Update Servlets?

A JDBC Update Servlet consists of a servlet that executes a SQL update statement i.e. 
queries that modify the database data either with a create query `insert into ...`, or 
a update one`update ...`, or a delete one `delete from ...`.
Thus, such JDBC servlets have to override 
at least three methods:

1. the first one `doFill` makes possible to grab parameter values of the HTTP request `HttpServletRequest` and to inject them into the SQL query `PreparaedStatement`;
2. the second one `doMap` consists in transforming the result of the SQL query `count` that provides the number of rows affected by the query into a Java object; 
3. the third one overrides Java servlets `doGet`, `doPost`, etc methods and could use the method `doProcess` and `doPrint` in order to exeutes the SQL query and to write the transformed result on the response output.

```java
public class MyJdbcServlet extends JdbcUpdateServlet<Boolean> {

        @Override
        protected void doFill(PreparedStatement statement, HttpServletRequest request) 
        throws Exception {
                String name = request.getParameter("name");
                statement.setString(1, name);
        }
        
        @Override
        protected Boolean doMap(HttpServletRequest request, int count) 
        throws Exception {
                return count > 0;
        }

        @Override
        public void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
                Boolean done = this.doProcess(request);
                this.doPrint(done, response);
        }
        
}
```

The file `my-sql-query.sql` located in the folders `com/github/queries`
inside the resource folders `src/main/resources`.
It consists of a SQL query that insert a parametric value into a table called `names` in this example.

```sql
insert into names (name) values (?);
```

### How to use JDBC Filters?
