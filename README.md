# Air Traffic Control (ATC)

[![Build Status](https://api.travis-ci.com/dwstanley/atc.svg?branch=develop)](https://travis-ci.com/dwstanley/atc)
[![Coverage Status](https://coveralls.io/repos/github/dwstanley/atc/badge.svg?branch=develop)](https://coveralls.io/github/dwstanley/atc?branch=develop)

A software subsystem of an air-traffic control system defined to manage a queue of aircraft (AC) in an airport.

The chosen architecture follows common web service design principles and makes use of **spring-data**, **spring-rest**, and project **lombok**.

The front end was written in React and is designed to demonstrate the functionality of REST service but due to time constraints is far from a production ready UI. 
Please note that while the UI is functional, user feedback is lacking so illegal requests my be consumed and not presented to the user. If the application is behaving unexpected I suggest running with dev tools open to view the error. 

Also note that when adding planes for arrival and departure their current state is considered. The system will ignore your request if you attempt to land a grounded plane, or similarly depart one that is already schedule for arrival. The demo is preloaded with an aircraft of each type that has a **null** status which can be added to either the arrival or departure queue.

### Build

ATC uses a standard Maven build file and can be compiled using the following command:

```sh
$ mvn clean package
```

Builds are automatically run at check in by travis-ci. The most recent build results can be found at <https://travis-ci.com/dwstanley/atc> 

[![Build Status](https://api.travis-ci.com/dwstanley/atc.svg?branch=develop)](https://travis-ci.com/dwstanley/atc)

### Test

Tests are written using SpringBootTest and JUnit 4. The consist of both Unit and Integration tests and focus on the queue data structure and rest interface. The can be run from the command line via:

```sh
$ mvn clean test
```

or a code coverage report from the last build can be viewed at <https://coveralls.io/github/dwstanley/atc>

[![Coverage Status](https://coveralls.io/repos/github/dwstanley/atc/badge.svg?branch=develop)](https://coveralls.io/github/dwstanley/atc?branch=develop) 

### Run

ATC is a spring-boot app running an in-memory H2 database. An instance of the server (hosting both the service and UI) can be started by running:

```sh
$ mvn clean spring-boot:run
```

Once running a demo application can be found: <http://localhost:8080>



If you would like to test the API directly it is exposed at <http://localhost:8080/atc/>.

### Notes and Next Steps

REST services are currently exposed in two different ways. The first is through **spring-data-rest**, which exposes the data repositories in a complete and standardized way with very little code. Second, is through a more service oriented approach that provides access to functions rather than collections. Using both approaches was convenient for a quick prototype but is a little disjoint and exposes more of the applications infrastructure than may be desired. Next steps would be to bring these two techniques into a single more cohesive api that only allows access to functionality we desire.

Departures are currently implemented as an in-memory queue. This works fine for a single server architecture but does not scale horizontally and adds the additional complexity of keeping the cache synched with the database if we were to start persisting state across restarts. A better approach might be to store departures in the database and perform the listing/removal operations on a sorted result set.