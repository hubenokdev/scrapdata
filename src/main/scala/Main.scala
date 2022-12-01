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

      var retval =  List("Title: " + hTitle.text)

      // get state name
      val pMainContainer = doc >> element(".buysell-main-container") 
      for (pelement <- pMainContainer.children){ 
        if (pelement.outerHtml contains "buysell-container-right buysell-profileinfo")
        {
          //println(pelement.outerHtml)
          val idx = pelement.outerHtml indexOf "gif\" alt="
          if (idx > 0)
          {
            var s_region = pelement.outerHtml.substring(idx + 12)
            val idx_end = s_region indexOf "</span>"
            retval :+= "region: " + s_region.substring(0, idx_end)
          }
        }
      }
      
      // get price value
      retval :+= ("Price : " + (doc >> element(".buysell-main-container") >> element(".buysell-container-right")).text)

      // get specs of good
      val pElements = doc >> element(".buysell-main-container") >> elements(".buysell-details-column")
      for (pElement <- pElements){  
        // split with break string
        val inner_tags = pElement.innerHtml.split("\\n").map(_.replace("<br>", "").replace("<b>", "").replace("</b>", "")) 
        for (item <- inner_tags)
        {
          retval :+= item
        }
      }

      // get description text
      val pContainerLeft = doc >> element(".buysell-container-left")
      for (pelement <- pContainerLeft.children){ 
        if (pelement.outerHtml contains "buysell-container description")
          {
            retval :+= ("Description: " + pelement.text)
          }
      } 

      retval  
    }
}
