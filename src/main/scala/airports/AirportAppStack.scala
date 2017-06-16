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
    // remove content type in case it was set through an action
    contentType = ""
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }
}
