# eWorldES Employee Access Tracking Service

A CRUD web app to help DHS Internal Access Coordinators, Project Managers, and Business Analysts at eWorldES update, add, and track the DHS access of all eWorldES employees across many DHS projects. 


## Technology

### Backend
- Spring Boot (need to fix tests)
- MySQL (done)
### Frontend
- HTML/CSS (in progress)
- JavaScript (in progress)
### Cloud Services
- AWS (not started)

## Screenshots
##### POST Request formats fullName & email fields and returns detailed descriptions of assigned Access Groups:
  
* Response Body is a GET of Access Groups, which shows Name and Type fields. Notice how Employee's JSON Body only lists Access Group IDs.

![POST1](screenshots/POST1.jpg)

* POST Response returns with detailed Access Group information. Employee's ID is 107.

![POST2](screenshots/POST2.jpg)

![POST3](screenshots/POST3.jpg)

* POST is reflected in MySQL DB.

![POST4](screenshots/POST4.jpg)

##### PUT Request preserves previous legitimate fields and updates Employee by removing absent Access Groups from DB while adding a new Access Group:

* In this PUT, Access Group 37 & 38 have been removed, but Access Group 39 has been added.

![PUT1](screenshots/PUT1.jpg)

* PUT is reflected in MySQL DB.

![PUT2](screenshots/PUT2.jpg)

##### POSTing the same email returns a "Duplicate Entry" error:

![DUPLICATE_ERROR](screenshots/DUPLICATE_ERROR.jpg)

##### POSTing an Employee w/ a non-eWorldES email returns an "Invalid Email" error:

![INVALID_EMAIL_ERROR](screenshots/INVALID_EMAIL_ERROR.jpg)

##### A list of all custom exceptions:

![ALL_ERRORS](screenshots/ALL_ERRORS.jpg)

## Goals: 
- Replace the current tracking system in which the DHS access PDFs are stored in a SharePoint and cannot be queried 
- Reduce back-and-forth between DHS Internal Access Coordinators and Project Managers/Business Analysts 
- Improve programming skills

## Personal note: 
- First Spring Boot project 
- First project with Unit & Integration Tests
- First project with custom error handling
- First project with DTOs 
- Includes an expiration field tied to Employee_Access_Group that is no longer relevant due to eWorld policy changes but
is still present to exemplify my understanding of joined tables

Created by an Internal Access Coordinator as a side project.
