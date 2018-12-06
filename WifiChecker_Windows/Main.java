import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String currentlyConnectedWifi = null;
        String newWifi;
        boolean shown = false;

        while(true){
            Thread.sleep(100);
            newWifi = scanWiFi();
            if(currentlyConnectedWifi == null && !shown){
                System.out.println("Disconnected");
                shown = true;
                continue;
            }
            if(newWifi != null && !newWifi.equals(currentlyConnectedWifi)){
                System.out.println("Wifi changed!");
                System.out.println("new SSID: " + newWifi);
                shown = false;
            }
            currentlyConnectedWifi = newWifi;
        }
    }

    public static String scanWiFi() {
        HashMap<String, String> networkList = new HashMap<>();
        try {
            // Execute command
            String command = "netsh wlan show interface";
            Process p = Runtime.getRuntime().exec(command);
            try {
                p.waitFor(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(p.getInputStream())
            );
            String line;
            StringBuilder sb = new StringBuilder();
            String ssidArr[];

            while ((line = reader.readLine()) != null) {
                if (line.contains("SSID ") && !line.contains("BSSID ")) {
                    sb.append(line);
                    ssidArr = line.split(":");
                    networkList.put(ssidArr[0].trim(), ssidArr[1].trim());
                }
            }
        } catch (IOException e) {
        }
        return networkList.get("SSID");
    }
}
