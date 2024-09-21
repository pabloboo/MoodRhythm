package com.moodrhythm

import com.google.common.truth.Truth.assertThat
import com.moodrhythm.utils.FakeSharedPrefsHelper
import com.moodrhythm.utils.SharedPrefsConstants
import com.moodrhythm.utils.StatsFunctions
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.Month

class StatsFunctionsTest {

    private lateinit var statsFunctions: StatsFunctions
    private lateinit var fakeSharedPrefsHelper: FakeSharedPrefsHelper

    @Before
    fun setUp() {
        fakeSharedPrefsHelper = FakeSharedPrefsHelper()
        statsFunctions = StatsFunctions(fakeSharedPrefsHelper)
    }

    @Test
    fun testGetCurrentYear() {
        val currentYear = statsFunctions.getCurrentYear()
        val expectedYear = LocalDate.now().year
        assertThat(currentYear).isEqualTo(expectedYear)
    }

    @Test
    fun testGetMonthName() {
        val monthName = statsFunctions.getMonthName(LocalDate.now().month)
        val expectedMonthName = LocalDate.now().month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH)
        assertThat(monthName).isEqualTo(expectedMonthName)
    }

    @Test
    fun testPreviousMonth() {
        val result = statsFunctions.previousMonth(Month.DECEMBER)
        assertThat(result).isEqualTo(Month.NOVEMBER)
        val result2 = statsFunctions.previousMonth(Month.JANUARY)
        assertThat(result2).isEqualTo(Month.DECEMBER)
    }

    @Test
    fun testNextMonth() {
        val result = statsFunctions.nextMonth(Month.JANUARY)
        assertThat(result).isEqualTo(Month.FEBRUARY)
        val result2 = statsFunctions.nextMonth(Month.DECEMBER)
        assertThat(result2).isEqualTo(Month.JANUARY)
    }

    @Test
    fun testGetMonthlyMoodData() = runTest {
        // Simulate mood for two days in the same month and one day in another month
        val date1 = LocalDate.of(LocalDate.now().year, 1, 1)
        val dateFormat1 = date1.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val key = SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + dateFormat1
        fakeSharedPrefsHelper.setSharedPreferencesValueInt(key, 1)

        val date2 = LocalDate.of(LocalDate.now().year, 1, 22)
        val dateFormat2 = date2.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val key2 = SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + dateFormat2
        fakeSharedPrefsHelper.setSharedPreferencesValueInt(key2, 3)

        val date3 = LocalDate.of(LocalDate.now().year, 6, 2)
        val dateFormat3 = date3.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val key3 = SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + dateFormat3
        fakeSharedPrefsHelper.setSharedPreferencesValueInt(key3, 5)

        val monthlyData = statsFunctions.getMonthlyMoodData(Month.JANUARY)
        assertThat(monthlyData.size).isEqualTo(2)
        assertThat(monthlyData[0].first).isEqualTo(date1)
        assertThat(monthlyData[0].second.id).isEqualTo(1)
        assertThat(monthlyData[1].first).isEqualTo(date2)
        assertThat(monthlyData[1].second.id).isEqualTo(3)
    }

    @Test
    fun testGetYearlyMoodData() = runTest {
        // Simulate mood for two days in the same year and one day in another year
        val date1 = LocalDate.of(LocalDate.now().year, 1, 1)
        val dateFormat1 = date1.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val key = SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + dateFormat1
        fakeSharedPrefsHelper.setSharedPreferencesValueInt(key, 1)

        val date2 = LocalDate.of(LocalDate.now().year, 3, 22)
        val dateFormat2 = date2.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val key2 = SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-" + dateFormat2
        fakeSharedPrefsHelper.setSharedPreferencesValueInt(key2, 3)

        val key3 = SharedPrefsConstants.CURRENT_DAY_EMOTION_ID + "-1999-06-02"
        fakeSharedPrefsHelper.setSharedPreferencesValueInt(key3, 5)

        val yearlyData = statsFunctions.getYearlyMoodData()
        assertThat(yearlyData.size).isEqualTo(2)
        assertThat(yearlyData[0].first).isEqualTo(date1)
        assertThat(yearlyData[0].second.id).isEqualTo(1)
        assertThat(yearlyData[1].first).isEqualTo(date2)
        assertThat(yearlyData[1].second.id).isEqualTo(3)
    }

}