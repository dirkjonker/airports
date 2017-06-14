package airports

import airports.dataload.DataLoader._
import airports.models._
import org.scalatest.FunSuite

/**
  * Created by Dirkjonker on 2017-06-14.
  */

class DataLoaderSuite extends FunSuite {

  test("load sample country data") {
    val expectedCountry = Country(
      302708, "NL", "Netherlands", "EU",
      "http://en.wikipedia.org/wiki/Netherlands", Some("Holland"))

    val loadedCountry = getCSVData[Country]("country_test")

    assert(loadedCountry == List(expectedCountry))
  }


  test("load sample airport data") {
    val expectedCountry = Airport(
      2513, "EHAM", "large_airport", "Amsterdam Airport Schiphol",
      52.3086013794, 4.763889789579999, Some(-11),
      "EU", "NL", "NL-NH", Some("Amsterdam"), "yes",
      Some("EHAM"), Some("AMS"), None,
      Some("http://www.schiphol.nl/"), Some("http://en.wikipedia.org/wiki/Amsterdam_Schiphol_Airport"), None)

    val loadedAirports = getCSVData[Airport]("airport_test")

    assert(loadedAirports == List(expectedCountry))
  }


  test("load sample runway data") {
    val expectedCountry = Runway(
      237925, 2513, "EHAM", Some(11483), Some(148),
      Some("ASP"), 1, 0, Some("06"), Some(52.2879), Some(4.73402), Some(-11), Some(58),
      Some(820), Some("24"), Some(52.3046), Some(4.77752), Some(-12), Some(238), None)

    val loadedRunways = getCSVData[Runway]("runway_test")

    assert(loadedRunways == List(expectedCountry))
  }
}
