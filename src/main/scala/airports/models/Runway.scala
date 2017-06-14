package airports.models

/**
  * Created by dirk on 6/14/17.
  */
case class Runway(id: Int,
                  airportId: Int,
                  airportIdent: String,
                  length: Option[Int],
                  width: Option[Int],
                  surface: Option[String],
                  lighted: Int,
                  closed: Int,
                  le_ident: Option[String],
                  le_latitude_deg: Option[Double],
                  le_longitude_deg: Option[Double],
                  le_elevation_ft: Option[Int],
                  le_heading_degT: Option[Double],
                  le_displaced_threshold_ft: Option[Int],
                  he_ident: Option[String],
                  he_latitude_deg: Option[Double],
                  he_longitude_deg: Option[Double],
                  he_elevation_ft: Option[Int],
                  he_heading_degT: Option[Double],
                  he_displaced_threshold_ft: Option[Int])
