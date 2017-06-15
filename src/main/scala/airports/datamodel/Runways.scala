package airports.datamodel

import airports.models.Runway
import slick.jdbc.H2Profile.api._

/**
  * Created by dirk on 6/15/17.
  */
class Runways(tag: Tag) extends Table[Runway](tag, "runways") {
  def id = column[Int]("id", O.PrimaryKey)

  def airportId = column[Int]("airport_id")

  def airportIdent = column[String]("airport_ident")

  def length = column[Option[Int]]("length")

  def width = column[Option[Int]]("width")

  def surface = column[Option[String]]("surface")

  def lighted = column[Int]("lighted")

  def closed = column[Int]("closed")

  def le_ident = column[Option[String]]("le_ident")

  def le_latitude_deg = column[Option[Double]]("le_latitude_deg")

  def le_longitude_deg = column[Option[Double]]("le_longitude_deg")

  def le_elevation_ft = column[Option[Int]]("le_elevation_ft")

  def le_heading_degT = column[Option[Double]]("le_heading_degT")

  def le_displaced_threshold_ft = column[Option[Int]]("le_displaced_threshold_ft")

  def he_ident = column[Option[String]]("he_ident")

  def he_latitude_deg = column[Option[Double]]("he_latitude_deg")

  def he_longitude_deg = column[Option[Double]]("he_longitude_deg")

  def he_elevation_ft = column[Option[Int]]("he_elevation_ft")

  def he_heading_degT = column[Option[Double]]("he_heading_degT")

  def he_displaced_threshold_ft = column[Option[Int]]("he_displaced_threshold_ft")

  def * = (id, airportId, airportIdent, length, width, surface, lighted, closed,
    le_ident, le_latitude_deg, le_longitude_deg, le_elevation_ft, le_heading_degT, le_displaced_threshold_ft,
    he_ident, he_latitude_deg, he_longitude_deg, he_elevation_ft, he_heading_degT, he_displaced_threshold_ft
  ) <> ((Runway.apply _).tupled, Runway.unapply)
}