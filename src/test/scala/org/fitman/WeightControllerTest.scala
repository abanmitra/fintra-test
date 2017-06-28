package org.fitman

import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.twitter.finagle.http.Status

class WeightControllerTest extends FeatureTest {
  override val server: EmbeddedHttpServer  = new EmbeddedHttpServer (twitterServer = new FitmanServer)

  "WeightResource" should  {

    "Save user weight when POST request is made" in {
      server.httpPost(

        path = "/weights",
        postBody =
          """
            |{
            |"user":"aban",
            |"weight":55,
            |"status":"Feeling great!!!"
            |}
          """.stripMargin,
        andExpect = Status.Created,
        withLocation = "/weights/aban"
      )
    }
  }

  "List all weights for a user when GET request is made" in {
    val response = server.httpPost(
      path = "/weights",
      postBody =
        """
          |{
          |"user":"test_user_1",
          |"weight":80,
          |"posted_at" : "2016-01-03T14:34:06.871Z"
          |}
        """.stripMargin,
      andExpect = Status.Created
    )

    server.httpGetJson[List[Weight]](
      path = response.location.get,
      andExpect = Status.Ok,
      withJsonBody =
        """
          |[
          |  {
          |    "user" : "test_user_1",
          |    "weight" : 80,
          |    "posted_at" : "2016-01-03T14:34:06.871Z"
          |  }
          |]
        """.stripMargin
    )
  }

}
