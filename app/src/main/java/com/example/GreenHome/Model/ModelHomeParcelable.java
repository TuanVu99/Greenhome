package com.example.GreenHome.Model;


import java.io.Serializable;

public class ModelHomeParcelable implements Serializable {
    String avatar,HFavorites;
    double numberPeopleRating,rating;

    public String getHFavorites() {
        return HFavorites;
    }

    public void setHFavorites(String HFavorites) {
        this.HFavorites = HFavorites;
    }

    public double getNumberPeopleRating() {
        return numberPeopleRating;
    }

    public void setNumberPeopleRating(int numberPeopleRating) {
        this.numberPeopleRating = numberPeopleRating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    String HomeID;
    String uID;
    String typeHome;
    String rentalType;
    String nameHouse_VN;
    String nameHouse_EN;
    String country;
    String home_lat;
    String home_long;
    String detailAddress;
    String intro_VN;
    String price_day;
    String price_night;
    String price_weekend;
    int sizeRoom, number_bedroom, number_bed, single_bed, double_bed, total_bath, total_kitchen;

    boolean wifi, internet, tv, airconditioner, fan, water_heater, washing_machine, working_desk, slippers, camera_security, sofa, socket, wardrobe, clother_hanger, extra_mattress, bathtub, bathroom_heater, toilet, shower_booth, towels, stove, oven, gas, hotpot, rice_cooker, gym, yoga, spa, hair_salon, smoking, pets, party, cooking;

    public ModelHomeParcelable() {
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getHomeID() {
        return HomeID;
    }

    public void setHomeID(String homeID) {
        HomeID = homeID;
    }

    public String getTypeHome() {
        return typeHome;
    }

    public void setTypeHome(String typeHome) {
        this.typeHome = typeHome;
    }

    public String getRentalType() {
        return rentalType;
    }

    public void setRentalType(String rentalType) {
        this.rentalType = rentalType;
    }

    public String getNameHouse_VN() {
        return nameHouse_VN;
    }

    public void setNameHouse_VN(String nameHouse_VN) {
        this.nameHouse_VN = nameHouse_VN;
    }

    public String getNameHouse_EN() {
        return nameHouse_EN;
    }

    public void setNameHouse_EN(String nameHouse_EN) {
        this.nameHouse_EN = nameHouse_EN;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHome_lat() {
        return home_lat;
    }

    public void setHome_lat(String home_lat) {
        this.home_lat = home_lat;
    }

    public String getHome_long() {
        return home_long;
    }

    public void setHome_long(String home_long) {
        this.home_long = home_long;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getIntro_VN() {
        return intro_VN;
    }

    public void setIntro_VN(String intro_VN) {
        this.intro_VN = intro_VN;
    }

    public String getPrice_day() {
        return price_day;
    }

    public void setPrice_day(String price_day) {
        this.price_day = price_day;
    }

    public String getPrice_night() {
        return price_night;
    }

    public void setPrice_night(String price_night) {
        this.price_night = price_night;
    }

    public String getPrice_weekend() {
        return price_weekend;
    }

    public void setPrice_weekend(String price_weekend) {
        this.price_weekend = price_weekend;
    }

    public int getSizeRoom() {
        return sizeRoom;
    }

    public void setSizeRoom(int sizeRoom) {
        this.sizeRoom = sizeRoom;
    }

    public int getNumber_bedroom() {
        return number_bedroom;
    }

    public void setNumber_bedroom(int number_bedroom) {
        this.number_bedroom = number_bedroom;
    }

    public int getNumber_bed() {
        return number_bed;
    }

    public void setNumber_bed(int number_bed) {
        this.number_bed = number_bed;
    }

    public int getSingle_bed() {
        return single_bed;
    }

    public void setSingle_bed(int single_bed) {
        this.single_bed = single_bed;
    }

    public int getDouble_bed() {
        return double_bed;
    }

    public void setDouble_bed(int double_bed) {
        this.double_bed = double_bed;
    }

    public int getTotal_bath() {
        return total_bath;
    }

    public void setTotal_bath(int total_bath) {
        this.total_bath = total_bath;
    }

    public int getTotal_kitchen() {
        return total_kitchen;
    }

    public void setTotal_kitchen(int total_kitchen) {
        this.total_kitchen = total_kitchen;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isInternet() {
        return internet;
    }

    public void setInternet(boolean internet) {
        this.internet = internet;
    }

    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

    public boolean isAirconditioner() {
        return airconditioner;
    }

    public void setAirconditioner(boolean airconditioner) {
        this.airconditioner = airconditioner;
    }

    public boolean isFan() {
        return fan;
    }

    public void setFan(boolean fan) {
        this.fan = fan;
    }

    public boolean isWater_heater() {
        return water_heater;
    }

    public void setWater_heater(boolean water_heater) {
        this.water_heater = water_heater;
    }

    public boolean isWashing_machine() {
        return washing_machine;
    }

    public void setWashing_machine(boolean washing_machine) {
        this.washing_machine = washing_machine;
    }

    public boolean isWorking_desk() {
        return working_desk;
    }

    public void setWorking_desk(boolean working_desk) {
        this.working_desk = working_desk;
    }

    public boolean isSlippers() {
        return slippers;
    }

    public void setSlippers(boolean slippers) {
        this.slippers = slippers;
    }

    public boolean isCamera_security() {
        return camera_security;
    }

    public void setCamera_security(boolean camera_security) {
        this.camera_security = camera_security;
    }

    public boolean isSofa() {
        return sofa;
    }

    public void setSofa(boolean sofa) {
        this.sofa = sofa;
    }

    public boolean isSocket() {
        return socket;
    }

    public void setSocket(boolean socket) {
        this.socket = socket;
    }

    public boolean isWardrobe() {
        return wardrobe;
    }

    public void setWardrobe(boolean wardrobe) {
        this.wardrobe = wardrobe;
    }

    public boolean isClother_hanger() {
        return clother_hanger;
    }

    public void setClother_hanger(boolean clother_hanger) {
        this.clother_hanger = clother_hanger;
    }

    public boolean isExtra_mattress() {
        return extra_mattress;
    }

    public void setExtra_mattress(boolean extra_mattress) {
        this.extra_mattress = extra_mattress;
    }

    public boolean isBathtub() {
        return bathtub;
    }

    public void setBathtub(boolean bathtub) {
        this.bathtub = bathtub;
    }

    public boolean isBathroom_heater() {
        return bathroom_heater;
    }

    public void setBathroom_heater(boolean bathroom_heater) {
        this.bathroom_heater = bathroom_heater;
    }

    public boolean isToilet() {
        return toilet;
    }

    public void setToilet(boolean toilet) {
        this.toilet = toilet;
    }

    public boolean isShower_booth() {
        return shower_booth;
    }

    public void setShower_booth(boolean shower_booth) {
        this.shower_booth = shower_booth;
    }

    public boolean isTowels() {
        return towels;
    }

    public void setTowels(boolean towels) {
        this.towels = towels;
    }

    public boolean isStove() {
        return stove;
    }

    public void setStove(boolean stove) {
        this.stove = stove;
    }

    public boolean isOven() {
        return oven;
    }

    public void setOven(boolean oven) {
        this.oven = oven;
    }

    public boolean isGas() {
        return gas;
    }

    public void setGas(boolean gas) {
        this.gas = gas;
    }

    public boolean isHotpot() {
        return hotpot;
    }

    public void setHotpot(boolean hotpot) {
        this.hotpot = hotpot;
    }

    public boolean isRice_cooker() {
        return rice_cooker;
    }

    public void setRice_cooker(boolean rice_cooker) {
        this.rice_cooker = rice_cooker;
    }

    public boolean isGym() {
        return gym;
    }

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public boolean isYoga() {
        return yoga;
    }

    public void setYoga(boolean yoga) {
        this.yoga = yoga;
    }

    public boolean isSpa() {
        return spa;
    }

    public void setSpa(boolean spa) {
        this.spa = spa;
    }

    public boolean isHair_salon() {
        return hair_salon;
    }

    public void setHair_salon(boolean hair_salon) {
        this.hair_salon = hair_salon;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public boolean isPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public boolean isParty() {
        return party;
    }

    public void setParty(boolean party) {
        this.party = party;
    }

    public boolean isCooking() {
        return cooking;
    }

    public void setCooking(boolean cooking) {
        this.cooking = cooking;
    }


}
