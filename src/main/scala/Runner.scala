import java.io.File
import org.farng.mp3.{AbstractMP3Tag, MP3File}
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey



object Runner extends Application {


    println("hi")

    val sourceFile = new File("src/test/resources/10 Suicide.mp3");

    val f = AudioFileIO.read(sourceFile);
    val tag = f.getTag();


    println(tag.getFirst(FieldKey.ARTIST))
}