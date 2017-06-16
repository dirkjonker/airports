package airports.controllers

import airports.AirportAppStack
import airports.models.Tables
import org.scalatra.FutureSupport
import org.scalatra.scalate.ScalateSupport
import slick.jdbc.H2Profile.api._

/**
  * Created by Dirkjonker on 2017-06-16.
  */
class AirportController(val db: Database) extends AirportAppStack with FutureSupport with ScalateSupport {
  get("/") {
    contentType = "text/html"

    val page: Int = params.getOrElse("page", "1").toInt
    val limit: Int = params.getOrElse("limit", "20").toInt
    val offset = (page - 1) * limit

    db.run(Tables.airports.drop(offset).take(limit).result) map {
      xs => ssp("/airports", "airports" -> xs, "page" -> page, "limit" -> limit)
    }
  }

  get("/:code") {
    contentType = "text/html"

    val code = params("code")

    val query = for {
      a <- Tables.airports.filter(_.ident === code).result
      r <- Tables.runways.filter(_.airportIdent === code).result
    } yield (a, r)

    db.run(query) map {
      case (Nil, _) => {
        status = 404
        s"not found: airport code: $code"
      }
      case (airports, runways) =>
        ssp("/singleAirport", "airport" -> airports.head, "runways" -> runways)
    }
  }
}
