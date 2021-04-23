# SmartHouseServer

![Build status](https://github.com/black-fluffy-cat/SmartHouseServer/actions/workflows/gradleBuild.yml/badge.svg)

## General description
Server written in Ktor to support and manage devices like Arduino, Raspberry, Mobile applications and data coming from them. Part of SmartHouse project.

#### [SmartHouse full documentation](https://github.com/black-fluffy-cat/SmartHouse_Documentation)

## Technologies
- Ktor v1.5.2
- Logback v1.2.1
- Kotlin Coroutines v1.4.3
- Koin v2.2.0
- JUnit5 v5.4.2
- Mockito v3.7.7
- MockitoKotlin2 v2.2.0

## How to use
To run this project locally, you will need Gradle for building and running server
Execute these commands from your command line:

```
# Clone repository
$ git clone https://github.com/black-fluffy-cat/SmartHouseServer

# Go inside cloned repository
$ cd SmartHouseServer

# Run the server
$ ./gradlew run
```

## Available endpoints

### PingRoutes
- `GET '/health'` - responds "Running"

### NodeDataRoutes
- `POST '/receivePhoto'` - receives the image as Multipart file and saves it
- `POST '/receiveVideo'` - receives video as Multipart file and saves it
- `POST '/receiveNgrokAddresses'` - receives NgrokAddressesCallData and processes it
- `POST '/bme280Data'` - receives BME280NodeData and processes it
- `POST 'sensor/postValues'` - receives SensorValues and processes it
- `POST '/pirAlert'` - receives empty POST message
- `POST '/pirAlertOff'` - receives empty POST message
- `POST '/ledStripIP'` - receives NodeIPData and processes it
- `POST '/lcdNodeIP'` - receives NodeIPData and processes it
- `POST '/alertArm'` - receives AlertArmSwitch and processes it

### MonitoringRoutes
- `POST '/alert'` - receives AlertData and processes it
- `POST '/heartbeat'` - receives HeartbeatData and processes it

### HouseSystemStateRoutes
- `GET '/houseState'` - responds HouseState

### DownloadDataRoutes
- `GET '/bigFileDownload'` - responds with a file to download

### RaspberryRoutes
- `POST '/raspPhoto'` - receives empty POST message
