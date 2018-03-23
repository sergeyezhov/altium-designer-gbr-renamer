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
}