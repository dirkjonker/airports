package airports.controllers

import airports.datamodel.DataLoader
import airports.models.{Airport, Country}
import kantan.csv.RowDecoder

/**
  * Created by Dirkjonker on 2017-06-16.
  */
object AirportController {

  implicit val airportDecoder: RowDecoder[Airport] = RowDecoder.ordered(Airport.apply _)

  private val data: List[Airport] = DataLoader.getCSVData[Airport]("airports")

  val numAirports: Int = data.size

  def fromCode(code: String): List[Airport] = data filter (_.ident == code)

  /**
    * The total number of pages - for pagination
    *
    * @param pageSize the number of airports per page
    * @return
    */
  def numPages(pageSize: Int = 20): Int = numAirports / pageSize

  /**
    * Get a slice of airports
    *
    * @param pageNum  the slice of data to get
    * @param pageSize the number of airports per page
    * @return
    */
  def page(pageNum: Int, pageSize: Int = 20): List[Airport] = {
    val offset = (pageNum - 1) * pageSize

    data.slice(offset, offset + pageSize)
  }

  /**
    * Get a single airport by its code
    *
    * @param code the code for which the airport is known
    * @return
    */
  def getByCode(code: String): Option[Airport] = data find (_.ident == code)

  /**
    * Get all airports for a certain country
    *
    * @param country the country ISO code to lookup the airports for
    * @return
    */
  def forCountry(country: String): List[Airport] = data filter (_.iso_country == country)

  /**
    * Get all airports for a certain country
    *
    * @param country the country object for which to lookup all airports
    * @return
    */
  def forCountry(country: Country): List[Airport] = data filter (_.iso_country == country.code)
}
