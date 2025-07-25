part of modules;

enum _DSBModuleConstants {
  licence,
  domain,
  data,
}

class DSBModule {
  MethodChannel channel;

  DSBModule(this.channel);

  Future<void> initWithLicense(String licence, [String? domain]) async {
    return channel
        .invokeListMethod(MethodNames.initWithLicense.name, {
          _DSBModuleConstants.licence.name: licence,
          _DSBModuleConstants.domain.name: domain ?? "",
        })
        .then((_) => Future.value())
        .catchError((error) => Future.error(AppgateSDKError.toError(error)));
  }

  void sendLoginData(Map<String, String> args) {
    channel
        .invokeListMethod(MethodNames.sendLoginData.name,
            {_DSBModuleConstants.data.name: args})
        .then((_) => {})
        .catchError((_) => {});
  }

  Future getDeviceID() {
    return channel
        .invokeListMethod(MethodNames.getDeviceID.name)
        .then((response) {
      if (response != null && response.isNotEmpty) {
        return Future.value(response.first);
      } else {
        return Future.error(SDKErrors.defaultError);
      }
    }).catchError((error) => Future.error(AppgateSDKError.toError(error)));
  }
}
