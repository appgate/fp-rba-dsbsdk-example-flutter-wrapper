part of modules;

class DeviceProtectorAPI {
  MethodChannel channel;

  DeviceProtectorAPI(this.channel);

  Future isDeviceRooted() {
    final method = Platform.isAndroid
        ? MethodNames.isDeviceRooted.name
        : MethodNames.deviceHasJailbreak.name;
    return channel.invokeListMethod(method, {}).then((response) {
      if ((response?.isNotEmpty ?? false) && response!.first is bool) {
        return Future.value(response!.first);
      }
      return Future.value(false);
    }).catchError((error) => Future.error(AppgateSDKError.toError(error)));
  }

  Future isDeviceOnInsecureNetwork() {
    if (Platform.isIOS) return Future.error(SDKErrors.platformError);
    return channel.invokeListMethod(
        MethodNames.isDeviceOnInsecureNetwork.name, {}).then((response) {
      if ((response?.isNotEmpty ?? false) && response!.first is bool) {
        return Future.value(response!.first);
      }
      return Future.value(false);
    });
  }

  Future isDeviceHostsFileInfected() {
    return channel.invokeListMethod(
        MethodNames.isDeviceHostsFileInfected.name, {}).then((response) {
      if ((response?.isNotEmpty ?? false) && response!.first is bool) {
        return Future.value(response!.first);
      }
      return Future.value(false);
    }).catchError((error) => Future.error(AppgateSDKError.toError(error)));
  }

  Future getDeviceHostsFileInfections() {
    return channel.invokeMapMethod(
        MethodNames.getDeviceHostsFileInfections.name, {}).then((response) {
      return Future.value(response);
    });
  }

  Future<void> restoreDeviceHosts() {
    if (Platform.isIOS) return Future.error(SDKErrors.platformError);
    return channel
        .invokeListMethod(MethodNames.restoreDeviceHosts.name, {})
        .then((_) => Future.value());
  }
}
