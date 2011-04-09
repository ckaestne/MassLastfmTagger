import java.net.{URLEncoder, URL}
import collection.mutable.Map


object TopTags {
    val baseURL = "http://ws.audioscrobbler.com/2.0/?" +
            "method=artist.gettoptags&" +
            "api_key=c561f0f547c3c57da054bdcdca3e4e5a&" +
            "artist="

    def topTagsFor(artist: String) = baseURL + (URLEncoder encode artist)


    import scala.xml._;
    import java.net._;

    var cache: Map[String, Seq[String]] = Map()


    def fetchTopTags(artist: String): Seq[String] =
        cache.getOrElseUpdate(artist,
            if (artist == "") List()
            else {
                val (success, body) = Http request topTagsFor(artist) /* 1 */
                if (!success) {
                    println(artist + ": error")
                    List()
                } else {
                    val xml = XML.load(body) /* 2 */

                    if (!(xml \\ "error code" isEmpty)) {
                        println(artist + ": " + (xml \\ "error code" text))
                        List()
                    } else
                        for (tag <- xml \\ "tag") yield tag \\ "name" text
                }
            })

}