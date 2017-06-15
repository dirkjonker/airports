package airports.datamodel

import slick.jdbc.H2Profile.api._

/**
  * Contains the logic to provision the database and put the data in there
  */
object Tables {

  // Table queries, to use for querying stuff
  val airports = TableQuery[Airports]
  val countries = TableQuery[Countries]
  val runways = TableQuery[Runways]

  // DBIO Action which runs several queries inserting sample data
  // first countries, then airports, then runways (FK constraints)
  val insertData = DBIO.seq(
    countries ++= DataLoader.countryData,
    airports ++= DataLoader.airportData,
    runways ++= DataLoader.runwayData
  )

  // creating/dropping the schema
  val allSchemas = airports.schema ++ countries.schema ++ runways.schema
  val createSchemaAction = allSchemas.create
  val dropSchemaAction = allSchemas.drop

  // Create database, composing create schema and insert sample data actions
  val createDatabase = DBIO.seq(createSchemaAction, insertData)

  val dropDatabase = DBIO.seq(dropSchemaAction)

}
