package airports.controllers

import airports.AirportAppStack
import airports.models.Tables
import org.scalatra.FutureSupport
import org.scalatra.scalate.ScalateSupport
import slick.jdbc.H2Profile.api._

/**
  * Created by Dirkjonker on 2017-06-16.
  */
class CountryController(val db: Database) extends AirportAppStack with FutureSupport with ScalateSupport {

  /**
    * List all countries. There are not a lot of them so no pagination.
    */
  get("/") {
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

  /**
    * List all the airports for a single country
    * Need to figure out how to re-use template parts to implement pagination
    * Needed for the US as it has 12k+ airports in the database
    */
  get("/:code") {
    contentType = "text/html"

    val code = params("code")
    db.run(Tables.airports.filter(_.iso_country === code).result) map {
      xs => ssp("/airports", "airports" -> xs, "page" -> -1, "limit" -> 0)
    } // page == -1 to disable pagination
  }
}
