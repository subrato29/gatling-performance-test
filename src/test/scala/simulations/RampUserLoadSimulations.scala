package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps

class RampUserLoadSimulations extends Simulation {

  val httpConf = http.baseUrl("https://gorest.co.in")
    .header("Authorization", "Bearer a67595542e465e56728c1499c323bc2acd1dbbfdbae9e1f53419729e00a1bd38")

  val csvFeeder = csv("./src/test/resources/data/getUser.csv").circular

  def getAllUsers() = {
    repeat(1) {
      feed(csvFeeder)
        .exec(http("Get Single user request")
          .get("/public/v1/users/${userid}")
          .check(jsonPath("$.data.name").is("${name}"))
          .check(status.in(200, 304)))
        .pause(2)
    }
  }

  val scn = scenario("Ramp users load simulation").exec(getAllUsers())

  setUp(scn.inject(nothingFor(5),
    constantUsersPerSec(10) during(10 seconds),
    rampUsersPerSec(1) to (5) during(20 seconds)
  ).protocols(httpConf)
  )
}
