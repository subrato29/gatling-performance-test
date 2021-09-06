package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class TestAPISimulation extends Simulation {

  //http configuration
  val httpConf = http.baseUrl("https://reqres.in")
    .header("Accept", "application/json")
    .header("content-type", "application/json")

  //scenario
    val scn = scenario("GET user")
      .exec(http("GET user request")
        .get("/api/users/2")
        .check(status is 200))

  //setup
    setUp(scn.inject(atOnceUsers(1000))).protocols(httpConf)

}
