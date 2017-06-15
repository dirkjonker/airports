package airports.models

import slick.jdbc.H2Profile.api._

/**
  * Created by dirk on 6/15/17.
  */
class Airports(tag: Tag) extends Table[Airport](tag, "airports") {
  def id = column[Int]("id", O.PrimaryKey)

  def ident = column[String]("ident")

  def airportType = column[String]("airport_type")

  def name = column[String]("name")

  def latitude = column[Double]("latitude")

  def longitude = column[Double]("longitude")

  def elevation = column[Option[Double]]("elevation")

  def continent = column[String]("continent")

  def iso_country = column[String]("iso_country")

  def iso_region = column[String]("iso_region")

  def municipality = column[Option[String]]("municipality")

  def scheduledService = column[String]("scheduled_service")

  def gpsCode = column[Option[String]]("gps_code")

  def iataCode = column[Option[String]]("iata_code")

  def localCode = column[Option[String]]("local_code")

  def homeLink = column[Option[String]]("home_link")

  def wikipediaLink = column[Option[String]]("wikipedia_link")

  def keywords = column[Option[String]]("keywords")

  def * = (id, ident, airportType, name, latitude, longitude, elevation,
    continent, iso_country, iso_region, municipality, scheduledService,
    gpsCode, iataCode, localCode, homeLink, wikipediaLink, keywords
  ) <> ((Airport.apply _).tupled, Airport.unapply)

  def country = foreignKey("fk_airport_country", iso_country, Tables.countries)(_.code)
}
