package com.phimpme.phimpme;

import android.media.ExifInterface;
import android.net.Uri;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by sensing on 14-7-6.
 */
public class ConvertLatlng {
    public String convertToDegreeForm(String imagePath) throws IOException {
        ExifInterface exifInterface = new ExifInterface(imagePath);
        return getDegree(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE),
                exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF))
                + ";"
                + getDegree(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE),
                exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
    }

    private static String getDegree(String GPSInfo, String GPSRef) {
        double sum = 0, divisor = 1;
        if (GPSInfo == null) {
            return "0;0";
        }
        String[] source = GPSInfo.split(",");
        for (String item : source) {
            String[] numbers = item.split("/");
            sum += Integer.parseInt(numbers[0]) / Integer.parseInt(numbers[1]) / divisor;
            divisor *= 60;
        }
        if (GPSRef.equals("S") || GPSRef.equals("W")) sum *= -1;
        return Double.valueOf(sum).toString();
    }


    public void saveSexagesimalBack(Uri imageUri, double latitude, double longitude) throws IOException {
        ExifInterface exifInterface = new ExifInterface(imageUri.getPath());
        if (latitude < 0) {
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
        } else {
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
        }
        exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, convertToSexagesimal(latitude));
        if (longitude < 0) {
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
        } else {
            exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
        }
        exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, convertToSexagesimal(longitude));
        exifInterface.saveAttributes();
    }

    private String convertToSexagesimal(double GPSInfo) {
        int du = (int) Math.floor(Math.abs(GPSInfo));
        double temp = getdPoint(Math.abs(GPSInfo)) * 60;
        int fen = (int) Math.floor(temp);
        int miao = (int) Math.floor(getdPoint(temp) * 600000);
        return du + "/1," + fen + "/1," + miao + "/10000";
    }

    private double getdPoint(double num) {
        double d = num;
        int fInt = (int) d;
        BigDecimal b1 = new BigDecimal(Double.toString(d));
        BigDecimal b2 = new BigDecimal(Integer.toString(fInt));
        double dPoint = b1.subtract(b2).floatValue();
        return dPoint;
    }
}

