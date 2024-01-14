package com.gamingmesh.jobs.economy;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.OfflinePlayer;

import com.gamingmesh.jobs.container.CurrencyType;

public class BufferedPayment {

    private OfflinePlayer offlinePlayer;

    private final Map<CurrencyType, Double> payments = new HashMap<>();
    private String jobName = "null";

    public BufferedPayment(OfflinePlayer offlinePlayer, Map<CurrencyType, Double> payments, String jobName) {
        this.offlinePlayer = offlinePlayer;
        this.jobName = jobName;

        this.payments.putAll(payments);
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    public String getJobName() {
        return jobName;
    }

    public double get(CurrencyType type) {
        return payments.getOrDefault(type, 0d);
    }

    public Double set(CurrencyType type, double amount) {
        return payments.put(type, amount);
    }

    public boolean containsPayment() {
        for (Double one : payments.values()) {
            if (one != 0D)
                return true;
        } return false;
    }

    public Map<CurrencyType, Double> getPayment() {
        return payments;
    }
}
