#
APIS :

POST /ticks - Rest API to monitor and read tick data. 
Request Body :
{ "instrument": "IBM.N", "price": 143.82, "timestamp": 1478192204000

GET /statistics - Rest API to monitor aggregated statistics for all ticks received in past 60 mins across all instruments.

GET /statistics/{instrument_identifier} - API to monitor statistics for a given instrument.

Solution based on Spring Boot & Maven project 
Use below command to run the application: 

mvn spring-boot:run.

Assumptions :
1 - Statistics of ticks received only in past 60 seconds can be monitored. Historical stats will not be available.
2 - Ticks will not be saved at server side.


It was a really interesting problem to solve.


