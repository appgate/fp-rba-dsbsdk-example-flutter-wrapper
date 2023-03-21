internal class SDKErrors {
    static let defaultError = FlutterError.init(code: "99", message: "Unknown library error", details: nil)
    static let errorNotArguments = FlutterError.init(code: "99", message: "Missing arguments", details: nil)
    static let errorChannelMethod = FlutterError.init(code: "99", message: "Unknown method in this channel", details: nil)
}
