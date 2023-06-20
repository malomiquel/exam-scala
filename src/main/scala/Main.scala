import scopt.OParser
import scalaj.http.{Http, HttpRequest}
import play.api.libs.json.{Json, JsArray}

case class Config(limit: Int = 10, keyword: String = "")
case class WikiPage(title: String, words: Int)

trait HttpUtils {
  def parse(url: String): HttpRequest
}

object HttpUtils extends HttpUtils {
  override def parse(url: String): HttpRequest = Http(url)
}

object Main extends App {
  parseArguments(args) match {
    case Some(config) => run(config)
    case _            => println("Unable to parse arguments")
  }

  def parseArguments(args: Array[String]): Option[Config] = {
    val builder = OParser.builder[Config]
    val parser = {
      import builder._
      OParser.sequence(
        programName("WikiStats"),
        opt[Int]('l', "limit")
          .action((x, c) => c.copy(limit = x))
          .text(
            "La limite du nombre de pages renvoyés par l’API (optionnel, valant 10 par défaut)"
          ),
        opt[String]('k', "keyword")
          .required()
          .action((x, c) => c.copy(keyword = x))
          .text("Le mot clef est un argument obligatoire")
      )
    }
    OParser.parse(parser, args, Config())
  }

  def formatUrl(keyword: String, limit: Int): String =
    s"https://en.wikipedia.org/w/api.php?action=query&format=json&prop=&sroffset=0&list=search&srsearch=$keyword&srlimit=$limit"

  def getPages(url: String, httpUtils: HttpUtils): Either[Int, String] = {
    val response = httpUtils.parse(url).asString
    response.code match {
      case 200 => Right(response.body)
      case _   => Left(response.code)
    }
  }

  def parseJson(rawJson: String): Seq[WikiPage] = {
    val json = Json.parse(rawJson)
    val pages = (json \ "query" \ "search").as[JsArray]
    pages.value.map { page =>
      val title = (page \ "title").as[String]
      val words = (page \ "wordcount").as[Int]
      WikiPage(title, words)
    }
  }

  def totalWords(pages: Seq[WikiPage]): Int = {
    pages.foldLeft(0)((acc, page) => acc + page.words)
  }

  def displayPages(pages: Seq[WikiPage]): Unit = {
    println(s"Nombre de pages: ${pages.length}")
    println(s"Nombre total de mots: ${totalWords(pages)}")
    println(
      s"Nombre moyen de mots par page: ${totalWords(pages) / pages.length}"
    )
    pages.foreach { page =>
      println(page)
    }
  }

  def run(config: Config): Unit = {
    val url = formatUrl(config.keyword, config.limit)
    getPages(url, httpUtils = HttpUtils) match {
      case Right(body) => displayPages(parseJson(body))
      case Left(code)  => println(s"Error: $code")
    }
  }
}
