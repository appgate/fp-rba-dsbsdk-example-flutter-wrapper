import 'package:dsbsdk/common/AppgateSDKError.dart';

class SDKErrors {
  static final platformError = AppgateSDKError(code: '98', message: 'This method is not available in this platform.');
  static final defaultError = AppgateSDKError(code: '99');
  static const missingManifestPermissions = '113';
  static const userDeniedPermissions = '115';

}
