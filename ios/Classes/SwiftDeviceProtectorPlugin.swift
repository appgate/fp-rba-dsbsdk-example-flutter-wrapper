import Flutter
import UIKit
import dsb_protector_sdk_iOS

fileprivate enum DeviceProtectorMethods: String {
    case deviceHasJailbreak
    case isDeviceHostsFileInfected
    case getDeviceHostsFileInfections
}

public class SwiftDeviceProtectorPlugin: NSObject, FlutterPlugin {
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: ChannelNames.deviceProtectorModule.rawValue, binaryMessenger: registrar.messenger())
        let instance = SwiftDeviceProtectorPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
       switch call.method {
       case DeviceProtectorMethods.deviceHasJailbreak.rawValue:
           deviceHasJailbreak(call, result)
           break
       case DeviceProtectorMethods.isDeviceHostsFileInfected.rawValue:
           isDeviceHostsFileInfected(call, result)
           break
       case DeviceProtectorMethods.getDeviceHostsFileInfections.rawValue:
           getDeviceHostsFileInfections(call, result)
           break
       default:
           result(SDKErrors.errorChannelMethod)
       }
    }
    
    func deviceHasJailbreak(_ call: FlutterMethodCall, _ result: @escaping FlutterResult) {
        result([DSB.sdk().device_PROTECTOR_API.deviceHasJailbreak()])
    }

    func isDeviceHostsFileInfected(_ call: FlutterMethodCall, _ result: @escaping FlutterResult) {
        result([DSB.sdk().device_PROTECTOR_API.isDeviceHostsFileInfected()])
    }

    func getDeviceHostsFileInfections(_ call: FlutterMethodCall, _ result: @escaping FlutterResult) {
        let hostFiles = DSB.sdk().device_PROTECTOR_API.getDeviceHostsFileInfections()
        result(hostFiles)
    }

}
