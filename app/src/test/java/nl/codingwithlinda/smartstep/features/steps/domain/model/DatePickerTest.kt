package nl.codingwithlinda.smartstep.features.steps.domain.model

import org.junit.Assert.*
import org.junit.Test

class DatePickerTest {

    @Test
    fun `feb 2000 returns 29 days`() {

       datePicker.onEach {
           println(it.year)
           println(it.months)
           println(it.daysInMonth)
       }

        datePicker.get(0).daysInMonth.get(1).also {
            assertEquals(29, it)

        }
    }

}