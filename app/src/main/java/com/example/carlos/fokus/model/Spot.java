package com.example.carlos.fokus.model;

/**
 * Created by eceybrito on 09/09/2017.
 */

public class Spot {

    private Integer id;
    private String name;
    private double lat;
    private double longit;
    private int userId;
    private String deletedAt;
    private String createdAt;
    private String updatedAt;
    private String description;
    private String image;
    private String deviceId;

    public Spot() {
    }

    public Spot(Integer id, String name, double lat, double longit,
                int userId, String deletedAt, String createdAt,
                String updatedAt, String description,
                String image, String deviceId) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.longit = longit;
        this.userId = userId;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.description = description;
        this.image = image;
        this.deviceId = deviceId;
    }

    public Spot(Integer id, String name, String createdAt, String description,
                String image) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.description = description;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Integer lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return longit;
    }

    public void setLong(Integer _long) {
        this.longit = _long;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Spot spot = (Spot) o;

        if (Double.compare(spot.lat, lat) != 0) return false;
        if (Double.compare(spot.longit, longit) != 0) return false;
        if (userId != spot.userId) return false;
        if (id != null ? !id.equals(spot.id) : spot.id != null) return false;
        if (name != null ? !name.equals(spot.name) : spot.name != null) return false;
        if (deletedAt != null ? !deletedAt.equals(spot.deletedAt) : spot.deletedAt != null)
            return false;
        if (createdAt != null ? !createdAt.equals(spot.createdAt) : spot.createdAt != null)
            return false;
        if (updatedAt != null ? !updatedAt.equals(spot.updatedAt) : spot.updatedAt != null)
            return false;
        if (description != null ? !description.equals(spot.description) : spot.description != null)
            return false;
        if (image != null ? !image.equals(spot.image) : spot.image != null) return false;
        return deviceId != null ? deviceId.equals(spot.deviceId) : spot.deviceId == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longit);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + userId;
        result = 31 * result + (deletedAt != null ? deletedAt.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        return result;
    }
}