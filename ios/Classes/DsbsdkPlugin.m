#import "DsbsdkPlugin.h"
#if __has_include(<dsbsdk/dsbsdk-Swift.h>)
#import <dsbsdk/dsbsdk-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "dsbsdk-Swift.h"
#endif

@implementation DsbsdkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftDsbsdkPlugin registerWithRegistrar:registrar];
  [SwiftDeviceProtectorPlugin registerWithRegistrar:registrar];
  [SwiftConnectionProtectorPlugin registerWithRegistrar:registrar];
}
@end
