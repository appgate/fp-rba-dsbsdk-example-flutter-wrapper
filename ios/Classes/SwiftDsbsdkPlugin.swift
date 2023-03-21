import Flutter
import UIKit
import dsb_protector_sdk_iOS

fileprivate enum DSBModulesNames: String {
    case initWithLicense
    case getDeviceID
    case sendLoginData
}

public class SwiftDsbsdkPlugin: NSObject, FlutterPlugin {
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: ChannelNames.dsbModule.rawValue, binaryMessenger: registrar.messenger())
        let instance = SwiftDsbsdkPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
       switch call.method {
       case DSBModulesNames.initWithLicense.rawValue:
           initWithLicense(call, result)
           break
       case DSBModulesNames.getDeviceID.rawValue:
           getDeviceID(call, result)
           break
       case DSBModulesNames.sendLoginData.rawValue:
           sendLoginData(call, result)
           break
       default:
           result(SDKErrors.errorChannelMethod)
       }
    }
    
    func initWithLicense(_ call: FlutterMethodCall, _ result: @escaping FlutterResult) {
        guard let args = parseParams(call),
              let licence = args[ArgumentsConstants.licence] as? String,
              let domain = args[ArgumentsConstants.domain] as? String,
              licence.count > 0
        else {
            result(SDKErrors.errorNotArguments)
            return
        }

        if domain.count > 0 {
            DSB.sdk().initWithLicense(licence, andDomain: domain, onSuccess: {
                result([""])
            }, onFailure: { error in
                if let `error` = error {
                    result(FlutterError.init(code: "\(error.code)", message: error.description, details: nil))
                } else {
                    result(SDKErrors.defaultError)
                }
            })
        } else {
            DSB.sdk().initWithLicense(licence, onSuccess: {
                result([""])
            }, onFailure: { error in
                if let `error` = error {
                    result(FlutterError.init(code: "\(error.code)", message: error.description, details: nil))
                } else {
                    result(SDKErrors.defaultError)
                }
            })
        }
    }
    
    func getDeviceID(_ call: FlutterMethodCall, _ result: @escaping FlutterResult) {
        result([DSB.sdk().getDeviceID()])
    }
    
    func sendLoginData(_ call: FlutterMethodCall, _ result: @escaping FlutterResult) {
        guard let args = parseParams(call),
              let data = args[ArgumentsConstants.data] as? [String: Any]
        else {
            result(SDKErrors.errorNotArguments)
            return
        }
        let loginData = data.keys.map({ key in
            return [key: data[key] ?? ""]
        })
        DSB.sdk().sendLoginData(loginData)
    }

    private func parseParams(_ call: FlutterMethodCall) -> Dictionary<String, Any>? {
        if let args = call.arguments as? Dictionary<String, Any>{
            return args
        }
        return nil
    }

}
