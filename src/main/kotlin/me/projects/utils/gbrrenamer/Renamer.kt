@file:JvmName("Main") // you can specify 'Main' in build.gradle: jar { manifest { 'Main-Class': 'me.projects.utils.gbrrenamer.Main' } }
package me.projects.utils.gbrrenamer

import java.io.File
import java.util.*

val gPattern = """[gG]([1-9][0-9]*)""".toRegex()

// java -jar altium-designer-gbr-renamer-1.0.0.jar -src=. -dst=./gbr -fname=filename_123
fun main(args: Array<String>) {
    val props = toProps(args)
    val srcFiles = File(props["src"] ?: ".").listFiles().toList()
    val dst = props["dst"] ?: ".${File.separator}gbr"
    val fName = props["fname"] ?: "filename_123"

    // clean dst
    File(dst).deleteRecursively()

    // rename files and copy them to dst
    toSrcDstMap(fName, srcFiles, dst).entries
            .forEach { it.key.copyTo(it.value) }
}

fun toSrcDstMap(fName: String, srcFiles: List<File>, dst: String): Map<File, File> {

    val fvRegex = """_(\d+)$""".toRegex()
    val fileVersion = fvRegex.find(fName)?.groupValues?.get(1) ?: "VER"
    val fileName = fName.replace(fvRegex, "")
    val destination = if (dst.indexOf(File.separatorChar) == dst.length - 1) dst else "$dst${File.separator}"

    val gblNumber = calcGblNumber(srcFiles)
    val projectName = fetchProjectName(srcFiles)

    return srcFiles.mapNotNull {

        var suffix: String? = null
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

        // G
        // {projectName}.G{n} -> {fileName}_L{n+1}_{fileVersion}.gbr
        gPattern.find(it.extension)?.let {
            suffix = "L${it.groupValues[1].toInt() + 1}"
        }

        // GP
        // {projectName}.GP{n} -> {fileName}_L{gblNum + n}_{fileVersion}.gbr
        """[gG][pP]([1-9][0-9]*)""".toRegex().find(it.extension)?.let {
            suffix = "L${gblNumber + it.groupValues[1].toInt()}"
        }

        // TXT
        // {projectName}.txt -> {fileName}_{fileVersion}.drl
        if (it.nameWithoutExtension == projectName && it.extension.equals("TXT", ignoreCase = true)) {
            it to File("$destination${fileName}_$fileVersion.drl")
        } else if (suffix != null) {
            it to File("$destination${fileName}_${suffix}_$fileVersion.$ext")
        } else null
    }.toMap()
}

fun calcGblNumber(files: List<File>): Int {

    var maxGNum = 0

    val exts = files.map { it.extension }.distinct()

    if (exts.size == 1 && exts[0].equals("GBL", ignoreCase = true)) {
        return 1
    }

    exts.forEach {
        gPattern.find(it)?.let {
            val layNum = it.groupValues[1].toInt()
            if (layNum > maxGNum) {
                maxGNum = layNum
            }
        }
    }
    return maxGNum + 2
}

fun fetchProjectName(files: List<File>): String {
    return files.firstOrNull { it.extension.equals("GTL", ignoreCase = true) }
            ?.nameWithoutExtension ?: throw RuntimeException("GTL file not found")
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