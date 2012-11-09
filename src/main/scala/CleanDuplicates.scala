import java.io.File

/**
 * syncing with Live Mesh somectimes creates annoying duplicates
 *
 * this program removes them
 *
 * duplicates end with " 1.mp3" or another number when the same
 * mp3 exists without that number
 *
 * call with folder, and second parameter -d to delete
 */
object CleanDuplicates {

    def main(args: Array[String]) {
        val path = args(0)
        val reallyDelete = if (args.size > 1) args(1)=="-d" else false

        clean(new File(path),reallyDelete)

    }

    def clean(dir: File, reallyDelete:Boolean) {
        if (!dir.exists) return;

        //        println(dir)



        for (subdir <- dir.listFiles)
            if (subdir.isDirectory)
                clean(subdir,reallyDelete)

        val files = dir.listFiles.filter(_.isFile)
        for (file <- files) {
            for (num <- 1 to 20) {
                val end = " " + num + ".mp3"
                if (file.getName.endsWith(end))
                    if (files.map(_.getName).contains(file.getName.dropRight(end.length) + ".mp3")) {
                        println(file)
                        if (reallyDelete)
                            file.delete
                    }
            }

        }
    }

}
