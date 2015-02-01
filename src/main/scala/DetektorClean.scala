import java.io.File
import java.util.logging.Level

import org.jaudiotagger.audio.mp3.MP3File
import org.jaudiotagger.audio.mp4.Mp4TagReader
import org.jaudiotagger.audio.{AudioFile, AudioFileIO}
import org.jaudiotagger.audio.generic.{AudioFileWriter, AudioFileReader}
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.datatype.AbstractDataType
import org.jaudiotagger.tag.id3.{AbstractID3Tag, AbstractTagItem}

/**
 * Gets title and artist tag from file name, loads genre from last.fm
 */
object DetektorClean {

    def main(args: Array[String]) {
        AudioFileIO.logger.setLevel(Level.WARNING)
        AudioFileReader.logger.setLevel(Level.WARNING)
        AudioFileWriter.logger.setLevel(Level.WARNING)
        AudioFile.logger.setLevel(Level.WARNING)
        AbstractTagItem.logger.setLevel(Level.WARNING)
        AbstractID3Tag.logger.setLevel(Level.WARNING)
        AbstractDataType.logger.setLevel(Level.WARNING)
        Mp4TagReader.logger.setLevel(Level.SEVERE)

        if (args.size <= 0)
            println("target file or directory as parameter required")
        else
            for (file <- args)
                tag(new File(file))
    }


    def tagFile(file: File) {
        file.setWritable(true)
        val audio = AudioFileIO.read(file)
        if (audio.isInstanceOf[MP3File]) {
            val f = audio.asInstanceOf[MP3File]
            val tag = f.getID3v2Tag()
            val n = file.getName
            val artisttitle = n.take(n.lastIndexOf(".")).split("   ").map(_.trim)
            if (artisttitle.size == 2) {
                val artist = artisttitle(0)
                val title = artisttitle(1)
                val lastfmTags = TopTags.fetchTopTags(artist)
                val tagStr = lastfmTags.take(5).mkString(", ")

                println(s"$artist - $title: $tagStr")
                tag.setField(FieldKey.GENRE, tagStr)
                tag.setField(FieldKey.COMMENT, "")
                tag.setField(FieldKey.ARTIST, artist)
                tag.setField(FieldKey.TITLE, title)

                AudioFileIO.write(f)
            } else {
                println(s"File $n cannot be split in artist and title: $artisttitle")
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
        try {
            if (file.isFile && file.getName.endsWith(".mp3"))
                tagFile(file)
            else if (file.isDirectory)
                tagDir(file)
        } catch {
            case e: Exception => e.printStackTrace
        }
    }

}
