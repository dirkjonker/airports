# airports #

## Build & Run ##

```sh
$ sbt
> jetty:start
```

Then create the database by going to 
[http://localhost:8080/db/create]()

List all countries:
[http://localhost:8080/countries]()

List one specific country:
[http://localhost:8080/countries/NL]()

List all airports (warning, terribly slow):
[http://localhost:8080/airports]()

List a single airport by code:
[http://localhost:8080/airports/EHAM]()
