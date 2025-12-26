package com.example.demo.util;

import com.example.demo.entity.HoldingRecord;
import com.example.demo.entity.enums.AssetClassType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AllocationUtil {

    private AllocationUtil() {
    }

    public static double calculateTotalValue(List<HoldingRecord> holdings) {
        double total = 0.0;
        for (HoldingRecord holding : holdings) {
            total += holding.getCurrentValue();
        }
        return total;
    }

    public static Map<AssetClassType, Double> calculateAllocationPercentages(
            List<HoldingRecord> holdings, double totalValue) {

        Map<AssetClassType, Double> allocation =
                new EnumMap<>(AssetClassType.class);

        for (HoldingRecord holding : holdings) {
            AssetClassType type = holding.getAssetClass();
            allocation.put(
                    type,
                    allocation.getOrDefault(type, 0.0) + holding.getCurrentValue()
            );
        }

        for (AssetClassType type : allocation.keySet()) {
            allocation.put(type,
                    (allocation.get(type) / totalValue) * 100
            );
        }

        return allocation;
    }

    public static String toJson(Map<AssetClassType, Double> allocation) {
        StringBuilder sb = new StringBuilder("{");
        int i = 0;

        for (Map.Entry<AssetClassType, Double> e : allocation.entrySet()) {
            sb.append("\"")
              .append(e.getKey().name())
              .append("\":")
              .append(String.format("%.2f", e.getValue()));

            if (i < allocation.size() - 1) {
                sb.append(",");
            }
            i++;
        }

        sb.append("}");
        return sb.toString();
    }
}
