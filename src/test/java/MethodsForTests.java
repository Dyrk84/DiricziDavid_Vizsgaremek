import java.io.*;
import java.util.*;

public class MethodsForTests {

    public static List<String> fileReader(String filePath) {
        String myFileContent = "";
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                myFileContent = myFileContent + myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException exceptionFromFileReader) {
            System.out.println("exception in listFromFile(): " + exceptionFromFileReader);
        }

        String[] strArray = myFileContent.split(",");
        List<String> listFromFileReader = new ArrayList<>();
        for (int i = 0; i < strArray.length; i++) {
            listFromFileReader.add(strArray[i].trim());
        }
        return listFromFileReader;
    }

    public static List<List<String>> multivaluedMapWriterForAccountHandling(int numberOfTestDataRow) {
        List<List<String>> multivaluedMap = new LinkedList<>();
        for (int i = 0; i < numberOfTestDataRow; i++) {
            String testName = "testName" + (i+1);
            String passwordForTest = "passwordForTest" + (i + 1);
            String testemail = "testemail" + (i + 1) + "@address.com";
            String testDescription = "test description text with number " + (i + 1);
            List<String> noNameList = new LinkedList<>();
            noNameList.add(testName);
            noNameList.add(passwordForTest);
            noNameList.add(testemail);
            noNameList.add(testDescription);
            multivaluedMap.add(noNameList);
        }
        return multivaluedMap;
    }

    public static void MapToFiles(String filePath, List<List<String>> multiValuedMap) {
        try {
            FileWriter accountHandlerFile = new FileWriter(filePath, true);
            for (int i = 0; i < multiValuedMap.size(); i++) {
                String[] a = multiValuedMap.get(i).toArray(new String[4]);
                accountHandlerFile.write(a[0] + "," + a[1] + "," + a[2] + "," + a[3] + "," + "\n");
            }
            accountHandlerFile.close();
        } catch (Exception e) {
            System.out.println("Record not saved" + e);
        }
    }

    public static List<List<String>> fromFileToStringList(String filePath) {
        List<List<String>> listForAccountHandling = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("files/multivaluedMapForAccountHandlingInFile.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                listForAccountHandling.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return listForAccountHandling;
    }

    public static void deleteFile(String filePath) {
        File myObj = new File(filePath);
        if (myObj.exists() && myObj.isFile()) {
            myObj.delete();
        }
    }

    public static void folderCreator(String folderName) {
        File folder = new File("files/" + folderName);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                try {
                    throw new IOException("Error creating directory.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<String> filesNamesFromFolderIntoList(String folderName) {
        File folder = new File("files/" + folderName);
        File[] listOfFiles = folder.listFiles();
        List<String> filenames = new ArrayList<>();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                filenames.add(file.getName());
            }
        }
        return filenames;
    }
}
