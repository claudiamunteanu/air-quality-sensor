<a name="readme-top"></a>

<!--[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url] 
[![Issues][issues-shield]][issues-url] -->
[![License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

# Air Quality Sensor
Raspberry Pi project created for the "Android Things" course at our university in our third year.

This project monitors the air quality. The board uses a set of sensor which includes a barometer, a humidity sensor and a temperature sensor. The sensor captures the temperature, air pressure and humidity levels and send them to the phone. The phone displays the current levels in real-time, as well as a graph which shows historical data for the air temperature. Moreover, when the sensors detect that the air's quality reached some configured threshold values, it sends a notification to the phone.

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#built-with">Built With</a></li>
    <li><a href="#schematics">Schematics</a></li>
    <li><a href="#prerequisites">Prerequisites</a></li>
    <li><a href="#setup-and-build">Setup and Build</a></li>
    <li><a href="#running">Running</a></li>
    <!--
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    -->
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <!--<li><a href="#acknowledgments">Acknowledgments</a></li>-->
  </ol>
</details>

## Built with

### Companion App
* [![Android][Android.com]][Android-url]
* [![Android Studio][AndroidStudio.com]][AndroidStudio-url]
* [![Kotlin][Kotlin.org]][Kotlin-url]

### Server
* [![Raspberry Pi][RaspberryPi]][RaspberryPi-url]
* [![Mosquitto][Mosquitto.org]][Mosquitto-url]
* [![Python][Python.org]][Python-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Schematics

![Schematics](schematics.png)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Prerequisites
- [Raspberry Pi 4B board](https://www.raspberrypi.com/products/raspberry-pi-4-model-b/)
- [Sense HAT (sensors anssemble)](https://www.raspberrypi.com/products/sense-hat/)
- [Eclipse Moquitto MQTT Broker](https://mosquitto.org/download/)
- [Android Studio](https://developer.android.com/studio) (We are using Android Studio Arctic Fox)
- An Android phone with a minimum API level of 27 (Android 8)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Setup and Build

To setup, follow these steps below.

1. Install Mosquitto MQTT Server on the Raspberry Pi
    ```
    sudo apt install mosquitto mosquitto-clients
    ```
2. Configure Mosquitto username and password
  - create the password file, which will contain the username and encrypted password
    ```
    sudo mosquitto_passwd -c /etc/mosquitto/passwd.txt <user_name>
    ```
  - add the following entries in the mosquitto.conf file, inside /etc/mosquitto folder
    ```
    allow_anonymous false
    password_file /etc/mosquitto/passwd.txt
    ```
  - restart the mosquitto server to make sure the changes are saved
    ```
    sudo systemctl restart mosquitto
    ```
3. Copy the AirQualitySensorApp folder to Raspberry Pi
4. Modify the username and password in app.config file, inside the AirQualitySensorApp folder
   - the username and password must be on the same line, separated by a semicolon: ';'
5. If needed, modify the configured threshold values inside de main.py file from the AirQualitySensorApp folder
6. In the CompanionApp project, inside the build.gradle file from the app folder, modify the mosquitto username and password, and the Raspberry Pi server's IP address. The port must remain 1883

The `CompanionApp` project will run on the companion device e.g. Android phone.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Running

To run the `CompanionApp` project on your Android phone:
1. Deploy and run the `CompanionApp` project
2. Verify that input is received from the broker and that it's displayed on the companion device's screen
3. Verify that the graph displays the historical data
4. Verify that a notification is received when the configured threshold values are hit

To run the `AirQualitySensorApp` module on a Raspberry Pi 4B board:

1. In the terminal, go to the AirQualitySensorApp folder and run the following command:
  ```
  python3 main.py
  ```
2. Verify that the sensor works and the program is printing the temperature, humidity, pressure and timestamp values

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Munteanu Claudia-Maria - Linkedin: [claudiamunteanu][linkedin-url]

Project Link: [https://github.com/claudiamunteanu/air-quality-sensor](https://github.com/claudiamunteanu/air-quality-sensor)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[license-shield]: https://img.shields.io/github/license/claudiamunteanu/air-quality-sensor.svg?style=for-the-badge
[license-url]: https://github.com/claudiamunteanu/air-quality-sensor/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/claudiamunteanu
[Android.com]: https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white
[Android-url]: https://www.android.com/
[AndroidStudio.com]: https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white
[AndroidStudio-url]: https://developer.android.com/studio
[Kotlin.org]: https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white
[Kotlin-url]: https://kotlinlang.org/
[Python.org]: https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54
[Python-url]: https://www.python.org/
[Mosquitto.org]: https://img.shields.io/badge/mosquitto-%233C5280.svg?style=for-the-badge&logo=eclipsemosquitto&logoColor=white
[Mosquitto-url]: https://mosquitto.org/
[RaspberryPi]: https://img.shields.io/badge/-RaspberryPi-C51A4A?style=for-the-badge&logo=Raspberry-Pi
[RaspberryPi-url]: https://www.raspberrypi.com/
