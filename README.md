# jdbc-servlet-api
Java library that provides a extended Servlet API customized for processing JDBC Query or Update Statements

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
      <groupId>com.github.servlet.jdbc</groupId>
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
<webapp>

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
  
</webapp>
```

### How to use JDBC Servets?

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

#### How to define a JDBC Query Servlet ?

```java
public class MyJdbcServlet extends JdbcQueryServlet<List<String>> {

	@Override
	protected void doFill(PreparedStatement statement, HttpServletRequest request) throws Exception { }

	@Override
	protected List<Category> doMap(HttpServletRequest request, ResultSet resultSet) throws Exception {
		List<String> names = new LinkedList<String>();
		while (resultSet.next()) {
			String name = resultSet.getString("name");
			names.add(name);
		}
		return categories;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		List<String> names = this.doProcess(request);
		this.doPrint(names, response);
	}
	
}
```

```sql
select name from names;
```

### How to use JDBC Update Servlets?

### How to use JDBC Filters?
