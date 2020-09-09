package com.ivangy.marsroversphotos.model;

import java.io.Serializable;

public class Photo implements Serializable {
    private String camera, cameraFullName, image, earthDate, queryRover, RoverStatus, RoverLandingDate, RoverLaunchDate;
    private int querySun, id;

    public Photo(int id, String camera, String cameraFullName, String image, String earthDate, String queryRover, int querySun, String roverStatus, String roverLandingDate, String roverLaunchDate) {
        this.id = id;
        this.camera = camera;
        this.cameraFullName = cameraFullName;
        this.image = image;
        this.queryRover = queryRover;
        this.querySun = querySun;
        this.earthDate = earthDate;
        RoverStatus = roverStatus;
        RoverLandingDate = roverLandingDate;
        RoverLaunchDate = roverLaunchDate;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public int getId() {
        return id;
    }

    public String getQueryRover() {
        return queryRover;
    }

    public int getQuerySun() {
        return querySun;
    }

    public String getRoverStatus() {
        return RoverStatus;
    }

    public String getRoverLandingDate() {
        return RoverLandingDate;
    }

    public String getRoverLaunchDate() {
        return RoverLaunchDate;
    }

    public String getCamera() {
        return camera;
    }

    public String getCameraFullName() {
        return cameraFullName;
    }

    public String getImage() {
        return image;
    }
}
