
# FOUR DISTINCT PARTS:

# DatabaseDriver:
* Straightforward but annoying, pretty much repurposing code from HW5
* Can assume that any requests sent to driver are logically correct, do not need to check logic

# CourseReviewDriver
* Middleman for databaseDriver and Front-end
* Handles all logic
* Basically on run a databaseDriver will be created, then a courseReviewDriver will be created holding that databaseDriver
* That object will then be passed to the front-end runner, and will handle all requests
* CAN NOT assume that any requests will logically be correct, must handle that and on request:
  * Pull information or send information to back-end (databaseDriver)
  * Send either query data or creationCode (success/failure) to front-end
* Also helper objects (review, course, user) need to be built out fully but not much work

# Front-End - patrick (not attached to doing but willing)
* Actual user interaction
* Holds onto courseReviewDriver, sends any queries to that
* Does NOT need to check if queries are logically correct, however MUST be ready to handle failed error codes smoothly
* Mainly design work while allowing CourseReviewDriver to do logical heavy lifting
* Probably the beefiest part but also once someone gets used to doing it probably best for them to do whole thing 

# QA (Quality Assurance)
* Responsible for all testing 
* Ideally: other 3 explain to QA what their function are and what they are supposed to do but
* QA should not rely on looking at the code from other three (blackbox testing)
* Given this one is worth a lot I think we should have the most intense testing procedures to date
* Also this person doesn't actually have to implement anything so should have ample time for testing
* This work will trail the others by a bit, so if anyone is really front loaded on work and is willing
to do some work after the exam maybe (doesn't have to be just opportunity for this to wait)
