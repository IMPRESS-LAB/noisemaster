import java.net.URLConnection;

public class NetworkControl {
    static String baseUrl = "http://monet.inu.ac.kr:8080/api/v1/noise";
    static URL url = null;
    static URLConnection conn = null;

    public static void Init() {
        try {
            url = new URL(baseUrl);
            conn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendData(String json) {
        System.out.println(callPostUrl(url, json));
    }

    public static String callPostUrl(URL url, String hm) {
        StringBuilder sb = new StringBuilder("");
        try {
            URLConnection connection = url.openConnection();
            HttpURLConnection huc = (HttpURLConnection) connection;
            huc.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            huc.setRequestMethod("POST");
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setUseCaches(false);
            huc.setDefaultUseCaches(false);

            PrintWriter out = new PrintWriter(huc.getOutputStream());
            out.println(hm);
            out.flush();
            out.close();

            BufferedInputStream in = new BufferedInputStream(huc.getInputStream());
            byte[] bufRead = new byte[4096];
            int lenRead = 0;
            while ((lenRead = in.read(bufRead)) > 0) {
                sb.append(new String(bufRead, 0, lenRead, "utf-8"));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        RequestJson json = new RequestJson();
        json.add("decibel", "30");
        json.add("device", "ff:ff:ff:ff:ff:ff:ff");
        json.add("gridX", "0");
        json.add("gridY", "0");
        json.add("tag", "45");
        json.add("temperature", "45");
        Init();
        sendData(json.toJsonString());
    }
}