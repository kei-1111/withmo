package io.github.kei_1111.withmo.core.featurebase

interface ViewModelState<S : State> {
    fun toState(): S
}
