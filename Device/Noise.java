public class Noise {
    public static final int AIN0 = 0x40;
    public static final int AIN1 = 0x41;
    public static final int AIN2 = 0x42;
    public static final int AIN3 = 0x43;
    double ParamA = 0.001129148;
    double ParamB = 0.000234125;
    double ParamC = 0.0000000876741;

    private int i2cAddress;
    private int ainNo;
    private int analogVal;

    private static String mac = "DC:A6:32:9E:23:34";
    private static String tag = "Engineering01";

    public double calTemp1(int lowValue) {
        double paramB = 3900.0;
        double resistor = (255.0 * 10000) / lowValue - 10000;
        double tempC = (paramB / (Math.log(resistor / 10000) + (paramB / (273.15 + 25)))) - 273.15;
        return tempC;
    }

    public double calTemp(int lowValue) {
        double Temp;
        Temp = Math.log(1000.0 / ((255.0 / lowValue - 1)));
        Temp = 1 / (ParamA + (ParamB + (ParamC * Temp * Temp)) * Temp);
        return ((Temp - 273.15));
    }

    public Noise(int i2cAddress, int ainNo) {
        this.i2cAddress = i2cAddress;
        this.ainNo = ainNo;
    }

    public int analogRead() throws Exception {
        I2CBus i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice i2cDevice = i2cBus.getDevice(i2cAddress);
        // dummy read
        i2cDevice.read(ainNo);
        analogVal = i2cDevice.read(ainNo);
        return analogVal;
    }

    public void analogWrite(byte value) throws Exception {
        I2CBus i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice i2cDevice = i2cBus.getDevice(i2cAddress);
        i2cDevice.write(AIN3, value);
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 2) {
            mac = args[1];
            tag = args[2];
        }

        Noise test = new Noise(0x48, AIN3);
        NetworkControl network = new NetworkControl();
        network.Init();
        //test.analogWrite((byte)0);
        while (true) {
            int value = test.analogRead();
            System.out.println(value);
            System.out.println("----");

            RequestJson json = new RequestJson();
            json.add("decibel", value + "");
            json.add("device", mac);
            json.add("gridX", "0");
            json.add("gridY", "0");
            json.add("tag", tag);
            json.add("temperature", "33");
            network.sendData(json.toJsonString());
            Thread.sleep(1000);
        }
    }
}
