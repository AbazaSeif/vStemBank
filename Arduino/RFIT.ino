
#include <SPI.h>
#include <RFID.h>

#define SS_PIN 10
#define RST_PIN 9

RFID rfid(SS_PIN, RST_PIN);

// Setup variables:
int inByte = 0;


void setup()
{
  Serial.begin(9600);
  SPI.begin();
  rfid.init();
}

void loop()
{
  if (Serial.available() > 0) {
    inByte = Serial.read();
    if(inByte == 'C'){
    Serial.write(inByte);
    delay(5000);
    Serial.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    Serial.println("CLEAR");
    }else{
    Serial.println(" ");  
    }
    serialFlush();
  }
  if (rfid.isCard()) {
    if (rfid.readCardSerial()) {
      Serial.print("TRUE:");
        for (int i = 0; i <= sizeof(rfid.serNum) -1; i++)
        {
          Serial.print(rfid.serNum[i]);
        }
        Serial.print("\r\n");
        delay(2000);
    }
  }else{
    Serial.print("FALSE\r\n");
        delay(2000);
  }

  rfid.halt();
}
void serialFlush(){
  while(Serial.available() > 0) {
    char t = Serial.read();
  }
}
