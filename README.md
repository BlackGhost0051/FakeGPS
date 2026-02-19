# Fake GPS

![](./app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp "Fake GPS")

**Fake GPS** is an Android application designed to simulate GPS locations on your device, allowing users to set custom locations and navigate between waypoints seamlessly. This application is useful for testing location-based apps, spoofing location, and other location-dependent functionalities.

# Content
- [Requirements](#requirements)
- [Fragments](#fragments)
    - [Map](#map)
    - [Script](#script)
    - [Settings](#settings)
    - [Info](#info)
- [TODO](#todo)

# Requirements

- Android 7.0+ (API 24)
- Developer mode enabled on device
- Mock location permission granted

# Documentation

## Fragments

### Map

The Map Fragment provides a detailed and interactive map interface that allows users to set, view, and manage their simulated GPS locations. Powered by OpenStreetMap.

### Script

The Script Fragment enables automated location simulation through customizable scripts, allowing waypoint-based route execution.

### Settings

The Settings Fragment offers customization options for the Fake GPS application, including map state persistence.

### Info

Displays application information and developer details.




[//]: # ( TODD: map clastering)
[//]: # ( TODD: map search)
[//]: # ( TODD: script logic in json format)
[//]: # ( TODD: app must work in 24/7 add parametr in setting ( need to make auto scripts )