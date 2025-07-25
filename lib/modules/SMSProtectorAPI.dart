part of modules;

class SMSProtectorAPI {
  MethodChannel channel;
  List<String> _missingPermissions = [];

  SMSProtectorAPI(this.channel);

  Future<void> requestSMSPermissions() {
    if (Platform.isIOS) return Future.error(SDKErrors.platformError);
    return channel.invokeListMethod(MethodNames.requestSMSPermissions.name, {})
    .then((response) {
      return Future.value();
    })
    .catchError((error) {
      if(error is PlatformException) {
        var list = error.details;
        if(error.code == SDKErrors.userDeniedPermissions && list is List) {
          _missingPermissions = list.map((e) => "$e").toList();
        }
      }
      return Future.error(AppgateSDKError.toError(error));
    });
  }

  List<String> getMissingPermissions() {
    return _missingPermissions;
  }

  Future<void> startMessageMonitoring([String? package, String? className]) {
    if (Platform.isIOS) return Future.error(SDKErrors.platformError);
    var payload = package != null && className != null ? {
      "package": package,
      "class": className,
    } : {};
    return channel.invokeListMethod(MethodNames.startMessageMonitoring.name, payload)
    .then((response) {
      return Future.value();
    })
    .catchError((error) {
      return Future.error(AppgateSDKError.toError(error));
    });
  }

}
