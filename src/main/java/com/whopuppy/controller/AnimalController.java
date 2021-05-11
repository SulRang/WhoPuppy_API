package com.whopuppy.controller;

import com.whopuppy.domain.AnimalDTO;
import com.whopuppy.service.AnimalService;
import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class AnimalController {

    @Resource
    AnimalService animalService;
    public static int INDENT_FACTOR = 4;

    @Value("${animal.service-key}")
    private String serviceKey;

    @Value("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?bgnde=")
    private String apiURL;

    @PostMapping("/getServiceKey")
    public String getApikey(){
        return serviceKey;
    }


    @PostMapping("/callAnimal")
    public String callAnimallist(@RequestParam Integer days, @RequestParam String page, @RequestParam String rows) throws IOException, ParseException {
        byte[] bytes;

        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        String today = date.format(currentDate);
        cal.setTime(new Date());

        cal.add(Calendar.DATE, -days);

        String recentDate = date.format(cal.getTime());

        String startDate = recentDate;
        String endDate = today;
        String pageNo = page;
        String Rows = rows;
        String resultURL = apiURL + startDate + "&endde=" + endDate + "&pageNo=" + pageNo + "&numOfRows=" + Rows + "&";

        StringBuilder urlBuilder = new StringBuilder(resultURL);
        urlBuilder.append(URLEncoder.encode("ServiceKey", "UTF-8") + serviceKey);

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        conn.disconnect();

        org.json.JSONObject xmlJSONObj = XML.toJSONObject(sb.toString());
        String jsonStr = xmlJSONObj.toString(INDENT_FACTOR);
        bytes = jsonStr.getBytes();

        String str = new String(bytes);

        return str;

    }


    @PostMapping("/saveAnimalList")
    public String saveAnimalList() throws IOException, ParseException {

        byte[] bytes;
        String apiString;

        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        String startDate = date.format(cal.getTime());
        String endDate = date.format(currentDate);

        Integer pageNo = 0;
        Integer Rows = 100;
        Integer currentCount = 0;
        Integer totalCount = 0;

        do{
            String resultURL = apiURL + startDate + "&endde=" + endDate + "&pageNo=" + ++pageNo + "&numOfRows=" + Rows + "&";
            StringBuilder urlBuilder = new StringBuilder(resultURL);
            urlBuilder.append(URLEncoder.encode("ServiceKey", "UTF-8") + serviceKey);
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            conn.disconnect();

            org.json.JSONObject xmlJSONObj = XML.toJSONObject(sb.toString());
            String jsonStr = xmlJSONObj.toString(INDENT_FACTOR);
            bytes = jsonStr.getBytes();
            apiString = new String(bytes);

            List<AnimalDTO> animalDTOList = new ArrayList<>();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(apiString);
            JSONObject parse_response = (JSONObject) jsonObj.get("response");
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            JSONObject parse_items = (JSONObject) parse_body.get("items");
            JSONArray parse_array = (JSONArray) parse_items.get("item");

            JSONObject obj;
            for(int i = 0; i < parse_array.size(); i++) {
                obj = (JSONObject) parse_array.get(i);
                AnimalDTO animalDTO = new AnimalDTO();

                animalDTO.setSexCd(obj.get("sexCd").toString());
                animalDTO.setKindCd(obj.get("kindCd").toString());
                animalDTO.setNoticeNo(obj.get("noticeNo").toString());
                animalDTO.setCareAddr(obj.get("careAddr").toString());
                animalDTO.setProcessState(obj.get("processState").toString());
                animalDTO.setNoticeSdt(obj.get("noticeSdt").toString());
                animalDTO.setWeight(obj.get("weight").toString());
                animalDTO.setChargeNm(Objects.toString(obj.get("chargeNm"), ""));
                animalDTO.setCareNm(obj.get("careNm").toString());
                animalDTO.setDesertionNo(obj.get("desertionNo").toString());
                animalDTO.setCareTel(obj.get("careTel").toString());
                animalDTO.setHappenPlace(obj.get("happenPlace").toString());
                animalDTO.setOfficetel(obj.get("officetel").toString());
                animalDTO.setOrgNm(obj.get("orgNm").toString());
                animalDTO.setFilename(obj.get("filename").toString());
                animalDTO.setPopfile(obj.get("popfile").toString());
                animalDTO.setNoticeEdt(obj.get("noticeEdt").toString());
                animalDTO.setNeuterYn(obj.get("neuterYn").toString());
                animalDTO.setSpecialMark(obj.get("specialMark").toString());
                animalDTO.setColorCd(obj.get("colorCd").toString());
                animalDTO.setHappenDt(obj.get("happenDt").toString());
                animalDTO.setAge(obj.get("age").toString());

                animalDTOList.add(animalDTO);
                animalService.insertAnimalJson(animalDTOList);
            }

            currentCount = pageNo * Rows;
            String count = parse_body.get("totalCount").toString();
            totalCount = Integer.parseInt(count);

        }while(currentCount < totalCount);

        return "OK : " + totalCount;

    }
}
