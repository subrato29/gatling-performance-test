package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class AddUserSimulation extends Simulation {
  val httpConf = http.baseUrl("https://reqres.in")
    .header("Accept", "application/json")
    .header("content-type", "application/json")

  val scn = scenario("POST- Add user scenario")
    .exec(http("add user request")
      .post("/api/users")
      .body(RawFileBody("./src/test/resources/bodies/AddUser.json")).asJson
      .header("content-type", "application/json")
      .check(status is 201))

    .pause(3)

    .exec(http("get user request")
      .get("/api/users/2")
      .check(status is 200)
      )

    .pause(2)

    .exec(http("get all users request")
      .get("/api/users?page=2")
      .check(status is 200)
    )

  setUp(scn.inject(atOnceUsers(100))).protocols(httpConf)
}
