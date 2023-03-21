package com.appgate.dsbsdk.constants

enum class DSBModulesNames(val value: String) {
    INIT_WITH_LICENSE("initWithLicense"),
    GET_DEVICE_ID("getDeviceID"),
    SEND_LOGIN_DATA("sendLoginData"),
    IS_DEVICE_ROOTED("isDeviceRooted"),
    IS_DEVICE_ON_INSECURE_NETWORK("isDeviceOnInsecureNetwork"),
    IS_DEVICE_HOSTS_FILE_INFECTED("isDeviceHostsFileInfected"),
    GET_DEVICE_HOSTS_FILE_INFECTIONS("getDeviceHostsFileInfections"),
    RESTORE_DEVICE_HOSTS("restoreDeviceHosts"),
    IS_SECURE_BY_RISK_RULES("isSecureByRiskRules"),
    GET_RISK_RULES_STATUS("getRiskRulesStatus"),
    IS_SECURE_CERTIFICATE("isSecureCertificate"),
    START_OVERLAPPING_PROTECTION ("startOverlappingProtection"),
    CONFIGURE_OVERLAPPING_MALWARE_GUI_NOTIFICATION ("configureOverlappingMalwareGUINotification"),
    SET_OVERLAY_LISTENER ("setOverlayListener"),
    SET_OVERLAY_GUI_NOTIFICATION_ENABLE ("setOverlayGUINotificationEnable"),
    SET_OVERLAY_TOAST_NOTIFICATION_ENABLE ("setOverlayToastNotificationEnable"),
    CONFIGURE_OVERLAPPING_MALWARE_TOAST_NOTIFICATION ("configureOverlappingMalwareToastNotification"),
}