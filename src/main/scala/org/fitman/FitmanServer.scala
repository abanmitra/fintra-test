package org.fitman

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.http.{Controller, HttpServer}
import org.joda.time.Instant

import scala.collection.mutable

object FitmanApp extends FitmanServer

class FitmanServer extends HttpServer {

  override protected def defaultFinatraHttpPort: String = ":8080"
  override protected def defaultTracingEnabled: Boolean = false
  override protected def defaultHttpServerName: String = "Fitman"

  override protected def configureHttp(router: HttpRouter) : Unit = {
    router.add[HelloController]
    router.add[WeightController]
  }
}

class HelloController extends Controller {
  get("/hello") { req: Request =>
    "This is Finatra Hello..."
  }
}

case class Weight (user: String, weight: Int, status: Option[String], postedAt: Instant = Instant.now())

class WeightController extends Controller {

  val db = mutable.Map[String, List[Weight]]()

  post("/weights") { weight: Weight =>
    val weightForUser = db.get(weight.user) match {
      case Some(weights) => weights :+ weight
      case None => List(weight)
    }
    db.put(weight.user, weightForUser)
    response.created.location(s"/weights/${weight.user}")
  }

  get("/weights/:user") { request: Request =>
    db.getOrElse(request.params("user"), List())
  }
}
