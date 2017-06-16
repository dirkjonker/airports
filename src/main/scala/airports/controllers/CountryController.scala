package airports.controllers

import airports.datamodel.DataLoader
import airports.models.{Airport, Country}
import kantan.csv.RowDecoder

/**
  * Created by Dirkjonker on 2017-06-16.
  */
object CountryController {
  implicit val airportDecoder: RowDecoder[Country] = RowDecoder.ordered(Country.apply _)

  private val data: List[Country] = DataLoader.getCSVData[Country]("countries")

  /**
    * List all the countries
    *
    * @return
    */
  def list: List[Country] = data.sortBy(_.name)

  /**
    * Search for a country using a query string
    *
    * @param query the (partial) country name
    * @return
    */
  def find(query: String): List[Country] = {
    val q = query.toUpperCase
    data.filter(c => c.name.toUpperCase.contains(q) || c.code == q)
  }
}
