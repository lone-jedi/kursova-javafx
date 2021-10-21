package com.crossplatform.filemanager;


import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class City {
    private String name;
    private long population;
    private double area;
    private int yearOfFoundation;
    private int countOfSchool;

    public void copyFrom(City city) {
        this.name = city.getName();
        this.population = city.getPopulation();
        this.area = city.getArea();
        this.yearOfFoundation = city.getYearOfFoundation();
        this.countOfSchool = city.getCountOfSchool();
    }

    public City(String name, long population, double area,
                int yearOfFoundation, int countOfSchool) throws Exception {
        setName(name);
        setPopulation(population);
        setArea(area);
        setYearOfFoundation(yearOfFoundation);
        setCountOfSchool(countOfSchool);
    }

    // Getters
    public String getName() {
        return name;
    }

    public long getPopulation() {
        return population;
    }

    public double getArea() {
        return area;
    }

    public int getYearOfFoundation() {
        return yearOfFoundation;
    }

    public int getCountOfSchool() {
        return countOfSchool;
    }

    //Setters
    public void setName(String name) throws Exception{
        if(name.trim().equals("")) {
            throw new Exception("Error! Name is empty String");
        }

        this.name = name.trim();
    }

    public void setPopulation(long population) throws Exception{
        if(population <= 0) {
            throw new Exception("Error! Population less or equal zero");
        }

        this.population = population;
    }

    public void setArea(double area) throws Exception{
        if(area <= 0) {
            throw new Exception("Error! Area less or equal zero");
        }

        this.area = area;
    }

    public void setYearOfFoundation(int yearOfFoundation){
        this.yearOfFoundation = yearOfFoundation;
    }

    public void setCountOfSchool(int countOfSchool) throws Exception{
        if(countOfSchool <= 0) {
            throw new Exception("Error! Count of school less or equal zero");
        }

        this.countOfSchool = countOfSchool;
    }

    public void writeToFile(boolean isAppend)
    {
        String text = name + "|" + population + "|" + area + "|" + yearOfFoundation + "|" + countOfSchool + "\n";

        try {
            FileWriter writer = new FileWriter("city.txt", isAppend);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(text);
            bufferWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public ArrayList<City> readFromFile() {
        ArrayList<City> cities = new ArrayList<City>();

        try {
            BufferedReader in = new BufferedReader(new FileReader("city.txt"));
            String line;

            while((line = in.readLine()) != null) {
                StringTokenizer t = new StringTokenizer(line,"|");
                cities.add(new City(t.nextToken(), Integer.parseInt(t.nextToken()), Double.parseDouble(t.nextToken()), Integer.parseInt(t.nextToken()), Integer.parseInt(t.nextToken())));
            }

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cities;
    }

    static public void deleteFromFile(City city) {
        ArrayList<City> cities = readFromFile();
        cities.remove(city);

        String result = "";

        deleteAllFromFile();

        for(City c : cities) {
            c.writeToFile(true);
        }
    }

    static public void deleteAllFromFile() {
        try {
            FileWriter writer = new FileWriter("city.txt", false);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write("");
            bufferWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean equals(City city) {
        if(this.getName() == city.getName()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return
                "Назва: " + name + "\n" +
                "Популяція: " + population + "\n" +
                "Площина: " + area + "\n" +
                "Рік засновнення: " + yearOfFoundation + "\n" +
                "Кількість шкіл: " + countOfSchool;
    }
}
