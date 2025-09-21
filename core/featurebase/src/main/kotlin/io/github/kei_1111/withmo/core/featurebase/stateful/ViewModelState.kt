package io.github.kei_1111.withmo.core.featurebase.stateful

interface ViewModelState<S : State> {
    fun toState(): S
}
