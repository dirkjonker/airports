package airports.datamodel


import airports.models._
import kantan.csv._
import kantan.csv.ops._
import org.slf4j.LoggerFactory

import scala.io.Source


class DataLoader

object DataLoader {

  private val logger = LoggerFactory.getLogger(classOf[DataLoader])

  logger.info("halloooo")

  // implicits for conversion by the CSV library
  // can I can put these somewhere else?
  implicit val airportDecoder: RowDecoder[Airport] = RowDecoder.ordered(Airport.apply _)
  implicit val countryDecoder: RowDecoder[Country] = RowDecoder.ordered(Country.apply _)
  implicit val runwayDecoder: RowDecoder[Runway] = RowDecoder.ordered(Runway.apply _)

  /**
    * Function to load data from CSV into a list.
    */
  def getCSVData[T](r: String)(implicit rd: RowDecoder[T]): List[T] = {
    val rawData = Source.fromResource(s"data/$r.csv").reader
    val rawList = rawData.asCsvReader[T](rfc.withHeader).toList

    val failedSize = rawList.count(_.isFailure)
    if (failedSize > 0) logger.warn(s"failed to load $failedSize records from $r")

    // figure out how to make it work like
    // rawList collect { case Success(x) => x }
    val data = rawList.filter(_.isSuccess).map(_.get)
    val num = data.size
    logger.info(s"loaded $num record(s) for $r")

    data
  }

  lazy val airportData = getCSVData[Airport]("airports")

  lazy val countryData = getCSVData[Country]("countries")

  lazy val runwayData = getCSVData[Runway]("runways")
}
