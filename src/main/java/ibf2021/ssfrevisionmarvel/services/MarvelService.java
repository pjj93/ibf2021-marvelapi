package ibf2021.ssfrevisionmarvel.services;

import static ibf2021.ssfrevisionmarvel.Constants.MARVEL_API_KEY;
import static ibf2021.ssfrevisionmarvel.Constants.MARVEL_API_PRIVATE_KEY;
import static ibf2021.ssfrevisionmarvel.Constants.MARVEL_URL;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ibf2021.ssfrevisionmarvel.SsfRevisionMarvelApplication;
import ibf2021.ssfrevisionmarvel.models.MarvelCharacter;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Service
public class MarvelService {
    private final Logger logger = Logger.getLogger(SsfRevisionMarvelApplication.class.getName());

    public List<MarvelCharacter> getCharacters(String nameStartsWith) {
        
        long ts = System.currentTimeMillis();

        String url = UriComponentsBuilder
            .fromUriString(MARVEL_URL)
            .queryParam("nameStartsWith", nameStartsWith)
            .queryParam("ts", ts)
            .queryParam("apikey", MARVEL_API_KEY)
            .queryParam("hash", genMd5(ts))
            .toUriString();

        logger.log(Level.INFO, "url: " + url);

        RequestEntity<Void> req = RequestEntity
            .get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req, String.class);

        logger.log(Level.INFO, "Status code: " + resp.getStatusCodeValue());
        
        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
            final JsonReader reader = Json.createReader(is);
            final JsonObject body = reader.readObject();
            final JsonObject data = body.getJsonObject("data");
            //final JsonArray results = data.getJsonArray(data.toString());
            final JsonArray results = body.getJsonObject("data").getJsonArray("results");
            List<MarvelCharacter> charList = new ArrayList<>();
            for(JsonValue v: results) {
                JsonObject o = (JsonObject)v;
                MarvelCharacter marvelChar = new MarvelCharacter(o);
                
                charList.add(marvelChar);
            }
 
            return charList;

        } catch (Exception ex) { }

        return Collections.EMPTY_LIST;
    }

    private String genMd5(long ts) {
        String text = ts + MARVEL_API_PRIVATE_KEY + MARVEL_API_KEY ;
        try {
  
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
  
            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(text.getBytes());
  
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
  
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } 
  
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
