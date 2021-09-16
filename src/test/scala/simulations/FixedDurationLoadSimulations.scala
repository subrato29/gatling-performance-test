package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps

class FixedDurationLoadSimulations extends Simulation {

  val httpConf = http.baseUrl("https://reqres.in")
    .header("Accept", "application/json")
    .header("content-type", "application/json")

  def getAllUsersRequests() = {
    repeat(2) {
      exec(http("get all users requests")
        .get("/api/users?page=2")
        .check(status.is(200)))
    }
  }

  def getSingleUserRequests() = {
    repeat(2) {
      exec(http("get single user request")
        .get("/api/users/2")
        .check(status.is(200)))
    }
  }

  def addSingleUsers() = {
    repeat(2) {
      exec(http("add single user request")
        .post("/api/users")
        .body(RawFileBody("./src/test/resources/bodies/AddUser.json")).asJson
        .check(status.is(201)))
    }
  }

  val scn = scenario("Fixed Duration Load Simulations")
    .forever() {
      exec(getAllUsersRequests())
        .pause(2)
        .exec(getSingleUserRequests())
        .pause(2)
        .exec(addSingleUsers())
    }

  setUp(
    scn.inject(
      nothingFor(5),
      atOnceUsers(10),
      rampUsers(50) during (30 seconds)
    ).protocols(httpConf)
  ).maxDuration(1 minute)
}
