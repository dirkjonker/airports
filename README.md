# airports #

## Build & Run ##

```sh
$ sbt
> jetty:start
```

Visit the website in your browser:
[http://localhost:8080/](http://localhost:8080/)

# What technology does it depend on?

Using the Scalatra framework for the web pages.
To easily query the airports and runways it uses an in-memory H2 database. 
Because it uses Slick to interface with the database, it is easy to substitute
H2 with a different database technology.

# How does it work?

The application uses the MVC pattern.

The models for Countries, Airports and Runways are defined as a `case class` to make it
easy to pass it around between the CSV loader, database, and web framework.

There is a single controller, with the routes defined in the AirportRoutes trait.

The views are created with Scalate/SSP templates.

# What if it needs to grow?

- The AppController is already getting big. It would be wise to implement real controllers
  and let the routes use those controllers.
- Definitely needs pagination for countries with a large number of airports.
