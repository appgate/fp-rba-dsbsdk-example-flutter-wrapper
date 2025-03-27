part of modules;

enum _ConnectionProtectorConstants { url }

class ConnectionProtectorAPI {
  MethodChannel channel;

  ConnectionProtectorAPI(this.channel);

  Future isSecureByRiskRules() {
    return channel
        .invokeListMethod(MethodNames.isSecureByRiskRules.name)
        .then((response) {
      if ((response?.isNotEmpty ?? false) && response!.first is bool) {
        return Future.value(response.first);
      }
      return Future.value(false);
    }).catchError((error) => Future.error(AppgateSDKError.toError(error)));
  }

  Future getRiskRulesStatus() {
    return channel
        .invokeMapMethod(MethodNames.getRiskRulesStatus.name)
        .then((response) {
      if (response != null) {
        return Future.value(RiskRulesStatus.map(response));
      } else {
        return Future.error(SDKErrors.defaultError);
      }
    }).catchError((error) => Future.error(AppgateSDKError.toError(error)));
  }

  Future isSecureCertificate(String url) {
    return channel.invokeListMethod(MethodNames.isSecureCertificate.name,
        {_ConnectionProtectorConstants.url.name: url}).then((response) {
      if ((response?.isNotEmpty ?? false) && response!.first is bool) {
        return Future.value(response.first);
      }
      return Future.error(SDKErrors.defaultError);
    }).catchError((error) => Future.error(AppgateSDKError.toError(error)));
  }
}
