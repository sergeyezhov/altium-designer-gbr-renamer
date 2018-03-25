package me.projects.utils.gbrrenamer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.File

class RenamerTest {

    @Test
    fun `calc gbl number`() {

        assertEquals(4, calcGblNumber(arrayOf(
                "плата питания1.GTL",
                "плата питания1.GBL",
                "плата питания1.G1",
                "плата питания1.G2"
        ).map { File(it) }))


        assertEquals(2, calcGblNumber(arrayOf(
                "плата питания1.GTL",
                "плата питания1.GBL"
        ).map { File(it) }))

        assertEquals(2, calcGblNumber(arrayOf(
                "плата питания1.GTL"
        ).map { File(it) }))

        assertEquals(1, calcGblNumber(arrayOf(
                "плата питания1.GBL"
        ).map { File(it) }))

        assertEquals(4, calcGblNumber(arrayOf(
                "плата питания1.GTL",
                "плата питания1.GBL",
                "плата питания1.G1",
                "плата питания1.G2",
                "программа.exe"
        ).map { File(it) }))

        assertEquals(7, calcGblNumber(arrayOf(
                "плата питания1.GTL",
                "плата питания1.GBL",
                "плата питания1.G1",
                "плата питания1.G2",
                "плата питания1.GP1",
                "плата питания1.GP2",
                "плата питания1.GP3"
        ).map { File(it) }))
    }

    @Test
    fun `calc max G number`() {
        assertEquals(2, calcMaxGNumber(arrayOf(
                "плата питания1.GTL",
                "плата питания1.GBL",
                "плата питания1.G1",
                "плата питания1.G2",
                "плата питания1.GP1",
                "плата питания1.GP2",
                "плата питания1.GP3"
        ).map { File(it) }))

        assertEquals(0, calcMaxGNumber(arrayOf(
                "плата питания1.GTL",
                "плата питания1.GBL",
                "плата питания1.GP1",
                "плата питания1.GP2",
                "плата питания1.GP3"
        ).map { File(it) }))
    }

    @Test
    fun `to props`() {
        var props = toProps(arrayOf("-src", ".", "-dst", "./folder"))
        assertEquals(".", props["src"])
        assertEquals("./folder", props["dst"])


        props = toProps(arrayOf("-src", ".", "-dst"))
        assertEquals(".", props["src"])
        assertNull(props["dst"])

        props = toProps(arrayOf("-src=.", "-dst=./folder"))
        assertEquals(".", props["src"])
        assertEquals("./folder", props["dst"])
    }

    @Test
    fun `to src dst map`() {

        var map = toSrcDstMap("filename_123", listOf(
                "плата питания1.GTL",
                "плата питания1.G1",
                "плата питания1.G2",
                "плата питания1.GBL"
        ).map { File(it) }, "./folder")

        assertEquals("./folder/filename_L1_123.gbr", map[File("плата питания1.GTL")]?.path)
        assertEquals("./folder/filename_L2_123.gbr", map[File("плата питания1.G1")]?.path)
        assertEquals("./folder/filename_L3_123.gbr", map[File("плата питания1.G2")]?.path)
        assertEquals("./folder/filename_L4_123.gbr", map[File("плата питания1.GBL")]?.path)

        map = toSrcDstMap("filename_123", listOf(
                "плата питания1.GTL",
                "плата питания1.GBL"
        ).map { File(it) }, "./folder")

        assertEquals("./folder/filename_L1_123.gbr", map[File("плата питания1.GTL")]?.path)
        assertEquals("./folder/filename_L2_123.gbr", map[File("плата питания1.GBL")]?.path)
    }

    @Test
    fun `to src dst map - no G`() {

        val map = toSrcDstMap("filename_123", listOf(
                "плата питания1.GTL",
                "плата питания1.GP1",
                "плата питания1.GP2",
                "плата питания1.GBL"
        ).map { File(it) }, "./folder")

        assertEquals("./folder/filename_L1_123.gbr", map[File("плата питания1.GTL")]?.path)
        assertEquals("./folder/filename_L2_123.gbr", map[File("плата питания1.GP1")]?.path)
        assertEquals("./folder/filename_L3_123.gbr", map[File("плата питания1.GP2")]?.path)
        assertEquals("./folder/filename_L4_123.gbr", map[File("плата питания1.GBL")]?.path)
    }

    @Test
    fun `to src dst map - real example #1`() {

        val map = toSrcDstMap("filename_123", listOf(
                "Status Report.Txt",
                "плата питания1-macro.APR_LIB",
                "плата питания1.apr",
                "плата питания1.DRR",
                "плата питания1.EXTREP",
                "плата питания1.GBL",
                "плата питания1.GBO",
                "плата питания1.GBS",
                "плата питания1.GM17",
                "плата питания1.GTL",
                "плата питания1.GTO",
                "плата питания1.GTS",
                "плата питания1.LDP",
                "плата питания1.REP",
                "плата питания1.RUL",
                "плата питания1.TXT"
        ).map { File(it) }, "./folder")

        assertEquals(8, map.size)
        assertEquals("./folder/filename_L2_123.gbr", map[File("плата питания1.GBL")]?.path)
        assertEquals("./folder/filename_S2_123.gbr", map[File("плата питания1.GBO")]?.path)
        assertEquals("./folder/filename_M2_123.gbr", map[File("плата питания1.GBS")]?.path)
        assertEquals("./folder/filename_KR_123.gbr", map[File("плата питания1.GM17")]?.path)
        assertEquals("./folder/filename_L1_123.gbr", map[File("плата питания1.GTL")]?.path)
        assertEquals("./folder/filename_S1_123.gbr", map[File("плата питания1.GTO")]?.path)
        assertEquals("./folder/filename_M1_123.gbr", map[File("плата питания1.GTS")]?.path)
        assertEquals("./folder/filename_123.drl", map[File("плата питания1.TXT")]?.path)
    }

    @Test
    fun `to src dst map - real example #2`() {

        val map = toSrcDstMap("filename_123", listOf(
                "Status Report.Txt",
                "плата питания1-macro.APR_LIB",
                "плата питания1.apr",
                "плата питания1.DRR",
                "плата питания1.EXTREP",
                "плата питания1.G1",
                "плата питания1.G2",
                "плата питания1.GBL",
                "плата питания1.GBO",
                "плата питания1.GBS",
                "плата питания1.GM17",
                "плата питания1.GP1",
                "плата питания1.GP2",
                "плата питания1.GTL",
                "плата питания1.GTO",
                "плата питания1.GTS",
                "плата питания1.LDP",
                "плата питания1.REP",
                "плата питания1.RUL",
                "плата питания1.TXT"
        ).map { File(it) }, "./folder")

        assertEquals(12, map.size)
        assertEquals("./folder/filename_L2_123.gbr", map[File("плата питания1.G1")]?.path)
        assertEquals("./folder/filename_L3_123.gbr", map[File("плата питания1.G2")]?.path)
        assertEquals("./folder/filename_L6_123.gbr", map[File("плата питания1.GBL")]?.path)
        assertEquals("./folder/filename_S2_123.gbr", map[File("плата питания1.GBO")]?.path)
        assertEquals("./folder/filename_M2_123.gbr", map[File("плата питания1.GBS")]?.path)
        assertEquals("./folder/filename_KR_123.gbr", map[File("плата питания1.GM17")]?.path)
        assertEquals("./folder/filename_L4_123.gbr", map[File("плата питания1.GP1")]?.path)
        assertEquals("./folder/filename_L5_123.gbr", map[File("плата питания1.GP2")]?.path)
        assertEquals("./folder/filename_L1_123.gbr", map[File("плата питания1.GTL")]?.path)
        assertEquals("./folder/filename_S1_123.gbr", map[File("плата питания1.GTO")]?.path)
        assertEquals("./folder/filename_M1_123.gbr", map[File("плата питания1.GTS")]?.path)
        assertEquals("./folder/filename_123.drl", map[File("плата питания1.TXT")]?.path)
    }

    @Test
    fun `to src dst map - 16 ~tons~ layers`() {
        val map = toSrcDstMap("filename_123", listOf(
                "Status Report.Txt",
                "плата питания1-macro.APR_LIB",
                "плата питания1.apr",
                "плата питания1.DRR",
                "плата питания1.EXTREP",
                "плата питания1.G1",
                "плата питания1.G2",
                "плата питания1.G3",
                "плата питания1.G4",
                "плата питания1.G5",
                "плата питания1.G6",
                "плата питания1.G7",
                "плата питания1.G8",
                "плата питания1.G9",
                "плата питания1.G10",
                "плата питания1.G11",
                "плата питания1.G12",
                "плата питания1.GBL",
                "плата питания1.GBO",
                "плата питания1.GBS",
                "плата питания1.GM17",
                "плата питания1.GP1",
                "плата питания1.GP2",
                "плата питания1.GTL",
                "плата питания1.GTO",
                "плата питания1.GTS",
                "плата питания1.LDP",
                "плата питания1.REP",
                "плата питания1.RUL",
                "плата питания1.TXT"
        ).map { File(it) }, "./folder")

        assertEquals(22, map.size)
        assertEquals("./folder/filename_L2_123.gbr", map[File("плата питания1.G1")]?.path)
        assertEquals("./folder/filename_L3_123.gbr", map[File("плата питания1.G2")]?.path)
        assertEquals("./folder/filename_L4_123.gbr", map[File("плата питания1.G3")]?.path)
        assertEquals("./folder/filename_L5_123.gbr", map[File("плата питания1.G4")]?.path)
        assertEquals("./folder/filename_L6_123.gbr", map[File("плата питания1.G5")]?.path)
        assertEquals("./folder/filename_L7_123.gbr", map[File("плата питания1.G6")]?.path)
        assertEquals("./folder/filename_L8_123.gbr", map[File("плата питания1.G7")]?.path)
        assertEquals("./folder/filename_L9_123.gbr", map[File("плата питания1.G8")]?.path)
        assertEquals("./folder/filename_L10_123.gbr", map[File("плата питания1.G9")]?.path)
        assertEquals("./folder/filename_L11_123.gbr", map[File("плата питания1.G10")]?.path)
        assertEquals("./folder/filename_L12_123.gbr", map[File("плата питания1.G11")]?.path)
        assertEquals("./folder/filename_L13_123.gbr", map[File("плата питания1.G12")]?.path)
        assertEquals("./folder/filename_L16_123.gbr", map[File("плата питания1.GBL")]?.path)
        assertEquals("./folder/filename_S2_123.gbr", map[File("плата питания1.GBO")]?.path)
        assertEquals("./folder/filename_M2_123.gbr", map[File("плата питания1.GBS")]?.path)
        assertEquals("./folder/filename_KR_123.gbr", map[File("плата питания1.GM17")]?.path)
        assertEquals("./folder/filename_L14_123.gbr", map[File("плата питания1.GP1")]?.path)
        assertEquals("./folder/filename_L15_123.gbr", map[File("плата питания1.GP2")]?.path)
        assertEquals("./folder/filename_L1_123.gbr", map[File("плата питания1.GTL")]?.path)
        assertEquals("./folder/filename_S1_123.gbr", map[File("плата питания1.GTO")]?.path)
        assertEquals("./folder/filename_M1_123.gbr", map[File("плата питания1.GTS")]?.path)
        assertEquals("./folder/filename_123.drl", map[File("плата питания1.TXT")]?.path)
    }
}