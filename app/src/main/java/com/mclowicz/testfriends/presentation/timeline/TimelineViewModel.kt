package com.mclowicz.testfriends.presentation.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mclowicz.testfriends.domain.timeline.TimeLineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val timelineRepository: TimeLineRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<TimelineState>(TimelineState())
    val state: StateFlow<TimelineState> = _state

    fun timelineFor(userId: String) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(userId = userId, isLoading = true))
            val result = withContext(Dispatchers.IO) {
                timelineRepository.getTimelineFor(userId)
            }
            _state.emit(result.copy(userId = userId, isLoading = false))
        }
    }
}