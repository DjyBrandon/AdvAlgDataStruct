import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AADS {

    public static void main(String[] args) {
        try {
            // 从标准输入读取 JSON 数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String inputJson = jsonBuilder.toString();

            // 解析实例名称
            String instanceName = extractValue(inputJson, "\"InstanceName\":");
            System.out.println("Instance Name: " + instanceName);

            // 解析车辆和订单信息
            List<Map<String, String>> vehicles = extractVehicles(inputJson);
            List<Map<String, String>> orders = extractOrders(inputJson);

            // 任务调度与格式化输出
            // System.out.println("VehicleName,JobId,JourneyTime,ArrivalTime,WaitTime,DelayTime,ServiceTime,DepartureTime,Break1Time,Break1Duration,Break2Time,Break2Duration");
            scheduleAndPrint(vehicles, orders);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 使用调度算法分配任务并格式化输出
    private static void scheduleAndPrint(List<Map<String, String>> vehicles, List<Map<String, String>> orders) {
        for (Map<String, String> vehicle : vehicles) {
            String vehicleId = vehicle.get("Id");
            String startTime = vehicle.get("StartTime");

            // 打印车辆开始任务
            System.out.printf("%s,Vehicle %s start,0h0m,%s,0h0m,0h0m,0h0m,%s,,,,\n", vehicleId, vehicleId, formatTime(startTime), addTime(startTime, 8, 0));

            for (Map<String, String> order : orders) {
                String collectId = order.get("CollectId");
                String deliverId = order.get("DeliverId");

                // 获取和计算时间
                String earliestCollect = order.get("EarliestCollect1");
                String earliestDeliver = order.get("EarliestDeliver1");

                // 格式化每个订单的收集与交付信息
                System.out.printf("%s,%s,0h0m,%s,0h0m,0h0m,0h0m,%s,,,,\n", vehicleId, collectId, formatTime(earliestCollect), formatTime(earliestCollect));
                System.out.printf("%s,%s,0h0m,%s,0h0m,0h0m,0h0m,%s,,,,\n", vehicleId, deliverId, formatTime(earliestDeliver), formatTime(earliestDeliver));
            }

            // 打印车辆结束任务
            System.out.printf("%s,Vehicle %s end,0h0m,%s,0h0m,0h0m,0h0m,%s,,,,\n", vehicleId, vehicleId, addTime(startTime, 8, 0), addTime(startTime, 9, 0));
        }
    }

    // 手动解析和格式化时间字符串
    private static String addTime(String time, int addHours, int addMinutes) {
        String[] parts = time.split("T")[1].split(":");
        int hours = Integer.parseInt(parts[0]) + addHours;
        int minutes = Integer.parseInt(parts[1]) + addMinutes;

        // 处理分钟进位
        hours += minutes / 60;
        minutes %= 60;

        return String.format("%02d:%02d", hours, minutes);
    }

    private static String formatTime(String time) {
        return time.split("T")[1].substring(0, 5);  // 获取 "HH:mm" 部分
    }

    // 提取 JSON 字符串中的指定键的值
    private static String extractValue(String json, String key) {
        int index = json.indexOf(key);
        if (index == -1) return "";
        int start = json.indexOf("\"", index + key.length()) + 1;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    // 使用正则表达式提取车辆信息
    private static List<Map<String, String>> extractVehicles(String json) {
        List<Map<String, String>> vehicles = new ArrayList<>();
        Pattern vehiclePattern = Pattern.compile("\\{[^\\}]*\"Id\"\\s*:\\s*\"(\\d+)\"[^\\}]*\"StartTime\"\\s*:\\s*\"([^\"]+)\"[^\\}]*}");
        Matcher matcher = vehiclePattern.matcher(json);

        while (matcher.find()) {
            Map<String, String> vehicle = new HashMap<>();
            vehicle.put("Id", matcher.group(1));
            vehicle.put("StartTime", matcher.group(2));
            vehicles.add(vehicle);
        }
        return vehicles;
    }

    // 使用正则表达式提取订单信息
    private static List<Map<String, String>> extractOrders(String json) {
        List<Map<String, String>> orders = new ArrayList<>();
        Pattern orderPattern = Pattern.compile("\\{[^\\}]*\"CollectId\"\\s*:\\s*\"([^\"]+)\"[^\\}]*\"DeliverId\"\\s*:\\s*\"([^\"]+)\"[^\\}]*\"EarliestCollect1\"\\s*:\\s*\"([^\"]+)\"[^\\}]*\"LatestCollect1\"\\s*:\\s*\"([^\"]+)\"[^\\}]*\"EarliestDeliver1\"\\s*:\\s*\"([^\"]+)\"[^\\}]*\"LatestDeliver1\"\\s*:\\s*\"([^\"]+)\"[^\\}]*}");
        Matcher matcher = orderPattern.matcher(json);

        while (matcher.find()) {
            Map<String, String> order = new HashMap<>();
            order.put("CollectId", matcher.group(1));
            order.put("DeliverId", matcher.group(2));
            order.put("EarliestCollect1", matcher.group(3));
            order.put("LatestCollect1", matcher.group(4));
            order.put("EarliestDeliver1", matcher.group(5));
            order.put("LatestDeliver1", matcher.group(6));
            orders.add(order);
        }
        return orders;
    }
}