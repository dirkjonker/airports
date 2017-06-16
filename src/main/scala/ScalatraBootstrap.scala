import javax.servlet.ServletContext

import airports.controllers.AppController
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
    context.mount(new AppController(db), "/*")
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
