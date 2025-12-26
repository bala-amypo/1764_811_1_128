package com.example.demo.util;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllocationUtil {

    // Calculate total portfolio value
    public static double calculateTotalValue(List<HoldingRecord> holdings) {
        return holdings.stream()
                .mapToDouble(HoldingRecord::getValue) // ✅ CORRECTED: method reference fixed
                .sum();
    }

    // Calculate allocation percentages per asset class
    public static Map<AssetClassType, Double> calculateAllocationPercentages(List<HoldingRecord> holdings, double totalValue) {
        Map<AssetClassType, Double> percentages = new HashMap<>();
        for (HoldingRecord h : holdings) {
            percentages.put(h.getAssetClass(), h.getValue() / totalValue * 100); // ✅ CORRECTED: replaced invalid h.getValue() reference
        }
        return percentages;
    }

    // Convert map to JSON-like string
    public static String toJson(Map<AssetClassType, Double> map) {
        StringBuilder sb = new StringBuilder("{");
        map.forEach((k, v) -> sb.append("\"").append(k).append("\":").append(v).append(","));
        if (!map.isEmpty()) sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }
}
