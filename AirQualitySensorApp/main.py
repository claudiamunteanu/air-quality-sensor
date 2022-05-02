from sense_hat import SenseHat
from time import sleep, time
import paho.mqtt.client as mqtt
import json

temperatureThreshold = 35
humidityThreshold = 31
pressureThreshold = 984

temperatureFlag = False
humidityFlag = False
pressureFlag = False

class AirQualityData:
    def __init__(self, pressure=0, humidity=0, temperature=0):
        self.pressure, self.humidity, self.temperature = pressure, humidity, temperature
        self.timestamp = int(round(time() * 1000))
        
    def __str__(self):
        return "pressure: " + str(self.pressure) + "\nhumidity: " + str(self.humidity) + "\ntemperature: " + str(self.temperature) + "\ntimestamp: " + str(self.timestamp)

def on_connect(client, userdata, flags, rc):
    print(f"Connected with result code {rc}")

f = open("app.config", "r")
line = f.read()
words = line.split(';')

if  len(words) != 2 :
    raise Exception("You have to provide an username and a password for the broker!")

username = words[0]
password = words[1].rstrip()

client = mqtt.Client()
client.on_connect = on_connect
client.username_pw_set(username=username, password=password)
client.connect("localhost")

hat = SenseHat()
while True :
    pressure = round(hat.pressure, 2)
    humidity = round(hat.humidity, 2)
    temperature = round(hat.temperature, 2)
    
    data = AirQualityData(pressure, humidity, temperature)
    dataJSON = json.dumps(data.__dict__)
    print(dataJSON)
    
    client.publish('raspberry/current', payload=dataJSON, qos=0, retain=False)
    
    if pressure > pressureThreshold and pressureFlag == False:
        pressureFlag = True
        client.publish('raspberry/notification', payload="pressure", qos=0, retain=False)
    else:
        if pressure <= pressureThreshold and pressureFlag == True:
            pressureFlag = False
        
    if humidity > humidityThreshold and humidityFlag == False:
        humidityFlag = True
        client.publish('raspberry/notification', payload="humidity", qos=0, retain=False)
    else:
        if humidity <= humidityThreshold and humidityFlag == True:
            humidityFlag = False
            
    if temperature > temperatureThreshold and temperatureFlag == False:
        temperatureFlag = True
        client.publish('raspberry/notification', payload="temperature", qos=0, retain=False)
    else:
        if temperature <= temperatureThreshold and temperatureFlag == True:
            temperatureFlag = False
            
    sleep(0.5)
    
client.loop_forever()