1. Download a **Liferay bundle** locally and unzip the bundle.

2. Navigate to Liferay home. Start Tomcat server with ./catalina.sh run, the path you need to navigate to start the server looks similar to this path but depends on where you have unzip Liferay bundle initially. 
```
/home/me/Documents/liferay-dxp-7.4.13.u70/tomcat-9.0.71/bin 
```

3. As soon as your Tomcat server starts , you will be redirected to the **localhost** on your default browser, you can update the basic configurations and **save** it. You get an warning **This instance is not registered**, that means you need to add an **Activation key**.

4. You need to add an **Activation key** for the liferay instance to run successfully. You can download it from here: 
```
http://localhost:8080/c/portal/license_activation 
```
Download **developer key**. You need to add this activation key in the **deploy** folder of Liferay home. As soon as you add the **Activation key**, the logs reflect the messages: 
- **Processing activation-key**
- **DXP Development license validation passed**
- **License registered for DXP Development**

5. For my practise, I have spinned up a **mysql:5.7** docker container and have set itup using command: 
```
docker run -d -p 3306:3306 --name mysql-practise -e MYSQL_ROOT_PASSWORD=root mysql:5.7. 
```
Check [compatability metrix](https://www.liferay.com/compatibility-matrix) to see support for the specific Liferay bundle downloaded.

6. Navigate into the Mysql container using command.
```
docker exec -ti <container-id> /bin/bash 
```
Get inside mysql with the command.
```
mysql -uroot -proot 
```
where '-u' represents username and 'p' represents password, you will need to add this details you specified at the time of spinning the container.

7. Note down the **ip address of mysql container** with the help of the following command as this is required for future setup.
```
docker inspect <container-id>.
```
8. Now that your mysql setup is done for now. You will need to copy the mysql connector jar file to this path: 
```
liferay-dxp-7.4.13.u70/tomcat-9.0.71/webapps/ROOT/WEB-INF/shielded-container-lib/
```
Important point to note hear is that the **connector jar** file you just moved, should be relevant to the **mysql version** you have spinned for your docker container 

9. Navigate to Liferay home, create a file named **portal-ext.properties**, this file will hold the configurations required to connect MySql running inside the docker container with the Liferay. Files content looks similar to this with few edits required.
```
jdbc.default.driverClassName=com.mysql.jdbc.Driver
jdbc.default.url=jdbc:mysql://172.17.0.2:3306/lportal?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false
jdbc.default.username=root
jdbc.default.password=root
```

Note: 
a. For **jdbc.default.url**, you need to change the **ip address** that you noted down in the step 7, change database name **lportal** with the one you specified while creating the mysql database.
b. Change the **password** and **username** with the one you specified while spinning up the mysql container.

10. Downgrade **Liferay** at this point and you should now navigate to Liferay home and make sure you removed 3 files:
- /osgi/state
- /tomcat/temp
- /tomcat/work

Purpose of removing these files is to **remove the cache**.

11. Sign-up credentials gets stored in **portal-setup-wizard.properties** on Liferay Home when you downgrade the Liferay bundle.

12. **Restart the Tomcat server** and check for the logs, you should now see the **mysql database** being accessed and **portal-ext.properties** that you added in before restarting the Liferay.

13. Congratulations! you have now **configured Liferay with MySql database**.
