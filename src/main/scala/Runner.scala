import java.io.File
import java.util.logging.{Level, Logger}
import org.jaudiotagger.audio._
import generic.{AudioFileWriter, AudioFileReader}
import mp4.Mp4TagReader
import org.jaudiotagger.audio.mp3._
import org.jaudiotagger.tag.datatype.AbstractDataType
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.id3.{AbstractID3Tag, AbstractTagItem}

object Runner  {

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
        val audio = AudioFileIO.read(file)
        if (audio.isInstanceOf[MP3File]) {
            val f = audio.asInstanceOf[MP3File]
            val tag = f.getID3v2Tag();
            val artist = tag.getFirst(FieldKey.ARTIST)
            val oldGenre = tag.getFirst(FieldKey.GENRE)
            val lastfmTags = TopTags.fetchTopTags(artist)
            val tagStr = lastfmTags.take(5).mkString(", ")
            println(artist + ": " + tagStr +" <- "+oldGenre)
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
        if (!file.exists)
            println("file does not exist "+file)
        else if (file.isFile)
            tagFile(file)
        else if (file.isDirectory)
            tagDir(file)
    }


}
