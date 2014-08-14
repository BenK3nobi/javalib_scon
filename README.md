# README #

### What is this repository for? ###

* This is the java library to interface with the scon python reference server backend
* version 0.1

### How do I get set up? ###

* import the library into your project
* get a database connection threw the ServerDatabaseSession object
* it needs the server url ()with https in the front, the user name and the hashed password
* call the methods of the object to get the information
* for this library to work, you need utf-8 support
* errors will be propagated threw Exceptions inheriting from SBSBaseException

