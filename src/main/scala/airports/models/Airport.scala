package airports.models

import airports.datamodel.DataLoader

/**
  * Created by dirk on 6/14/17.
  */
case class Airport(id: Int,
                   ident: String,
                   airportType: String,
                   name: String,
                   latitude: Double,
                   longitude: Double,
                   elevation: Option[Double],
                   continent: String,
                   iso_country: String,
                   iso_region: String,
                   municipality: Option[String],
                   scheduledService: String,
                   gpsCode: Option[String],
                   iataCode: Option[String],
                   localCode: Option[String],
                   homeLink: Option[String],
                   wikipediaLink: Option[String],
                   keywords: Option[String])
