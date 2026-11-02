package com.example

import com.example.data.JoyLiveRepository
import com.example.model.Language
import com.example.model.StreamCategory
import com.example.viewmodel.HomeScreenTab
import com.example.viewmodel.JoyLiveViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testJoyLiveRepositoryData() {
    assertTrue(JoyLiveRepository.sampleLiveStreams.isNotEmpty())
    assertTrue(JoyLiveRepository.giftCatalog.isNotEmpty())
    assertTrue(JoyLiveRepository.samplePartyRooms.isNotEmpty())
    assertTrue(JoyLiveRepository.rechargePackages.isNotEmpty())

    val firstStream = JoyLiveRepository.sampleLiveStreams.first()
    assertNotNull(firstStream.titleBn)
    assertNotNull(firstStream.titleEn)
  }

  @Test
  fun testJoyLiveViewModelStateAndActions() {
    val viewModel = JoyLiveViewModel()
    val initialState = viewModel.uiState.value

    assertEquals(HomeScreenTab.LIVE_FEED, initialState.currentTab)
    assertEquals(Language.BN, initialState.currentLanguage)
    assertTrue(initialState.isDarkMode)

    // Test tab selection
    viewModel.selectTab(HomeScreenTab.WALLET)
    assertEquals(HomeScreenTab.WALLET, viewModel.uiState.value.currentTab)

    viewModel.selectTab(HomeScreenTab.PARTY_ROOMS)
    assertEquals(HomeScreenTab.PARTY_ROOMS, viewModel.uiState.value.currentTab)

    // Test category filter
    viewModel.setCategory(StreamCategory.MUSIC)
    assertEquals(StreamCategory.MUSIC, viewModel.uiState.value.selectedCategory)

    // Test Daily Bonus claim
    val initialCoins = viewModel.uiState.value.userProfile.coinBalance
    viewModel.claimDailyBonus()
    assertEquals(initialCoins + 100, viewModel.uiState.value.userProfile.coinBalance)
    assertTrue(viewModel.uiState.value.userProfile.dailyBonusClaimed)

    // Test Gift sending
    val gift = JoyLiveRepository.giftCatalog.first()
    viewModel.sendGift(gift)
    assertEquals(initialCoins + 100 - gift.coinPrice, viewModel.uiState.value.userProfile.coinBalance)

    // Test User Comment
    viewModel.sendUserComment("হ্যালো জয় লাইভ!")
    assertTrue(viewModel.uiState.value.chatMessages.any { it.text == "হ্যালো জয় লাইভ!" })

    // Test Language switch
    viewModel.setLanguage(Language.EN)
    assertEquals(Language.EN, viewModel.uiState.value.currentLanguage)

    // Test Theme toggle
    viewModel.toggleTheme()
    assertFalse(viewModel.uiState.value.isDarkMode)
  }
}
