package airports

import org.scalatra.scalate.ScalateSupport
import org.scalatra.{FutureSupport, ScalatraServlet}

/**
  * Base trait for all controllers
  */
trait AirportAppStack extends ScalatraServlet with FutureSupport with ScalateSupport {

  /**
    * 404 and such
    */
  notFound {
    contentType = ""

    resourceNotFound()
  }
}
