package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.datasource.HymnCatalog
import com.example.data.datasource.SeedHymns
import com.example.data.model.Hymn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Triune Hymn", appName)
  }

  @Test
  fun `seed hymns are valid and ordered`() {
    val hymns = SeedHymns.initialHymns
    assertTrue(hymns.isNotEmpty())
    assertEquals(1, hymns.first().number)
    assertEquals("Holy, Holy, Holy! Lord God Almighty", hymns.first().title)
  }

  @Test
  fun `full 1000 hymns catalog ranges are correct`() {
    val all = HymnCatalog.getFullHymnLibrary()
    assertEquals(1000, all.size)
    assertEquals(1, all.first().number)
    assertEquals(1000, all.last().number)

    // 601-750 Christmas
    val christmas = all.filter { it.number in 601..750 }
    assertEquals(150, christmas.size)
    assertTrue(christmas.all { it.category == "Christmas" })

    // 751-850 Easter
    val easter = all.filter { it.number in 751..850 }
    assertEquals(100, easter.size)
    assertTrue(easter.all { it.category == "Easter" })

    // 851-1000 Others
    val others = all.filter { it.number in 851..1000 }
    assertEquals(150, others.size)
  }

  @Test
  fun `hymn model formatting`() {
    val hymn = Hymn(
      id = "1",
      number = 1,
      title = "Praise",
      lyrics = "Verse 1\nVerse 2",
      category = "Praise"
    )
    assertNotNull(hymn.formattedLyrics())
    assertEquals(1, hymn.number)
  }
}
