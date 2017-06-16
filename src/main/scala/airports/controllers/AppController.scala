package airports.controllers

import airports.AirportAppStack
import airports.models.Airport
import org.scalatra.scalate.ScalateSupport
import org.scalatra.{FutureSupport, NotFound, ScalatraBase}
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
    val countries = search match {
      case Some(q) => CountryController.find(q)
      case _ => CountryController.list
    }

    ssp("/countries", "countries" -> countries, "search" -> search)
  }

  get("/countries/:code") {
    contentType = "text/html"

    val code = params("code")
    val airports = AirportController.forCountry(code)

    ssp("/airports", "airports" -> airports, "page" -> 0, "limit" -> 0,
      "numPages" -> 0)
  }

  get("/airports") {
    contentType = "text/html"

    val page: Int = params.getOrElse("page", "1").toInt
    val limit: Int = params.getOrElse("limit", "20").toInt
    val offset = (page - 1) * limit

    val airports = AirportController.page(page, limit)

    ssp("/airports", "airports" -> airports, "page" -> page, "limit" -> limit,
      "numPages" -> AirportController.numPages())
  }

  get("/airports/:code") {
    contentType = "text/html"

    val code = params("code")

    AirportController.getByCode(code) match {
      case Some(a: Airport) =>
        val runways = RunwayController.forAirport(a)
        ssp("/singleAirport", "airport" -> a, "runways" -> runways)
      case None => NotFound(s"no such airport: $code")
    }
  }

  /**
    * Show the report
    *
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
  } */
}

class AppController(val db: Database) extends AirportAppStack with AppRoutes {
  protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
}
