import 'package:dsbsdk/modules/modules.dart';
import 'package:flutter/services.dart';

class Dsbsdk {

  static final Dsbsdk _instance = Dsbsdk._channelName("dsbsdk");

  late DSBModule _dsbModule;
  late DeviceProtectorAPI _deviceProtector;
  late ConnectionProtectorAPI _connectionProtector;
  late MalwareProtectorAPI _malwareProtectorAPI;

  Dsbsdk._channelName(String prefix) {
    _dsbModule = DSBModule(MethodChannel(prefix));
    _deviceProtector = DeviceProtectorAPI(MethodChannel("${prefix}_device_protector"));
    _connectionProtector = ConnectionProtectorAPI(MethodChannel("${prefix}_connection_protector"));
    _malwareProtectorAPI = MalwareProtectorAPI(MethodChannel("${prefix}_malware_protector"));
  }

  static Future<void> initWithLicense(String licence, [String? domain]) {
    return _instance._dsbModule.initWithLicense(licence, domain);
  }

  static void sendLoginData(Map<String, String> args) {
    _instance._dsbModule.sendLoginData(args);
  }

  static Future getDeviceID() {
    return _instance._dsbModule.getDeviceID();
  }

  static DeviceProtectorAPI getDeviceProtectorAPI() => _instance._deviceProtector;
  static ConnectionProtectorAPI getConnectionProtectorAPI() => _instance._connectionProtector;
  static MalwareProtectorAPI getMalwareProtectorAPI() => _instance._malwareProtectorAPI;

}
