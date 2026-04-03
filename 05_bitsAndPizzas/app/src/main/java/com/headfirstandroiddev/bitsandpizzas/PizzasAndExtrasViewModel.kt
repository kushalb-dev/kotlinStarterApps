package com.headfirstandroiddev.bitsandpizzas

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

/**
 * I have used encapsulation for
 * the state variables **pizzasState** and **extrasState** in this ViewModel
 */
class PizzasAndExtrasViewModel: ViewModel() {
    private  val pizzas = listOf(
        Pizza("Margherita", false),
        Pizza("Pepperoni", false)
    )
    private val extras = listOf(
        Extra("Cheese", false),
        Extra("Olives", false),
        Extra("Mushrooms", false)
    )
    private val _pizzasState = mutableStateOf(pizzas)
    private val _extrasState = mutableStateOf(extras)
    val pizzasState: State<List<Pizza>> = _pizzasState
    val extrasState: State<List<Extra>> = _extrasState

    fun onPizzaClick(pizza: Pizza) {
        _pizzasState.value = _pizzasState.value.map {
            if (it.name == pizza.name) {
                it.copy(isSelected = !it.isSelected)
            } else {
                it.copy(isSelected = false)
            }
        }
    }

    fun onExtraClick(extra: Extra) {
        if (!_pizzasState.value.none { it.isSelected }) {
            _extrasState.value = _extrasState.value.map {
                if (it.name == extra.name) {
                    it.copy(isSelected = !it.isSelected)
                } else {
                    it
                }
            }
        }
    }
}
