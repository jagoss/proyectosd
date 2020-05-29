#include <ESP8266WiFi.h>
#include <WebSocketClient.h>

const char* INFO = "INFO";
const char* STAT = "STAT";
const char* ORDER = "ORDER";
const char* ORDER_SUCCESS = "ORDER;SUCCESS";
const char* ORDER_FAILURE = "ORDER;FAILURE";

const char* LIGHT = "LIGHT";
const char* DIMMER = "DIMMER";

const char* ON = "ON";
const char* OFF = "OFF";
const char* TOGGLE = "TOGGLE";

#define BUFF_SIZE 50
const char* DELIM = ";";

#define LED 13

#define NAME "esp"

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
    
    webSocketClient.getData(data);
    if (data.length() > 0) {
      Serial.print("Received data: ");
      Serial.println(data);
      handleData(data, webSocketClient);
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
  delay(1000);
}

void handleData(String dataString, WebSocketClient wc){
  char data[BUFF_SIZE];
  char* saveptr;
  dataString.toCharArray(data, BUFF_SIZE);

  char ret[50];

  char* iterator = strtok_r(data, DELIM, &saveptr);
  // iterator tendria el primer valor: INFO, STATUS u ORDER
  // caso INFO: devuelvo INFO;name;LIGHT
  if(strcmp(iterator,INFO) == 0){
    strcat(ret, INFO);
    strcat(ret, DELIM);
    strcat(ret, LIGHT);
    wc.sendData(ret);
    return;
  } else if(strcmp(iterator, STAT)==0){
    strcat(ret, STAT);
    strcat(ret, DELIM);
    const char* stat = digitalRead(LED)==HIGH ? ON : OFF;
    strcat(ret, stat);
    wc.sendData(ret);   
    return; 
  } else if (strcmp(iterator, ORDER)==0){
    // el servidor manda ORDER;LIGHT;toggle/0/1
    if ((iterator=strtok_r(NULL, DELIM, &saveptr))!=NULL && strcmp(iterator,LIGHT)==0) {
      // ahora leo que quiere hacer
      if ((iterator=strtok_r(NULL, DELIM, &saveptr))!=NULL) {
        if (strcmp(iterator, ON)) {
          digitalWrite(LED, HIGH);
          wc.sendData(ORDER_SUCCESS);
          return;
        } else if(strcmp(iterator, OFF)) {
          digitalWrite(LED, LOW);
          wc.sendData(ORDER_SUCCESS);
          return;
        } else if(strcmp(iterator, TOGGLE)) {
          if(digitalRead(LED)==HIGH) {
            digitalWrite(LED, LOW);
          } else {
            digitalWrite(LED, HIGH);
          }
          wc.sendData(ORDER_SUCCESS);
          return;
        }
      }
     }
     wc.sendData(ORDER_FAILURE);
    }
  }

/*
 * while((iterator = strtok_r(NULL, DELIM, &saveptr))!=NULL){
 *    strcat(ret, DELIM);
 *    strcat
 *  }
 */
