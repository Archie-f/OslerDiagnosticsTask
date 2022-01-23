package com.oslerdiagnostics.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class OslerUtils {

    public static void createAnalysisFile(List<String[]> oldList, List<String[]> newList, String path) throws IOException {

        FileWriter writer = new FileWriter(path);

        Map<String, String[]> oldMap = new HashMap<>();
        oldList.stream().forEach(arr -> oldMap.put(arr[0], new String[] {arr[1],arr[2],arr[3]}));

        Map<String, String[]> newMap = new HashMap<>();
        newList.stream().forEach(arr -> newMap.put(arr[0], new String[] {arr[1],arr[2].split("\t")[0],arr[2].split("\t")[1]}));

        int count = 1;
        for(java.util.Map.Entry<String, String[]> oldItem : oldMap.entrySet()) {

            if(newMap.containsKey(oldItem.getKey())) {
                String[] newItemsArray = newMap.get(oldItem.getKey());
                writer.write(oldItem.getKey() + "\t" + oldItem.getValue()[0] + "\t" + oldItem.getValue()[1] + "\t" + oldItem.getValue()[2]);
                if(Arrays.equals(oldItem.getValue(), newItemsArray)) {
                    writer.write("\t" + "\t" + "\t" + "CORRECT");
                    System.out.println(count + " " + "CORRECT" + " " + oldItem.getKey() + " " +  Arrays.toString(oldItem.getValue()) + " " +  Arrays.toString(newItemsArray));
                    count++;
                }else {
                    writer.write("\t" + "\t" + "\t" + "ERROR");
                    System.out.println(count + " " + "ERROR" + " " + oldItem.getKey());
                    count++;
                }
            }
            writer.write("\n");
        }
        writer.close();

    }

    public static void check(List<String[]> oldList, List<String[]> newList) {

        Map<String, String[]> oldMap = new HashMap<>();
        oldList.stream().forEach(arr -> oldMap.put(arr[0], new String[] {arr[2],arr[3]}));

        Map<String, String[]> newMap = new HashMap<>();
        newList.stream().forEach(arr -> newMap.put(arr[0], arr[2].split("\t")));

        int count = 1;
        for(java.util.Map.Entry<String, String[]> oldItem : oldMap.entrySet()) {

            if(newMap.containsKey(oldItem.getKey())) {
                String[] newItemsArray = newMap.get(oldItem.getKey());
                if(Arrays.equals(oldItem.getValue(), newItemsArray)) {
                    System.out.println(count + " " + "CORRECT" + " " + oldItem.getKey() + " " +  Arrays.toString(oldItem.getValue()) + " " +  Arrays.toString(newItemsArray));
                    count++;
                }else {
                    System.out.println(count + " " + "ERROR" + " " + oldItem.getKey());
                    count++;
                }
            }
        }

    }

    public static List<String[]> resolveHex(List<String[]> list) {

        for(String[] array : list) {

            Map<String,String> status = new HashMap<>();

            status = checkStatus(array);
            array[2] = status.get("Authorization") + status.get("Administration") + "\t" + status.get("Training");
        }

        return list;
    }

    public static List<String[]> singularUsers(List<String[]> list) {

        List<String[]>  lastList = new ArrayList<>();

        for(String[] arr : list) {

            lastList.removeIf(l -> l[0].equals(arr[0]));
            lastList.add(arr);

        }

        return lastList;
    }

    public static List<String[]> readFile(String filePath) throws FileNotFoundException {

        File file = new File(filePath);

        @SuppressWarnings("resource")
        Scanner scan = new Scanner(file);
        List<String[]> fileList = new ArrayList<>();

        while (scan.hasNextLine()) {
            String[] array = scan.nextLine().split("\t");
            fileList.add(array);
        }

        return fileList;
    }

    public static List<String[]> getDeviceDataList(List<String[]> fileList, String deviceID) {

        List<String[]>  deviceDataList = new ArrayList<>();

        for(String[] arr : fileList) {
            if(arr[1].equals(deviceID)) {
                deviceDataList.add(arr);
            }
        }

        return deviceDataList;
    }

    public static List<String[]> createUpdatedList(List<String[]> existingDeviceList, List<String[]> downloadedDeviceList) {

        List<String[]>  updatedList = new ArrayList<>();

        for(String[] each : existingDeviceList) {
            if(!downloadedDeviceList.stream().anyMatch(arr -> arr[0].equals(each[0]))) {
                updatedList.add(each);
            }
        }
        updatedList.addAll(downloadedDeviceList);

        return updatedList;
    }

    public static String hexToBin(String hex){
        String bin = "";
        String binFragment = "";
        int iHex;
        hex = hex.trim();
        hex = hex.replaceFirst("0x", "");

        for(int i = 0; i < hex.length(); i++){
            iHex = Integer.parseInt(""+hex.charAt(i),16);
            binFragment = Integer.toBinaryString(iHex);

            while(binFragment.length() < 4){
                binFragment = "0" + binFragment;
            }
            bin += binFragment;
        }
        return bin;
    }

    public static Map<String,String> checkStatus(String[] array) {
        Map<String,String> statMap = new HashMap<>();

        String binar = hexToBin(array[2]);
        String authorizationStat = "INVALID AUTHORIZATION STATUS DATA";
        String trainingStat = "INVALID TRAINING STATUS DATA";
        String adminStat = "INVALID ADMIN STATUS DATA";

        if(("" + binar.charAt(0)).equals("1")) {
            authorizationStat = "Authorised";
        }else if (("" + binar.charAt(0)).equals("0")) {
            authorizationStat = "Disabled";
        }

        statMap.put("Authorization", authorizationStat);

        if(("" + binar.charAt(1)).equals("1")) {
            trainingStat = "Trained";
        }else if (("" + binar.charAt(1)).equals("0")) {
            trainingStat = "Untrained";
        }

        statMap.put("Training", trainingStat);

        if(("" + binar.charAt(2)).equals("1")) {
            adminStat = "Operator";
        }else if (("" + binar.charAt(2)).equals("0")) {
            adminStat = "Admin";
        }

        statMap.put("Administration", adminStat);


        return statMap;
    }


}
