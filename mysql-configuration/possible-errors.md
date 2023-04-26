If you are caught with some exceptions, please re-check few of the **checkpoints** and start your Liferay Tomcat again.

1. connector driver error
In the first line of **portal-ext.properties**, on this line few times you provide it like this: **jdbc.default.driverClassName=com.mysql.jdbc.Driver** or **jdbc.default.driverClassName=com.mysql.cj.jdbc.Driver** 


Reason:
According to the changes in the Connector/J API "The name of the class that implements java.sql.Driver in MySQL Connector/J has changed from com.mysql.jdbc.Driver to com.mysql.cj.jdbc.Driver. The old class name has been deprecated."

This means that you just need to change the name of the driver:

Class.forName("com.mysql.jdbc.Driver");
to

Class.forName("com.mysql.cj.jdbc.Driver");

To summarise driver class depends on the MySql version you have used for this setup.

2. You haven't removed the **cache** before **restarting the Liferay bundle** specified in the step 10.
