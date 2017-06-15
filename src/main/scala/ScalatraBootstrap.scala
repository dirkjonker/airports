import com.mchange.v2.c3p0.ComboPooledDataSource
import org.slf4j.LoggerFactory
import airports._
import org.scalatra._
import javax.servlet.ServletContext

import airports.datamodel.Tables
import slick.jdbc.H2Profile.api._


/**
  * Main application settings/inits/etc.
  */
class ScalatraBootstrap extends LifeCycle {

  private val logger = LoggerFactory.getLogger(getClass)

  val cpds = new ComboPooledDataSource
  logger.info("Created c3p0 connection pool")

  override def init(context: ServletContext): Unit = {
    // TODO provision the database here
    val db = Database.forDataSource(cpds, None)
    context.mount(new AirportsApp(db), "/*")

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
