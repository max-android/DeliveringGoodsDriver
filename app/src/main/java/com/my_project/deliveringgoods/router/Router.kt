package com.my_project.deliveringgoods.router

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.entities.Order
import com.my_project.deliveringgoods.ui.home.HomeFragment
import com.my_project.deliveringgoods.ui.info.InfoFragment
import com.my_project.deliveringgoods.ui.list_orders.ListOrdersFragment
import com.my_project.deliveringgoods.ui.map.DeliveryMapFragment
import com.my_project.deliveringgoods.ui.order.OrderFragment
import com.my_project.deliveringgoods.ui.photo.PhotoFragment
import com.my_project.deliveringgoods.ui.profile.ProfileFragment
import com.my_project.deliveringgoods.ui.search.SearchFragment
import com.my_project.deliveringgoods.ui.settings.SettingsFragment


class Router {

    private var fragmentManager: FragmentManager? = null
    var actualScreen: Screen = Screen.HOME

    fun init(fragmentManager: FragmentManager?) {
        this.fragmentManager = fragmentManager
    }

    fun replace(screen: Screen, data: Any = Any()) {
        if (backStackCount() > 0) {
            clearBackStack()
        }
        applyCommand(screen, Command.REPLACE, data)
    }

    fun forward(screen: Screen, data: Any = Any()) {
        applyCommand(screen, Command.FORWARD, data)
    }

    fun detach() {
        fragmentManager = null
    }

    private fun clearBackStack() {
        fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun back() {
        fragmentManager?.popBackStack()
        updateActualBackScreen()
    }

    fun backTo(nameFrag: String?) {
        fragmentManager?.popBackStackImmediate(nameFrag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun backStackCount() = fragmentManager?.backStackEntryCount ?: 0

    private fun applyCommand(screen: Screen, command: Command, data: Any) {
        updateActualScreen(screen)
        doFragmentTransaction(screen, command, data)
    }

    private fun updateActualScreen(screen: Screen) {
        actualScreen = screen
    }

    private fun doFragmentTransaction(screen: Screen, command: Command, data: Any) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.main_container, getFragment(screen, data))
            ?.apply { if (command == Command.FORWARD) addToBackStack(screen.name) }
            ?.setCustomAnimations(
                R.animator.slide_in_right,
                R.animator.slide_out_left,
                R.animator.slide_in_left,
                R.animator.slide_out_right
            )
            ?.commitAllowingStateLoss()
    }

    private fun getFragment(screen: Screen, data: Any): Fragment {
        return when (screen) {
            Screen.HOME -> HomeFragment.newInstance()
            Screen.LIST_TASKS -> ListOrdersFragment.newInstance()
            Screen.TASK -> OrderFragment.newInstance(data as Order)
            Screen.MAP -> DeliveryMapFragment.newInstance()
            Screen.SETTINGS -> SettingsFragment.newInstance()
            Screen.PROFILE -> ProfileFragment.newInstance()
            Screen.SEARCH -> SearchFragment.newInstance(data as String)
            Screen.INFO -> InfoFragment.newInstance()
            Screen.PHOTO -> PhotoFragment.newInstance()
        }
    }

    private fun updateActualBackScreen() {
        actualScreen = when (actualScreen) {
            Screen.INFO -> Screen.HOME
            Screen.SEARCH -> Screen.MAP
            Screen.TASK -> Screen.LIST_TASKS
            Screen.PHOTO -> Screen.PROFILE
            else -> Screen.HOME
        }
    }
}