package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class UpdateAndDeleteUserSimulations extends Simulation {

  val httpConf = http.baseUrl("https://reqres.in")
    .header("Accept", "application/json")
    .header("content-type", "application/json")

  val scn = scenario("Update user scenario")

    //Updating user
    .exec(http("update specific user")
      .put("/api/users/2")
      .body(RawFileBody("./src/test/resources/bodies/UpdateUser.json")).asJson
      .check(status.in(200 to 210)))

    .pause(3)

    //Deleting user
    .exec(http("delete user")
      .delete("/api/users/2")
      .check(status.in(200 to 204)))

  setUp(scn.inject(atOnceUsers(100))).protocols(httpConf)
}
