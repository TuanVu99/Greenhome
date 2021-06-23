package com.example.GreenHome.Model;


import android.os.Parcel;
import android.os.Parcelable;

public class ModelHome implements Parcelable {
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

    protected ModelHome(Parcel in) {
        avatar = in.readString();
        HFavorites =in.readString();
        HomeID = in.readString();
        uID = in.readString();
        typeHome = in.readString();
        rentalType = in.readString();
        nameHouse_VN = in.readString();
        nameHouse_EN = in.readString();
        country = in.readString();
        home_lat = in.readString();
        home_long = in.readString();
        detailAddress = in.readString();
        intro_VN = in.readString();
        price_day = in.readString();
        price_night = in.readString();
        price_weekend = in.readString();
        sizeRoom = in.readInt();
        number_bedroom = in.readInt();
        number_bed = in.readInt();
        single_bed = in.readInt();
        double_bed = in.readInt();
        total_bath = in.readInt();
        total_kitchen = in.readInt();
        rating = in.readDouble();
        numberPeopleRating=in.readDouble();
        wifi = in.readByte() != 0;
        internet = in.readByte() != 0;
        tv = in.readByte() != 0;
        airconditioner = in.readByte() != 0;
        fan = in.readByte() != 0;
        water_heater = in.readByte() != 0;
        washing_machine = in.readByte() != 0;
        working_desk = in.readByte() != 0;
        slippers = in.readByte() != 0;
        camera_security = in.readByte() != 0;
        sofa = in.readByte() != 0;
        socket = in.readByte() != 0;
        wardrobe = in.readByte() != 0;
        clother_hanger = in.readByte() != 0;
        extra_mattress = in.readByte() != 0;
        bathtub = in.readByte() != 0;
        bathroom_heater = in.readByte() != 0;
        toilet = in.readByte() != 0;
        shower_booth = in.readByte() != 0;
        towels = in.readByte() != 0;
        stove = in.readByte() != 0;
        oven = in.readByte() != 0;
        gas = in.readByte() != 0;
        hotpot = in.readByte() != 0;
        rice_cooker = in.readByte() != 0;
        gym = in.readByte() != 0;
        yoga = in.readByte() != 0;
        spa = in.readByte() != 0;
        hair_salon = in.readByte() != 0;
        smoking = in.readByte() != 0;
        pets = in.readByte() != 0;
        party = in.readByte() != 0;
        cooking = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ModelHome> CREATOR = new Parcelable.Creator<ModelHome>() {
        @Override
        public ModelHome createFromParcel(Parcel in) {
            return new ModelHome(in);
        }

        @Override
        public ModelHome[] newArray(int size) {
            return new ModelHome[size];
        }
    };

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

    public ModelHome() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(HFavorites);
        parcel.writeString(avatar);
        parcel.writeString(HomeID);
        parcel.writeString(uID);
        parcel.writeString(typeHome);
        parcel.writeString(rentalType);
        parcel.writeString(nameHouse_VN);
        parcel.writeString(nameHouse_EN);
        parcel.writeString(country);
        parcel.writeString(home_lat);
        parcel.writeString(home_long);
        parcel.writeString(detailAddress);
        parcel.writeString(intro_VN);
        parcel.writeString(price_day);
        parcel.writeString(price_night);
        parcel.writeString(price_weekend);
        parcel.writeInt(sizeRoom);
        parcel.writeInt(number_bedroom);
        parcel.writeInt(number_bed);
        parcel.writeInt(single_bed);
        parcel.writeInt(double_bed);
        parcel.writeInt(total_bath);
        parcel.writeInt(total_kitchen);
        parcel.writeDouble(rating);
        parcel.writeDouble(numberPeopleRating);
        parcel.writeByte((byte) (wifi ? 1 : 0));
        parcel.writeByte((byte) (internet ? 1 : 0));
        parcel.writeByte((byte) (tv ? 1 : 0));
        parcel.writeByte((byte) (airconditioner ? 1 : 0));
        parcel.writeByte((byte) (fan ? 1 : 0));
        parcel.writeByte((byte) (water_heater ? 1 : 0));
        parcel.writeByte((byte) (washing_machine ? 1 : 0));
        parcel.writeByte((byte) (working_desk ? 1 : 0));
        parcel.writeByte((byte) (slippers ? 1 : 0));
        parcel.writeByte((byte) (camera_security ? 1 : 0));
        parcel.writeByte((byte) (sofa ? 1 : 0));
        parcel.writeByte((byte) (socket ? 1 : 0));
        parcel.writeByte((byte) (wardrobe ? 1 : 0));
        parcel.writeByte((byte) (clother_hanger ? 1 : 0));
        parcel.writeByte((byte) (extra_mattress ? 1 : 0));
        parcel.writeByte((byte) (bathtub ? 1 : 0));
        parcel.writeByte((byte) (bathroom_heater ? 1 : 0));
        parcel.writeByte((byte) (toilet ? 1 : 0));
        parcel.writeByte((byte) (shower_booth ? 1 : 0));
        parcel.writeByte((byte) (towels ? 1 : 0));
        parcel.writeByte((byte) (stove ? 1 : 0));
        parcel.writeByte((byte) (oven ? 1 : 0));
        parcel.writeByte((byte) (gas ? 1 : 0));
        parcel.writeByte((byte) (hotpot ? 1 : 0));
        parcel.writeByte((byte) (rice_cooker ? 1 : 0));
        parcel.writeByte((byte) (gym ? 1 : 0));
        parcel.writeByte((byte) (yoga ? 1 : 0));
        parcel.writeByte((byte) (spa ? 1 : 0));
        parcel.writeByte((byte) (hair_salon ? 1 : 0));
        parcel.writeByte((byte) (smoking ? 1 : 0));
        parcel.writeByte((byte) (pets ? 1 : 0));
        parcel.writeByte((byte) (party ? 1 : 0));
        parcel.writeByte((byte) (cooking ? 1 : 0));
    }
}
