package domain.services;

import helpers.StringManipulation;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;

public class FinishCodeService {

    public static JSONObject getKohlerCodes()throws Exception{
        JSONObject jsonObject = new JSONObject();
        String csvFile = "finish-code-files/kohler-fc.csv";
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(csvFile));
            String line = "";
            String cvsSplitBy = ",";
            int counter = 1;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                if(counter != 1){
                    jsonObject.put(row[0].toUpperCase().trim(),StringManipulation.capitalCase(row[1].toLowerCase().trim()));
                }
                counter++;
            }
            return  jsonObject;
    }
}
