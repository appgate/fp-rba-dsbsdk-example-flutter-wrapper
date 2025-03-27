package com.appgate.dsbsdk.util

import net.easysol.dsb.application.exceptions.DSBException

enum class DSBExceptionNumber(val code: Int) {
    LicensingErrorException(100),
    ConnectionToDSBServerCannotBeEstablished(101),
    CannotExtractPinsFromCertificates(102),
    CannotGetCertificatesFromURL(103),
    SDKNotInitializedException(104),
    DSBServerAuthenticationFailed(105),
    FailedToRetrieveRiskRules(106),
    FailedToRetrieveApplicationLists(107),
    MalformedURL(108),
    DSBListVersionInvalid(109),
    MessageMonitoringBroadcastNotFound(110),
    MessageMonitoringException(111),
    MessageReceivedBroadcastNotFound(112),
    MissingManifestPermission(113),
    ParseMessageIntentError(114),
    UserDeniedPermissions(115);

    override fun toString(): String {
        return "$code"
    }

    companion object {
        fun DSBException.number(): Int {
            if (this is net.easysol.dsb.application.exceptions.FailedToRetrieveRiskRules)
                return FailedToRetrieveRiskRules.code

            if (this is net.easysol.dsb.application.exceptions.FailedToRetrieveApplicationLists)
                return FailedToRetrieveApplicationLists.code

            if (this is net.easysol.dsb.application.exceptions.LicensingErrorException)
                return LicensingErrorException.code

            if (this is net.easysol.dsb.application.exceptions.MessageReceivedBroadcastNotFound)
                return MessageReceivedBroadcastNotFound.code

            if (this is net.easysol.dsb.application.exceptions.MissingManifestPermission)
                return MissingManifestPermission.code

            if (this is net.easysol.dsb.application.exceptions.UserDeniedPermissions)
                return UserDeniedPermissions.code

            if (this is net.easysol.dsb.application.exceptions.ConnectionToDSBServerCannotBeEstablished)
                return ConnectionToDSBServerCannotBeEstablished.code

            if (this is net.easysol.dsb.application.exceptions.CannotExtractPinsFromCertificates)
                return CannotExtractPinsFromCertificates.code

            if (this is net.easysol.dsb.application.exceptions.CannotGetCertificatesFromURL)
                return CannotGetCertificatesFromURL.code

            if (this is net.easysol.dsb.application.exceptions.SDKNotInitializedException)
                return SDKNotInitializedException.code

            if (this is net.easysol.dsb.application.exceptions.DSBServerAuthenticationFailed)
                return DSBServerAuthenticationFailed.code

            if (this is net.easysol.dsb.application.exceptions.MalformedURL)
                return MalformedURL.code

            if (this is net.easysol.dsb.application.exceptions.DSBListVersionInvalid)
                return DSBListVersionInvalid.code

            if (this is net.easysol.dsb.application.exceptions.MessageMonitoringBroadcastNotFound)
                return MessageMonitoringBroadcastNotFound.code

            if (this is net.easysol.dsb.application.exceptions.MessageMonitoringException)
                return MessageMonitoringException.code

            if (this is net.easysol.dsb.application.exceptions.ParseMessageIntentError)
                return ParseMessageIntentError.code
            return 0;
        }
    }
}
