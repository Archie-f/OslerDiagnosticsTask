package com.oslerdiagnostics.tests;

import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.oslerdiagnostics.utilities.OslerUtils.*;

public class VerifyDeviceUpdate {

    String portalUsersPath = "/Users/archie/Documents/JOB/Test Engineer Mini-Project/PortalUserList.txt";
    String deviceUsersPath = "/Users/archie/Documents/JOB/Test Engineer Mini-Project/DeviceUserList.txt";
    String updatedUsersList = "/Users/archie/Documents/JOB/Test Engineer Mini-Project/DeviceUserList_updated.txt";
    String analysisFilePath = "/Users/archie/Documents/JOB/Test Engineer Mini-Project/Analysis.txt";
    String deviceID = "47057";

    @Test
    public void deviceTest() throws IOException {

        List<String[]> portalUsersList = readFile(portalUsersPath);
        portalUsersList = getDeviceDataList(portalUsersList, deviceID);
        portalUsersList = singularUsers(portalUsersList);

        List<String[]> deviceUserList = readFile(deviceUsersPath);
        deviceUserList = singularUsers(deviceUserList);

        List<String[]> updatedDeviceList = createUpdatedList(deviceUserList, portalUsersList);
        updatedDeviceList = resolveHex(updatedDeviceList);

        updatedDeviceList.stream().forEach(l-> System.out.print(Arrays.toString(l)));
        System.out.println();

        List<String[]> deviceUpdatedList = readFile(updatedUsersList);
        deviceUpdatedList.stream().forEach(l-> System.out.print(Arrays.toString(l)));
        System.out.println();

        createAnalysisFile(deviceUpdatedList, updatedDeviceList, analysisFilePath);

    }

}
