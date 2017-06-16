import javax.servlet.ServletContext

import airports.controllers.AppController
import org.scalatra._
import org.slf4j.LoggerFactory


/**
  * Main application settings/inits/etc.
  */
class ScalatraBootstrap extends LifeCycle {

  private val logger = LoggerFactory.getLogger(getClass)

  override def init(context: ServletContext): Unit = {
    context.mount(new AppController, "/*")
  }

  override def destroy(context: ServletContext): Unit = {
    super.destroy(context)
  }
}
