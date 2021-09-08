package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Correlation extends Simulation {

  val httpConf = http.baseUrl("https://gorest.co.in")
    .header("Authorization", "Bearer a67595542e465e56728c1499c323bc2acd1dbbfdbae9e1f53419729e00a1bd38")

  val scn = scenario("Check correlation and extract data")

    //first call- get all the users
    .exec(http("GET all users")
      .get("/public/v1/users")
      .check(jsonPath("$.data[0].id").saveAs("userId")))

  //second call- get a specific user based on id
    .exec(http("GET specific user")
      .get("/public/v1/users/${userId}")
      .check(jsonPath("$.data.id").is("1844"))
      .check(jsonPath("$.data.name").is("MoureDev"))
      .check(jsonPath("$.data.email").is("mour@mail.com")))

    setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)

}
