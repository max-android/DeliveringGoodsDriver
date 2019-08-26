package com.my_project.deliveringgoods.viewmodels.home

import com.my_project.deliveringgoods.data.entities.TypesShift


sealed class HomeShiftViewState {
    class SuccessTypesShift(val types: List<TypesShift>): HomeShiftViewState()
    class SuccessSelectShift(val status:Boolean): HomeShiftViewState()
    class Error(val error: Throwable): HomeShiftViewState()
}