package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class RampUsersPerSecond extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  def getAllVideoGames() = {
    exec(
      http("Get all video games")
        .get("videogames/2")
        .check(status.is(200))
    )
  }

  def getSpecificGame() = {
    exec(
      http("Get specific game")
        .get("videogames/2")
        .check(status.is(200))
    )
  }

  val  scn = scenario("Basic Load Simulation")
    .exec(getAllVideoGames())
    .pause(5)
    .exec(getSpecificGame())
    .pause(5)
    .exec(getAllVideoGames())

  setUp(
    scn.inject(
      nothingFor(5 seconds),
      rampUsersPerSec(1) to (5) during(20 seconds),
    ).protocols(httpConf.inferHtmlResources())
  )

}
