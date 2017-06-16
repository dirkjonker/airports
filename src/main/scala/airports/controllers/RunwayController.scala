package airports.controllers

import airports.datamodel.DataLoader
import airports.models.{Airport, Runway}
import kantan.csv.RowDecoder

/**
  * Created by Dirkjonker on 2017-06-16.
  */
object RunwayController {
  implicit val airportDecoder: RowDecoder[Runway] = RowDecoder.ordered(Runway.apply _)

  private val data: List[Runway] = DataLoader.getCSVData[Runway]("runways")
  private val airportMap: Map[Int, Seq[Runway]] = data.groupBy(_.airportId)

  /**
    * Get all runways that belong to an airport
    * @param airport
    * @return
    */
  def forAirport(airport: Airport): Seq[Runway] = airportMap.get(airport.id) match {
    case Some(x) => x
    case _ => Nil
  }

}
