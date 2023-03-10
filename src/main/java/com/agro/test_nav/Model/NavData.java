package com.agro.test_nav.Model;

import java.io.BufferedReader;
import java.io.FileReader;

//Отвечает за работу с логами и подсчетом пройденного пути
public class NavData {
    private static final double R=6371;  // Радиус Земли
    private static final double radDegrees = 57.295779513;
    private static final double radMinutes = 3437.747;

    private String path;

    private Double traversedPath;

    public NavData(String path){
        this.path = path;
        this.traversedPath = 0.0;
    }

    //Парсит файл логов с просчетом последних координат и проверкой скорости. Если скорость есть, то считает расстояние между двумя последними координатами
    //и добавляет к общему пути. Возвращает весь пройденный путь.
    public double countPath() {
        double traversedPath = 0;
        boolean firstReady = false,secondReady = false,allReady = false;
        String[] first = null,second = null,speed = null;
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            while (bufferedReader.ready()){
                String s = bufferedReader.readLine();
                String[] split = s.split(",");
                if(split[0].contains("GGA")){
                    if(!firstReady){
                        first = split;
                        firstReady = true;
                    }else {
                        secondReady = true;
                        second = split;
                    }
                }
                if(split[0].contains("VTG")){
                    speed = split;
                    allReady = true;
                }
                if(firstReady&&secondReady&&allReady){
                    traversedPath = traversedPath + countSplits(first,second,speed);
                    first = second;
                    secondReady=false;
                    allReady=false;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return traversedPath;
    }

    //Если скорость была больше 0, то подсчитывается пройденный путь
    //Если есть смена координат, то считает пройденный путь по ним
    //В обратном случае по изменению времени и доступной скорости
    private double countSplits(String[] first, String[] second, String[] speed){
        double lon1 = 0,lon2 = 0,lat2 = 0,lat1 = 0;
        if(speed.length>1&&!speed[7].isEmpty()){
            if(Double.parseDouble(speed[7])>1){
                if(!first[2].isEmpty()&&!second[2].isEmpty()&&!second[4].isEmpty()&&!first[4].isEmpty()) {
                    lat1 = Double.parseDouble(first[2]);
                    lat2 = Double.parseDouble(second[2]);
                    lon1 = Double.parseDouble(first[4]);
                    lon2 = Double.parseDouble(second[4]);
                    lat1 = convertToRadian(lat1 % 100, (lat1 - lat1 % 100) / 100);
                    if (first[3].equals(second[3])) {
                        lat2 = convertToRadian(lat2 % 100, (lat2 - lat2 % 100) / 100);
                    } else {
                        lat2 = convertToRadian(359 - lat2 % 100, 60 - (lat2 - lat2 % 100) / 100);
                    }
                    lon1 = convertToRadian(lon1 % 100, (lon1 - lon1 % 100) / 100);
                    if (first[5].equals(second[5])) {
                        lon2 = convertToRadian(lon2 % 100, (lon2 - lon2 % 100) / 100);
                    } else {
                        lon2 = convertToRadian(359 - lon2 % 100, 60 - (lon2 - lon2 % 100) / 100);
                    }
                    traversedPath = countHaversine(lat1, lon1, lat2, lon2);
                } else {
                    double timeNew = Double.parseDouble(second[1]);
                    double timeOld = Double.parseDouble(first[1]);
                    timeNew = convertToHours((timeNew-timeNew%10000)/10000,(timeNew%10000-timeNew%100)/100,timeNew%100);
                    timeOld = convertToHours((timeOld-timeOld%10000)/10000,(timeOld%10000-timeOld%100)/100,timeOld%100);
                    traversedPath = Double.parseDouble(speed[7])*(timeNew-timeOld);
                }
            }
        }
        return traversedPath;
    }

    //Подсчет расстояния по широте и долготе по формуле Хаверсина
    //Широты и долгота должны находится в одной полосе.
    private double countHaversine(double lat1,double lon1, double lat2, double lon2){
        double sin1=Math.sin((lat1-lat2)/2);
        double sin2=Math.sin((lon1-lon2)/2);
        return 2*R*Math.asin(Math.sqrt(sin1*sin1+sin2*sin2*Math.cos(lat1)*Math.cos(lat2)));
    }

    //Перевод градусов в радианы для подсчета
    private double convertToRadian(double minutes, double degrees){
        double rad = 0;
        rad = rad + degrees/radDegrees + minutes/radMinutes;
        return rad;
    }
    //Перевод в часы
    private double convertToHours(double hours, double minutes, double seconds){
        return hours+minutes/60+seconds/3600;
    }
}
