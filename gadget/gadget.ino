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
  pinMode(LED, OUTPUT);
  
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
 
  if (client.connect(host, 8081)) {
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

void loop() {
  String data;
  int i = 0;
  delay(1000);
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
}

void handleData(String dataString, WebSocketClient wc){
  Serial.println("adentro de handle data");
  Serial.println(dataString);
  char data[BUFF_SIZE];
  char* saveptr;
  dataString.toCharArray(data, BUFF_SIZE);

  char ret[50] = {0};

  char* iterator = strtok_r(data, DELIM, &saveptr);
  // iterator tendria el primer valor: INFO, STATUS u ORDER
  // caso INFO: devuelvo INFO;name;LIGHT
  if(strcmp(iterator,INFO) == 0){
    Serial.println("adentro de info");
    strcat(ret, INFO);
    strcat(ret, DELIM);
    strcat(ret, NAME);
    strcat(ret, DELIM);
    strcat(ret, LIGHT);
    Serial.println("antes de enviar");
    Serial.println(ret);
    wc.sendData(ret);
    Serial.println("despues de enviar");
    return;
  } else if(strcmp(iterator, STAT)==0){
    strcat(ret, STAT);
    strcat(ret, DELIM);
    const char* stat = digitalRead(LED)==HIGH ? ON : OFF;
    strcat(ret, stat);
    wc.sendData(ret);   
    return; 
  } else if (strcmp(iterator, ORDER)==0){
    Serial.println("adentri de order");
    // el servidor manda ORDER;LIGHT;toggle/0/1
    if ((iterator=strtok_r(NULL, DELIM, &saveptr))!=NULL && strcmp(iterator,LIGHT)==0) {
      Serial.println("adentri de light");
      // ahora leo que quiere hacer
      Serial.println(iterator);
      if ((iterator=strtok_r(NULL, DELIM, &saveptr))!=NULL) {
        
      Serial.println(iterator);
        if (strcmp(iterator, ON)==0) {
          digitalWrite(LED, HIGH);
          wc.sendData(ORDER_SUCCESS);
          return;
        } else if(strcmp(iterator, OFF)==0) {
          digitalWrite(LED, LOW);
          wc.sendData(ORDER_SUCCESS);
          return;
        } else if(strcmp(iterator, TOGGLE)==0) {
          Serial.println("adentro de toggle");
          Serial.println(digitalRead(LED));
          if(digitalRead(LED)==HIGH) {
            Serial.println("apagando");
            digitalWrite(LED, LOW);
          } else {
            Serial.println("prendiendo");
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
