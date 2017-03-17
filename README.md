# jdbc-servlet-api
Java library that provides an extended Servlet API customized for processing JDBC Query or Update Statements

## Getting Started

- [How to use this library?](https://github.com/JeromeRocheteau/jdbc-servlet-api#how-to-use-this-library) 
- [How to connect to a database?](https://github.com/JeromeRocheteau/jdbc-servlet-api#how-to-connect-to-a-database) 
- [How to use JDBC Servlets?](https://github.com/JeromeRocheteau/jdbc-servlet-api#how-to-use-jdbc-servlets) 
  - [How to use JDBC Query Servlets?](https://github.com/JeromeRocheteau/jdbc-servlet-api#how-to-use-jdbc-query-servlets) 
  - [How to use JDBC Update Servlets?](https://github.com/JeromeRocheteau/jdbc-servlet-api#how-to-use-jdbc-update-servlets) 
  - [How to compose JDBC Servlets?](https://github.com/JeromeRocheteau/jdbc-servlet-api#how-to-compose-jdbc-servlets) 
- [How to use JDBC Filters?](https://github.com/JeromeRocheteau/jdbc-servlet-api#how-to-use-jdbc-filters) 

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
2. specify a context parameter called `jdbc-resource` with the JDBC resource reference attribute `res-ref-name` value
3. specify the context servlet listener `JdbcListener` that will manage the previous JDBC resource

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
    <listener-class>com.github.jeromerocheteau.JdbcListener</listener-class>
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
    <servlet-class>com.github.jeromerocheteau.MyJdbcServlet</servlet-class>
    <init-param>
      <param-name>sql-query</param-name>
      <param-value>/com/github/jeromerocheteau/queries/my-sql-query.sql</param-value>
    </init-param>
  </servlet>
```

The JDBC servlet corresponds either to a JdbcQueryServlet or to a JdbcUpdateServlet.

#### How to use JDBC Query Servlets?

A JDBC Query Servlet consists of a servlet that executes a SQL query statement `select ... from ...`
i.e. queries that returns a JDBC `ResultSet`. Thus, such JDBC servlets have to override 
at least three methods:

1. the first one `doFill` makes possible to grab parameter values of the HTTP request `HttpServletRequest` and to inject them into the SQL query `PreparedStatement`;
2. the second one `doMap` consists in transforming the content of the `ResultSet` into a Java object that stands for the result of SQL query; 
3. the third one overrides Java servlets `doGet`, `doPost`, etc methods and could use the method `doProcess` and `doWrite` in order to exeutes the SQL query and to write the transformed result on the response output.

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
		this.doWrite(names, response);
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

#### How to use JDBC Update Servlets?

A JDBC Update Servlet consists of a servlet that executes a SQL update statement i.e. 
queries that modify the database data either with a create query `insert into ...`, or 
a update one`update ...`, or a delete one `delete from ...`.
Thus, such JDBC servlets have to override 
at least three methods:

1. the first one `doFill` makes possible to grab parameter values of the HTTP request `HttpServletRequest` and to inject them into the SQL query `PreparedStatement`;
2. the second one `doMap` consists in transforming the result of the SQL query into a Java object;
  - the first SQL query result is an integer `count` that corresponds to the number of rows affected by the query; 
  - the second SQL query result is a `ResultSet` that corresponds to the list of the generated keys if the query is an `insert into ...` statement; 
3. the third one overrides Java servlets `doGet`, `doPost`, etc methods and could use the method `doProcess` and `doWrite` in order to exeutes the SQL query and to write the transformed result on the response output.

```java
public class MyJdbcServlet extends JdbcUpdateServlet<Boolean> {

        @Override
        protected void doFill(PreparedStatement statement, HttpServletRequest request) 
        throws Exception {
                String name = request.getParameter("name");
                statement.setString(1, name);
        }
        
        @Override
        protected Boolean doMap(HttpServletRequest request, int count, ResultSet resultSet) 
        throws Exception {
                return count > 0;
        }

        @Override
        public void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException {
                Boolean done = this.doProcess(request);
                this.doWrite(done, response);
        }
        
}
```

The file `my-sql-query.sql` located in the folders `com/github/queries`
inside the resource folders `src/main/resources`.
It consists of a SQL query that insert a parametric value into a table called `names` in this example.

```sql
insert into names (name) values (?);
```

#### How to compose JDBC Servlets?

This API makes possible to compose JDBC servlets in a simple way. 
Assuming that 2 JDBC servlets are declared within the file `webv.xml` as seen previously.

```xml
  <servlet>
    <servlet-name>my-first-jdbc-servlet</servlet-name>
    <servlet-class>com.github.jeromerocheteau.MyFirstJdbcServlet</servlet-class>
    <init-param>
      <param-name>sql-query</param-name>
      <param-value>/com/github/jeromerocheteau/queries/my-first-sql-query.sql</param-value>
    </init-param>
  </servlet>
  
  <servlet>
    <servlet-name>my-second-jdbc-servlet</servlet-name>
    <servlet-class>com.github.jeromerocheteau.MySecondJdbcServlet</servlet-class>
    <init-param>
      <param-name>sql-query</param-name>
      <param-value>/com/github/jeromerocheteau/queries/my-second-sql-query.sql</param-value>
    </init-param>
  </servlet>
```

The JDBC servlets `MyFirstJdbcServlet` and `MySecondJdbcServlet` shall to be implemented 
as a class that extends `JdbcQueryServlet` or `JdbcUpdateServlet` 
and they shall provide their SQL query results as attributes of the HTTP request.

```java
public class MySecondServlet extends JdbcQueryServlet<String> {
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException {
		String result = this.doProcess(request);
		request.setAttribute("result", result);
	}
	
}
```

It is then possible to define a JDBC servlet that processes the first JDBC servlet followed by the second one. 
It consists of declaring a JDBC servlet within the file `web.xml` without `sql-query` parameter:

```xml
  <servlet>
    <servlet-name>my-jdbc-servlet</servlet-name>
    <servlet-class>com.github.jeromerocheteau.MyJdbcServlet</servlet-class>
  </servlet>
```
This JDBC servlet `MyJdbcServlet` extends that of `JdbcServlet` in calling these two JDBC servlets 
by the means of the `doCall` method inside an overrided `doGet` or `doPost` method.
Results of previous JDBC servlets can then be grabbed from the request attributes 
and written to the response output stream for example.

```java
public class MyJdbcServlet extends JdbcServlet {
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException {
		this.doCall(request, response, "my-first-jdbc-servlet");
		this.doCall(request, response, "my-second-jdbc-servlet");
		String result = (String) request.getAttribute("result");
		this.doWrite(result, response);
	}
	
}
```
### How to use JDBC Filters?

A JDBC filter consists of a filter that can execute a SQL query statement i.e. 
queries that retrieve data from the database with a select query `select ...`.

Firstly, JDBC filters have to be declared within the `web.xml` file 
by specifying a Java Filter class and, eventually, by providing a parameter `sql-query` 
that references a SQL query statement.

```xml
  <filter>
    <filter-name>my-jdbc-filter</filter-name>
    <filter-class>com.github.jeromerocheteau.MyFilter</filter-class>
    <init-param>
      <param-name>sql-query</param-name>
      <param-value>/com/github/jeromerocheteau/queries/my-sql-query.sql</param-value>
    </init-param>
  </filter>
```

Finally, JDBC filter scope have to be defined by a `filter-mapping` the follows:

```xml
  <filter-mapping>
    <filter-name>my-jdbc-filter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```

Secondly, JDBC filters have to be defined by a Java class that extends `JdbcFilter` which extends `Filter` itself. 
JDBC filters merely consists in setting the request and response encoding (default is UTF-8), 
in executing the given SQL query if provided 
and in delegating the HTTP request to the next filter or servlet in the processing chain.
Such JDBC filters have to override at least two methods:

1. the first one `doFill` makes possible to grab parameter values of the HTTP request `HttpServletRequest` and to inject them into the SQL query `PreparedStatement`;
2. the second one `doMap` consists in transforming the result set of the SQL query.

JDBC filters can help to filter out authenticated users or clients as the following example:

```java
public class MyFilter extends JdbcFilter {

	@Override
	protected void doFill(PreparedStatement statement, ServletRequest request) throws Exception {
		String passphrase = request.getHeader("X-Passphrase");
		statement.setString(1, passphrase);
	}
	
	@Override
	protected void doMap(ServletRequest request, ResultSet resultSet) throws Exception {
		if (resultSet.next()) {
			String user = resultSet.getString("username");
			request.setAttribute("user", user);
		} else {
			throw new ServletException("user");
		}
	}
	
}
```
The SQL query that corresponds to this example can be defined as follows:

```sql
select 
  u.username as username 
from users u 
inner join passphrases p on p.user = u.username
where p.pass = ?;
```
