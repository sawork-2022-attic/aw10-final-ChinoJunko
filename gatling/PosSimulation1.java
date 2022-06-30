/*
 * Copyright 2011-2022 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pos;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.time.Duration;

public class PosSimulation1 extends Simulation {

  HttpProtocolBuilder httpProtocol =
      http
          // Here is the root for all relative URLs
          .baseUrl("http://localhost:8080")
          // Here are the common headers
          .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
          .doNotTrackHeader("1")
          .acceptLanguageHeader("en-US,en;q=0.5")
          .acceptEncodingHeader("gzip, deflate")
          .userAgentHeader(
              "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0");

  // A scenario is a chain of requests and pauses
  

  ScenarioBuilder scn =
      scenario("Pos_customer")
          .exec(http("list_products").get("/products"))
          .pause(1)
          .exec(http("get_product1").get("/products/13284888"))
          .pause(Duration.ofMillis(150))
          .exec(http("get_product2").get("/products/13122155"))
          .pause(Duration.ofMillis(150))
          .exec(http("create_cart").post("/carts"))
          .pause(Duration.ofMillis(300))
          .exec(http("add_cartitem").post("/carts/1?productId=13284888&productCount=3"))
          .pause(Duration.ofMillis(150))
          .exec(http("get_cart").get("/carts/1"))
          .pause(Duration.ofMillis(150))
          .exec(http("checkout").get("/carts/1/total"))
          .pause(Duration.ofMillis(150))
          .exec(http("create_order").post("/orders"))
          .pause(Duration.ofMillis(150))
          .exec(http("list_order").get("/orders"))
          .pause(Duration.ofMillis(150))
          .exec(http("get_delivery").get("/delivery/1"))
          .pause(Duration.ofMillis(150));
  {
    setUp(scn.injectOpen(atOnceUsers(500)).protocols(httpProtocol));
  }
}
