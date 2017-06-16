package airports.controllers

import airports.AirportAppStack
import slick.jdbc.H2Profile.api._


class AppController extends AirportAppStack {
  get("/") {
    contentType = "text/html"
    ssp("/index")
  }
}
