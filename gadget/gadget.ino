#include <ESP8266WiFi.h>
#include <WebSocketClient.h>
 
const char* ssid     = "Colonia";
const char* password = "48019530";
 
char path[] = "/socket";
char host[] = "192.168.0.107";
 
WebSocketClient webSocketClient;
WiFiClient client;
 
void setup() {
  Serial.begin(115200);
  delay(10);
  Serial.println("pepe");
  WiFi.begin(ssid, password);
 
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
 
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
 
  delay(1000);
 
  if (client.connect(host, 8080)) {
    Serial.println("Connected");
  } else {
    Serial.println("Connection failed.");
  }
 
  webSocketClient.path = path;
  webSocketClient.host = host;
  if (webSocketClient.handshake(client)) {
    Serial.println("Handshake successful");
  } else {
    Serial.println("Handshake failed.");
    while(1) {
      // Hang on failure
    } 
  }
 
}
 int i = 0;
void loop() {
  String data;
 
  if (client.connected()) {
    String g = "esp;2;servicio,";
    Serial.println("lala"+g + i++);
    String hola = g+i;
    webSocketClient.sendData(hola);
 
    webSocketClient.getData(data);
    if (data.length() > 0) {
      Serial.print("Received data: ");
      Serial.println(data);
    }
    delay(10);
 
  } else {
    Serial.println("Client disconnected.");
      webSocketClient.path = path;
  webSocketClient.host = host;
  if (webSocketClient.handshake(client)) {
    Serial.println("Handshake successful");
  } else {
    Serial.println("Handshake failed.");
    while(1) {
      // Hang on failure
    } 
  }
  }
 
  delay(3000);
 
}
