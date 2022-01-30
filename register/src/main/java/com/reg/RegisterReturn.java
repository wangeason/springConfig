package com.reg;

import com.google.gson.Gson;

import java.util.Objects;
import java.util.UUID;

public class RegisterReturn {
    static String WELCOME = "welcome %s from %s";
    UUID uuid;
    String msg;

    public RegisterReturn(String name, String city) {
        this.uuid = UUID.randomUUID();
        this.msg = String.format(WELCOME, name, city);
    }

    public RegisterReturn(String message) {
        this.msg = message;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toJson() {

        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterReturn that = (RegisterReturn) o;
        return msg.equals(that.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, msg);
    }
}
