package airports.datamodel

import airports.models.Country
import slick.jdbc.H2Profile.api._

/**
  * Representation of countries in the database
  */
class Countries(tag: Tag) extends Table[Country](tag, "countries") {
  def id = column[Int]("id", O.PrimaryKey)

  def code = column[String]("code")

  def name = column[String]("name")

  def continent = column[String]("continent")

  def wikipediaLink = column[String]("wikipedia_link")

  def keywords = column[Option[String]]("keywords")

  def * = (id, code, name, continent, wikipediaLink, keywords) <> ((Country.apply _).tupled, Country.unapply)
}
