package airports

import airports.datamodel.Tables
import org.scalatra.scalate.ScalateSupport
import org.scalatra.{FutureSupport, ScalatraBase, ScalatraServlet}
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext


/**
  * Contains all the routes of the application
  */
trait AirportRoutes extends ScalatraBase with FutureSupport with ScalateSupport {

  def db: Database

  get("/") {
    contentType = "text/html"
    ssp("/index")
  }

  get("/db/create") {
    db.run(Tables.createDatabase)
  }

  get("/db/drop") {
    db.run(Tables.dropDatabase)
  }

  get("/countries") {
    contentType = "text/html"

    db.run(Tables.countries.result) map {
      xs => ssp("/countries", "countries" -> xs)
    }
  }

  get("/countries/:code") {
    val code = params("code")
    db.run(Tables.countries.filter(_.code === code).result) map {
      xs => xs.mkString("\n")
    }
  }

  get("/airports") {
    contentType = "text/html"

    db.run(Tables.airports.result) map {
      xs => ssp("/airports", "airports" -> xs)
    }
  }

  get("/runways") {
    db.run(Tables.runways.result) map {
      xs => xs.mkString("\n")
    }
  }

  get("/airports/:code") {
    val code = params("code")
    db.run(Tables.airports.filter(_.gpsCode === code).result) map {
      xs => xs.mkString("\n")
    }
  }

  /**
    * Get the top N countries by number of airports
    */
  get("/countries/top/:num") {
    val num = params("num")

    val query =
      sql"""
           SELECT countries.name, COUNT(*) AS num_airports
           FROM countries
           JOIN airports ON airports.iso_country = countries.code
           GROUP BY countries.name
           ORDER BY 2 DESC
           LIMIT $num
         """.as[(String, Int)]
    db.run(query) map { xs => xs.mkString("\n") }
  }

}

class AirportsApp(val db: Database) extends ScalatraServlet with FutureSupport with AirportRoutes {
  protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
}
