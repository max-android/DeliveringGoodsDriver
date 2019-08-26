package com.my_project.deliveringgoods.viewmodels.info

import com.my_project.deliveringgoods.data.entities.PrivacyPolicy
import com.my_project.deliveringgoods.data.entities.TermsDelivery
import com.my_project.deliveringgoods.data.entities.UserAgreement


sealed class InfoViewState {
    class SuccessUserAgreement(val userAgreement: UserAgreement): InfoViewState()
    class SuccessPrivacyPolicy(val privacyPolicy: PrivacyPolicy): InfoViewState()
    class SuccessTermsDelivery(val termsDelivery: TermsDelivery): InfoViewState()
    class Loading: InfoViewState()
    class Error(val error: Throwable): InfoViewState()
}