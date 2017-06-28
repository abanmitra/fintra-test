package org.fitman

import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.twitter.finagle.http.Status

class HelloControllerTest extends FeatureTest {
  override val server: EmbeddedHttpServer  = new EmbeddedHttpServer (twitterServer = new FitmanServer)

  "Say Hello" in {

    server.httpGet(
      path = "/hello",
      andExpect = Status.Ok,
      withBody = "This is Finatra Hello..."
    )
  }
}
