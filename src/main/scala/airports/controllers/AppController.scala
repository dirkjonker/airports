package airports.controllers

import airports.models.Tables
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

  get("/countries") {
    contentType = "text/html"

    val search = params.get("search")
    val query = search match {
      case Some(q) => for {
        country <- Tables.countries
        if country.name.toLowerCase like s"%$q%".toLowerCase || country.code === q.toUpperCase()
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
    db.run(Tables.airports.filter(_.ident === code).result) map {
      xs => ssp("/singleAirport", "airport" -> xs.head, "runways" -> Nil)
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

  /**
    * Show the report
    */
  get("/report") {

  }
}

class AppController(val db: Database) extends ScalatraServlet with FutureSupport with AirportRoutes {
  protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  /**
    * 404 and such
    */
  notFound {
    // remove content type in case it was set through an action
    contentType = ""
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }
}