package com.oslerdiagnostics.utilities;

import com.oslerdiagnostics.base.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class OslerUtils {

    public static List<User> readFile(String path) throws FileNotFoundException {


        File file = new File(path);

        Scanner scan = new Scanner(file);
        List<User> userList = new ArrayList<>();

        while (scan.hasNextLine()) {
            String[] array = scan.nextLine().split("\t");
            //System.out.println(array[0] + " " + array[1] + " " + array[2]);
            List<User> list = new ArrayList<>();
            User user = new User();
            user.setUserID(array[0]);
            user.setDeviceID(array[1]);
            if(array.length<4){
                user.setHexCode(array[2]);
            }else {
                user.setStatus(array[2] + "\t" + array[3]);
            }



            list.add(user);
            userList.addAll(list);
        }
        return userList;
    }

    public static List<User> getDeviceDataList(List<User> fileList, String deviceID){

        List<User> deviceDataList = new ArrayList<>();

        for(User user : fileList){
            if(user.getDeviceID().equals(deviceID)){
                deviceDataList.add(user);
            }
        }
        return deviceDataList;
    }

    public static List<User> singularUsers(List<User> list){

        List<User>  lastList = new ArrayList<>();

        for (User user : list){
            lastList.removeIf(l -> l.getUserID().equals(user.getUserID()));
            lastList.add(user);
        }
        return lastList;
    }

    public static List<User> createUpdatedList(List<User> existingDeviceList, List<User> downloadedDeviceList){

        List<User>  updatedList = new ArrayList<>();

        for(User user : existingDeviceList){

            if(downloadedDeviceList.stream().noneMatch(dUser -> dUser.getUserID().equals(user.getUserID()))){
                updatedList.add(user);
            }
        }
        updatedList.addAll(downloadedDeviceList);

        return updatedList;
    }

    public static String hexToBin(String hex){
        String binary = "";
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
            binary += binFragment;
        }
        return binary;
    }

    public static Map<String,String> checkStatus(User user) {
        Map<String,String> statMap = new HashMap<>();

        String binar = hexToBin(user.getHexCode());
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

    public static List<User> resolveHex(List<User> list) {

        for(User user : list) {

            Map<String,String> status = checkStatus(user);
            String stat= status.get("Authorization") + status.get("Administration") + "\t" + status.get("Training");
            user.setStatus(stat);
        }

        return list;
    }

    public static void createAnalysisFile(List<User> oldList, List<User> newList, String path) throws IOException {

        FileWriter writer = new FileWriter(path);

        Map<String, User> oldMap = new HashMap<>();
        oldList.stream().forEach(old -> oldMap.put(old.getUserID(),old));

        Map<String, User> newMap = new HashMap<>();
        newList.stream().forEach(neW -> newMap.put(neW.getUserID(),neW));

        for(Map.Entry<String, User> oldItem : oldMap.entrySet()){

            if(newMap.containsKey(oldItem.getKey())){
                writer.write(oldItem.getKey() + "\t" + oldItem.getValue().getDeviceID()
                        + "\t" + oldItem.getValue().getStatus() + "\t");
                if(newMap.get(oldItem.getKey()).getDeviceID().equals(oldItem.getValue().getDeviceID())
                        && newMap.get(oldItem.getKey()).getStatus().equals(oldItem.getValue().getStatus())){
                    writer.write("\t" + "\t" + "\t" + "CORRECT");
                }else {
                    writer.write("\t" + "\t" + "\t" + "ERROR");
                }
            }
            writer.write("\n");
        }
        writer.close();

        compare(oldMap,newMap);
    }

    public static void createAnalysisAlternate(List<User> oldList, List<User> newList, String path) throws IOException {

        FileWriter writer1 = new FileWriter(path);

        for(User oldItem : oldList){
            for(User newItem : newList){
                if(oldItem.getUserID().equals(newItem.getUserID())){
                    writer1.write(oldItem.getUserID() + "\t" + oldItem.getDeviceID()
                            + "\t" + oldItem.getStatus());

                    if(oldItem.getDeviceID().equals(newItem.getDeviceID()) &&
                            oldItem.getStatus().equals(newItem.getStatus())){
                        writer1.write("\t" + "\t" + "\t" + "CORRECT");
                        //break;
                    }else {
                        writer1.write("\t" + "\t" + "\t" + "ERROR");
                        //break;
                    }
                }
            }
            writer1.write("\n");
        }
        writer1.close();
    }

    public static void compare(Map<String, User> old, Map<String, User> neW){

        System.out.println(old.get("78fc6a6d-1b2e-4077-b25d-ed252ba51d80").getDeviceID() +
                " " + old.get("78fc6a6d-1b2e-4077-b25d-ed252ba51d80").getStatus());
        System.out.println(neW.get("78fc6a6d-1b2e-4077-b25d-ed252ba51d80").getDeviceID() +
                " " + neW.get("78fc6a6d-1b2e-4077-b25d-ed252ba51d80").getStatus());

    }

}
