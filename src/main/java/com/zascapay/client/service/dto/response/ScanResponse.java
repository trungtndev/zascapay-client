package com.zascapay.client.service.dto.response;
import java.util.List;

public class ScanResponse {
    private List<Detection> objects;
    private String image; // base64 encoded image

    public List<Detection> getObjects() {
        return objects;
    }

    public void setObjects(List<Detection> objects) {
        this.objects = objects;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
