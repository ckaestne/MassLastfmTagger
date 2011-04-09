object Http {
   import java.io.InputStream;
   import java.net.URL;

   def request(urlString:String): (Boolean, InputStream) =
      try {
         val url = new URL(urlString)
         val body = url.openStream
         (true, body)
      }
      catch {
         case ex:Exception =>
             ex.printStackTrace;             (false, null)
      }


}