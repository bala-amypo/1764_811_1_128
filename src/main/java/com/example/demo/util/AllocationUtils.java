package com.example.demo.util;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AllocationUtil {

    private AllocationUtil() {
        // utility class
    }

    /**
     * Calculate total portfolio value
     */
    public static double calculateTotalValue(List<HoldingRecord> holdings) {
        double total = 0.0;
        for (HoldingRecord holding : holdings) {
            total += holding.getCurrentValue();
        }
        return total;
    }

    /**
     * Calculate percentage allocation per asset class
     */
    public static Map<AssetClassType, Double> calculateAllocationPercentages(
            List<HoldingRecord> holdings,
            double totalValue
    ) {
        Map<AssetClassType, Double> allocation = new EnumMap<>(AssetClassType.class);

        for (HoldingRecord holding : holdings) {
            AssetClassType type = holding.getAssetClass();
            allocation.put(
                    type,
                    allocation.getOrDefault(type, 0.0) + holding.getCurrentValue()
            );
        }

        for (AssetClassType type : allocation.keySet()) {
            double percentage = (allocation.get(type) / totalValue) * 100;
            allocation.put(type, percentage);
        }

        return allocation;
    }

    /**
     * Convert allocation map to JSON string
     * (simple JSON, avoids external dependencies)
     */
    public static String toJson(Map<AssetClassType, Double> allocation) {
        StringBuilder json = new StringBuilder("{");

        int index = 0;
        for (Map.Entry<AssetClassType, Double> entry : allocation.entrySet()) {
            json.append("\"")
                .append(entry.getKey().name())
                .append("\":")
                .append(String.format("%.2f", entry.getValue()));

            if (index < allocation.size() - 1) {
                json.append(",");
            }
            index++;
        }

        json.append("}");
        return json.toString();
    }
}
