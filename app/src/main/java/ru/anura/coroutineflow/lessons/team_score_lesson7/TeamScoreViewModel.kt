package ru.anura.coroutineflow.lessons.team_score_lesson7

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TeamScoreViewModel : ViewModel() {

    private var cashedState: TeamScoreState = (TeamScoreState.Game(0, 0))

    private val _state = MutableSharedFlow<TeamScoreState>(replay = 1) //сохранение состояния при перевороте
    val state = _state.asSharedFlow()
        .onEach { cashedState = it }

    fun increaseScore(team: Team) {
        viewModelScope.launch {
            val currentState = cashedState
            if (currentState is TeamScoreState.Game) {
                if (team == Team.TEAM_1) {
                    val oldValue = currentState.score1
                    val newValue = oldValue + 1
                    _state.emit(currentState.copy(score1 = newValue))
                    if (newValue >= WINNER_SCORE) {
                        _state.emit(
                            TeamScoreState.Winner(
                                winnerTeam = Team.TEAM_1,
                                newValue,
                                currentState.score2
                            )
                        )
                    }
                } else {
                    val oldValue = currentState.score2
                    val newValue = oldValue + 1
                    _state.emit(currentState.copy(score2 = newValue))
                    if (newValue >= WINNER_SCORE) {
                        _state.emit(
                            TeamScoreState.Winner(
                                winnerTeam = Team.TEAM_2,
                                currentState.score1,
                                newValue
                            )
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val WINNER_SCORE = 7
    }
}
