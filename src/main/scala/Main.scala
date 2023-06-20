import scopt.OParser

case class Config(limit: Int = 10, keyword: String = "")

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

  def run(config: Config): Unit = {
    println(config)
  }
}
