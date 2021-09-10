package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DataFeederCSV extends Simulation {

  //Circular, Shuffle, Random, Queue
  val httpConf = http.baseUrl("https://gorest.co.in")
    .header("Authorization", "Bearer a67595542e465e56728c1499c323bc2acd1dbbfdbae9e1f53419729e00a1bd38")

  val csvFeeder = csv("./src/test/resources/data/getUser.csv").circular

  def getAllUsers() = {
    repeat(7) {
      feed(csvFeeder)
        .exec(http("Get Single user request")
        .get("/public/v1/users/${userid}")
        .check(jsonPath("$.data.name").is("${name}"))
        .check(status.in(200, 304)))
        .pause(2)
    }
  }

  val scn = scenario("CSV FEEDER test").exec(getAllUsers())

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)

}
