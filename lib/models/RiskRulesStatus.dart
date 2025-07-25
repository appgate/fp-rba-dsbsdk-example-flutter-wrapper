
enum _RiskRulesStatusAttributes {
  isSecureByRiskRules,
  riskRulesStatus,
}

class RiskRulesStatus {
  bool isSecureByRiskRules = false;
  Map riskRulesStatus = {};

  RiskRulesStatus.map(Map map) {
    bool? isSecure = map[_RiskRulesStatusAttributes.isSecureByRiskRules.name];
    if(isSecure != null) {
      isSecureByRiskRules = isSecure;
    }
    var status = map[_RiskRulesStatusAttributes.riskRulesStatus.name];
    if(status is Map) {
      var map = {};
      for (var k in status.keys) {
        var value = status[k];
        if(value is bool) {
          map[k] = value;
        } else if(value is String) {
          final isTrue = value == "true";
          final isFalse = value == "false";
          map[k] = isTrue ? true : isFalse ? false : value;
        }
      }
      riskRulesStatus = map;
    }
  }
}