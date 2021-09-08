package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class CheckPauseTimeAndResponse extends Simulation {

  val httpConf = http.baseUrl("https://reqres.in")
    .header("Accept", value = "application/json")

  val scn = scenario("User API calls")
    .exec(http("list all users")
      .get("/api/users?page=2")
      .check(status.is(200)))

    .pause(5)

    .exec(http("single user API")
      .get("/api/users/2")
      .check(status.in(200 to 210)))

    .pause(1, 5)

    .exec(http("single user not found API")
      .get("/api/users/2xxx")
      .check(status.not(400), status.not(500)))

    .pause(3000.millisecond)

    setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)

}
