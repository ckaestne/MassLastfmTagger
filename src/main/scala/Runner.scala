import java.io.File
import java.util.logging.{Level, Logger}
import org.jaudiotagger.audio._
import org.jaudiotagger.audio.mp3._
import org.jaudiotagger.tag.FieldKey


object Runner  {

    def main(args: Array[String]) {
        AudioFileIO.logger.setLevel(Level.WARNING)
        if (args.size <= 0)
            println("target file or directory as parameter required")
        else
            for (file <- args)
                tag(new File(file))
    }


    def tagFile(file: File) {
        val audio = AudioFileIO.read(file)
        if (audio.isInstanceOf[MP3File]) {
            val f = audio.asInstanceOf[MP3File]
            val tag = f.getID3v2Tag();
            val artist = tag.getFirst(FieldKey.ARTIST)
            val oldGenre = tag.getFirst(FieldKey.GENRE)
            println("old tag for " + artist + ": " + oldGenre)
            val lastfmTags = TopTags.fetchTopTags(artist)
            val tagStr = lastfmTags.take(5).mkString(", ")
            println("new tag for " + artist + ": " + tagStr)
            if (!lastfmTags.isEmpty && oldGenre != tagStr) {
                tag.setField(FieldKey.GENRE, tagStr)
                AudioFileIO.write(f)
            }
        }
    }

    def tagDir(dir: File) {
        if (dir.exists) {
            for (file <- dir.listFiles)
                tag(file)
        }
    }

    def tag(file: File) {
        if (file.isFile)
            tagFile(file)
        else if (file.isDirectory)
            tagDir(file)
    }


}