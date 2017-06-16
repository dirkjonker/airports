import javax.servlet.ServletContext

import airports.controllers.{AirportController, AppController, CountryController, ReportController}
import airports.models.Tables
import com.mchange.v2.c3p0.ComboPooledDataSource
import org.scalatra._
import org.slf4j.LoggerFactory
import slick.jdbc.H2Profile.api._


/**
  * Main application settings/inits/etc.
  */
class ScalatraBootstrap extends LifeCycle {

  private val logger = LoggerFactory.getLogger(getClass)

  val cpds = new ComboPooledDataSource
  logger.info("Created c3p0 connection pool")

  override def init(context: ServletContext): Unit = {
    val db = Database.forDataSource(cpds, None)

    // for some reason this doesn't work like you think it should
    context.mount(new AppController, "/*")
    context.mount(new AirportController(db), "/airports/*")
    context.mount(new CountryController(db), "/countries/*")
    context.mount(new ReportController(db), "/report/*")

    // provision the database - we need data in there!
    db.run(DBIO.seq(Tables.createDatabase))
  }

  private def closeDbConnection(): Unit = {
    logger.info("Closing c3po connection pool")
    cpds.close()
  }

  override def destroy(context: ServletContext): Unit = {
    super.destroy(context)
    closeDbConnection()
  }
}
