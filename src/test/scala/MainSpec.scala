import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MainSpec extends AnyFlatSpec with Matchers {
  "formatUrl" should "return a valid url" in {
    val keyword = "scala"
    val limit = 10
    val url = Main.formatUrl(keyword, limit)
    url should be(
      "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=&sroffset=0&list=search&srsearch=scala&srlimit=10"
    )
  }

  "parseJson" should "return a list of WikiPage" in {
    val rawJson =
      """{
    "batchcomplete": true,
    "continue": {
        "sroffset": 10,
        "continue": "-||"
    },
    "query": {
        "searchinfo": {
            "totalhits": 9157,
            "suggestion": "salt",
            "suggestionsnippet": "salt"
        },
        "search": [
            {
                "ns": 0,
                "title": "Scala",
                "pageid": 35200598,
                "size": 3302,
                "wordcount": 426,
                "snippet": "up <span class=\"searchmatch\">Scala</span>, <span class=\"searchmatch\">scala</span>, or <span class=\"searchmatch\">scală</span> in Wiktionary, the free dictionary. <span class=\"searchmatch\">Scala</span> or <span class=\"searchmatch\">SCALA</span> may refer to: Renault <span class=\"searchmatch\">Scala</span>, multiple automobile models Škoda <span class=\"searchmatch\">Scala</span>, a Czech",
                "timestamp": "2023-01-21T02:20:11Z"
            },
            {
                "ns": 0,
                "title": "Scala (programming language)",
                "pageid": 3254510,
                "size": 103220,
                "wordcount": 9603,
                "snippet": "<span class=\"searchmatch\">Scala</span> (/ˈskɑːlə/ SKAH-lah) is a strong statically typed high-level general-purpose programming language that supports both object-oriented programming",
                "timestamp": "2023-06-16T09:15:37Z"
            }
            ]
        }
    }"""
    val pages = Main.parseJson(rawJson)
    pages should have size 2
    pages.head.title should be("Scala")
    pages.head.words should be(426)
  }

  "totalWords" should "return 0 when the list is empty" in {
    val pages = Seq.empty
    val total = Main.totalWords(pages)
    total should be(0)
  }

  "totalWords" should "return the total number of words when the list is not empty" in {
    val pages = Seq(
      WikiPage("Scala", 426),
      WikiPage("Scala (programming language)", 9603)
    )
    val total = Main.totalWords(pages)
    total should be(10029)
  }

  "parseArguments" should "return an error when the arguments are not valid" in {
    val args = Array("-l", "EF", "-k", "fd")
    val config = Main.parseArguments(args)
    config should be(None)
  }

  "parseArguments" should "return an error when the required argument is not valid pass" in {
    val args = Array("-l", "10")
    val config = Main.parseArguments(args)
    config should be(None)
  }

  "parseArguments" should "return a Config when the arguments are valid" in {
    val args = Array("-l", "10", "-k", "scala")
    val config = Main.parseArguments(args)
    config should be(Some(Config(10, "scala")))
  }
}
