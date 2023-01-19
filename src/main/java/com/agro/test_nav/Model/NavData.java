package com.agro.test_nav.Model;

//Отвечает за работу с логами и подсчетом пройденного пути
public class NavData {
    private String path;
    private static final double R=6371;  // Радиус Земли
    private static final double radDegrees = 57.295779513;
    private static final double radMinutes = 3437.747;
    private static final double radSeconds = 206264.8;
    private Double traversedPath;
    public NavData(String path){
        this.path = path;
        this.traversedPath = 0.0;
    }

    private double countPath(){
        double path = 0;


        return path;
    }

    //Подсчет расстояния по широте и долготе по формуле Хаверсина
    //Широты и долгота должны находится в одной полосе.
    public static double countHaversine(double lat1,double lon1, double lat2, double lon2){
        double sin1=Math.sin((lat1-lat2)/2);
        double sin2=Math.sin((lon1-lon2)/2);
        return 2*R*Math.asin(Math.sqrt(sin1*sin1+sin2*sin2*Math.cos(lat1)*Math.cos(lat2)));
    }

    //Перевод градусов в радианы для подсчета
    public static double convertToRadian(double degrees, double minutes, double seconds){
        double rad = 0;
        rad = rad + degrees/radDegrees + minutes/radMinutes + seconds/radSeconds;
        return rad;
    }
}
