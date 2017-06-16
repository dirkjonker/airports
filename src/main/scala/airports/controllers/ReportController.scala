package airports.controllers

import airports.AirportAppStack
import org.scalatra.FutureSupport
import org.scalatra.scalate.ScalateSupport
import slick.jdbc.H2Profile.api._


class ReportController(val db: Database) extends AirportAppStack with FutureSupport with ScalateSupport {

  /**
    * Show the report containing top/bottom 10 countries by number of airports
    * and the list of runway surfaces per country
    */
  get("/") {
    contentType = "text/html"

    val numAirportQuery =
      sql"""
           SELECT countries.code, countries.name, COUNT(*) AS num_airports
           FROM countries
           JOIN airports ON airports.iso_country = countries.code
           GROUP BY countries.name
           ORDER BY 3 DESC
         """.as[(String, String, Int)]

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
