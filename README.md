# GarmentGardens

Your one-stop management system for the world's next e-commerce giant for sustainable clothing. 🚀

Steps to deploy and run the project:
Initialise database and netbeans project:
	1. Navigate to our source folder and open the folder labelled Netbeans. This folder contains the netbeans project. 
	2. Navigate to the services tab in netbeans.
	3. Under database, locate MySQL Server at localhost:3306. This might differ based on your free ports available.
	4. Right Click this MySQL Server node and create database.
	5. Enter the database name, "garmentgardens" without the quotes. For Mac users, ensure that your database is all in lowercase. For Windows users, it will be lowercase by default.
	6. Grant Full Access to mysql.sys@localhost
	7. Navigate back to your Project Tab and deploy the Main Project node (The purple triangle). This step
		will allow our test data to be initialised thus there would be no further instructions for initialisation.
	8. In your browser, head to localhost:4848 to ensure that GlassFish Server is running in the background.
Run Angular Application:
	1. navigate to our source folder and open the folder labelled Angular. This folder contains the Angular project.
	2. Navigate into it's sub folder and open it in Visual Studio Code.
	3. run the CLI command in your command shell, "npm install". This step restores the node modules that are needed for this project.
	4. next, run the command, "ng serve"
	5. In your web browser, navigate to localhost:4200

Things to note:
Ensure that the netbeans project is deployed and perform an additional check to see if the tables are populated in the database.

