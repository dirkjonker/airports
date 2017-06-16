package airports.controllers

import airports.AirportAppStack
import airports.models.Tables
import org.scalatra.scalate.ScalateSupport
import org.scalatra.{FutureSupport, ScalatraBase}
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext


/**
  * Contains all the routes of the application
  */
trait AppRoutes extends ScalatraBase with FutureSupport with ScalateSupport {

  def db: Database

  get("/") {
    contentType = "text/html"
    status = 200
    ssp("/index")
  }

  get("/countries") {
    contentType = "text/html"

    val search = params.get("search")
    val query = search match {
      case Some(q) => for {
        country <- Tables.countries
        if (country.name.toLowerCase like s"%$q%".toLowerCase) || (country.code === q.toUpperCase)
      } yield country
      case _ => Tables.countries
    }

    db.run(query.sortBy(_.name).result) map {
      xs => ssp("/countries", "countries" -> xs, "search" -> search)
    }
  }

  get("/countries/:code") {
    contentType = "text/html"

    val code = params("code")
    db.run(Tables.airports.filter(_.iso_country === code).result) map {
      xs => ssp("/airports", "airports" -> xs, "page" -> -1, "limit" -> 0)
    } // page == -1 to disable pagination
  }

  get("/airports") {
    contentType = "text/html"

    val page: Int = params.getOrElse("page", "1").toInt
    val limit: Int = params.getOrElse("limit", "20").toInt
    val offset = (page - 1) * limit

    db.run(Tables.airports.drop(offset).take(limit).result) map {
      xs => ssp("/airports", "airports" -> xs, "page" -> page, "limit" -> limit)
    }
  }

  get("/runways") {
    db.run(Tables.runways.result) map {
      xs => ssp("/runways", "runways" -> xs)
    }
  }

  get("/airports/:code") {
    contentType = "text/html"

    val code = params("code")

    val query = for {
      a <- Tables.airports.filter(_.ident === code).result
      r <- Tables.runways.filter(_.airportIdent === code).result
    } yield (a, r)

    db.run(query) map { case (airports, runways) =>
      ssp("/singleAirport", "airport" -> airports.head, "runways" -> runways)
    }
  }

  /**
    * Show the report
    */
  get("/report") {
    contentType = "text/html"

    val numAirportQuery =
      sql"""
           SELECT countries.name, COUNT(*) AS num_airports
           FROM countries
           JOIN airports ON airports.iso_country = countries.code
           GROUP BY countries.name
           ORDER BY 2 DESC
         """.as[(String, Int)]

    val surfacesQuery =
      sql"""
            SELECT DISTINCT countries.name, runways.surface
            FROM countries
            JOIN airports ON airports.iso_country = countries.code
            JOIN runways ON runways.airport_id = airports.id
            WHERE runways.surface IS NOT NULL
            ORDER BY 1, 2
        """.as[(String, String)]

    val composed = for {
      a <- numAirportQuery
      b <- surfacesQuery
    } yield (a, b)

    db.run(composed) map { case (countriesAirports, countriesRunways) =>
      ssp("/report", "countriesAirports" -> countriesAirports,
        "countriesRunways" -> countriesRunways.groupBy(_._1))
    }
  }
}

class AppController(val db: Database) extends AirportAppStack with AppRoutes {
  protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
}
