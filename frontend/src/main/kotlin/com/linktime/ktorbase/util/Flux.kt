package com.linktime.ktorbase.util

import react.RComponent
import react.RProps
import react.RState

/** see [flux-concepts - Actions](https://github.com/facebook/flux/tree/master/examples/flux-concepts#actions) */
interface Action {
    val typeName: String
}

/** see [flux-concepts - Views](https://github.com/facebook/flux/tree/master/examples/flux-concepts#views) */
abstract class RFluxComponent<P : RProps, S : RState>(private vararg val actionTypes: String) : RComponent<P, S>() {

    override fun componentDidMount() {
        subscribeToActions()
    }

    protected fun subscribeToActions() {
        actionTypes.forEach { Dispatcher.subscribe(this, it) }
    }

    override fun componentWillUnmount() {
        Dispatcher.unsubscribe(this)
    }

    abstract fun notify(action: Action)

}

/** see [flux-concepts - Stores](https://github.com/facebook/flux/tree/master/examples/flux-concepts#store) */
abstract class FluxStore(val actionTypeName: String) {

    private var components = setOf<RFluxComponent<*, *>>()

    fun subscribe(component: RFluxComponent<*, *>, init: Boolean) {
        components += component
        if (init) init(component)
    }

    fun unsubscribe(component: RFluxComponent<*, *>) {
        components -= component
    }

    fun notify(action: Action) {
        components.forEach { it.notify(action) }
    }

    abstract fun accept(action: Action)

    abstract fun init(component: RFluxComponent<*, *>)

    protected fun reInitAllComponents() {
        components.forEach(::init)
    }

}


/** see [flux-concepts - Dispatcher](https://github.com/facebook/flux/tree/master/examples/flux-concepts#dispatcher) */
object Dispatcher {

    private var stores: List<FluxStore> = emptyList()

    /**
     * @param init whether to send current state to the subscribing component; defaults to true
     */
    fun subscribe(component: RFluxComponent<*, *>, actionType: String, init: Boolean = true) =
        stores.find { it.actionTypeName == actionType }?.subscribe(component, init)

    fun unsubscribe(component: RFluxComponent<*, *>) =
        stores.forEach { it.unsubscribe(component) }

    fun init(vararg stores: FluxStore) {
        clear()
        Dispatcher.stores += stores
    }

    fun clear() {
        stores = emptyList()
    }

    fun dispatch(action: Action) =
        stores
            .filter { it.actionTypeName == action.typeName }
            .forEach { it.accept(action) }

}