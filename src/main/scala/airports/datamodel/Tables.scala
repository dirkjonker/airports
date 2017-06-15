package airports.datamodel

import airports.models.{Airport, Country, Runway}
import slick.jdbc.H2Profile.api._

/**
  * Contains all the database objects
  * Also, all the logic to provision the database and put the data in there
  */
object Tables {

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
  }

  class Countries(tag: Tag) extends Table[Country](tag, "countries") {
    def id = column[Int]("id", O.PrimaryKey)

    def code = column[String]("code")

    def name = column[String]("name")

    def continent = column[String]("continent")

    def wikipediaLink = column[String]("wikipedia_link")

    def keywords = column[Option[String]]("keywords")

    def * = (id, code, name, continent, wikipediaLink, keywords) <> ((Country.apply _).tupled, Country.unapply)
  }

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

  // Table queries, to use for querying stuff
  val airports = TableQuery[Airports]
  val countries = TableQuery[Countries]
  //  val runways = TableQuery[Runway]

  // DBIO Action which runs several queries inserting sample data
  val insertData = DBIO.seq(
    airports ++= DataLoader.airportData,
    countries ++= DataLoader.countryData
    //    runways ++= DataLoader.runwayData
  )

  // DBIO Action which creates the schema
  val allSchemas = (airports.schema ++ countries.schema)
  val createSchemaAction = allSchemas.create
  val dropSchemaAction = allSchemas.drop

  // Create database, composing create schema and insert sample data actions
  val createDatabase = DBIO.seq(createSchemaAction, insertData)

  val dropDatabase = DBIO.seq(dropSchemaAction)

}
