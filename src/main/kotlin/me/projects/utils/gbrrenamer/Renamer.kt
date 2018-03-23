package me.projects.utils.gbrrenamer

import java.io.File
import java.util.*
import java.util.regex.Pattern

val gPattern = Pattern.compile("[gG]([1-9][0-9]*)")
val fileVersionPattern = Pattern.compile("^.+(_\\d+)")

// renamer -src=. -dst=./gbr/ -fname
// GTL > G1 > GBL ===> L1 > L2 > L3
fun main(args: Array<String>) {
    val props = toProps(args)
    val srcFiles = File(props["src"] ?: ".").listFiles().toList()
    val dst = props["dst"] ?: "./gbr/"
    val fName = props["fname"] ?: "filename_123"

    // fileName = filename_123
    // srcFileNames -> dstFilenames
    // ./плата питания1.GTL -> ./gbr/filename_L1_123.gbr
    // ./плата питания1.G1 -> ./gbr/filename_L2_123.gbr
    // ./плата питания1.G2 -> ./gbr/filename_L3_123.gbr
    // ./плата питания1.GBL -> ./gbr/filename_L4_123.gbr

    toSrcDstMap(fName, srcFiles, dst).entries
            .forEach { it.key.copyTo(it.value) }
}

fun toSrcDstMap(fName: String, srcFiles: List<File>, dst: String): Map<File, File> {

    val fileVersion = fileVersionPattern.matcher(fName).group(1)
    val fileName = fName.replace(fileVersion, "_")

    val gblNumber = calcGblNumber(srcFiles)

    return srcFiles.map {

        var suffix = ""
        var ext = "gbr"

        when (it.extension.toUpperCase()) {
            "GM17" -> suffix = "KR"
            "GTO" -> suffix = "S1"
            "GBO" -> suffix = "S2"
            "GTS" -> suffix = "M1"
            "GBS" -> suffix = "M2"
            "GTL" -> suffix = "L1"
            "GBL" -> suffix = "L$gblNumber"
            "TXT" -> {
                if (it.name.contains("NonPlated")) {
                    suffix = "NMET"
                    ext = "drl"
                } else if (it.name.contains("Plated")) {
                    suffix = "MET"
                    ext = "drl"
                }
            }
        }

        val matcher = gPattern.matcher(it.extension)
        if (matcher.matches()) {
            suffix = "L${matcher.group(1).toInt() + 1}"
        }

        it to File("$fileName$suffix$fileVersion.$ext")
    }.toMap()
}

fun calcGblNumber(files: List<File>): Int {

    var maxGNum = 0

    val exts = files.map { it.extension }.distinct()

    if (exts.size == 1 && exts[0].equals("GBL", ignoreCase = true)) {
        return 1
    }

    exts.forEach {
        val matcher = gPattern.matcher(it)
        if (matcher.matches()) {
            val layNum = matcher.group(1).toInt()
            if (layNum > maxGNum) {
                maxGNum = layNum
            }
        }
    }
    return maxGNum + 2
}

fun toProps(args: Array<String>): Map<String, String> {
    val ar = args.flatMap { it.split("=") }

    val map = HashMap<String, String>()
    var i = 0
    while (i + 1 < ar.size) {
        val key = ar[i].replaceFirst("-", "") //-src --> src
        map[key] = ar[i + 1]
        i += 2
    }
    return map
}