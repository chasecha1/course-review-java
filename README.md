## Authors
1) Patrick Nagy, pcnagy
2) Chase Cha, chasecha1
3) George Wray, GeorgeWray
4) Zain Bangash, zainkbangash

## To Run

CourseReviewsApplication has main method that will open our application if run with the correct VM arguments.

### Patrick Nagy

* In charge of View and Controller aspect (Presentation Layer)
* implemented entire front end and user interface
* worked with JavaFX and FXML to update the display based on user input and make requests of the model

### Chase Cha

* In charge of the Business logic layer
* acted as a middle man between database and front end
* receive requests from Patrick, impose rules specified in writeup on whatever user provided, and return correct error codes to front end when needed.
* made requests to database driver (George) if user input was valid and returned answer to front end (Patrick)

### George Wray

* In charge of the data layer
* wrote all SQL queries for the database based on requests from business logic layer
* retrieve, update, and create new data when needed

### Zain Bangash

* In charge of quality assurance / system design
* wrote tests for course review driver
* Tested system as whole and ensured that everything worked as intended
