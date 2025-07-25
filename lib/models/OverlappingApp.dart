class OverlappingApp {
  String signature = "";
  String appName = "";
  String packageName = "";
  String path = "";
  String versionName = "";
  String versionNumber = "";
  String storeName = "";

  OverlappingApp(this.signature, this.appName, this.packageName, this.path,
      this.versionName, this.versionNumber, this.storeName);

  factory OverlappingApp.fromJson(Map<String, dynamic> json) {
    return OverlappingApp(
        json['signature'],
        json['appName'],
        json['packageName'],
        json['path'],
        json['versionName'],
        json['versionNumber'],
        json['storeName']);
  }
}
