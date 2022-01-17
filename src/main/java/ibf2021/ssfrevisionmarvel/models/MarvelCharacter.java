package ibf2021.ssfrevisionmarvel.models;

import jakarta.json.JsonObject;

public class MarvelCharacter {
    private String name;
    private String description;
    private String thumbnail;

    public MarvelCharacter(JsonObject o) {
        this.name = o.getString("name");
        this.description = o.getString("description");
        this.thumbnail = o.getJsonObject("thumbnail").getString("path").toString() + ".jpg";
    }

    public String getName() {
        return name;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setName(String name) {
        this.name = name;
    }
}
