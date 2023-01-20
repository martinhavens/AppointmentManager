This is AppointmentManager. The purpose of this application is to read from a client's SQL database,
and maintain customer and appointment records. This applications provides the ability to add new,
update existing, or delete customer and appointment records.
• Martin Havens mhaven7@wgu.edu SOFTWARE II - ADVANCED JAVA CONCEPTS — C195 PRFA — QAM2, 01/19/2023

• IntelliJ Community 2021.1.3 Community Edition, Java 17.0.1, JavaFX SDK 17.0.2

Run the program by opening the AppointmentManager project in IntelliJ. Run HelloApplication.java. Enter a valid username
and password.

To add a Customer:
Click on "Add Customer", Enter a Name, Address, Phone Numner, Postal Code. Select a Country then select a division.
Click the save button to the right of the modify customer button.

To add an Appointment:
Click on "Add Appointment", Enter a Title, Description, Location, Type, a valid Customer ID, and valid User ID.
Select a valid date and valid time for start and end. Select a contact name for the appointment. Click the save button
above the User ID text label.

To filter Appointments by month or week:
Verify that at least one appointment is added to the database. With the filter cleared and the Frame of Reference label
as "All", click on the month or week radio button. Advance forward or backward in the filter by click on the left or
right arrow buttons to the left and the right of the radio buttons.

To modify a Customer:
Verify that at least one customer is added to the database. Select a single customer item from the top tableview.
Click on "Modify Customer". Change the relevant fields of the customer. Click the save button to the right of the
modify customer button.

To modify an Appointment:
Verify that at least one appointment is added to the database. Select a single appointment item from the bottom tableview.
Click on "Modify Appointment". Change the relevant fields of the appointment. Click the save button above the User ID
text label.

To delete a Customer:
Select a single customer item from the top tableview. Click the delete customer button. If the customer has appointments
then an error will occur. The appointments with the same customer ID as the customer wanting to be deleted must also be
deleted.

To delete an Appointment:
Select a single appointment item from the bottom tableview. Click the delete appointment button.

To view a contact's schedule:
Select a contact name from the selection combination box to the left of the "View Contact Schedule" button. If the contact
has no appointments then the tableview of their schedule will be empty.

To view the number of appointments by month and type:
Verify that at least one appointment is entered in the database. If there are no appointments in the database, an alert
will appear. Otherwise, click on the "Refresh Monthly Report" button to list appointments by amount of month, and type.

•  a description of the additional report of your choice you ran in part A3f
•  The country report button allows examination of the total number of customers within the database per country. For
example, if there are 3 customers in the U.S. and 1 in Canada. The country report button will display that in a table view
whenever the button is pressed. If there are no customers, an alert will be displayed.

• mysql server version 8.0.32 and mysql workbench version 8.0.32 and mysql-connector-java:8.0.30
