package airports

import airports.datamodel.{DataLoader, Tables}
import airports.models._
import org.scalatra.scalate.ScalateSupport
import org.scalatra.{FutureSupport, ScalatraBase, ScalatraServlet}
import slick.jdbc.H2Profile.api._

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
import scala.util.Success


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

    val f = db.run(Tables.countries.result)

    f map {
      x =>
        ssp("/countries", "countries" -> x)
    }
  }

  get("/countries/:code") {
    val code = params("code")
    db.run(Tables.countries.filter(_.code === code).result) map { xs =>
      xs.mkString("\n")
    }
  }

  get("/airports/") {
    db.run(Tables.airports.result) map { xs =>
      xs.mkString("\n")
    }
  }

  get("/airports/:code") {
    val code = params("code")
    db.run(Tables.airports.filter(_.gpsCode === code).result) map { xs =>
      xs.mkString("\n")
    }
  }

}

class AirportsApp(val db: Database) extends ScalatraServlet with FutureSupport with AirportRoutes {
  protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
}
