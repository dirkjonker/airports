package airports.models

/**
  * Created by dirk on 6/14/17.
  */
case class Country(id: Int,
                   code: String,
                   name: String,
                   continent: String,
                   wikipediaLink: String,
                   keywords: Option[String])
