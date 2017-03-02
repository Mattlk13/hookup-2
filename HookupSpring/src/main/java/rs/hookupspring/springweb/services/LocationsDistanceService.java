package rs.hookupspring.springweb.services;

import javafx.geometry.Point2D;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Bandjur on 8/24/2016.
 */
@Service
public class LocationsDistanceService {

    // This method returns distance between 2 geo locations in desired unit ('K', 'N' , ...)
    // using Haversine formula
    public double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public Point2D getRandomLocationWithinCity(Point centralLocation, double radius) {
        Point2D newPoint = null;
        /*
        From http://gis.stackexchange.com/questions/25877/generating-random-locations-nearby/68275#68275
        'u' and 'v' are random numbers between [0, 1), 'x0' and 'y0' are central(referent) point/location.
        Note if your radius parameter is linear value (e.g. meters) then it should be converted from radians to degree
        for 1000m radius = 1000/111300
        compute following parameters
        w = r * sqrt(u)
        t = 2 * Pi * v
        x = w * cos(t)
        y = w * sin(t)
        */
        double radiusInDegrees = radius / 111000f;

        double u = ThreadLocalRandom.current().nextDouble(); //random.nextDouble();
        double v = ThreadLocalRandom.current().nextDouble();//random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(Math.toRadians(centralLocation.getX()));

        double foundLongitude = new_x + centralLocation.getX();
        double foundLatitude = y + centralLocation.getY();
        newPoint = new Point2D(foundLatitude, foundLongitude);
        System.out.println("Longitude: " + foundLongitude + "  Latitude: " + foundLatitude );

        return newPoint;
    }


}

