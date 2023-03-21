import Flutter
import UIKit
import dsb_protector_sdk_iOS

fileprivate enum ConnectionProtectorMethods: String {
    case isSecureByRiskRules
    case getRiskRulesStatus
    case isSecureCertificate
}

public class SwiftConnectionProtectorPlugin: NSObject, FlutterPlugin {
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: ChannelNames.connectionProtectorModule.rawValue, binaryMessenger: registrar.messenger())
        let instance = SwiftConnectionProtectorPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
       switch call.method {
       case ConnectionProtectorMethods.isSecureByRiskRules.rawValue:
           isSecureByRiskRules(call, result)
           break
       case ConnectionProtectorMethods.getRiskRulesStatus.rawValue:
           getRiskRulesStatus(call, result)
           break
       case ConnectionProtectorMethods.isSecureCertificate.rawValue:
           isSecureCertificate(call, result)
           break
       default:
           result(SDKErrors.errorChannelMethod)
       }
    }
    
    func isSecureByRiskRules(_ call: FlutterMethodCall, _ result: @escaping FlutterResult) {
        result([DSB.sdk().connection_PROTECTOR_API.isSecureByRiskRules()])
    }
    
    func getRiskRulesStatus(_ call: FlutterMethodCall, _ result: @escaping FlutterResult) {
        guard let status = DSB.sdk().connection_PROTECTOR_API.getRiskRulesStatus(),
              let dict = status.riskRulesStatus as? Dictionary<String, Any>
        else {
            result([
                "isSecureByRiskRules": false,
                "riskRulesStatus": [:]
            ])
            return
        }
        result([
            "isSecureByRiskRules": status.isSecureByRiskRules,
            "riskRulesStatus": dict
        ])
    }
    
    func isSecureCertificate(_ call: FlutterMethodCall, _ result: @escaping FlutterResult) {
        guard let args = parseParams(call),
              let url = args[ArgumentsConstants.url] as? String,
              url.count > 0
        else {
            result(SDKErrors.errorNotArguments)
            return
        }
        DSB.sdk().connection_PROTECTOR_API.isSecureCertificate(url, onSuccess: { isSecure in
            result([isSecure])
        }, onFailure: { error in
            if let `error` = error {
                result(FlutterError.init(code: "\(error.code)", message: error.description, details: nil))
            } else {
                result(SDKErrors.defaultError)
            }
        })
    }
    
    private func parseParams(_ call: FlutterMethodCall) -> Dictionary<String, Any>? {
        if let args = call.arguments as? Dictionary<String, Any>{
            return args
        }
        return nil
    }

}
