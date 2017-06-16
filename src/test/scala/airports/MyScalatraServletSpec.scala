package airports

import airports.controllers.AppController
import org.scalatra.test.specs2._
import slick.jdbc.H2Profile.api._

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class MyScalatraServletSpec extends ScalatraSpec { def is =
    "GET / on AppController"                ^
      "should return status 200"            ! root200^
      "should give the proper content type" ! contentType^
      "should give the welcome text"        ! bodyContent^
      end

  // new empty database for testing
  val db = Database.forURL("jdbc:h2:mem:test")

  addServlet(new AppController(db), "/*")

  def root200 = get("/") {
    status must_== 200
  }

  def contentType = get("/") {
    header("Content-Type") must startWith("text/html;")
  }

  def bodyContent = get("/") {
    body must contain("Welcome to the airports browser")
  }
}
