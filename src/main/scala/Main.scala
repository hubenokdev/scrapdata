import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.model._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

object Main {
    def main(args: Array[String]) = {
      println("Looking contents: " + args(0))
      val contents = get_contents_from_url(args(0))

      contents.foreach(println)
      println("---------------------")   
    }

    def get_contents_from_url(url: String): List[String] = {
      val browser = JsoupBrowser()
      val doc = browser.get(url)

      // find title tag
      val hTitle = doc >> element(".buysell-title") >> element("h1")
      // find requirement content tag
      val pElements = doc >> element(".buysell-main-container") >> element(".buysell-details-column")
      
      // split with break string
      val arrays_htags = pElements.innerHtml.split("\\n")

      // remove html tags <br>, <b>, </bget contents data 
      val retval = List("Title: " + hTitle.text) ++ arrays_htags.map(_.replace("<br>", "").replace("<b>", "").replace("</b>", ""))
      
 
      retval  
    }
}