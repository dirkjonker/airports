package airports

import org.scalatra.scalate.ScalateSupport
import org.scalatra.{FutureSupport, ScalatraServlet}

import scala.concurrent.ExecutionContext

/**
  * Base trait for all controllers
  */
trait AirportAppStack extends ScalatraServlet with FutureSupport with ScalateSupport {

  protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  /**
    * 404 and such
    */
  notFound {
    status = 404
    "404 NOT FOUND"
  }
}
